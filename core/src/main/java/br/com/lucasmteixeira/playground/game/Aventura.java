package br.com.lucasmteixeira.playground.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import br.com.lucasmteixeira.playground.Main;

public class Aventura {
	protected final Map<long[], ArrayList<GameObject>> mapGameObjects;
	protected final World world;

	protected Aventura() {
		this.mapGameObjects = new HashMap<long[], ArrayList<GameObject>>();
		this.world = new World(new Vector2(0, -10), true);
	}

	public World getWorld() {
		return this.world;
	}

	public void addGameObject(GameObject gameObject) {
		long[] key = gameObject.getQuadrante();
		if (!this.mapGameObjects.containsKey(key)) {
			this.mapGameObjects.put(key, new ArrayList<GameObject>());
		}
		this.mapGameObjects.get(key).add(gameObject);
	}

	public void logic(Long deltaTime) {
		// TODO usar coroutines para parar atualização de elementos quando demorar muito
		// tempo
		// TODO pegar do mapa os gameobjects que são obrigados a atualizar

		this.world.step(1 / 60f, 6, 2);// TODO usar deltaTime
	}

	public List<MaterialObject> getDrawableGameObjects(Camera camera) {
		// order of quadrantes
		/**
		 * 0 - middle middle
		 * <p>
		 * 1 - top middle
		 * <p>
		 * 2 - top right
		 * <p>
		 * 3 - middle right
		 * <p>
		 * 4 - bottom right
		 * <p>
		 * 5 - middle bottom
		 * <p>
		 * 6 - bottom left
		 * <p>
		 * 7 - middle left
		 * <p>
		 * 8 - top left
		 * <p>
		 */

		final long[] middle = { Math.round(camera.position.x / Main.CONSTANTE_DO_QUADRANTE),
				Math.round(camera.position.y / Main.CONSTANTE_DO_QUADRANTE) };

		final List<MaterialObject> drawableGameObjects = new ArrayList<MaterialObject>();
		/*
		 * for (final long[] key : new long[][] { middle, { middle[0], middle[1]++ }, {
		 * middle[0]++, middle[1] }, { middle[0], middle[1]-- }, { middle[0],
		 * middle[1]-- }, { middle[0]--, middle[1] }, { middle[0]--, middle[1] }, {
		 * middle[0], middle[1]++ }, { middle[0], middle[1]++ } }) { if
		 * (this.mapGameObjects.containsKey(key)) { for (GameObject gameObject :
		 * this.mapGameObjects.get(key)) { if (gameObject instanceof MaterialObject) {
		 * drawableGameObjects.add((MaterialObject) gameObject); } } } }
		 */
		for (ArrayList<GameObject> gameObjects : this.mapGameObjects.values()) {
			for (GameObject gameObject : gameObjects) {
				if (gameObject instanceof MaterialObject) {
					drawableGameObjects.add((MaterialObject) gameObject);
				}
			}
		}
		return drawableGameObjects;

	}
}
