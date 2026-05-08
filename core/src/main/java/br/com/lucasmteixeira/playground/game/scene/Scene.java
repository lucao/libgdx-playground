package br.com.lucasmteixeira.playground.game.scene;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public interface Scene {
    void update(float delta);
    void render(ModelBatch modelBatch, GameCamera camera);
    InputProcessor getInputProcessor();
    void resize(int width, int height);
    void dispose();
}
