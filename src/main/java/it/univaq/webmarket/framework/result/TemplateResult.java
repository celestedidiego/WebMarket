/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * TemplateResult.java
 *
 * Questa classe permette di generare facilmente output a partire da template
 * Freemarker. Gestisce vari modelli di dati, passati direttamente o attraverso
 * la request, l'uso di outline automatici, e si configura automaticamente
 * in base a una serie di init parameters del contesto (si veda il codice e
 * il file web.xml per informazioni)
 *
 * This class supports the output generation using the Freemarkr template
 * engine. It handles data models passed explicitly or through the request,
 * automatic page outline, and automatically configures using the context
 * init parameters (see web.xml).
 *
 */

package it.univaq.webmarket.framework.result;


import freemarker.core.HTMLOutputFormat;
import freemarker.core.JSONOutputFormat;
import freemarker.core.XMLOutputFormat;
import freemarker.template.*;
import no.api.freemarker.java8.Java8ObjectWrapper;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cdidi
 */

public class TemplateResult {
    
    protected ServletContext context;
    protected Configuration cfg;
    protected Version version = Configuration.VERSION_2_3_32;

    public TemplateResult(ServletContext context) {
        this.context = context;
        init();
    }

    private void init() {
        cfg = new Configuration(version);
        //impostiamo l'encoding di default per l'input e l'output
        //set the default input and outpout encoding
        String encoding = "utf-8";
        if (context.getInitParameter("view.encoding") != null) {
            encoding = context.getInitParameter("view.encoding");
        }
        cfg.setOutputEncoding(encoding);
        cfg.setDefaultEncoding(encoding);

        //impostiamo la directory (relativa al contesto) da cui caricare i templates
        //set the (context relative) directory for template loading
        if (context.getInitParameter("view.template_directory") != null) {
            cfg.setServletContextForTemplateLoading(context, context.getInitParameter("view.template_directory"));
        } else {
            cfg.setServletContextForTemplateLoading(context, "templates");
        }

        //impostiamo un handler per gli errori nei template - utile per il debug
        //set an error handler for debug purposes       
        if (context.getInitParameter("view.debug") != null && context.getInitParameter("view.debug").equals("true")) {
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        } else {
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        }

        //formato di default per data/ora
        //date/time default formatting
        // Il valore viene settato a quel tipo di formattazione ma di fatto non
        // viene utilizzato quel tipo di formattazione per le date
        if (context.getInitParameter("view.date_format") != null) {
            cfg.setDateTimeFormat(context.getInitParameter("view.date_format"));
        }

        //impostiamo il gestore degli oggetti - trasformerà in hash i Java beans
        Java8ObjectWrapper ow = new Java8ObjectWrapper(version);
        ow.setDefaultDateType(TemplateDateModel.DATETIME);
        ow.setForceLegacyNonListCollections(false);
        cfg.setObjectWrapper(ow);
    }

    /* questo metodo restituisce un data model (hash) di base,
     * (qui inizializzato anche con informazioni di base utili alla gestione dell'outline)
     */
    protected Map<String, Object> getDefaultDataModel(HttpServletRequest request) {
        Map<String, Object> default_data_model = new HashMap<>();

        //default_data_model.put("compiled_on", LocalDateTime.now()); //data di compilazione del template

        return default_data_model;
    }

    protected void process(String tplname, Map<String, Object> datamodel, HttpServletRequest request, Writer out) throws TemplateManagerException {
        Template t;
        Map<String, Object> localdatamodel = getDefaultDataModel(request);

        if (datamodel != null) {
            localdatamodel.putAll(datamodel);
        }

        try {
            t = cfg.getTemplate(tplname);
            t.process(localdatamodel, out);
        } catch (IOException | TemplateException e) {
            throw new TemplateManagerException("Template error: " + e.getMessage(), e);
        }
    }

    //questa versione di activate accetta un modello dati esplicito
    //this activate method gets an explicit data model
    public void activate(String tplname, Map<String, Object> datamodel, HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        Map<String, Object> localdatamodel = getRequestDataModel(request);
        localdatamodel.putAll(datamodel);
        setupServletResponse(localdatamodel, response);
        try {
            process(tplname, localdatamodel, request, response.getWriter());
        } catch (IOException ex) {
            throw new TemplateManagerException("Template error: " + ex.getMessage(), ex);
        }
    }

    protected Map<String,Object> getRequestDataModel(HttpServletRequest request) {
        // Questo è utile per riprendere "logininfo", cioè i dati salvati dell'utente
        // nella sessione in automatico. L'aggiunta di login info alla request viene
        // fatta nell'AbstractBaseController
        Map<String, Object> datamodel = new HashMap<>();
        Enumeration<String> attrs = request.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String attrname = attrs.nextElement();
            datamodel.put(attrname, request.getAttribute(attrname));
        }
        return datamodel;
    }

    //metodo interno per il setup della response
    //internal method for response setup
    private void setupServletResponse(Map<String, Object> datamodel, HttpServletResponse response) {
        //impostiamo il content type, se specificato dall'utente, o usiamo il default
        String contentType = (String) datamodel.get("contentType");
        if (contentType == null) {
            contentType = "text/html";
        }
        response.setContentType(contentType);

        //impostiamo l'encoding, se specificato dall'utente, o usiamo il default
        String encoding = (String) datamodel.get("encoding");
        if (encoding == null) {
            encoding = cfg.getOutputEncoding();
        }
        response.setCharacterEncoding(encoding);

        //impostiamo il tipo di output: in questo modo freemarker abiliterà il necessario escaping
        switch (contentType) {
            case "text/html":
                cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
                break;
            case "text/xml":
            case "application/xml":
                cfg.setOutputFormat(XMLOutputFormat.INSTANCE);
                break;
            case "application/json":
                cfg.setOutputFormat(JSONOutputFormat.INSTANCE);
                break;
            default:
                break;
        }

    }

    //questa versione di activate può essere usata per generare output non diretto verso il browser, ad esempio
    //su un file
    //this activate method can be used to generate output and save it to a file
    public void activate(String tplname, Map<String, Object> datamodel, OutputStream out) throws TemplateManagerException {
        //impostiamo l'encoding, se specificato dall'utente, o usiamo il default
        String encoding = (String) datamodel.get("encoding");
        if (encoding == null) {
            encoding = cfg.getOutputEncoding();
        }
        try {
            //notare la gestione dell'encoding, che viene invece eseguita implicitamente tramite il setContentType nel contesto servlet
            process(tplname, datamodel, null, new OutputStreamWriter(out, encoding));
        } catch (UnsupportedEncodingException ex) {
            throw new TemplateManagerException("Template error: " + ex.getMessage(), ex);
        }
    }

    public String activate(String tplname, Map<String, Object> datamodel, StringWriter stringWriter) throws TemplateManagerException {
        try {
            // Load template
            Template template = cfg.getTemplate(tplname);
            template.process(datamodel, stringWriter);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            throw new TemplateManagerException("Template error: " + e.getMessage(), e);
        }
    }
}
