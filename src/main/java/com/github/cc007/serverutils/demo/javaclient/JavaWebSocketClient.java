package com.github.cc007.serverutils.demo.javaclient;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.*;

@ClientEndpoint
public class JavaWebSocketClient {

    private static final Object waitLock = new Object();
    private static boolean running = true;
    private static int timeOut = 20;


    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Opened connection for session " + session.getRequestURI() + " (" + session.getId() + ")");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Closed connection for session " + session.getRequestURI() + " (" + session.getId() + ")");
        synchronized (waitLock) {
            waitLock.notifyAll();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received msg: " + message);
    }

    private static void onPong(PongMessage message) {
        System.out.println("Received pong: " + message);
        synchronized (waitLock) {
            timeOut = 20;
        }
    }

    private static void wait4TerminateSignal() {
        synchronized (waitLock) {
            try {
                waitLock.wait();
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Received terminate signal.");
    }

    private static void heartBeat(final Session session) {
        session.addMessageHandler(new MessageHandler.Whole<PongMessage>() {
            @Override
            public void onMessage(PongMessage message) {
                onPong(message);
            }
        });
        new Thread(() -> {
            while (running) {
                try {
                    synchronized (waitLock) {
                        if (timeOut == 0) {
                            System.out.println("20 seconds without response from session" + session.getRequestURI() + " (" + session.getId() + "), closing connection");
                            running = false;
                        } else if (timeOut == 10) {
                            System.out.println("10 seconds without response from session" + session.getRequestURI() + " (" + session.getId() + ")");
                        }
                        timeOut--;

                        session.getBasicRemote().sendPing(ByteBuffer.allocate(1).put((byte) 0));
                    }
                    Thread.sleep(1000);

                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
            synchronized (waitLock) {
                waitLock.notifyAll();
            }
        }).start();
    }

    public static void main(String[] args) {
        WebSocketContainer container;
        Session session = null;
        try {
            container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(JavaWebSocketClient.class, URI.create("ws://localhost:4568/chatsocket"));
            heartBeat(session);
            wait4TerminateSignal();
        } catch (DeploymentException | IOException ex) {
            Logger.getLogger(JavaWebSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.out.println("Closing program...");
            if (session != null) {
                try {
                    session.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
