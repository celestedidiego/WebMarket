/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.application.WebmarketDataLayer;
import it.univaq.webmarket.data.model.Ordine;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.result.TemplateManagerException;
import it.univaq.webmarket.framework.result.TemplateResult;
import it.univaq.webmarket.framework.security.SecurityHelpers;
import it.univaq.webmarket.framework.utils.Ruolo;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cdidi
 */

public class TGestioneOrdiniController extends ApplicationBaseController {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ruoliAutorizzati = List.of(Ruolo.TECNICO);
    }

    private void renderGestioneOrdinePage(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        Map<String, Object> datamodel = new HashMap<>();
        WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
        try {

            Tecnico tecnico = dl.getTecnicoDAO().getTecnicoByEmail((String) SecurityHelpers.checkSession(request).getAttribute("email"));

            if (request.getParameter("page") != null) {
                Integer page = Integer.parseInt(request.getParameter("page"));
                datamodel.put("ordini", dl.getOrdineDAO().getAllOrdiniByTecnico(tecnico, page));
                datamodel.put("page", page);
            } else {
                datamodel.put("ordini", dl.getOrdineDAO().getAllOrdiniByTecnico(tecnico, 0));
                datamodel.put("page", 0);
            }
        } catch (DataException e) {
            handleError(e, request, response);
        }


        result.activate("tGestioneOrdini.ftl.html", datamodel, request, response);
    }


    private void handleModificaOrdineToInConsegna(HttpServletRequest request, HttpServletResponse response, Integer ordine_key) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Tecnico tecnico = dl.getTecnicoDAO().getTecnicoByEmail((String) SecurityHelpers.checkSession(request).getAttribute("email"));

            Ordine ordine = dl.getOrdineDAO().getOrdine(ordine_key);

            ordine.setStatoConsegna(Ordine.StatoConsegna.IN_CONSEGNA);

            ordine.setOrdinante(dl.getOrdinanteDAO().getOrdinante(ordine.getOrdinanteKey()));

            dl.getOrdineDAO().storeOrdine(ordine);

            datamodel.put("success", "1"); // Lo stato dell'ordine è: In Consegna.
            if (request.getParameter("page") != null) {
                Integer page = Integer.parseInt(request.getParameter("page"));
                datamodel.put("ordini", dl.getOrdineDAO().getAllOrdiniByTecnico(tecnico, page));
                datamodel.put("page", page);

            } else {
                datamodel.put("ordini", dl.getOrdineDAO().getAllOrdiniByTecnico(tecnico, 0));
                datamodel.put("page", 0);
            }

            result.activate("tGestioneOrdini.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

    private void handleModificaOrdineToConsegnato(HttpServletRequest request, HttpServletResponse response, Integer ordine_key) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            Tecnico tecnico = dl.getTecnicoDAO().getTecnicoByEmail((String) SecurityHelpers.checkSession(request).getAttribute("email"));

            Ordine ordine = dl.getOrdineDAO().getOrdine(ordine_key);

            ordine.setStatoConsegna(Ordine.StatoConsegna.CONSEGNATO);
            ordine.setDataConsegna(LocalDate.now());

            ordine.setOrdinante(dl.getOrdinanteDAO().getOrdinante(ordine.getOrdinanteKey()));

            dl.getOrdineDAO().storeOrdine(ordine);

            datamodel.put("success", "2"); // Lo stato dell'ordine è: Consegnato.
            if (request.getParameter("page") != null) {
                Integer page = Integer.parseInt(request.getParameter("page"));
                datamodel.put("ordini", dl.getOrdineDAO().getAllOrdiniByTecnico(tecnico, page));
                datamodel.put("page", page);

            } else {
                datamodel.put("ordini", dl.getOrdineDAO().getAllOrdiniByTecnico(tecnico, 0));
                datamodel.put("page", 0);
            }

            result.activate("tGestioneOrdini.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }


    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if ("In consegna".equals(request.getParameter("ordine"))) {
                //Se devo creare un ordine da una proposta accettata
                handleModificaOrdineToInConsegna(request, response, Integer.parseInt(request.getParameter("id")));

            } else if ("Consegnato".equals(request.getParameter("ordine"))) {
                handleModificaOrdineToConsegnato(request, response, Integer.parseInt(request.getParameter("id")));

            } else renderGestioneOrdinePage(request, response);

        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
}
