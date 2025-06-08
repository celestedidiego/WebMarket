/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.framework.controller;

/**
 *
 * @author cdidi
 */

import it.univaq.webmarket.framework.data.DataLayer;
import it.univaq.webmarket.framework.result.FailureResult;
import it.univaq.webmarket.framework.security.SecurityHelpers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class AbstractBaseController extends HttpServlet {

    private DataSource ds;
    private Pattern protect;

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    //creare la propria classe derivata da DataLayer
    //create your own datalayer derived class
    protected abstract DataLayer createDataLayer(DataSource ds) throws ServletException;

    //override per inizializzare altre informazioni da offrire a tutte le servlet
    //override to init other information to offer to all the servlets
    protected void initRequest(HttpServletRequest request, DataLayer dl) {
        String completeRequestURL = request.getRequestURL() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        request.setAttribute("thispageurl", completeRequestURL);
        request.setAttribute("datalayer", dl);
    }

    //override to enforce your policy and/or change the login url
    protected void accessCheckFailed(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        String completeRequestURL = request.getRequestURL() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        response.sendRedirect("login?referrer=" + URLEncoder.encode(completeRequestURL, StandardCharsets.UTF_8));
    }

    //override to provide your login information in the request
    protected void accessCheckSuccessful(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        HttpSession s = request.getSession(false);
        if (s != null) {
            Map<String, Object> li = new HashMap<>();
            request.setAttribute("logininfo", li);
            li.put("session-start-ts", s.getAttribute("session-start-ts"));
            li.put("email", s.getAttribute("email"));
            li.put("id", s.getAttribute("id"));
            li.put("ip", s.getAttribute("ip"));
            li.put("role", s.getAttribute("role"));
        }
    }

    protected void processBaseRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        this.ds = (DataSource) getServletContext().getAttribute("datasource");
        this.protect = (Pattern) getServletContext().getAttribute("protect");
        try (DataLayer datalayer = createDataLayer(ds)) {
            datalayer.init();
            initRequest(request, datalayer);
            //questo blocco di controlli può essere usato in alternativa al SessionCheckFilter
            //e richiede la specifica degli url-pattern delle risorse protette come init-parameters della web application
            //this block can be used as an alternative to the SessionCheckFilter
            //and requires the specification of the protected resources url-patterns as web application init-parameters
            if (checkAccess(request, response)) {
                accessCheckSuccessful(request, response);
                processRequest(request, response);
            } else {
                accessCheckFailed(request, response);
            }
        } catch (Exception ex) {
            handleError(ex, request, response);
        }
    }

    protected boolean checkAccess(HttpServletRequest request, HttpServletResponse response)  {
        HttpSession s = SecurityHelpers.checkSession(request);
        String uri = request.getRequestURI();
        return !(s == null && protect != null && protect.matcher(uri).find());
    }

    protected void handleError(String message, HttpServletRequest request, HttpServletResponse response) {
        new FailureResult(getServletContext()).activate(message, request, response);
    }

    protected void handleError(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        new FailureResult(getServletContext()).activate(exception, request, response);
    }

    protected void handleError(HttpServletRequest request, HttpServletResponse response) {
        new FailureResult(getServletContext()).activate(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        processBaseRequest(request, response);
    }
}
