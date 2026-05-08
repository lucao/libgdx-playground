package br.com.lucasmteixeira.playground.network;

import java.util.logging.Logger;

import com.google.gson.Gson;

import br.com.lucasmteixeira.playground.model.NetMessage;

public class NetworkManager {
    private static final Gson GSON = new Gson();
    private static final Logger LOG = Logger.getLogger(NetworkManager.class.getName());

    private NetworkHost host;
    private NetworkClient client;
    private final String peerId;
    private NetworkListener listener;

    public NetworkManager(String peerId) {
        this.peerId = peerId;
    }

    public void setListener(NetworkListener listener) {
        this.listener = listener;
    }

    public void startHost() {
        host = new NetworkHost(new NetworkListener() {
            @Override
            public void onMessage(NetMessage message) {
                // Host receives from clients — forward to the app listener
                // but ignore messages from ourselves (we already handled them locally)
                if (!message.senderId.equals(peerId)) {
                    listener.onMessage(message);
                }
            }
            @Override
            public void onDisconnected() {}
        });
        host.start();
        // Host also connects as a client to send messages through the same channel
        connectTo("localhost");
    }

    public void connectTo(String ip) {
        client = new NetworkClient(ip, new NetworkListener() {
            @Override
            public void onMessage(NetMessage message) {
                // Client receives broadcasts from server
                // Ignore our own messages echoed back
                if (!message.senderId.equals(peerId)) {
                    listener.onMessage(message);
                }
            }
            @Override
            public void onDisconnected() {
                listener.onDisconnected();
            }
        });
        client.connect();
    }

    public void send(NetMessage.Type type, Object payload) {
        if (client == null || !client.isOpen()) return;
        NetMessage msg = new NetMessage(type, peerId, GSON.toJson(payload));
        client.send(msg);
    }

    public boolean isHost() { return host != null; }
    public String getPeerId() { return peerId; }

    public void leaveLobby() {
        send(NetMessage.Type.PEER_LEAVE, peerId);
    }

    public void dispose() {
        try {
            if (client != null) { client.close(); client = null; }
            if (host != null) { host.stop(); host = null; }
        } catch (Exception e) {
            LOG.severe("Error disposing network: " + e.getMessage());
        }
    }
}
