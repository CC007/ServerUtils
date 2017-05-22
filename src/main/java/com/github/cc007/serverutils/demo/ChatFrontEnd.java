/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cc007.serverutils.demo;

import com.github.cc007.serverutils.FrontEnd;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

/**
 *
 * @author Rik Schaaf aka CC007 (http://coolcat007.nl/)
 */
public class ChatFrontEnd extends FrontEnd {

    /************ HTTP ************/
    
    @Override
    protected void initHTTP() {
        Spark.get("/chat", ChatFrontEnd::chat, engine);
    }

    private static ModelAndView chat(Request req, Response res) {
        LoggerFactory.getLogger(ChatFrontEnd.class).info("Request received");
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(model, "/chat.ftl");
    }
    
    
    
    /********* WebSockets *********/

    @Override
    protected void initWebSockets() {
        Spark.webSocket("/chatsocket", ChatWebSocketHandler.class);
    }
    
    
}
