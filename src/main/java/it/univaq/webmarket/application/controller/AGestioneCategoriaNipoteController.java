/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application.controller;

import it.univaq.webmarket.application.ApplicationBaseController;
import it.univaq.webmarket.application.WebmarketDataLayer;
import it.univaq.webmarket.data.model.CategoriaFiglio;
import it.univaq.webmarket.data.model.CategoriaNipote;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.result.TemplateManagerException;
import it.univaq.webmarket.framework.result.TemplateResult;
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

public class AGestioneCategoriaNipoteController extends ApplicationBaseController {
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ruoliAutorizzati = List.of(Ruolo.AMMINISTRATORE);
    }


    private void renderCategoriePage(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, IOException {   //all'avvio della pagina
        TemplateResult result = new TemplateResult(getServletContext());
        Map<String, Object> datamodel = new HashMap<>();
        WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");

        try {
            if (request.getParameter("id_categoria_genitore") != null) {
                CategoriaFiglio categoriaFiglio = dl.getCategoriaDAO()
                        .getCategoriaFiglio(Integer.parseInt(request.getParameter("id_categoria_genitore")));
                List<CategoriaNipote> categoriaNipote = dl.getCategoriaDAO().getCategorieNipoteByFiglio(categoriaFiglio);
                datamodel.put("categorie", categoriaNipote);

                datamodel.put("id_categoria_genitore", request.getParameter("id_categoria_genitore"));
            } else {

                datamodel.put("categorie", dl.getCategoriaDAO().getAllCategorieNipote());
            }
        } catch (DataException e) {
            handleError(e, request, response);
        }

        if (request.getParameter("showAlert") != null) {
            if (request.getParameter("showAlert").equals("1")) {
                datamodel.put("success", "1"); 
            }
            if (request.getParameter("showAlert").equals("2")) {
                datamodel.put("success", "2"); 
            }
        }

        result.activate("aCategoriaNipote.ftl.html", datamodel, request, response);
    }

    private void renderModify(HttpServletRequest request, HttpServletResponse response, Integer categoriaNipote_key) throws TemplateManagerException, IOException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            CategoriaNipote categoriaNipote = dl.getCategoriaDAO().getCategoriaNipote(categoriaNipote_key);
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();

            if (!request.getParameter("id_categoria_genitore").equals("null")) {


                CategoriaFiglio categoriaFiglio = dl.getCategoriaDAO()
                        .getCategoriaFiglio(Integer.parseInt(request.getParameter("id_categoria_genitore")));
                List<CategoriaNipote> categorieNipote = dl.getCategoriaDAO().getCategorieNipoteByFiglio(categoriaFiglio);
                datamodel.put("id_categoria_genitore", request.getParameter("id_categoria_genitore"));
                datamodel.put("categorie", categorieNipote);
                datamodel.put("categorieFiglio", dl.getCategoriaDAO().getAllCategorieFiglio());
                datamodel.put("categoriaGenitoreEsistente", categoriaNipote.getCategoriaGenitore());
                datamodel.put("visibilityUpdate", "flex");
                datamodel.put("categoriaModifica", dl.getCategoriaDAO().getCategoriaNipote(categoriaNipote_key));
            } else {

                datamodel.put("categoriaModifica", dl.getCategoriaDAO().getCategoriaNipote(categoriaNipote_key));
                datamodel.put("categorie", dl.getCategoriaDAO().getAllCategorieNipote());
                datamodel.put("categorieFiglio", dl.getCategoriaDAO().getAllCategorieFiglio());
                datamodel.put("categoriaGenitoreEsistente", categoriaNipote.getCategoriaGenitore());
                datamodel.put("visibilityUpdate", "flex");

            }

            result.activate("aCategoriaNipote.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }

    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, Integer categoriaNipote_key) {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            CategoriaNipote categoriaNipote = dl.getCategoriaDAO().getCategoriaNipote(categoriaNipote_key);
            dl.getCategoriaDAO().deleteCategoriaNipote(categoriaNipote);


            String id_categoria_genitore = request.getParameter("id_categoria_genitore");


            if (id_categoria_genitore.equals("null")) {
                response.sendRedirect("categoria_nipote");
            } else {
                response.sendRedirect("categoria_nipote?id_categoria_genitore=" + id_categoria_genitore);
            }
            response.sendRedirect("categoria_nipote");
        } catch (IOException | DataException ex) {
            handleError(ex, request, response);
        }
    }

    private void handleModify(HttpServletRequest request, HttpServletResponse response, Integer categoriaNipote_key) throws TemplateManagerException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            CategoriaNipote categoriaNipote = dl.getCategoriaDAO().getCategoriaNipote(categoriaNipote_key);
            categoriaNipote.setNome(request.getParameter("nome"));

            String id_categoria_genitore = request.getParameter("id_categoria_genitore");

            if (request.getParameter("sceltaCategoriaFiglio") != null) {
                categoriaNipote.getCategoriaGenitore().setKey(Integer.parseInt(request.getParameter("sceltaCategoriaFiglio")));
            }

            dl.getCategoriaDAO().storeCategoriaNipote(categoriaNipote);

            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();

            datamodel.put("categoriaModifica", dl.getCategoriaDAO().getCategoriaNipote(categoriaNipote_key));
            datamodel.put("categorie", dl.getCategoriaDAO().getAllCategorieNipote());
            datamodel.put("success", "1");

            if (id_categoria_genitore.equals("null")) {
                response.sendRedirect("categoria_nipote?showAlert=1");
            } else {
                response.sendRedirect("categoria_nipote?id_categoria_genitore=" + id_categoria_genitore + "&showAlert=1");
            }

        } catch (DataException ex) {
            handleError(ex, request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleInsert(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, IOException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();

            CategoriaNipote categoriaNipote = dl.getCategoriaDAO().createCategoriaNipote();

            categoriaNipote.setNome(request.getParameter("nome"));


            if (request.getParameter("sceltaCategoriaFiglio") != null && !request.getParameter("sceltaCategoriaFiglio").isEmpty()) {
                categoriaNipote.setCategoriaGenitore(dl.getCategoriaDAO().getCategoriaFiglio(Integer.parseInt(request.getParameter("sceltaCategoriaFiglio"))));
            } else {
                datamodel.put("success", "-1"); //errore => non è stata selezionata nessuna categoria figlio
                datamodel.put("categorie", dl.getCategoriaDAO().getAllCategorieNipote());
                result.activate("aCategoriaNipote.ftl.html", datamodel, request, response);
                return;
            }


            dl.getCategoriaDAO().storeCategoriaNipote(categoriaNipote);


            datamodel.put("categorie", dl.getCategoriaDAO().getAllCategorieNipote());
            datamodel.put("success", "2"); //successo => categoria nipote aggiunta


            result.activate("aCategoriaNipote.ftl.html", datamodel, request, response);

        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

    private void renderInsert(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException, IOException {
        try {
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");
            TemplateResult result = new TemplateResult(getServletContext());
            Map<String, Object> datamodel = new HashMap<>();

            datamodel.put("categorie", dl.getCategoriaDAO().getAllCategorieNipote());
            datamodel.put("categorieFiglio", dl.getCategoriaDAO().getAllCategorieFiglio());
            datamodel.put("visibilityInsert", "flex");

            result.activate("aCategoriaNipote.ftl.html", datamodel, request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {

            if (request.getParameter("render") != null) {
                //Se l'utente richiede qualche elemento non renderizzato

                if ("Modifica".equals(request.getParameter("render"))) {
                    //Se devo renderizzare il menù per la modifica
                    renderModify(request, response, Integer.parseInt(request.getParameter("id")));

                } else if ("Aggiungi".equals(request.getParameter("render"))) {
                    //Se devo renderizzare il menù per l'aggiunta
                    renderInsert(request, response);
                } else renderCategoriePage(request, response);

            } else if (request.getParameter("action") != null) {
                // Se l'utente richiede un'azione

                if ("Modifica".equals(request.getParameter("action"))) {
                    // Se devo effettuare la modifica
                    handleModify(request, response, Integer.parseInt(request.getParameter("id")));
                } else if ("Elimina".equals(request.getParameter("action"))) {
                    // Se devo effettuare l'eliminazione
                    handleDelete(request, response, Integer.parseInt(request.getParameter("id")));
                } else if ("Aggiungi".equals(request.getParameter("action"))) {

                    // Se devo effettuare l'aggiunta
                    handleInsert(request, response);
                } else renderCategoriePage(request, response);
            } else {
                renderCategoriePage(request, response);
            }
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
}
