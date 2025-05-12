/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.framework.utils;

import com.lowagie.text.DocumentException;
import it.univaq.webmarket.data.model.*;
import it.univaq.webmarket.framework.result.TemplateManagerException;
import it.univaq.webmarket.framework.result.TemplateResult;
import org.xhtmlrenderer.pdf.ITextRenderer;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author cdidi
 */
public class EmailSender {
    
    public enum Event {
        RICHIESTA_REGISTRATA,
        RICHIESTA_PRESA_IN_CARICO, 
        PROPOSTA_INSERITA, 
        PROPOSTA_ACCETTATA,
        PROPOSTA_RIFIUTATA, 
        ORDINE_CREATO,
        ORDINE_CHIUSO, 
    }

    private String emailFrom, password;
    private Properties properties;

    public EmailSender(String emailFrom, String password, Properties properties) {
        this.emailFrom = emailFrom;
        this.password = password;
        this.properties = properties;
    }

    public void sendEmail(String to, String messageText) {
        // Uso un thread perchè potrebbe metterci del tempo ad inviare l'email
        new Thread(() -> {

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailFrom, password);
                }
            });

            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(emailFrom));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                // Set Subject: header field
                message.setSubject("This is the Subject Line!");

                // Now set the actual message
                message.setText(messageText);

                // Send message
                Transport.send(message);
                Logger.getLogger(EmailSender.class.getName()).info("Sent message successfully....");
            } catch (MessagingException mex) {
                Logger.getLogger(EmailSender.class.getName()).severe(mex.getMessage());
            }
        }).start();
    }

    public void sendPDFWithEmail(ServletContext context, String to, Object obj, Event event) {
        try {
            TemplateResult result = new TemplateResult(context);
            String htmlresult = "";
            Map<String, Object> datamodel = new HashMap<>();
            Map<String, String> values = new HashMap<>();
            StringBuilder sb = new StringBuilder();

            switch (event) {
                case RICHIESTA_REGISTRATA:
                    RichiestaOrdine richiesta = (RichiestaOrdine) obj;

                    datamodel = new HashMap<>();
                    datamodel.put("richiesta", richiesta);
                    htmlresult = result.activate("pdfRichiesta.ftl", datamodel, new StringWriter());

                    sb.append("Richiesta: ").append(richiesta.getCodiceRichiesta()).append("\n");
                    sb.append("Data: ").append(richiesta.getData()).append("\n");
                    sb.append("Ordinante: ").append(richiesta.getOrdinante().getEmail()).append("\n");
                    sb.append("Note: ").append(richiesta.getNote()).append("\n");
                    sb.append("Caratteristiche: ").append("\n");
                    for (CaratteristicaRichiesta cr : richiesta.getCaratteristicheRichiesta()) {
                        sb.append(cr.getCaratteristica().getNome()).append(": ").append(cr.getValore()).append("\n");
                    }

                    values.put("filename", "richiesta_" + richiesta.getCodiceRichiesta());
                    values.put("subject", "Richiesta: " + richiesta.getCodiceRichiesta());
                    values.put("text", sb.toString());

                    newEmailSender(context, to, htmlresult, values);
                    break;
                case RICHIESTA_PRESA_IN_CARICO:
                    RichiestaPresaInCarico richiestaPresaInCarico = (RichiestaPresaInCarico) obj;

                    datamodel.put("richiestaPresaInCarico", richiestaPresaInCarico);
                    htmlresult = result.activate("pdfRichiestaPresaInCarico.ftl", datamodel, new StringWriter());

                    sb.append("Richiesta presa in carico").append("\n");
                    sb.append("Codice richiesta: ").append(richiestaPresaInCarico.getRichiestaOrdine().getCodiceRichiesta()).append("\n");
                    sb.append("Tecnico: ").append(richiestaPresaInCarico.getTecnico().getEmail()).append("\n");
                    sb.append("\n");
                    sb.append("Richiesta: ").append(richiestaPresaInCarico.getRichiestaOrdine().getCodiceRichiesta()).append("\n");
                    sb.append("Data: ").append(richiestaPresaInCarico.getRichiestaOrdine().getData()).append("\n");
                    sb.append("Ordinante: ").append(richiestaPresaInCarico.getRichiestaOrdine().getOrdinante().getEmail()).append("\n");
                    sb.append("Note: ").append(richiestaPresaInCarico.getRichiestaOrdine().getNote()).append("\n");
                    sb.append("Caratteristiche: ").append("\n");
                    for (CaratteristicaRichiesta ccv : richiestaPresaInCarico.getRichiestaOrdine().getCaratteristicheRichiesta()) {
                        sb.append(ccv.getCaratteristica().getNome()).append(": ").append(ccv.getValore()).append("\n");
                    }

                    values.put("filename", "richiesta_presa_in_carico_" + richiestaPresaInCarico.getRichiestaOrdine().getCodiceRichiesta());
                    values.put("subject", "Richiesta presa in carico: " + richiestaPresaInCarico.getRichiestaOrdine().getCodiceRichiesta());
                    values.put("text", sb.toString());

                    newEmailSender(context, to, htmlresult, values);

                    break;
                case PROPOSTA_INSERITA:
                    PropostaAcquisto proposta = (PropostaAcquisto) obj;

                    datamodel.put("proposta", proposta);
                    htmlresult = result.activate("pdfProposta.ftl", datamodel, new StringWriter());

                    sb.append("Proposta creata con successo: ").append(proposta.getCodiceProdotto()).append("\n");
                    sb.append("Produttore: ").append(proposta.getProduttore()).append("\n");
                    sb.append("Nome Prodotto: ").append(proposta.getNomeProdotto()).append("\n");
                    sb.append("Prezzo: ").append(proposta.getPrezzo()).append("\n");
                    sb.append("Note: ").append(proposta.getNote()).append("\n");
                    sb.append("Stato Proposta: ").append(proposta.getStatoProposta()).append("\n");
                    sb.append("\n");
                    sb.append("Richiesta presa in carico da: ").append(proposta.getRichiestaPresaInCarico().getTecnico().getEmail()).append("\n");
                    sb.append("\n");
                    sb.append("Richiesta: ").append(proposta.getRichiestaPresaInCarico().getRichiestaOrdine().getCodiceRichiesta()).append("\n");
                    sb.append("Data: ").append(proposta.getRichiestaPresaInCarico().getRichiestaOrdine().getData()).append("\n");
                    sb.append("Ordinante: ").append(proposta.getRichiestaPresaInCarico().getRichiestaOrdine().getOrdinante().getEmail()).append("\n");
                    sb.append("Note: ").append(proposta.getRichiestaPresaInCarico().getRichiestaOrdine().getNote()).append("\n");
                    sb.append("Caratteristiche: ").append("\n");
                    for (CaratteristicaRichiesta cr : proposta.getRichiestaPresaInCarico().getRichiestaOrdine().getCaratteristicheRichiesta()) {
                        sb.append(cr.getCaratteristica().getNome()).append(": ").append(cr.getValore()).append("\n");
                    }

                    values.put("filename", "proposta_" + proposta.getCodiceProdotto());
                    values.put("subject", "Proposta: " + proposta.getCodiceProdotto());
                    values.put("text", sb.toString());

                    newEmailSender(context, to, htmlresult, values);
                    break;
                case PROPOSTA_ACCETTATA:
                    PropostaAcquisto propostaAccettata = (PropostaAcquisto) obj;

                    datamodel.put("proposta", propostaAccettata);
                    htmlresult = result.activate("pdfProposta.ftl", datamodel, new StringWriter());

                    sb.append("PROPOSTA ACCETTATA").append("\n\n");
                    sb.append("Proposta: ").append(propostaAccettata.getCodiceProdotto()).append("\n");
                    sb.append("Produttore: ").append(propostaAccettata.getProduttore()).append("\n");
                    sb.append("Nome Prodotto: ").append(propostaAccettata.getNomeProdotto()).append("\n");
                    sb.append("Prezzo: ").append(propostaAccettata.getPrezzo()).append("\n");
                    sb.append("Note: ").append(propostaAccettata.getNote()).append("\n");
                    sb.append("Stato Proposta: ").append(propostaAccettata.getStatoProposta()).append("\n");
                    sb.append("\n");
                    sb.append("Richiesta presa in carico da: ").append(propostaAccettata.getRichiestaPresaInCarico().getTecnico().getEmail()).append("\n");
                    sb.append("\n");
                    sb.append("Richiesta: ").append(propostaAccettata.getRichiestaPresaInCarico().getRichiestaOrdine().getCodiceRichiesta()).append("\n");
                    sb.append("Data: ").append(propostaAccettata.getRichiestaPresaInCarico().getRichiestaOrdine().getData()).append("\n");
                    sb.append("Ordinante: ").append(propostaAccettata.getRichiestaPresaInCarico().getRichiestaOrdine().getOrdinante().getEmail()).append("\n");
                    sb.append("Note: ").append(propostaAccettata.getRichiestaPresaInCarico().getRichiestaOrdine().getNote()).append("\n");
                    sb.append("Caratteristiche: ").append("\n");
                    for (CaratteristicaRichiesta cr : propostaAccettata.getRichiestaPresaInCarico().getRichiestaOrdine().getCaratteristicheRichiesta()) {
                        sb.append(cr.getCaratteristica().getNome()).append(": ").append(cr.getValore()).append("\n");
                    }

                    values.put("filename", "proposta_accettata_" + propostaAccettata.getCodiceProdotto());
                    values.put("subject", "Proposta accettata: " + propostaAccettata.getCodiceProdotto());
                    values.put("text", sb.toString());

                    newEmailSender(context, to, htmlresult, values);
                    break;
                case PROPOSTA_RIFIUTATA:
                    PropostaAcquisto propostaRifiutata = (PropostaAcquisto) obj;

                    datamodel.put("proposta", propostaRifiutata);
                    htmlresult = result.activate("pdfProposta.ftl", datamodel, new StringWriter());

                    sb.append("PROPOSTA RIFIUTATA").append("\n\n");
                    sb.append("Proposta: ").append(propostaRifiutata.getCodiceProdotto()).append("\n");
                    sb.append("Produttore: ").append(propostaRifiutata.getProduttore()).append("\n");
                    sb.append("Nome Prodotto: ").append(propostaRifiutata.getNomeProdotto()).append("\n");
                    sb.append("Prezzo: ").append(propostaRifiutata.getPrezzo()).append("\n");
                    sb.append("Note: ").append(propostaRifiutata.getNote()).append("\n");
                    sb.append("Stato Proposta: ").append(propostaRifiutata.getStatoProposta()).append("\n");
                    sb.append("Motivazione: ").append(propostaRifiutata.getMotivazione()).append("\n");
                    sb.append("\n");
                    sb.append("Richiesta presa in carico da: ").append(propostaRifiutata.getRichiestaPresaInCarico().getTecnico().getEmail()).append("\n");
                    sb.append("\n");
                    sb.append("Richiesta: ").append(propostaRifiutata.getRichiestaPresaInCarico().getRichiestaOrdine().getCodiceRichiesta()).append("\n");
                    sb.append("Data: ").append(propostaRifiutata.getRichiestaPresaInCarico().getRichiestaOrdine().getData()).append("\n");
                    sb.append("Ordinante: ").append(propostaRifiutata.getRichiestaPresaInCarico().getRichiestaOrdine().getOrdinante().getEmail()).append("\n");
                    sb.append("Note: ").append(propostaRifiutata.getRichiestaPresaInCarico().getRichiestaOrdine().getNote()).append("\n");
                    sb.append("Caratteristiche: ").append("\n");
                    for (CaratteristicaRichiesta cr : propostaRifiutata.getRichiestaPresaInCarico().getRichiestaOrdine().getCaratteristicheRichiesta()) {
                        sb.append(cr.getCaratteristica().getNome()).append(": ").append(cr.getValore()).append("\n");
                    }

                    values.put("filename", "proposta_rifiutata_" + propostaRifiutata.getCodiceProdotto());
                    values.put("subject", "Proposta rifiutata: " + propostaRifiutata.getCodiceProdotto());
                    values.put("text", sb.toString());

                    newEmailSender(context, to, htmlresult, values);
                    break;

                case ORDINE_CREATO:
                    Ordine ordine = (Ordine) obj;
                    datamodel = new HashMap<>();
                    datamodel.put("ordine", ordine);
                    datamodel.put("proposta", ordine.getPropostaAcquisto());
                    htmlresult = result.activate("pdfOrdineCreato.ftl", datamodel, new StringWriter());


                    sb.append("Ordine creato").append("\n");
                    sb.append("Stato ordine: ").append(ordine.getStatoConsegna()).append("\n");
                    sb.append("Tecnico Referente: ").append(ordine.getTecnico().getEmail()).append("\n\n");
                    sb.append("Riepilogo Proposta Accettata:").append("\n");
                    sb.append("Codice proposta: ").append(ordine.getPropostaAcquisto().getCodiceProdotto()).append("\n");
                    sb.append("Prodotto: ").append(ordine.getPropostaAcquisto().getNomeProdotto()).append("\n");
                    sb.append("Note: ").append(ordine.getPropostaAcquisto().getNote()).append("\n");
                    sb.append("URL: ").append(ordine.getPropostaAcquisto().getURL()).append("\n");
                    sb.append("Prezzo: ").append(ordine.getPropostaAcquisto().getPrezzo()).append("\n");
                    sb.append("Produttore: ").append(ordine.getPropostaAcquisto().getProduttore()).append("\n");
                    sb.append("Stato Proposta: ").append(ordine.getPropostaAcquisto().getStatoProposta()).append("\n");
                    sb.append("\n");
                    sb.append("Richiesta presa in carico da: ").append(ordine.getPropostaAcquisto().getRichiestaPresaInCarico().getTecnico().getEmail()).append("\n");
                    sb.append("\n");
                    sb.append("Richiesta: ").append(ordine.getPropostaAcquisto().getRichiestaPresaInCarico().getRichiestaOrdine().getCodiceRichiesta()).append("\n");
                    sb.append("Data: ").append(ordine.getPropostaAcquisto().getRichiestaPresaInCarico().getRichiestaOrdine().getData()).append("\n");
                    sb.append("Ordinante: ").append(ordine.getPropostaAcquisto().getRichiestaPresaInCarico().getRichiestaOrdine().getOrdinante().getEmail()).append("\n");
                    sb.append("Note: ").append(ordine.getPropostaAcquisto().getRichiestaPresaInCarico().getRichiestaOrdine().getNote()).append("\n");
                    sb.append("Caratteristiche: ").append("\n");
                    for (CaratteristicaRichiesta cr : ordine.getPropostaAcquisto().getRichiestaPresaInCarico().getRichiestaOrdine().getCaratteristicheRichiesta()) {
                        sb.append(cr.getCaratteristica().getNome()).append(": ").append(cr.getValore()).append("\n");
                    }


                    values.put("filename", "ordine_creato_" + ordine.getPropostaAcquisto().getCodiceProdotto());
                    values.put("subject", "Ordine creato: " + ordine.getPropostaAcquisto().getCodiceProdotto());
                    values.put("text", sb.toString());


                    newEmailSender(context, to, htmlresult, values);
                    break;
                case ORDINE_CHIUSO:
                    datamodel = new HashMap<>();
                    Ordine ordineChiuso = (Ordine) obj;

                    datamodel.put("ordine", ordineChiuso);
                    htmlresult = result.activate("pdfRichiestaChiusa.ftl", datamodel, new StringWriter());


                    sb.append("Ordine chiuso").append("\n");
                    sb.append("Stato ordine: ").append(ordineChiuso.getStatoConsegna()).append("\n");
                    sb.append("Tecnico Referente: ").append(ordineChiuso.getTecnico().getEmail()).append("\n\n");
                    sb.append("Riepilogo Proposta Accettata:").append("\n");
                    sb.append("Codice proposta: ").append(ordineChiuso.getPropostaAcquisto().getCodiceProdotto()).append("\n");
                    sb.append("Prodotto: ").append(ordineChiuso.getPropostaAcquisto().getNomeProdotto()).append("\n");
                    sb.append("Note: ").append(ordineChiuso.getPropostaAcquisto().getNote()).append("\n");
                    sb.append("URL: ").append(ordineChiuso.getPropostaAcquisto().getURL()).append("\n");
                    sb.append("Prezzo: ").append(ordineChiuso.getPropostaAcquisto().getPrezzo()).append("\n");
                    sb.append("Produttore: ").append(ordineChiuso.getPropostaAcquisto().getProduttore()).append("\n");
                    sb.append("Stato Proposta: ").append(ordineChiuso.getPropostaAcquisto().getStatoProposta()).append("\n");
                    sb.append("\n");
                    sb.append("Richiesta presa in carico da: ").append(ordineChiuso.getPropostaAcquisto().getRichiestaPresaInCarico().getTecnico().getEmail()).append("\n");
                    sb.append("\n");
                    sb.append("Richiesta: ").append(ordineChiuso.getPropostaAcquisto().getRichiestaPresaInCarico().getRichiestaOrdine().getCodiceRichiesta()).append("\n");
                    sb.append("Data: ").append(ordineChiuso.getPropostaAcquisto().getRichiestaPresaInCarico().getRichiestaOrdine().getData()).append("\n");
                    sb.append("Ordinante: ").append(ordineChiuso.getPropostaAcquisto().getRichiestaPresaInCarico().getRichiestaOrdine().getOrdinante().getEmail()).append("\n");
                    sb.append("Note: ").append(ordineChiuso.getPropostaAcquisto().getRichiestaPresaInCarico().getRichiestaOrdine().getNote()).append("\n");
                    sb.append("Caratteristiche: ").append("\n");
                    for (CaratteristicaRichiesta cr : ordineChiuso.getPropostaAcquisto().getRichiestaPresaInCarico().getRichiestaOrdine().getCaratteristicheRichiesta()) {
                        sb.append(cr.getCaratteristica().getNome()).append(": ").append(cr.getValore()).append("\n");
                    }

                    values.put("filename", "ordine_chiuso_" + ordineChiuso.getPropostaAcquisto().getCodiceProdotto());
                    values.put("subject", "Ordine chiuso: " + ordineChiuso.getPropostaAcquisto().getCodiceProdotto());
                    values.put("text", sb.toString());

                    newEmailSender(context, to, htmlresult, values);

                    break;
            }
        } catch (TemplateManagerException e) {
            Logger.getLogger(EmailSender.class.getName()).severe(e.getMessage());

        }

    }

    private void newEmailSender(ServletContext context, String to, String htmlresult, Map<String, String> values) {

        new Thread(() -> {
            String outputPdf = context.getRealPath("/WEB-INF/") + values.get("filename") + ".pdf";

            try {

                // Create a new document
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(htmlresult);
                renderer.layout();

                // Write PDF to file
                FileOutputStream fos = new FileOutputStream(outputPdf);
                renderer.createPDF(fos);
                fos.close();

                // Creazione della sessione di posta
                Session session = Session.getInstance(properties, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailFrom, password);
                    }
                });

                // Messaggio da allegare all'email
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(this.emailFrom)); // Inserire il proprio indirizzo email
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // Inserire il destinatario
                message.setSubject(values.get("subject"));

                // Creazione del corpo del messaggio
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(values.get("text"));

                // Creazione della parte dell'allegato
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(outputPdf);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(values.get("filename") + ".pdf");

                // Composizione del messaggio
                MimeMultipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                multipart.addBodyPart(attachmentPart);

                // Impostazione del contenuto del messaggio
                message.setContent(multipart);

                // Invio del messaggio
                Transport.send(message);

                Logger.getLogger(EmailSender.class.getName()).info("Email inviata con successo!");

                // Cancella il file PDF dopo l'invio dell'email
                File pdfFile = new File(outputPdf);
                if (pdfFile.exists()) {
                    if (pdfFile.delete()) {
                        Logger.getLogger(EmailSender.class.getName()).info("File PDF eliminato con successo.");
                    } else {
                        Logger.getLogger(EmailSender.class.getName()).warning("Impossibile eliminare il file PDF.");
                    }
                } else {
                    Logger.getLogger(EmailSender.class.getName()).warning("File PDF non trovato durante la cancellazione.");
                }

            } catch (DocumentException | MessagingException | IOException e) {
                Logger.getLogger(EmailSender.class.getName()).severe(e.getMessage());
            }
        }).start();
    }
}
