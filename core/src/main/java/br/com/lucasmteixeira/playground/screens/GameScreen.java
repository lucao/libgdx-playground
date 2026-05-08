package br.com.lucasmteixeira.playground.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;

import br.com.lucasmteixeira.playground.GameApp;
import br.com.lucasmteixeira.playground.game.scene.GameCamera;
import br.com.lucasmteixeira.playground.game.scene.SceneManager;
import br.com.lucasmteixeira.playground.game.scene.TestAdventureScene;
import br.com.lucasmteixeira.playground.gm.GMCommand;
import br.com.lucasmteixeira.playground.gm.GMPanel;
import br.com.lucasmteixeira.playground.model.AdventureData;
import br.com.lucasmteixeira.playground.model.NetMessage;
import br.com.lucasmteixeira.playground.network.NetworkListener;

public class GameScreen implements Screen, NetworkListener, GMPanel.GMActionListener {

    public enum SceneType { ISO, SIDE_VIEW, TEST }
    public enum PlayMode { STANDARD, DIALOG, FIGHT }

    private final GameApp game;
    private final Gson gson = new Gson();

    private Stage uiStage;
    private Skin skin;
    private InputMultiplexer inputMultiplexer;
    private GameCamera gameCamera;
    private SceneManager sceneManager;

    private PlayMode playMode = PlayMode.STANDARD;
    private Label modeLabel;
    private GMPanel gmPanel;
    private float syncTimer = 0;
    private static final float SYNC_INTERVAL = 0.05f;

    public GameScreen(GameApp game) {
        this.game = game;
    }

    @Override
    public void show() {
        AdventureData adv = game.getSelectedAdventure();

        uiStage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        gameCamera = new GameCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(uiStage);
        // Scroll to zoom
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                gameCamera.zoom(amountY * 2f);
                return true;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        sceneManager = new SceneManager(gameCamera, inputMultiplexer);
        sceneManager.setLocalPlayerId(game.getNetwork().getPeerId());

        SceneType sceneType;
        if ("TEST".equals(adv.sceneType)) {
            sceneType = SceneType.TEST;
        } else if ("2D".equals(adv.sceneType)) {
            sceneType = SceneType.SIDE_VIEW;
        } else {
            sceneType = SceneType.ISO;
        }
        sceneManager.switchTo(sceneType);

        game.getNetwork().setListener(this);
        buildUI(adv);
    }

    private void buildUI(AdventureData adv) {
        Table root = new Table();
        root.setFillParent(true);
        uiStage.addActor(root);

        // Top HUD bar
        Table topBar = new Table(skin);
        topBar.pad(4);
        modeLabel = new Label(adv.name + "  |  " + sceneManager.getActiveType() + "  |  " + playMode, skin);
        topBar.add(modeLabel).expandX().left();

        if (game.isGameMaster()) {
            TextButton toggleView = new TextButton("Toggle ISO/Side", skin);
            toggleView.addListener(e -> {
                if (toggleView.isPressed()) {
                    SceneType next = sceneManager.getActiveType() == SceneType.ISO ? SceneType.SIDE_VIEW : SceneType.ISO;
                    sceneManager.switchTo(next);
                    updateModeLabel(adv);
                }
                return false;
            });
            topBar.add(toggleView).padLeft(10);
        }
        root.add(topBar).fillX().top().row();
        root.add().expand().row();

        // Bottom action bar
        Table bottomBar = new Table(skin);
        bottomBar.pad(6);
        for (String label : new String[]{"Move", "Attack", "Interact", "Talk"}) {
            TextButton btn = new TextButton(label, skin);
            bottomBar.add(btn).padRight(8);
        }
        root.add(bottomBar).fillX().bottom().row();

        // GM panel
        if (game.isGameMaster()) {
            gmPanel = new GMPanel(skin, this);
            Table gmContainer = gmPanel.getTable();
            gmContainer.setSize(220, Gdx.graphics.getHeight() - 80f);
            gmContainer.setPosition(Gdx.graphics.getWidth() - 225f, 40f);
            uiStage.addActor(gmContainer);
        }
    }

    private void updateModeLabel(AdventureData adv) {
        if (modeLabel != null)
            modeLabel.setText(adv.name + "  |  " + sceneManager.getActiveType() + "  |  " + playMode);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        sceneManager.update(delta);
        sceneManager.render();

        // Sync local player position
        if (sceneManager.getActiveScene() instanceof TestAdventureScene) {
            syncTimer += delta;
            if (syncTimer >= SYNC_INTERVAL) {
                syncTimer = 0;
                TestAdventureScene scene = (TestAdventureScene) sceneManager.getActiveScene();
                TestAdventureScene.PlayerSphere lp = scene.getLocalPlayer();
                float[] pos = {lp.x, lp.y, lp.z};
                game.getNetwork().send(NetMessage.Type.PLAYER_ACTION, pos);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            // TODO: in-game pause menu
        }

        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int w, int h) {
        uiStage.getViewport().update(w, h, true);
        sceneManager.resize(w, h);
    }

    @Override
    public void onCommand(GMCommand command) {
        Gdx.app.log("GM", command.type + " -> " + command.targetId);
        game.getNetwork().send(NetMessage.Type.GM_COMMAND, command);
    }

    @Override
    public void onMessage(NetMessage message) {
        switch (message.type) {
            case GM_COMMAND:
                GMCommand cmd = gson.fromJson(message.payload, GMCommand.class);
                onCommand(cmd);
                break;
            case PLAYER_ACTION:
                if (sceneManager.getActiveScene() instanceof TestAdventureScene) {
                    TestAdventureScene scene = (TestAdventureScene) sceneManager.getActiveScene();
                    float[] pos = gson.fromJson(message.payload, float[].class);
                    scene.addPlayer(message.senderId);
                    scene.updateRemotePlayer(message.senderId, pos[0], pos[1], pos[2]);
                }
                break;
            case PEER_LEAVE:
                if (sceneManager.getActiveScene() instanceof TestAdventureScene) {
                    ((TestAdventureScene) sceneManager.getActiveScene()).removePlayer(message.senderId);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDisconnected() {
        Gdx.app.log("GameScreen", "Peer disconnected");
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }

    @Override
    public void dispose() {
        uiStage.dispose();
        skin.dispose();
        sceneManager.dispose();
    }
}
