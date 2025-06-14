package br.com.lucasmteixeira.playground.game;

import java.util.Set;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Aventura extends Stage {
	private final Set<GameObject> gameObjects;

	private final Camera camera;

	private static Integer STD_ZOOM_W = 50;// 50 meters
	private static Integer STD_ZOOM_H = 10;// 10 meters

	private Float zoomRatio;

	public Aventura(Set<GameObject> gameObjects, Camera camera) {
		super(new ExtendViewport(STD_ZOOM_W, STD_ZOOM_H, camera));
		this.gameObjects = gameObjects;
		this.camera = camera;
		this.zoomRatio = 1f;
	}

	public void resize(boolean zoomIn) {
		if (zoomIn) {
			this.zoomRatio -= 0.5f;
		} else {
			this.zoomRatio += 0.5f;
		}
		// use true here to center the camera
		// that's what you probably want in case of a UI
		this.getViewport().update(Math.round(zoomRatio * STD_ZOOM_W), Math.round(zoomRatio * STD_ZOOM_H), true);
	}
}
