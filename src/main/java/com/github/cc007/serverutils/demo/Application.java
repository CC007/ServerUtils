/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cc007.serverutils.demo;

import com.github.cc007.serverutils.FrontEnd;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rik Schaaf aka CC007 (http://coolcat007.nl/)
 */
public class Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        LoggerFactory.getLogger(Application.class).info("Creating front end handler...");
        FrontEnd frontEnd = new ChatFrontEnd().port(4568).htmlDebug().init();
        LoggerFactory.getLogger(Application.class).info("Front end handler created");
        
    }

}
