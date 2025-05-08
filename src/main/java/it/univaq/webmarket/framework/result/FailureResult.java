/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * FailureResult.java
 *
 * Si tratta di una semplice classe che incapsula un TemplateResult per offrire
 * un comodo sistema di visualizzazione degli errori. Si basa su un template
 * il cui nome deve essere presente nella configurazione dell'applicazione
 * (web.xml, parametro view.error_template). In mancanza di questo, degrada
 * a un errore http.
 *
 * This simple class wraps TemplateResult to provide an easy error displaying
 * system. It uses a template whose name must be configured as a context
 * parameter (web.xml, view.error_template parameter). If no template is found,
 * the class uses simple http errors.
 *
 */

package it.univaq.webmarket.framework.result;

import it.univaq.webmarket.framework.utils.ServletUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author cdidi
 */

public class FailureResult {
    
    protected ServletContext context;
    private final TemplateResult template;

    public FailureResult(ServletContext context) {
        this.context = context;
        template = new TemplateResult(context);
    }

    public void activate(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        String message = "Unknown exception";
        if (exception != null && exception.getMessage() != null && !exception.getMessage().isEmpty()) {
            message = exception.getMessage();
        } else if (exception != null) {
            message = exception.getClass().getName();
        }

        //Solo per motivi di debug
        exception.printStackTrace();

        activate(message, request, response);
    }

    public void activate(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") instanceof Exception) {
            activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            activate("Unknown error", request, response);
        }
    }

    public void activate(String message, HttpServletRequest request, HttpServletResponse response) {
        try {
            Logger.getLogger(FailureResult.class.getName()).log(Level.SEVERE, message);

            //se abbiamo registrato un template per i messaggi di errore, proviamo a usare quello
            if (context.getInitParameter("view.error_template") != null) {


                // Estraggo gli attributi dalla request e li passo come datamodel al template
                Map<String, Object> datamodel = new HashMap<>();
                Enumeration<String> attrs = request.getAttributeNames();
                while (attrs.hasMoreElements()) {
                    String attrname = attrs.nextElement();
                    datamodel.put(attrname, request.getAttribute(attrname));
                }
                if(Boolean.parseBoolean(context.getInitParameter("debug"))) {
                    datamodel.put("error_message", message);
                } else {
                    datamodel.put("error_message", "An error occurred while processing your request. Please try again later.");
                }

                datamodel.put("error_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                datamodel.put("referrer", ServletUtils.getPreviousPagePath(request));
                template.activate(context.getInitParameter("view.error_template"), datamodel, request, response);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
            }
        } catch (Exception ex) {
            message += ". In addition, the following exception was generated while trying to display the error page: " + ex.getMessage();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
            } catch (IOException ex1) {
                Logger.getLogger(FailureResult.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
