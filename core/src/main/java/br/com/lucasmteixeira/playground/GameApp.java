package br.com.lucasmteixeira.playground;

import java.util.UUID;

import com.badlogic.gdx.Game;

import br.com.lucasmteixeira.playground.model.AdventureData;
import br.com.lucasmteixeira.playground.model.CharacterData;
import br.com.lucasmteixeira.playground.network.NetworkManager;
import br.com.lucasmteixeira.playground.screens.CharacterScreen;
import br.com.lucasmteixeira.playground.state.GameState;

public class GameApp extends Game {

    private NetworkManager network;

    @Override
    public void create() {
        network = new NetworkManager(UUID.randomUUID().toString());
        setScreen(new CharacterScreen(this));
    }

    public CharacterData getCharacter() { return GameState.get().getCharacter(); }
    public boolean isGameMaster() { return GameState.get().isGameMaster(); }
    public AdventureData getSelectedAdventure() { return GameState.get().getSelectedAdventure(); }

    public void setCharacter(CharacterData character, boolean isGameMaster) {
        GameState.get().setCharacter(character);
        GameState.get().setGameMaster(isGameMaster);
    }

    public void setSelectedAdventure(AdventureData adventure) {
        GameState.get().setSelectedAdventure(adventure);
    }

    public NetworkManager getNetwork() { return network; }

    @Override
    public void dispose() {
        super.dispose();
        network.dispose();
    }
}
