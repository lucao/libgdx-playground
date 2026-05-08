package br.com.lucasmteixeira.playground.state;

import br.com.lucasmteixeira.playground.model.AdventureData;
import br.com.lucasmteixeira.playground.model.CharacterData;
import br.com.lucasmteixeira.playground.model.LobbyState;

public class GameState {

    private static final GameState INSTANCE = new GameState();
    public static GameState get() { return INSTANCE; }

    private CharacterData character;
    private boolean gameMaster;
    private AdventureData selectedAdventure;
    private LobbyState lobbyState;

    private GameState() {}

    public CharacterData getCharacter() { return character; }
    public void setCharacter(CharacterData character) { this.character = character; }

    public boolean isGameMaster() { return gameMaster; }
    public void setGameMaster(boolean gameMaster) { this.gameMaster = gameMaster; }

    public AdventureData getSelectedAdventure() { return selectedAdventure; }
    public void setSelectedAdventure(AdventureData adventure) { this.selectedAdventure = adventure; }

    public LobbyState getLobbyState() { return lobbyState; }
    public void setLobbyState(LobbyState lobbyState) { this.lobbyState = lobbyState; }
}
