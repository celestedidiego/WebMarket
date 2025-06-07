/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.application.WebmarketDataLayer;
import it.univaq.webmarket.data.model.*;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.result.TemplateManagerException;
import it.univaq.webmarket.framework.result.TemplateResult;
import it.univaq.webmarket.framework.security.SecurityHelpers;
import it.univaq.webmarket.framework.utils.EmailSender;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cdidi
 */

public class OCreaRichiestaController extends ApplicationBaseController {
    
    private void renderTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();

            datamodel.put("categoriePadre", dl.getCategoriaDAO().getAllCategoriePadre());

            result.activate("oCreaRichiesta.ftl.html", datamodel, request, response);
        } catch (DataException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

    private void handleRequests(HttpServletRequest request, HttpServletResponse response) {

        WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
        Map<String, String[]> parameterMap = request.getParameterMap();

        try {
            if (parameterMap.containsKey("categoriaPadre")) {
                int idCategoriaPadre = Integer.parseInt(parameterMap.get("categoriaPadre")[0]);
                ObjectMapper mapper = new ObjectMapper();
                CategoriaPadre categoriaPadre = dl.getCategoriaDAO()
                        .getCategoriaPadre(idCategoriaPadre);
                String json = mapper.writeValueAsString(dl.getCategoriaDAO().getCategorieFiglioByPadre(categoriaPadre));

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().print(json);
                response.getWriter().flush();

            } else if (parameterMap.containsKey("categoriaFiglio")) {

                int idCategoriaFiglio = Integer.parseInt(parameterMap.get("categoriaFiglio")[0]);
                ObjectMapper mapper = new ObjectMapper();
                CategoriaFiglio categoriafiglio = dl.getCategoriaDAO()
                        .getCategoriaFiglio(idCategoriaFiglio);
                String json = mapper.writeValueAsString(dl.getCategoriaDAO().getCategorieNipoteByFiglio(categoriafiglio));

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().print(json);
                response.getWriter().flush();

            } else if (parameterMap.containsKey("categoriaNipote")) {

                int idCategoriaNipote = Integer.parseInt(parameterMap.get("categoriaNipote")[0]);
                ObjectMapper mapper = new ObjectMapper();
                CategoriaNipote categoriaNipote = dl.getCategoriaDAO()
                        .getCategoriaNipote(idCategoriaNipote);
                List<Caratteristica> caratteristiche = dl.getCaratteristicaDAO().getAllCaratteristiche(categoriaNipote);
                String json = mapper.writeValueAsString(caratteristiche);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().print(json);
                response.getWriter().flush();

            } else if(parameterMap.containsKey("key")){
                RichiestaOrdine richiesta = dl.getRichiestaOrdineDAO().createRichiestaOrdine();
                richiesta.setData(LocalDate.now());

                String[] noteArr = parameterMap.get("note");
                if (noteArr == null || noteArr.length == 0) {
                    richiesta.setNote("");
                } else {
                    richiesta.setNote(noteArr[0]);
                }

                HttpSession session = SecurityHelpers.checkSession(request);
                if (session == null || session.getAttribute("email") == null) {
                    throw new DataException("Sessione scaduta o email non trovata");
                }
                Ordinante ordinante = dl.getOrdinanteDAO()
                        .getOrdinanteByEmail((String) session.getAttribute("email"));
                if (ordinante == null) {
                    throw new DataException("Ordinante non trovato");
                }
                richiesta.setOrdinante(ordinante);

                dl.getRichiestaOrdineDAO().storeRichiestaOrdine(richiesta);
                int richiestaID = richiesta.getKey();

                String[] keys = parameterMap.get("key");
                if (keys == null) {
                    throw new DataException("Nessuna caratteristica selezionata");
                }

                for(String key : keys) {
                    if(parameterMap.get(key) != null && !"".equals(parameterMap.get(key)[0])){
                        CaratteristicaRichiesta caratteristicaRichiesta = dl.getCaratteristicaDAO().createCaratteristicaRichiesta();
                        Caratteristica caratteristica = dl.getCaratteristicaDAO()
                                .getCaratteristica(Integer.parseInt(key));

                        caratteristicaRichiesta.setCaratteristica(caratteristica);
                        caratteristicaRichiesta.setValore(parameterMap.get(key)[0]);
                        dl.getCaratteristicaDAO().storeCaratteristicaRichiesta(caratteristicaRichiesta, richiestaID);
                    }
                }
                EmailSender sender = (EmailSender) getServletContext().getAttribute("emailsender");
                sender.sendPDFWithEmail(getServletContext(), ordinante.getEmail(), richiesta, EmailSender.Event.RICHIESTA_REGISTRATA);

                response.sendRedirect("ordinante");

            } else {
                renderTemplate(request, response);
            }
        } catch(DataException | IOException ex) {
            handleError(ex, request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if(parameterMap.isEmpty()) {
            renderTemplate(request, response);
        } else {
            handleRequests(request, response);
        }
    }
}
