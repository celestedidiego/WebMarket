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

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author cdidi
 */

public class LoginController extends ApplicationBaseController {
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            if (request.getParameter("login") != null) {
                handleLogin(request, response);
            } else {
                renderLoginPage(request, response);
            }
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }


    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Ruolo role = Ruolo.valueOf(request.getParameter("role"));

        // Mi prendo la sessione in caso esista già
        HttpSession s = SecurityHelpers.checkSession(request);

        // Se mi ero loggato con un ruolo, è scaduta la sessione e sto cambiando ruolo,
        // redireziono verso la dashboard corretta per il nuovo ruolo
        if (s != null && s.getAttribute("role") != null && !s.getAttribute("role").equals(role)) {
            s.invalidate();
            handleRedirect(role, request, response);
            return;
        }

        // Se non ha inserito email e password redireziono con errore
        if (email.isEmpty() || password.isEmpty()) {
            response.sendRedirect("login?error=3");
        }

        Logger.getLogger(LoginController.class.getName()).info("Login: " + email + " " + password + " " + role);

        try {
            Utente u = null;
            WebmarketDataLayer dl = (WebmarketDataLayer) request.getAttribute("datalayer");

            // Selezione del giusto utente
            switch (role) {
                case ORDINANTE:
                    u = dl.getOrdinanteDAO().getOrdinanteByEmail(email);
                    break;
                case TECNICO:
                    u = dl.getTecnicoDAO().getTecnicoByEmail(email);
                    break;
                case AMMINISTRATORE:
                    u = dl.getAmministratoreDAO().getAmministratoreByEmail(email);
                    break;
                default:
                    handleError("Login fallito!", request, response);
            }

            // Se la password è errata
            if (u == null || !SecurityHelpers.checkPasswordHashPBKDF2(password, u.getPassword())) {
                response.sendRedirect("login?error=2");
                return;
            }

            // Creo la sessione
            SecurityHelpers.createSession(request, email, u.getKey(), role);

            // Se ha un referrer, lo reindirizzo lì
            if (request.getParameter("referrer") != null) {
                response.sendRedirect(request.getParameter("referrer"));
                return;
            }

            // Reindirizzo in base al ruolo
            handleRedirect(role, request, response);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | DataException e) {
            //Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            handleError(e, request, response);
        }
    }

    private void handleRedirect(Ruolo role, HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (role) {
            case ORDINANTE:
                response.sendRedirect("ordinante");
                break;
            case TECNICO:
                response.sendRedirect("tecnico");
                break;
            case AMMINISTRATORE:
                response.sendRedirect("admin");
                break;
            default:
                handleError("Login fallito!", request, response);
        }
    }
    
    private void renderLoginPage(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateManagerException {
        TemplateResult result = new TemplateResult(getServletContext());
        Map<String, Object> datamodel = new HashMap<>();
        datamodel.put("AMMINISTRATORE", Ruolo.AMMINISTRATORE);
        datamodel.put("TECNICO", Ruolo.TECNICO);
        datamodel.put("ORDINANTE", Ruolo.ORDINANTE);

        // Per questi errori preferisco avere una gestione con messaggio direttamente
        // nella pagina di login in modo tale da poter far loggare l'utente più velocemente
        //if (request.getParameter("error") != null) displayError(datamodel, request.getParameter("error"));

        datamodel.put("referrer", request.getParameter("referrer"));
        result.activate("login.ftl.html", datamodel, request, response);
    }
}
