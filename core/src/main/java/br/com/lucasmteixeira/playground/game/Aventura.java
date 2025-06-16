package br.com.lucasmteixeira.playground.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Aventura {
	protected final Map<GameObject, ArrayList<GameObject>> mapGameObjects;
	protected final World world;
	public final Camera camera;

	private static Integer STD_ZOOM_W = 50;// 50 meters
	private static Integer STD_ZOOM_H = 10;// 10 meters

	private Float zoomRatio;

	private final ExtendViewport viewport;

	protected Aventura(Camera camera) {
		this.mapGameObjects = new HashMap<>();
		this.viewport = new ExtendViewport(0, 0);
		this.camera = camera;
		this.world = new World(new Vector2(0, -10), true);
		this.zoomRatio = 1f;
	}

	public void logic(Long deltaTime) {
		
		// TODO usar coroutines para parar atualização de elementos quando demorar muito
		// tempo
		// TODO pegar do mapa os gameobjects que são obrigados a atualizar
		for (List<GameObject> listOfGameObjects : mapGameObjects.values()) {
			for (GameObject gameObject : listOfGameObjects) {
				gameObject.play(deltaTime);
			}
		}
		this.world.step(1 / 60f, 6, 2);// TODO usar deltaTime
	}
	
	public List<GameObject> getDrawableGameObjects() {
		// order of quadrantes 
		/*
		 * 0 - middle middle
		 * 1 - top middle
		 * 2 - top right
		 * 3 - middle right
		 * 4 - bottom right
		 * 5 - middle bottom
		 * 6 - bottom left
		 * 7 - middle left
		 * 8 - top left
		 */
		final long[] quadrantes = new long[9] {};
		return null;
		
	}

	public void resize(boolean zoomIn) {
		if (zoomIn) {
			this.zoomRatio -= 0.5f;
		} else {
			this.zoomRatio += 0.5f;
		}
		// use true here to center the camera
		// that's what you probably want in case of a UI
		this.viewport.update(Math.round(zoomRatio * STD_ZOOM_W), Math.round(zoomRatio * STD_ZOOM_H), true);
	}
}
