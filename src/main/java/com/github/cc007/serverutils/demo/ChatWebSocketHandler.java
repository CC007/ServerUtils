package com.github.cc007.serverutils.demo;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
public class ChatWebSocketHandler {

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session newSession) {
        sessions.add(newSession);
        LoggerFactory.getLogger(ChatWebSocketHandler.class).info("New session connected");
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    newSession.getRemote().sendPing(ByteBuffer.allocate(0));
//                } catch (IOException ex) {
//                    LoggerFactory.getLogger(ChatWebSocketHandler.class).error(null, ex);
//                }
//            }
//        }, 2000, 2000);
    }

    @OnWebSocketClose
    public void closed(Session oldSession, int statusCode, String reason) {
        sessions.remove(oldSession);
        LoggerFactory.getLogger(ChatWebSocketHandler.class).info("Session closed");
    }

    @OnWebSocketMessage
    public void message(Session msgSession, String message) throws IOException {
        LoggerFactory.getLogger(ChatWebSocketHandler.class).info("Session message received");
        System.out.println("Got: " + message);   // Print message
        for (Session session : sessions) {
            session.getRemote().sendString(message); // and send it back to all connected sessions
        }
    }

}
