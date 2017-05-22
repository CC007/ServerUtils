/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cc007.serverutils;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * This abstract class is used to setup a webserver using the Spark framework
 * and the FreeMarker templating engine.
 *
 * @author Rik Schaaf aka CC007 (http://coolcat007.nl/)
 */
public abstract class FrontEnd {

    private final Configuration config;
    private TemplateExceptionHandler teh;
    private int port;

    protected FreeMarkerEngine engine;

    /**
     * Constructor for the <code>FrontEnd</code> class
     */
    public FrontEnd() {
        this.config = new Configuration(Configuration.VERSION_2_3_23);
        this.teh = TemplateExceptionHandler.RETHROW_HANDLER;
        this.port = 4567;
    }

    /**
     * Set the port that will be used by the Spark framework
     *
     * @param port the port that will be used by the Spark framework
     * @return the object itself
     */
    public FrontEnd port(int port) {
        this.port = port;
        return this;
    }

    /**
     * This method enables the debug template exception handler for the
     * FreeMarker.
     *
     * NOTE: this method must be called before init if you want to change the
     * handler. If called after init, the handler will remain unchanged
     * (Default: <code>TemplateExceptionHandler.RETHROW_HANDLER</code>)
     *
     * @return the object itself
     */
    public FrontEnd debug() {
        this.teh = TemplateExceptionHandler.DEBUG_HANDLER;
        return this;
    }

    /**
     * This method enables the html debug template exception handler for the
     * FreeMarker.
     *
     * NOTE: this method must be called before init if you want to change the
     * handler. If called after init, the handler will remain unchanged
     * (Default: <code>TemplateExceptionHandler.RETHROW_HANDLER</code>)
     *
     * @return the object itself
     */
    public FrontEnd htmlDebug() {
        this.teh = TemplateExceptionHandler.HTML_DEBUG_HANDLER;
        return this;
    }

    /**
     * This method initializes the template configuration, http configuration
     * and the websocket config
     *
     * @return the object itself
     */
    public final FrontEnd init() {
        initConfig();
        initWebSockets();
        initHTTP();
        Spark.awaitInitialization();
        return this;
    }

    private void initConfig() {
        //Spark config
        Spark.port(port);
        Spark.staticFileLocation("/web/static");

        config.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(), "/web/ftl");
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        config.setLogTemplateExceptions(false);
        engine = new FreeMarkerEngine(config);
    }

    /**
     * This initializes the http requests defined in extending classes. Override
     * this method if you want to process HTTP requests
     */
    protected void initHTTP() {
        Spark.init();
    }

    /**
     * This initializes the websocket connections defined in extending classes.
     * Override this method if you want to add websocket connections
     */
    protected void initWebSockets() {
    }

    /**
     * This method stops the HTTP server
     */
    public final void stop() {
        Spark.stop();
    }
}
