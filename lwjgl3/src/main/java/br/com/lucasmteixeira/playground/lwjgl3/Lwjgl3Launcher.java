package br.com.lucasmteixeira.playground.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import br.com.lucasmteixeira.playground.GameApp;

public class Lwjgl3Launcher {

    public static void main(String[] args) {
        new Lwjgl3Application(new GameApp(), buildConfig());
    }

    private static Lwjgl3ApplicationConfiguration buildConfig() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("RPG Game");
        config.useVsync(true);
        config.setForegroundFPS(0);
        config.setWindowedMode(1280, 720);
        config.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return config;
    }
}
