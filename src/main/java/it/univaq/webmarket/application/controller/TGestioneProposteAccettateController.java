/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.application.WebmarketDataLayer;
import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.data.model.Ordine;
import it.univaq.webmarket.data.model.PropostaAcquisto;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cdidi
 */

public class TGestioneProposteAccettateController extends ApplicationBaseController {
    
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
            if (request.getParameter("page") != null) {
                Integer page = Integer.parseInt(request.getParameter("page"));
                datamodel.put("proposte", dl.getPropostaAcquistoDAO().getAllProposteAccettate(page));
                datamodel.put("page", page);
            } else {
                datamodel.put("proposte", dl.getPropostaAcquistoDAO().getAllProposteAccettate(0));
                datamodel.put("page", 0);
            }
        } catch (DataException e) {
            handleError(e, request, response);
        }


        result.activate("tGestioneProposteAccettate.ftl.html", datamodel, request, response);
    }


    private void handleCreaOrdine(HttpServletRequest request, HttpServletResponse response, Integer proposta_key) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();

            Tecnico tecnico = dl.getTecnicoDAO().getTecnicoByEmail((String) SecurityHelpers.checkSession(request).getAttribute("email"));

            Ordine ordine = dl.getOrdineDAO().createOrdine();
            PropostaAcquisto proposta = dl.getPropostaAcquistoDAO().getPropostaAcquisto(proposta_key);

            ordine.setPropostaAcquisto(proposta);
            ordine.setTecnico(tecnico);
            ordine.setStatoConsegna(Ordine.StatoConsegna.PRESA_IN_CARICO);

            Ordinante ordinante = proposta.getRichiestaPresaInCarico().getRichiestaOrdine().getOrdinante();
            ordine.setOrdinante(ordinante);

            dl.getOrdineDAO().storeOrdine(ordine);

            EmailSender sender = (EmailSender) getServletContext().getAttribute("emailsender");
            String mailOrdinante = ordine.getPropostaAcquisto().getRichiestaPresaInCarico().getRichiestaOrdine().getOrdinante().getEmail();

            sender.sendPDFWithEmail(getServletContext(), mailOrdinante, ordine, EmailSender.Event.ORDINE_CREATO);


            datamodel.put("success", "1"); // Ordine creato con successo
            if (request.getParameter("page") != null) {
                Integer page = Integer.parseInt(request.getParameter("page"));
                datamodel.put("proposte", dl.getPropostaAcquistoDAO().getAllProposteAccettate(page));
                datamodel.put("page", page);

            } else {
                datamodel.put("proposte", dl.getPropostaAcquistoDAO().getAllProposteAccettate(0));
                datamodel.put("page", 0);
            }

            result.activate("tGestioneProposteAccettate.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }


    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {


            if ("Crea Ordine".equals(request.getParameter("ordine"))) {
                //Se devo creare un ordine da una proposta accettata
                handleCreaOrdine(request, response, Integer.parseInt(request.getParameter("id")));

            } else renderGestioneOrdinePage(request, response);

        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
}
