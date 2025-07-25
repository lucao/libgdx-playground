package br.com.lucasmteixeira.playground.game.aventuras;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import br.com.lucasmteixeira.playground.Main;
import br.com.lucasmteixeira.playground.game.GameObject;
import br.com.lucasmteixeira.playground.game.MaterialObject;
import br.com.lucasmteixeira.playground.game.WorldContactListener;

public class Aventura {
	protected final Map<KeyQuadrante, ArrayList<GameObject>> mapGameObjects;
	protected final World world;

	protected Aventura() {
		this.mapGameObjects = new HashMap<KeyQuadrante, ArrayList<GameObject>>();
		this.world = new World(new Vector2(0, -160), true);

		this.world.setContactListener(new WorldContactListener());
	}

	public World getWorld() {
		return this.world;
	}

	private class KeyQuadrante {
		private final long[] key;

		public KeyQuadrante(long[] key) {
			this.key = key;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			return Arrays.equals(key, ((KeyQuadrante) obj).key);
		}

		@Override
		public int hashCode() {
			return Arrays.hashCode(key);
		}
	}

	public void addGameObject(GameObject gameObject) {
		KeyQuadrante key = new KeyQuadrante(gameObject.getQuadrante());
		if (!this.mapGameObjects.containsKey(key)) {
			this.mapGameObjects.put(key, new ArrayList<GameObject>());
		}
		this.mapGameObjects.get(key).add(gameObject);
	}

	/**
	 * Process all logic from the game and return the game objects that are
	 * necessary for drawing
	 * 
	 * @param now
	 * @param deltaTime
	 */
	public List<MaterialObject> run(Camera camera, Instant now, Long deltaTime) {
		// TODO pegar do mapa os gameobjects que são obrigados a atualizar

		this.world.step(1 / 60f, 6, 2);// TODO usar deltaTime

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

		// order of quadrantes

		for (final long[] key : new long[][] { middle, { middle[0], middle[1]++ }, { middle[0]++, middle[1] },
				{ middle[0], middle[1]-- }, { middle[0], middle[1]-- }, { middle[0]--, middle[1] },
				{ middle[0]--, middle[1] }, { middle[0], middle[1]++ }, { middle[0], middle[1]++ } }) {
			KeyQuadrante keyObject = new KeyQuadrante(key);
			if (this.mapGameObjects.containsKey(keyObject)) {

				for (GameObject gameObject : this.mapGameObjects.get(keyObject)) {
					gameObject.play(now, deltaTime);
					if (gameObject instanceof MaterialObject) {
						drawableGameObjects.add((MaterialObject) gameObject);
					}
				}
			}
		}

		Collections.sort(drawableGameObjects);
		return drawableGameObjects;
	}
}
