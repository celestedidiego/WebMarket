/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.framework.application;

//AGGIUNGERE CODICE PER LE MAIL E ANCHE SU UTILS
import it.univaq.webmarket.framework.utils.EmailSender;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import javax.sql.DataSource;

/**
 *
 * @author cdidi
 */

public class ApplicationInitializer implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent event) {

        DataSource ds = null;
        Pattern protect = null;
        EmailSender sender = null;

        //init protection pattern
        String p = event.getServletContext().getInitParameter("security.protect.patterns");
        if (p != null && !p.isBlank()) {
            String[] split = p.split("\\s*,\\s*");
            protect = Pattern.compile(Arrays.stream(split).collect(Collectors.joining("$)|(?:", "(?:", "$)")));
        } else {
            protect = Pattern.compile("a^"); //this regular expression does not match anything!
        }

        //init data source
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/" + event.getServletContext().getInitParameter("data.source"));

            String emailSender = (String) ctx.lookup("java:comp/env/email");
            String passwordSender = (String) ctx.lookup("java:comp/env/password");

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", "smtp-mail.outlook.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

            sender = new EmailSender(emailSender, passwordSender, properties);
        } catch (NamingException ex) {
            Logger.getLogger(ApplicationInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }

        event.getServletContext().setAttribute("protect", protect);
        event.getServletContext().setAttribute("datasource", ds);
        event.getServletContext().setAttribute("emailsender", sender);
    }
}
