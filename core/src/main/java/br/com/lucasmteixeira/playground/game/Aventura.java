package br.com.lucasmteixeira.playground.game;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Aventura extends Stage {
	protected final List<GameObject> gameObjects;
	protected final World world;
	protected final Camera camera;

	private static Integer STD_ZOOM_W = 50;// 50 meters
	private static Integer STD_ZOOM_H = 10;// 10 meters

	private Float zoomRatio;
	

	protected Aventura(Camera camera) {
		super(new ExtendViewport(STD_ZOOM_W, STD_ZOOM_H, camera));
		this.gameObjects = new ArrayList<>();
		
		this.camera = camera;
		this.world = new World(new Vector2(0, -10), true);
		this.zoomRatio = 1f;
	}
	

	@Override
	public void act(float delta) {
		for (GameObject gameObjects: gameObjects) {
			gameObjects.act(delta);
		}
		super.act(delta);
	}

	public void logic(Long deltaTime) {
		this.world.step(1/60f, 6, 2);//TODO usar deltaTime
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

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
}
