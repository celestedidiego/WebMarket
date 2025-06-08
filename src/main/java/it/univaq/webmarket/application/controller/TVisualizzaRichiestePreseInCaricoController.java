/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.application.WebmarketDataLayer;
import it.univaq.webmarket.data.model.PropostaAcquisto;
import it.univaq.webmarket.data.model.RichiestaOrdine;
import it.univaq.webmarket.data.model.RichiestaPresaInCarico;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.result.TemplateManagerException;
import it.univaq.webmarket.framework.result.TemplateResult;
import it.univaq.webmarket.framework.security.SecurityHelpers;
import it.univaq.webmarket.framework.utils.EmailSender;
import it.univaq.webmarket.framework.utils.Ruolo;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cdidi
 */

public class TVisualizzaRichiestePreseInCaricoController extends ApplicationBaseController {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ruoliAutorizzati = List.of(Ruolo.TECNICO);
    }

    private void renderTemplate(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        TemplateResult result = new TemplateResult(getServletContext());
        Map<String, Object> datamodel = new HashMap<>();
        WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
        Map<String, String[]> parameterMap = request.getParameterMap();

        try {
            HttpSession session = SecurityHelpers.checkSession(request);
            Tecnico tecnico = dl.getTecnicoDAO().getTecnicoByEmail(String.valueOf(session.getAttribute("email")));

            if (parameterMap.containsKey("id")) {
                datamodel.put("richiestePreseInCarico", List.of(dl.getRichiestaPresaInCaricoDAO().getRichiestaPresaInCarico(Integer.parseInt(parameterMap.get("id")[0]))));
                datamodel.put("id", parameterMap.get("id")[0]);
            } else if (parameterMap.containsKey("page")) {
                Integer page = Integer.parseInt(parameterMap.get("page")[0]);
                datamodel.put("richiestePreseInCarico", dl.getRichiestaPresaInCaricoDAO().getAllRichiestePresaInCaricoByTecnico(tecnico, page));
                datamodel.put("page", page);
            } else {
                datamodel.put("richiestePreseInCarico", dl.getRichiestaPresaInCaricoDAO().getAllRichiestePresaInCaricoByTecnico(tecnico, 0));
                datamodel.put("page", 0);
            }
        } catch (DataException e) {
            handleError(e, request, response);
        }

        result.activate("tRichiestePreseInCarico.ftl.html", datamodel, request, response);
    }

    private void renderCaratteristiche(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException{
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();

            Tecnico tecnico = dl.getTecnicoDAO().getTecnicoByEmail((String) SecurityHelpers.checkSession(request).getAttribute("email"));

            if(parameterMap.containsKey("page")) {
                int richiestaPresaInCaricoId = Integer.parseInt(parameterMap.get("id")[0]);
                int page = Integer.parseInt(parameterMap.get("page")[0]);

                RichiestaPresaInCarico richiestaPresaInCarico = dl.getRichiestaPresaInCaricoDAO().getRichiestaPresaInCarico(richiestaPresaInCaricoId);

                datamodel.put("page", page);
                datamodel.put("visibilityCaratteristiche", "flex");
                datamodel.put("caratteristiche", richiestaPresaInCarico.getRichiestaOrdine().getCaratteristicheRichiesta());
                datamodel.put("richiestePreseInCarico", dl.getRichiestaPresaInCaricoDAO().getAllRichiestePresaInCaricoByTecnico(tecnico, page));
            } else if(parameterMap.containsKey("id")) {

                int richiestaPresaInCaricoId = Integer.parseInt(parameterMap.get("id")[0]);

                RichiestaPresaInCarico richiestaPresaInCarico = dl.getRichiestaPresaInCaricoDAO().getRichiestaPresaInCarico(richiestaPresaInCaricoId);

                datamodel.put("visibilityCaratteristiche", "flex");
                datamodel.put("caratteristiche", richiestaPresaInCarico.getRichiestaOrdine().getCaratteristicheRichiesta());
                datamodel.put("richiestePreseInCarico", List.of(richiestaPresaInCarico));
                datamodel.put("id", richiestaPresaInCaricoId);
            } else {
                //Questo perchè potrebbe cancellare dalla URL il parametro id o page
                datamodel.put("page", 0);
                datamodel.put("richiestePreseInCarico", dl.getRichiestaPresaInCaricoDAO().getAllRichiestePresaInCaricoByTecnico(tecnico, 0));
            }

            result.activate("tRichiestePreseInCarico.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

    private void renderProposta(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();

            Tecnico tecnico = dl.getTecnicoDAO().getTecnicoByEmail((String) SecurityHelpers.checkSession(request).getAttribute("email"));

            if(parameterMap.containsKey("page")) {
                int richiestaPresaInCaricoId = Integer.parseInt(parameterMap.get("id")[0]);
                int page = Integer.parseInt(parameterMap.get("page")[0]);

                RichiestaPresaInCarico richiestaPresaInCarico = dl.getRichiestaPresaInCaricoDAO().getRichiestaPresaInCarico(richiestaPresaInCaricoId);

                datamodel.put("page", page);
                datamodel.put("visibilityProposta", "flex");
                datamodel.put("richiestePreseInCarico", dl.getRichiestaPresaInCaricoDAO().getAllRichiestePresaInCaricoByTecnico(tecnico, page));
                datamodel.put("richiestaPresaInCarico", richiestaPresaInCarico);
            } else if(parameterMap.containsKey("id")) {

                int richiestaPresaInCaricoId = Integer.parseInt(parameterMap.get("id")[0]);

                RichiestaPresaInCarico richiestaPresaInCarico = dl.getRichiestaPresaInCaricoDAO().getRichiestaPresaInCarico(richiestaPresaInCaricoId);

                datamodel.put("visibilityProposta", "flex");
                datamodel.put("richiestePreseInCarico", List.of(richiestaPresaInCarico));
                datamodel.put("richiestaPresaInCarico", richiestaPresaInCarico);
                datamodel.put("id", richiestaPresaInCaricoId);
            } else {
                //Questo perchè potrebbe cancellare dalla URL il parametro id o page
                datamodel.put("page", 0);
                datamodel.put("richiestePreseInCarico", dl.getRichiestaPresaInCaricoDAO().getAllRichiestePresaInCaricoByTecnico(tecnico, 0));
            }

            result.activate("tRichiestePreseInCarico.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

    private void handleProposta(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();

            Tecnico tecnico = dl.getTecnicoDAO().getTecnicoByEmail((String) SecurityHelpers.checkSession(request).getAttribute("email"));

            if(parameterMap.containsKey("page")) {

                    int richiestaPresaInCaricoId = Integer.parseInt(parameterMap.get("id")[0]);
                    int page = Integer.parseInt(parameterMap.get("page")[0]);

                try {
                    RichiestaPresaInCarico richiestaPresaInCarico = dl.getRichiestaPresaInCaricoDAO().getRichiestaPresaInCarico(richiestaPresaInCaricoId);

                    String produttore = parameterMap.get("produttore")[0];
                    Float prezzo = Float.parseFloat(parameterMap.get("prezzo")[0]);
                    String nomeProdotto = parameterMap.get("nome_prodotto")[0];
                    String URL = new URL(parameterMap.get("URL")[0]).toString(); // Check if URL is valid (throws MalformedURLException if not valid)
                    String note = parameterMap.get("note")[0];

                    PropostaAcquisto proposta = dl.getPropostaAcquistoDAO().createPropostaAcquisto();
                    proposta.setProduttore(produttore);
                    proposta.setPrezzo(prezzo);
                    proposta.setNomeProdotto(nomeProdotto);
                    proposta.setURL(URL);
                    proposta.setNote(note);
                    proposta.setStatoProposta(PropostaAcquisto.StatoProposta.IN_ATTESA);
                    proposta.setRichiestaPresaInCarico(richiestaPresaInCarico);

                    dl.getPropostaAcquistoDAO().storePropostaAcquisto(proposta);

                    EmailSender sender = (EmailSender) getServletContext().getAttribute("emailsender");
                    sender.sendPDFWithEmail(getServletContext(), proposta.getRichiestaPresaInCarico().getRichiestaOrdine().getOrdinante().getEmail(), proposta, EmailSender.Event.PROPOSTA_INSERITA);

                    datamodel.put("page", page);
                    datamodel.put("success", "1");
                    datamodel.put("richiestePreseInCarico", dl.getRichiestaPresaInCaricoDAO().getAllRichiestePresaInCaricoByTecnico(tecnico, page));
                } catch(NumberFormatException e) {
                    datamodel.put("page", page);
                    datamodel.put("richiestePreseInCarico", dl.getRichiestaPresaInCaricoDAO().getAllRichiestePresaInCaricoByTecnico(tecnico, page));
                    datamodel.put("success", "-1");
                } catch (MalformedURLException e) {
                    datamodel.put("page", page);
                    datamodel.put("richiestePreseInCarico", dl.getRichiestaPresaInCaricoDAO().getAllRichiestePresaInCaricoByTecnico(tecnico, page));
                    datamodel.put("success", "-2");
                }
            } else if(parameterMap.containsKey("id")) {

                int richiestaPresaInCaricoId = Integer.parseInt(parameterMap.get("id")[0]);

                RichiestaPresaInCarico richiestaPresaInCarico = dl.getRichiestaPresaInCaricoDAO().getRichiestaPresaInCarico(richiestaPresaInCaricoId);

                try {

                    String produttore = parameterMap.get("produttore")[0];
                    Float prezzo = Float.parseFloat(parameterMap.get("prezzo")[0]);
                    String nomeProdotto = parameterMap.get("nome_prodotto")[0];
                    String URL = new URL(parameterMap.get("URL")[0]).toString(); // Check if URL is valid (throws MalformedURLException if not valid)
                    String note = parameterMap.get("note")[0];

                    PropostaAcquisto proposta = dl.getPropostaAcquistoDAO().createPropostaAcquisto();
                    proposta.setProduttore(produttore);
                    proposta.setPrezzo(prezzo);
                    proposta.setNomeProdotto(nomeProdotto);
                    proposta.setURL(URL);
                    proposta.setNote(note);
                    proposta.setStatoProposta(PropostaAcquisto.StatoProposta.IN_ATTESA);
                    proposta.setRichiestaPresaInCarico(richiestaPresaInCarico);

                    dl.getPropostaAcquistoDAO().storePropostaAcquisto(proposta);

                    EmailSender sender = (EmailSender) getServletContext().getAttribute("emailsender");
                    sender.sendPDFWithEmail(getServletContext(), proposta.getRichiestaPresaInCarico().getRichiestaOrdine().getOrdinante().getEmail(), proposta, EmailSender.Event.PROPOSTA_INSERITA);


                    // Passo la lista vuota perchè una volta che ho creato la proposta, non è più
                    // una RichiestaPresaInCarico in attesa
                    datamodel.put("richiestePreseInCarico", List.of());
                    datamodel.put("id", richiestaPresaInCaricoId);
                    datamodel.put("success", "1");
                } catch(NumberFormatException e) {
                    datamodel.put("richiestePreseInCarico", List.of());
                    datamodel.put("id", richiestaPresaInCaricoId);
                    datamodel.put("success", "-1");
                } catch(MalformedURLException e) {
                    datamodel.put("richiestePreseInCarico", List.of());
                    datamodel.put("id", richiestaPresaInCaricoId);
                    datamodel.put("success", "-2");
                }
            } else {
                //Questo perchè potrebbe cancellare dalla URL il parametro id o page
                datamodel.put("page", 0);
                datamodel.put("richieste", dl.getRichiestaPresaInCaricoDAO().getAllRichiestePresaInCaricoByTecnico(tecnico, 0));
            }

            result.activate("tRichiestePreseInCarico.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();

            if (parameterMap.containsKey("render")) {
                switch (parameterMap.get("render")[0]) {
                    case "Caratteristiche":
                        renderCaratteristiche(request, response);
                        break;
                    case "Crea Proposta":
                        renderProposta(request, response);
                        break;
                    default:
                        renderTemplate(request, response);
                        break;
                }
            } else if (parameterMap.containsKey("action")) {

                if ("Crea".equals(parameterMap.get("action")[0])) {
                    handleProposta(request, response);
                } else {
                    renderTemplate(request, response);
                }
            } else {
                renderTemplate(request, response);
            }
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
}
