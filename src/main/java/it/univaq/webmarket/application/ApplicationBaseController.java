/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application;

import it.univaq.webmarket.framework.controller.AbstractBaseController;
import it.univaq.webmarket.framework.data.DataLayer;
import it.univaq.webmarket.framework.utils.Ruolo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author cdidi
 */

public abstract class ApplicationBaseController extends AbstractBaseController {
    
    protected List<Ruolo> ruoliAutorizzati;

    @Override
    protected DataLayer createDataLayer(DataSource ds) throws ServletException {
        try {
            return new WebmarketDataLayer(ds);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    /*
    * Tengo traccia dei ruoli autorizzati per accedere alla pagina perchè un utente potrebbe
    * potenzialmente far scadere la sessione, verrebbe quindi messo un referrer nell'url della pagina,
    * se modificasse il referrer per poter accedere a pagine protette per altri ruoli, allora potrebbe
    * loggarsi con le sue credenziali e accedere a contenuti sensibili di altri ruoli. In questo modo,
    * anche se il login viene effettuato correttamente, se il ruolo non è autorizzato, l'utente verrà
    * reindirizzato ad una pagina di errore.
    *
    * Per riprodurre l' "attacco", loggarsi con un ruolo diverso dall'admin, far scadere la sessione,
    * modificare l'url insernedo admin al posto del nome della servlet a cui eravamo collegati primi
    * e riloggarsi con le proprie crendenziali. Ovviamente bisogna commentare il metodo sottostante.
    */
    
    @Override
    protected void processBaseRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession session = request.getSession(false);
        if(ruoliAutorizzati != null && session != null && session.getAttribute("role") != null) {
            Ruolo ruolo = Ruolo.valueOf((String) session.getAttribute("role"));
            if (!this.ruoliAutorizzati.contains(ruolo)) {
                handleError("Utente non autorizzato!", request, response);
                return;
            }
        }
        super.processBaseRequest(request, response);
    }
}
