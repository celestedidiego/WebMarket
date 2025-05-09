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
import it.univaq.webmarket.framework.utils.Ruolo;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author cdidi
 */

public class OrdinanteController extends ApplicationBaseController {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ruoliAutorizzati = List.of(Ruolo.ORDINANTE);
    }

    protected void renderOrdinantePage(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        TemplateResult result = new TemplateResult(getServletContext());
        Map<String, Object> datamodel = new HashMap<>();
        WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
        HttpSession session = SecurityHelpers.checkSession(request);


        try {
            Ordinante ordinante = dl.getOrdinanteDAO().getOrdinanteByEmail(session.getAttribute("email").toString());
            List<RichiestaOrdine> richieste = dl.getRichiestaOrdineDAO().getRichiesteByOrdinante(ordinante, 0);
            List<PropostaAcquisto> proposte = dl.getPropostaAcquistoDAO().getAllProposteDaDecidereByOrdinante(ordinante, 0);
            List<Ordine> ordini = dl.getOrdineDAO().getAllOrdiniByOrdinante(ordinante, 0)
                    .stream()
                    .filter(ordine -> !ordine.getStatoConsegna().equals("Consegnato"))
                    .collect(Collectors.toList());

            richieste = richieste.size() > 3 ? richieste.subList(0, 3) : richieste;
            proposte = proposte.size() > 3? proposte.subList(0, 3) : proposte;
            ordini = ordini.size() > 3 ? ordini.subList(0, 3) : ordini;

            datamodel.put("richieste", richieste);
            datamodel.put("proposte", proposte);
            datamodel.put("ordini", ordini);
        } catch (DataException e) {
            handleError(e, request, response);
        }
        result.activate("ordinante.ftl", datamodel, request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            renderOrdinantePage(request, response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
}
