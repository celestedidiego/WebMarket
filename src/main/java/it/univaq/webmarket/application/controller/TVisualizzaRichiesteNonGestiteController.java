/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.application.WebmarketDataLayer;
import it.univaq.webmarket.data.model.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cdidi
 */

public class TVisualizzaRichiesteNonGestiteController extends ApplicationBaseController {
    
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
            if (parameterMap.containsKey("id")) {
                datamodel.put("richieste", List.of(dl.getRichiestaOrdineDAO().getRichiestaOrdine(Integer.parseInt(parameterMap.get("id")[0]))));
                datamodel.put("id", parameterMap.get("id")[0]);
            } else if (parameterMap.containsKey("page")) {
                Integer page = Integer.parseInt(parameterMap.get("page")[0]);
                datamodel.put("richieste", dl.getRichiestaOrdineDAO().getRichiesteNonGestite(page));
                datamodel.put("page", page);
            } else {
                datamodel.put("richieste", dl.getRichiestaOrdineDAO().getRichiesteNonGestite(0));
                datamodel.put("page", 0);
            }
        } catch (DataException e) {
            datamodel.put("richieste", List.of());
            handleError(e, request, response);
            return; // Interrompe l'esecuzione per evitare doppia risposta
        }

        result.activate("tPrendiInCarico.ftl.html", datamodel, request, response);
    }

    private void renderCaratteristiche(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException{
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();

            Tecnico tecnico = dl.getTecnicoDAO().getTecnicoByEmail((String) SecurityHelpers.checkSession(request).getAttribute("email"));

            if(parameterMap.containsKey("page")) {
                int richiestaId = Integer.parseInt(parameterMap.get("id")[0]);
                int page = Integer.parseInt(parameterMap.get("page")[0]);

                RichiestaOrdine richiesta = dl.getRichiestaOrdineDAO().getRichiestaOrdine(richiestaId);

                datamodel.put("page", page);
                datamodel.put("visibilityModify", "flex");
                datamodel.put("caratteristiche", richiesta.getCaratteristicheRichiesta());
                datamodel.put("richieste", dl.getRichiestaOrdineDAO().getRichiesteNonGestite(page));
            } else if(parameterMap.containsKey("id")) {

                int ordineId = Integer.parseInt(parameterMap.get("id")[0]);

                RichiestaOrdine richiesta = dl.getRichiestaOrdineDAO().getRichiestaOrdine(ordineId);

                datamodel.put("visibilityModify", "flex");
                datamodel.put("caratteristiche", richiesta.getCaratteristicheRichiesta());
                datamodel.put("richieste", List.of(richiesta));
                datamodel.put("id", ordineId);
            } else {
                //Questo perchè potrebbe cancellare dalla URL il parametro id o page
                datamodel.put("page", 0);
                datamodel.put("richieste", dl.getRichiestaOrdineDAO().getRichiesteNonGestite(0));
            }

            result.activate("tPrendiInCarico.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
            return;
        }
    }

    private void handlePrendiInConsegna(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();

            Tecnico tecnico = dl.getTecnicoDAO().getTecnicoByEmail((String) SecurityHelpers.checkSession(request).getAttribute("email"));

            if(parameterMap.containsKey("page")) {
                int richiestaId = Integer.parseInt(parameterMap.get("id")[0]);
                int page = Integer.parseInt(parameterMap.get("page")[0]);

                RichiestaOrdine richiesta = dl.getRichiestaOrdineDAO().getRichiestaOrdine(richiestaId);
                RichiestaPresaInCarico richiestaPresaInCarico = dl.getRichiestaPresaInCaricoDAO().createRichiestaPresaInCarico();

                richiestaPresaInCarico.setRichiestaOrdine(richiesta);
                richiestaPresaInCarico.setTecnico(tecnico);

                dl.getRichiestaPresaInCaricoDAO().storeRichiestaPresaInCarico(richiestaPresaInCarico);

                EmailSender sender = (EmailSender) getServletContext().getAttribute("emailsender");
                sender.sendPDFWithEmail(getServletContext(), richiesta.getOrdinante().getEmail(), richiestaPresaInCarico, EmailSender.Event.RICHIESTA_PRESA_IN_CARICO);

                datamodel.put("page", page);
                datamodel.put("success", "1");
                datamodel.put("richieste", dl.getRichiestaOrdineDAO().getRichiesteNonGestite(page));


            } else if(parameterMap.containsKey("id")) {

                int richiestaId = Integer.parseInt(parameterMap.get("id")[0]);

                RichiestaOrdine richiesta = dl.getRichiestaOrdineDAO().getRichiestaOrdine(richiestaId);
                RichiestaPresaInCarico richiestaPresaInCarico = dl.getRichiestaPresaInCaricoDAO().createRichiestaPresaInCarico();

                richiestaPresaInCarico.setRichiestaOrdine(richiesta);
                richiestaPresaInCarico.setTecnico(tecnico);

                dl.getRichiestaPresaInCaricoDAO().storeRichiestaPresaInCarico(richiestaPresaInCarico);

                EmailSender sender = (EmailSender) getServletContext().getAttribute("emailsender");
                sender.sendPDFWithEmail(getServletContext(), richiesta.getOrdinante().getEmail(), richiestaPresaInCarico, EmailSender.Event.RICHIESTA_PRESA_IN_CARICO);

                // Restituisco una lista vuota se effettivamente prendo in consegna questo ordine
                datamodel.put("richieste", List.of());
                datamodel.put("id", richiestaId);
                datamodel.put("success", "1");
            } else {
                //Questo perchè potrebbe cancellare dalla URL il parametro id o page
                datamodel.put("page", 0);
                datamodel.put("richieste", dl.getRichiestaOrdineDAO().getRichiesteNonGestite(0));
            }

            result.activate("tPrendiInCarico.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
            return;
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();

            if (parameterMap.containsKey("render")) {
                if (parameterMap.get("render")[0].equals("Caratteristiche")) {
                    renderCaratteristiche(request, response);
                } else {
                    renderTemplate(request, response);
                }
            } else if (parameterMap.containsKey("action")) {

                if ("Prendi in consegna".equals(parameterMap.get("action")[0])) {
                    handlePrendiInConsegna(request, response);
                } else {
                    renderTemplate(request, response);
                }
            } else {
                renderTemplate(request, response);
            }
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
            return;
        }
    }
}
