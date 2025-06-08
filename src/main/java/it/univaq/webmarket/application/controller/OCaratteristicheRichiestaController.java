/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.application.WebmarketDataLayer;
import it.univaq.webmarket.data.model.CaratteristicaRichiesta;
import it.univaq.webmarket.data.model.RichiestaOrdine;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.result.TemplateManagerException;
import it.univaq.webmarket.framework.result.TemplateResult;
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

public class OCaratteristicheRichiestaController extends ApplicationBaseController {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ruoliAutorizzati = List.of(Ruolo.ORDINANTE);
    }

    private void renderTemplate(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        TemplateResult result = new TemplateResult(getServletContext());
        Map<String, Object> datamodel = new HashMap<>();
        WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
        Map<String, String[]> parameterMap = request.getParameterMap();

        try {

            if (!parameterMap.containsKey("id_richiesta")) handleError("ID Richiesta not specified", request, response);

            int richiestaID = Integer.parseInt(parameterMap.get("id_richiesta")[0]);
            RichiestaOrdine richiesta = dl.getRichiestaOrdineDAO().getRichiestaOrdine(richiestaID);
            datamodel.put("caratteristiche", dl.getCaratteristicaDAO().getCaratteristicheRichiesta(richiesta));
            datamodel.put("richiesta", richiesta);
        } catch (DataException e) {
            handleError(e, request, response);
        }

        result.activate("caratteristicaRichiesta.ftl.html", datamodel, request, response);

    }

    private void renderModify(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();


            if (!parameterMap.containsKey("id")) handleError("ID Richiesta not specified", request, response);
            datamodel.put("visibilityModify", "flex");
            RichiestaOrdine richiesta = dl.getRichiestaOrdineDAO().getRichiestaOrdine(Integer.parseInt(parameterMap.get("id_richiesta")[0]));
            datamodel.put("richiesta", richiesta);
            datamodel.put("caratteristiche", dl.getCaratteristicaDAO().getCaratteristicheRichiesta(richiesta));
            int caratteristicaRichiestaID = Integer.parseInt(parameterMap.get("id")[0]);
            CaratteristicaRichiesta caratteristicaRichiesta = dl.getCaratteristicaDAO().getCaratteristicaRichiesta(caratteristicaRichiestaID);
            datamodel.put("caratteristicaRichiesta", caratteristicaRichiesta);

            result.activate("caratteristicaRichiesta.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

    private void handleModify(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();

            int caratteristicaRichiestaID = Integer.parseInt(parameterMap.get("id")[0]);
            int richiestaID = Integer.parseInt(parameterMap.get("id_richiesta")[0]);

            CaratteristicaRichiesta caratteristicaRichiesta = dl.getCaratteristicaDAO().getCaratteristicaRichiesta(caratteristicaRichiestaID);
            String valore = parameterMap.containsKey("valore") ? parameterMap.get("valore")[0] : "";

            if (!valore.equals(caratteristicaRichiesta.getValore())) {
                caratteristicaRichiesta.setValore(valore);
                datamodel.put("success", "1");
            } else {
                datamodel.put("success", "-1");
            }

            dl.getCaratteristicaDAO().storeCaratteristicaRichiesta(caratteristicaRichiesta, richiestaID);

            RichiestaOrdine richiesta = dl.getRichiestaOrdineDAO().getRichiestaOrdine(richiestaID);
            datamodel.put("caratteristiche", dl.getCaratteristicaDAO().getCaratteristicheRichiesta(richiesta));
            datamodel.put("richiesta", richiesta);

            result.activate("caratteristicaRichiesta.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Map<String, String[]> parameterMap = request.getParameterMap();

            int caratteristicaRichiestaId = Integer.parseInt(parameterMap.get("id")[0]);
            int richiestaId = Integer.parseInt(parameterMap.get("id_richiesta")[0]);

            RichiestaOrdine richiesta = dl.getRichiestaOrdineDAO().getRichiestaOrdine(richiestaId);
            CaratteristicaRichiesta caratteristicaRichiesta = dl.getCaratteristicaDAO().getCaratteristicaRichiesta(caratteristicaRichiestaId);

            dl.getCaratteristicaDAO().deleteCaratteristicaRichiesta(caratteristicaRichiesta);

            datamodel.put("richiesta", richiesta);
            datamodel.put("caratteristiche", dl.getCaratteristicaDAO().getCaratteristicheRichiesta(richiesta));
            datamodel.put("success", "2");

            result.activate("caratteristicaRichiesta.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

    private void renderInsert(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();

            //Se l'utente richiede qualche elemento non renderizzato
            if (parameterMap.containsKey("render")) {
                if ("Modifica".equals(parameterMap.get("render")[0])) {
                    renderModify(request, response);
                } else if("Aggiungi".equals(parameterMap.get("render")[0])) {
                    renderInsert(request, response);
                } else renderTemplate(request, response);

            } else if (parameterMap.containsKey("action")) {
                // Se l'utente richiede un'azione

                if ("Modifica".equals(parameterMap.get("action")[0])) {
                    handleModify(request, response);
                } else if ("Elimina".equals(request.getParameter("action"))) {
                    handleDelete(request, response);
                } else renderTemplate(request, response);
            } else {
                renderTemplate(request, response);
            }
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
}
