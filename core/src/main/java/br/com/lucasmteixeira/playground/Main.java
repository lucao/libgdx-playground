package br.com.lucasmteixeira.playground;

import java.time.Instant;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.com.lucasmteixeira.playground.game.Aventura;
import br.com.lucasmteixeira.playground.game.MaterialObject;
import br.com.lucasmteixeira.playground.game.aventuras.AventuraPadrao;
import br.com.lucasmteixeira.playground.game.characters.Player;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
	private OrthographicCamera camera;
	private MaterialObject followedObject;

	private final static float lerp = 0.1f;

	private Viewport viewport;
	private SpriteBatch batch;

	private static final int WORLD_DISTANCE_UNIT = 100;
	public static final int CONSTANTE_DO_QUADRANTE = 10000;

	private Aventura aventura;

	private static final CircularFifoQueue<Long> frameTimes = new CircularFifoQueue<Long>(4);

	@Override
	public void create() {
		this.aventura = new AventuraPadrao();
		float elementWidth = WORLD_DISTANCE_UNIT * 0.2f; // 20% of viewport width
		float elementHeight = WORLD_DISTANCE_UNIT * 0.1f; // 10% of viewport height
		float elementX = WORLD_DISTANCE_UNIT / 2 - elementWidth / 2;
		float elementY = WORLD_DISTANCE_UNIT / 2 - elementHeight / 2;
		// final Player player = new Player(0f, 0f, 50f, 50f, new Texture("libgdx.png"),
		// this.aventura.getWorld());
		final Player player = new Player(elementX, elementY, elementWidth, elementHeight, new Texture("libgdx.png"),
				this.aventura.getWorld());
		this.followedObject = player;
		this.aventura.addGameObject(player);

		// Create camera and viewport
		float worldWidth = WORLD_DISTANCE_UNIT;
		float worldHeight = WORLD_DISTANCE_UNIT * ((float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth());

		camera = new OrthographicCamera();
		camera.position.set(player.x, player.y, 0);

		camera.update();
		viewport = new ExtendViewport(worldWidth, worldHeight, camera);
		viewport.apply();

		batch = new SpriteBatch();

		Main.frameTimes.add(Instant.now().getEpochSecond());
	}

	@Override
	public void render() {
		final Long now = Instant.now().getEpochSecond();
		ScreenUtils.clear(Color.DARK_GRAY);
		this.aventura.logic(now - Main.frameTimes.peek());

		camera.position.x = ((followedObject.x + followedObject.w / 2) - camera.position.x) * lerp;
		camera.position.y = ((followedObject.y + followedObject.h / 2) - camera.position.y) * lerp;
		camera.update();

		batch.setProjectionMatrix(this.camera.combined);
		batch.begin();
		// draw all aventura's pertinent objects
		for (MaterialObject materialObject : this.aventura.getDrawableGameObjects(this.camera)) {
			batch.draw(materialObject.getTexture(), materialObject.x, materialObject.y, materialObject.w,
					materialObject.h);
		}

		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
