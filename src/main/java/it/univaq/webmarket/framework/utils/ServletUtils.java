/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.framework.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author cdidi
 */

public class ServletUtils {
    
    public static void printRequest(HttpServletRequest request) {
        System.out.println("Sto printando la request...");
        StringBuilder sb = new StringBuilder();

        sb.append("Request Method = [" + request.getMethod() + "]\n ");
        sb.append("Request URL Path = [" + request.getRequestURL() + "]\n");
        sb.append("Request URI = [" + request.getRequestURI() + "]\n");

        String headers =
                Collections.list(request.getHeaderNames()).stream()
                        .map(headerName -> headerName + " : " + Collections.list(request.getHeaders(headerName)) )
                        .collect(Collectors.joining("\n"));

        if (headers.isEmpty()) {
            sb.append("Request headers: NONE\n");
        } else {
            sb.append("Request headers: ["+headers+"]\n");
        }

        String parameters =
                Collections.list(request.getParameterNames()).stream()
                        .map(p -> p + " : " + Arrays.asList( request.getParameterValues(p)) )
                        .collect(Collectors.joining("\n"));

        if (parameters.isEmpty()) {
            sb.append("Request parameters: NONE.");
        } else {
            sb.append("Request parameters: [" + parameters + "].");
        }
        System.out.println(sb);
    }

    public static String getPreviousPagePath(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        try {
            if (referrer == null)  throw new ServletException("No referrer found");
            URL referrerURL = new URL(referrer);
            return referrerURL.getPath();
        } catch (ServletException | MalformedURLException e) {
            // nel caso in cui ci sia un'eccezione ulteriore diamo un path di default
            return "/login";
        }
    }
}
