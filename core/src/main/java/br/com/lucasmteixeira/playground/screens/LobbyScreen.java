package br.com.lucasmteixeira.playground.screens;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;

import br.com.lucasmteixeira.playground.GameApp;
import br.com.lucasmteixeira.playground.model.LobbyState;
import br.com.lucasmteixeira.playground.model.LobbyState.LobbyPlayer;
import br.com.lucasmteixeira.playground.model.NetMessage;
import br.com.lucasmteixeira.playground.network.LobbyBeacon;
import br.com.lucasmteixeira.playground.network.LobbyBeacon.BeaconPayload;
import br.com.lucasmteixeira.playground.network.LobbyDiscovery;
import br.com.lucasmteixeira.playground.network.NetworkListener;

public class LobbyScreen implements Screen, NetworkListener {
    private final GameApp game;
    private final Gson gson = new Gson();
    private Stage stage;
    private Skin skin;

    private LobbyState lobbyState;
    private LobbyBeacon beacon;
    private LobbyDiscovery discovery;
    private boolean isReady = false;
    private boolean inLobby = false;

    private Table playerListTable;
    private Table discoveredTable;
    private Label statusLabel;
    private TextButton readyBtn;
    private TextButton hostBtn;

    public LobbyScreen(GameApp game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        lobbyState = new LobbyState();
        lobbyState.adventureId = game.getSelectedAdventure().id;
        lobbyState.adventureName = game.getSelectedAdventure().name;

        game.getNetwork().setListener(this);
        buildUI();

        discovery = new LobbyDiscovery(this::onLobbiesUpdated);
        discovery.start();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        root.pad(24);
        stage.addActor(root);

        // Header
        Label title = new Label(game.getSelectedAdventure().name, skin);
        title.setFontScale(1.5f);
        title.setAlignment(Align.center);
        root.add(title).expandX().padBottom(6).row();

        statusLabel = new Label("Looking for games on your network...", skin);
        statusLabel.setAlignment(Align.center);
        statusLabel.setColor(Color.LIGHT_GRAY);
        root.add(statusLabel).expandX().padBottom(20).row();

        // Two-column layout
        Table columns = new Table();
        root.add(columns).expand().fill().row();

        // Left: Available games
        Table leftPanel = new Table(skin);
        leftPanel.pad(16);
        Label leftTitle = new Label("Games on Network", skin);
        leftTitle.setFontScale(1.1f);
        leftPanel.add(leftTitle).left().padBottom(12).row();

        discoveredTable = new Table();
        ScrollPane discoverScroll = new ScrollPane(discoveredTable, skin);
        discoverScroll.setFadeScrollBars(true);
        leftPanel.add(discoverScroll).expand().fill().row();

        hostBtn = new TextButton("Host New Game", skin);
        hostBtn.addListener(e -> {
            if (hostBtn.isPressed()) hostGame();
            return false;
        });
        leftPanel.add(hostBtn).expandX().fillX().height(42).padTop(12).row();

        columns.add(leftPanel).expand().fill().padRight(12);

        // Right: Current lobby
        Table rightPanel = new Table(skin);
        rightPanel.pad(16);
        Label rightTitle = new Label("Current Lobby", skin);
        rightTitle.setFontScale(1.1f);
        rightPanel.add(rightTitle).left().padBottom(12).row();

        playerListTable = new Table();
        ScrollPane playerScroll = new ScrollPane(playerListTable, skin);
        playerScroll.setFadeScrollBars(true);
        rightPanel.add(playerScroll).expand().fill().row();

        readyBtn = new TextButton("Ready", skin);
        readyBtn.setDisabled(true);
        readyBtn.addListener(e -> {
            if (readyBtn.isPressed() && !readyBtn.isDisabled()) {
                isReady = !isReady;
                readyBtn.setText(isReady ? "Cancel Ready" : "Ready");
                updateMyReadyState(isReady);
            }
            return false;
        });
        rightPanel.add(readyBtn).expandX().fillX().height(42).padTop(12).row();

        columns.add(rightPanel).expand().fill();

        // Bottom
        Table bottom = new Table();
        bottom.padTop(16);
        TextButton backBtn = new TextButton("Back", skin);
        backBtn.addListener(e -> {
            if (backBtn.isPressed()) {
                leaveLobby();
                game.setScreen(new WorldMapScreen(game));
            }
            return false;
        });
        bottom.add(backBtn).width(160).height(36);
        root.add(bottom).row();

        refreshPlayerList();
    }

    private void hostGame() {
        if (inLobby) return;
        game.getNetwork().startHost();

        LobbyPlayer me = new LobbyPlayer(game.getNetwork().getPeerId(), game.getCharacter().name, game.isGameMaster());
        lobbyState.hostPeerId = game.getNetwork().getPeerId();
        lobbyState.players.clear();
        lobbyState.players.add(me);
        inLobby = true;

        beacon = new LobbyBeacon(lobbyState);
        beacon.start();

        hostBtn.setDisabled(true);
        readyBtn.setDisabled(false);
        setStatus("Hosting — waiting for players...", Color.GREEN);
        refreshPlayerList();
    }

    private void joinLobby(String hostIp) {
        if (inLobby) return;
        game.getNetwork().connectTo(hostIp);

        stage.addAction(Actions.delay(0.3f, Actions.run(() -> {
            LobbyPlayer me = new LobbyPlayer(game.getNetwork().getPeerId(), game.getCharacter().name, game.isGameMaster());
            game.getNetwork().send(NetMessage.Type.PEER_JOIN, me);
        })));

        inLobby = true;
        hostBtn.setDisabled(true);
        readyBtn.setDisabled(false);
        setStatus("Joined! Waiting for everyone...", Color.SKY);
    }

