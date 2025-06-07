/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.application.WebmarketDataLayer;
import it.univaq.webmarket.data.model.Ordinante;
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

/**
 *
 * @author cdidi
 */

public class OVisualizzaOrdiniController extends ApplicationBaseController {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ruoliAutorizzati = List.of(Ruolo.ORDINANTE);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            Map<String, String[]> parameterMap = request.getParameterMap();

            try {
                HttpSession session = SecurityHelpers.checkSession(request);
                Ordinante ordinante = dl.getOrdinanteDAO().getOrdinanteByEmail(String.valueOf(session.getAttribute("email")));

                if (parameterMap.containsKey("id")) {
                    datamodel.put("ordini", List.of(dl.getOrdineDAO().getOrdine(Integer.parseInt(parameterMap.get("id")[0]))));
                    datamodel.put("id", parameterMap.get("id")[0]);
                } else if(parameterMap.containsKey("page")) {
                    Integer page = Integer.parseInt(parameterMap.get("page")[0]);
                    datamodel.put("ordini", dl.getOrdineDAO().getAllOrdiniByOrdinante(ordinante, page));
                    datamodel.put("page", page);
                } else {
                    datamodel.put("ordini", dl.getOrdineDAO().getAllOrdiniByOrdinante(ordinante, 0));
                    datamodel.put("page", 0);
                }
            } catch (DataException e) {
                handleError(e, request, response);
            }

            result.activate("oOrdini.ftl.html", datamodel, request, response);
        } catch (TemplateManagerException e){
            handleError(e, request, response);
        }
    }
}
