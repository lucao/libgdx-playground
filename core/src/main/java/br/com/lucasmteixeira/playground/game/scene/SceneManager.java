package br.com.lucasmteixeira.playground.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import br.com.lucasmteixeira.playground.screens.GameScreen.SceneType;

public class SceneManager {

    private Scene activeScene;
    private SceneType activeType;
    private final GameCamera camera;
    private final ModelBatch modelBatch;
    private final InputMultiplexer inputMultiplexer;
    private String localPlayerId;

    public SceneManager(GameCamera camera, InputMultiplexer inputMultiplexer) {
        this.camera = camera;
        this.inputMultiplexer = inputMultiplexer;
        this.modelBatch = new ModelBatch();
    }

    public void setLocalPlayerId(String playerId) {
        this.localPlayerId = playerId;
    }

    public void switchTo(SceneType type) {
        if (activeScene != null) {
            inputMultiplexer.removeProcessor(activeScene.getInputProcessor());
            activeScene.dispose();
        }

        switch (type) {
            case ISO:
                camera.setMode(GameCamera.Mode.ISOMETRIC);
                activeScene = new TestAdventureScene(localPlayerId != null ? localPlayerId : "local");
                break;
            case SIDE_VIEW:
                camera.setMode(GameCamera.Mode.SIDE_VIEW);
                activeScene = new TestAdventureScene(localPlayerId != null ? localPlayerId : "local");
                break;
            case TEST:
                camera.setMode(GameCamera.Mode.ISOMETRIC);
                activeScene = new TestAdventureScene(localPlayerId != null ? localPlayerId : "local");
                break;
        }

        activeType = type;
        inputMultiplexer.addProcessor(activeScene.getInputProcessor());
        Gdx.app.log("SceneManager", "Switched to " + type);
    }

    public void update(float delta) {
        if (activeScene != null) activeScene.update(delta);
    }

    public void render() {
        if (activeScene != null) activeScene.render(modelBatch, camera);
    }

    public void resize(int width, int height) {
        camera.resize(width, height);
        if (activeScene != null) activeScene.resize(width, height);
    }

    public GameCamera getCamera() { return camera; }
    public SceneType getActiveType() { return activeType; }
    public Scene getActiveScene() { return activeScene; }

    public void dispose() {
        if (activeScene != null) activeScene.dispose();
        modelBatch.dispose();
    }
}