    private void leaveLobby() {
        if (inLobby) {
            game.getNetwork().leaveLobby();
            inLobby = false;
        }
        if (beacon != null) { beacon.stop(); beacon = null; }
        if (discovery != null) { discovery.stop(); discovery = null; }
        game.getNetwork().dispose();
    }

    private void setStatus(String text, Color color) {
        statusLabel.setText(text);
        statusLabel.setColor(color);
    }

    private void onLobbiesUpdated(Map<String, BeaconPayload> lobbies) {
        Gdx.app.postRunnable(() -> {
            discoveredTable.clear();
            if (lobbies.isEmpty()) {
                Label empty = new Label("No games found yet...", skin);
                empty.setColor(Color.GRAY);
                empty.setAlignment(Align.center);
                discoveredTable.add(empty).expand().center().pad(20);
            } else {
                for (BeaconPayload lobby : lobbies.values()) {
                    Table row = new Table(skin);
                    row.pad(8);

                    Table info = new Table();
                    Label nameLabel = new Label(lobby.adventureName, skin);
                    nameLabel.setFontScale(1.0f);
                    Label metaLabel = new Label(lobby.hostCharacterName + "  •  " + lobby.playerCount + " player(s)", skin);
                    metaLabel.setColor(Color.LIGHT_GRAY);
                    metaLabel.setFontScale(0.85f);
                    info.add(nameLabel).left().row();
                    info.add(metaLabel).left();

                    TextButton joinBtn = new TextButton("Join", skin);
                    joinBtn.setDisabled(inLobby);
                    joinBtn.addListener(e -> {
                        if (joinBtn.isPressed() && !joinBtn.isDisabled()) joinLobby(lobby.hostIp);
                        return false;
                    });

                    row.add(info).expandX().left();
                    row.add(joinBtn).width(72).height(32).padLeft(8);
                    discoveredTable.add(row).expandX().fillX().padBottom(4).row();
                }
            }
        });
    }

    private void broadcastLobby() {
        game.getNetwork().send(NetMessage.Type.LOBBY_STATE, lobbyState);
    }

    private void updateMyReadyState(boolean ready) {
        lobbyState.players.stream()
            .filter(p -> p.peerId.equals(game.getNetwork().getPeerId()))
            .findFirst().ifPresent(p -> p.ready = ready);
        broadcastLobby();
        refreshPlayerList();
        if (lobbyState.allReady()) startGame();
    }

    private void startGame() {
        setStatus("All ready! Starting...", Color.YELLOW);
        stage.addAction(Actions.delay(0.4f, Actions.run(() -> {
            game.getNetwork().send(NetMessage.Type.GAME_START, lobbyState);
            game.setScreen(new GameScreen(game));
        })));
    }

    private void refreshPlayerList() {
        Gdx.app.postRunnable(() -> {
            playerListTable.clear();
            if (!inLobby || lobbyState.players.isEmpty()) {
                Label empty = new Label("Join or host a game", skin);
                empty.setColor(Color.GRAY);
                empty.setAlignment(Align.center);
                playerListTable.add(empty).expand().center().pad(20);
            } else {
                for (LobbyPlayer p : lobbyState.players) {
                    Table row = new Table();
                    row.pad(6);

                    String prefix = p.isGameMaster ? "[GM] " : "";
                    Label nameLabel = new Label(prefix + p.characterName, skin);
                    Label stateLabel = new Label(p.ready ? "Ready" : "...", skin);
                    stateLabel.setColor(p.ready ? Color.GREEN : Color.GRAY);

                    row.add(nameLabel).expandX().left();
                    row.add(stateLabel).right();
                    playerListTable.add(row).expandX().fillX().padBottom(2).row();
                }
            }
        });
    }

    @Override
    public void onMessage(NetMessage message) {
        Gdx.app.postRunnable(() -> {
            switch (message.type) {
                case LOBBY_STATE:
                    lobbyState = gson.fromJson(message.payload, LobbyState.class);
                    refreshPlayerList();
                    if (lobbyState.allReady()) startGame();
                    break;
                case PEER_JOIN:
                    LobbyPlayer joining = gson.fromJson(message.payload, LobbyPlayer.class);
                    lobbyState.players.removeIf(p -> p.peerId.equals(joining.peerId));
                    lobbyState.players.add(joining);
                    if (game.getNetwork().isHost()) broadcastLobby();
                    refreshPlayerList();
                    setStatus(joining.characterName + " joined!", Color.SKY);
                    break;
                case PEER_LEAVE:
                    lobbyState.players.removeIf(p -> p.peerId.equals(message.senderId));
                    if (game.getNetwork().isHost()) broadcastLobby();
                    refreshPlayerList();
                    setStatus("A player left", Color.ORANGE);
                    break;
                default: break;
            }
        });
    }

    @Override
    public void onDisconnected() {
        Gdx.app.postRunnable(() -> {
            setStatus("Disconnected", Color.RED);
            inLobby = false;
            readyBtn.setDisabled(true);
            hostBtn.setDisabled(false);
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.06f, 0.06f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }
    @Override public void dispose() {
        if (beacon != null) { beacon.stop(); beacon = null; }
        if (discovery != null) { discovery.stop(); discovery = null; }
        stage.dispose();
        skin.dispose();
    }
}
