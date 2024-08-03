package com.example.androidexample;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketManager {

    private static WebSocketManager instance;
    private WebSocketClient webSocketClient;
    private String serverUrl;
    private WebSocketListener listener;

    private WebSocketManager() {
    }

    public static WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public void setWebSocketListener(WebSocketListener listener) {
        this.listener = listener;
    }

    public void connectWebSocket(String serverUrl) {
        this.serverUrl = serverUrl;
        URI uri;
        try {
            uri = new URI(serverUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                if (listener != null) {
                    listener.onWebSocketOpen(handshakedata);
                }
            }

            @Override
            public void onMessage(String message) {
                if (listener != null) {
                    listener.onWebSocketMessage(message);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (listener != null) {
                    listener.onWebSocketClose(code, reason, remote);
                }
            }

            @Override
            public void onError(Exception ex) {
                if (listener != null) {
                    listener.onWebSocketError(ex);
                }
            }
        };
        webSocketClient.connect();
    }

    public void sendMessage(String message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(message);
        }
    }

    public void reconnectWebSocket() {
        if (webSocketClient != null && !webSocketClient.isOpen()) {
            connectWebSocket(serverUrl);
        }
    }

    public void disconnectWebSocket() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}
