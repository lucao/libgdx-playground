package br.com.lucasmteixeira.playground.model;

import java.util.ArrayList;
import java.util.List;

public class LobbyState {
    public String adventureId;
    public String adventureName;
    public String hostPeerId;
    public List<LobbyPlayer> players = new ArrayList<>();

    public static class LobbyPlayer {
        public String peerId;
        public String characterName;
        public boolean isGameMaster;
        public boolean ready;

        public LobbyPlayer() {}

        public LobbyPlayer(String peerId, String characterName, boolean isGameMaster) {
            this.peerId = peerId;
            this.characterName = characterName;
            this.isGameMaster = isGameMaster;
        }
    }

    public boolean allReady() {
        return !players.isEmpty() && players.stream().allMatch(p -> p.ready);
    }
}
