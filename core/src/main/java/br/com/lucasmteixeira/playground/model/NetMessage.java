package br.com.lucasmteixeira.playground.model;

public class NetMessage {
    public enum Type {
        PEER_JOIN, PEER_LEAVE, LOBBY_STATE, PLAYER_READY, GAME_START,
        GM_COMMAND, PLAYER_ACTION, CHAT
    }

    public Type type;
    public String senderId;
    public String payload; // JSON string of the specific payload

    public NetMessage() {}

    public NetMessage(Type type, String senderId, String payload) {
        this.type = type;
        this.senderId = senderId;
        this.payload = payload;
    }
}
