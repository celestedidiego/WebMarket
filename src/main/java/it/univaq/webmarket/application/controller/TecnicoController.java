/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.framework.result.TemplateManagerException;
import it.univaq.webmarket.framework.result.TemplateResult;
import it.univaq.webmarket.framework.utils.Ruolo;
import it.univaq.webmarket.application.WebmarketDataLayer;
import it.univaq.webmarket.framework.security.SecurityHelpers;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.data.model.*;

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

public class TecnicoController extends ApplicationBaseController {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ruoliAutorizzati = List.of(Ruolo.TECNICO);
    }

    private void renderTecnicoPage(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            HttpSession session = SecurityHelpers.checkSession(request);

            Tecnico tecnico = dl.getTecnicoDAO().getTecnicoByEmail(session.getAttribute("email").toString());
            List<RichiestaOrdine> richieste = dl.getRichiestaOrdineDAO().getRichiesteNonGestite(0);
            List<RichiestaPresaInCarico> richiestePreseInCarico = dl.getRichiestaPresaInCaricoDAO().getAllRichiestePresaInCaricoByTecnico(tecnico, 0);
            List<PropostaAcquisto> proposte = dl.getPropostaAcquistoDAO().getAllProposteByTecnico(tecnico, 0);

            richieste = richieste.size() > 3 ? richieste.subList(0, 3) : richieste;
            richiestePreseInCarico = richiestePreseInCarico.size() > 2 ? richiestePreseInCarico.subList(0, 2) : richiestePreseInCarico;
            proposte = proposte.size() > 3 ? proposte.subList(0, 3) : proposte;

            datamodel.put("richieste", richieste);
            datamodel.put("richiestePreseInCarico", richiestePreseInCarico);
            datamodel.put("proposte", proposte);

            result.activate("tecnico.ftl.html", datamodel, request, response);
        } catch (DataException e) {
            handleError(e, request, response);
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            renderTecnicoPage(request, response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
}
