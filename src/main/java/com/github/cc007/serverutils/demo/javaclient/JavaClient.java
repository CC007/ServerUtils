/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cc007.serverutils.demo.javaclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

/**
 *
 * @author Rik Schaaf aka CC007 (http://coolcat007.nl/)
 */
public class JavaClient {

    WebSocketClient ws;

    public JavaClient() {
        try {
            this.ws = new WebSocketClient(new URI("ws://localhost:4568/chatsocket"), new Draft_10()) {
                @Override
                public void onMessage(String message) {
                    System.out.println(message);
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("opened connection");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("closed connection");
                }

                @Override
                public void onError(Exception ex) {
                    Logger.getLogger(JavaClient.class.getName()).log(Level.SEVERE, null, ex);
                }

            };
        } catch (URISyntaxException ex) {
            Logger.getLogger(JavaClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public WebSocketClient getWs() {
        return ws;
    }

    public static void main(String[] args) {
        JavaClient jc = new JavaClient();
        WebSocketClient ws = jc.getWs();
        System.out.print("Trying to connect");
        ws.connect();
        for (int i = 0; ws.getReadyState() != WebSocket.READYSTATE.OPEN; i++) {
            if (i % 10000000 == 0) {
                System.out.print(".");
            }
        }
        String message = "Hello there";
        //send message

        ws.send(message);

        Scanner in = new Scanner(System.in);
        in.next();
    }

}
