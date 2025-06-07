/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.application.WebmarketDataLayer;
import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.data.model.Ordine;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cdidi
 */

public class OStoricoController extends ApplicationBaseController {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ruoliAutorizzati = List.of(Ruolo.ORDINANTE);
    }

    private void renderTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            Map<String, String[]> parameterMap = request.getParameterMap();

            try {

                HttpSession session = SecurityHelpers.checkSession(request);
                Ordinante ordinante = dl.getOrdinanteDAO().getOrdinanteByEmail(String.valueOf(session.getAttribute("email")));

                if (parameterMap.containsKey("id")) {
                    datamodel.put("ordini", List.of(dl.getOrdineDAO().getOrdineInStorico(Integer.parseInt(parameterMap.get("id")[0]))));
                    datamodel.put("risposte", List.of(Ordine.Risposta.ACCETTATO, Ordine.Risposta.RESPINTO, Ordine.Risposta.RIFIUTATO));
                    datamodel.put("id", parameterMap.get("id")[0]);
                } else if (parameterMap.containsKey("page")) {
                    Integer page = Integer.parseInt(parameterMap.get("page")[0]);
                    datamodel.put("risposte", List.of(Ordine.Risposta.ACCETTATO, Ordine.Risposta.RESPINTO, Ordine.Risposta.RIFIUTATO));
                    datamodel.put("ordini", dl.getOrdineDAO().getStorico(ordinante, page));
                    datamodel.put("page", page);
                } else {
                    datamodel.put("risposte", List.of(Ordine.Risposta.ACCETTATO, Ordine.Risposta.RESPINTO, Ordine.Risposta.RIFIUTATO));
                    datamodel.put("ordini", dl.getOrdineDAO().getStorico(ordinante, 0));
                    datamodel.put("page", 0);
                }
            } catch (DataException e) {
                handleError(e, request, response);
            }

            result.activate("oStorico.ftl.html", datamodel, request, response);
        } catch (TemplateManagerException e) {
            handleError(e, request, response);
        }
    }

    private void renderAddrisposta(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();

            Ordinante ordinante = dl.getOrdinanteDAO().getOrdinanteByEmail((String) SecurityHelpers.checkSession(request).getAttribute("email"));

            if (parameterMap.containsKey("page")) {
                int ordineId = Integer.parseInt(parameterMap.get("id")[0]);
                int page = Integer.parseInt(parameterMap.get("page")[0]);

                Ordine ordine = dl.getOrdineDAO().getOrdine(ordineId);

                datamodel.put("page", page);
                datamodel.put("visibilityModify", "flex");
                datamodel.put("ordineDaRispondere", ordine);
                datamodel.put("risposte", List.of(Ordine.Risposta.ACCETTATO, Ordine.Risposta.RESPINTO, Ordine.Risposta.RIFIUTATO));
                datamodel.put("ordini", dl.getOrdineDAO().getStorico(ordinante, page));
            } else if (parameterMap.containsKey("id")) {

                int ordineId = Integer.parseInt(parameterMap.get("id")[0]);

                Ordine ordine = dl.getOrdineDAO().getOrdineInStorico(ordineId);

                datamodel.put("visibilityModify", "flex");
                datamodel.put("ordineDaRispondere", ordine);
                datamodel.put("risposte", List.of(Ordine.Risposta.ACCETTATO, Ordine.Risposta.RESPINTO, Ordine.Risposta.RIFIUTATO));
                datamodel.put("ordini", List.of(ordine));
                datamodel.put("id", ordineId);
            } else {
                //Questo perchè potrebbe cancellare dalla URL il parametro id o page
                datamodel.put("page", 0);
                datamodel.put("risposte", List.of(Ordine.Risposta.ACCETTATO, Ordine.Risposta.RESPINTO, Ordine.Risposta.RIFIUTATO));
                datamodel.put("ordini", dl.getOrdineDAO().getStorico(ordinante, 0));
            }

            result.activate("oStorico.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

    private void handleAddrisposta(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();

            Ordinante ordinante = dl.getOrdinanteDAO().getOrdinanteByEmail((String) SecurityHelpers.checkSession(request).getAttribute("email"));

            if (parameterMap.containsKey("page")) {
                int ordineId = Integer.parseInt(parameterMap.get("id")[0]);
                int page = Integer.parseInt(parameterMap.get("page")[0]);
                String risposta = parameterMap.get("risposta")[0];

                Ordine ordine = dl.getOrdineDAO().getOrdine(ordineId);

                ordine.setRisposta(risposta);

                dl.getOrdineDAO().storeOrdine(ordine);

                EmailSender sender = (EmailSender) getServletContext().getAttribute("emailsender");
                String emailTecnico = ordine.getTecnico().getEmail();
                sender.sendPDFWithEmail(getServletContext(), emailTecnico, ordine, EmailSender.Event.ORDINE_CHIUSO);

                datamodel.put("page", page);
                datamodel.put("success", "1");
                datamodel.put("ordini", dl.getOrdineDAO().getStorico(ordinante, page));
                datamodel.put("risposte", List.of(Ordine.Risposta.ACCETTATO, Ordine.Risposta.RESPINTO, Ordine.Risposta.RIFIUTATO));
            } else if (parameterMap.containsKey("id")) {

                int ordineId = Integer.parseInt(parameterMap.get("id")[0]);
                String risposta = parameterMap.get("risposta")[0];

                Ordine ordine = dl.getOrdineDAO().getOrdineInStorico(ordineId);

                ordine.setRisposta(risposta);

                dl.getOrdineDAO().storeOrdine(ordine);

                EmailSender sender = (EmailSender) getServletContext().getAttribute("emailsender");
                String emailTecnico = ordine.getTecnico().getEmail();
                sender.sendPDFWithEmail(getServletContext(), emailTecnico, ordine, EmailSender.Event.ORDINE_CHIUSO);

                datamodel.put("ordini", List.of(ordine));
                datamodel.put("id", ordineId);
                datamodel.put("success", "1");
                datamodel.put("risposte", List.of(Ordine.Risposta.ACCETTATO, Ordine.Risposta.RESPINTO, Ordine.Risposta.RIFIUTATO));
            } else {
                //Questo perchè potrebbe cancellare dalla URL il parametro id o page
                datamodel.put("page", 0);
                datamodel.put("ordini", dl.getOrdineDAO().getStorico(ordinante, 0));
                datamodel.put("risposte", List.of(Ordine.Risposta.ACCETTATO, Ordine.Risposta.RESPINTO, Ordine.Risposta.RIFIUTATO));
            }

            result.activate("oStorico.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }


    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();

            if (parameterMap.containsKey("render")) {
                //Se l'utente richiede qualche elemento non renderizzato
                if ("Aggiungi".equals(parameterMap.get("render")[0])) {
                    //Se devo renderizzare il menù per il rifiuto della proposta
                    renderAddrisposta(request, response);
                } else renderTemplate(request, response);
            } else if (parameterMap.containsKey("action")) {
                // Se l'utente richiede un'azione
                if ("Aggiungi".equals(parameterMap.get("action")[0])) {
                    // Se devo effettuare il rifiuto
                    handleAddrisposta(request, response);
                } else renderTemplate(request, response);
            } else {
                renderTemplate(request, response);
            }
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
}
