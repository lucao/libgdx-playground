package br.com.lucasmteixeira.playground;

import java.time.Instant;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.com.lucasmteixeira.playground.game.Aventura;
import br.com.lucasmteixeira.playground.game.InputProcessorPC;
import br.com.lucasmteixeira.playground.game.MaterialObject;
import br.com.lucasmteixeira.playground.game.aventuras.AventuraPadrao;
import br.com.lucasmteixeira.playground.game.characters.Player;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
	private Camera camera;
	private MaterialObject followedObject;

	private final static float lerp = 0.1f;

	private Viewport viewport;
	private SpriteBatch batch;

	private static final int WORLD_DISTANCE_UNIT = 10;
	public static final int CONSTANTE_DO_QUADRANTE = 10000;

	private int VIEWPORT_WIDTH;
	private int VIEWPORT_HEIGHT;

	private Aventura aventura;

	private static final CircularFifoQueue<Long> frameTimes = new CircularFifoQueue<Long>(4);

	@Override
	public void create() {
		this.aventura = new AventuraPadrao();

		// Create camera and viewport
		this.VIEWPORT_WIDTH = WORLD_DISTANCE_UNIT;
		this.VIEWPORT_HEIGHT = Math
				.round(WORLD_DISTANCE_UNIT * ((float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));

		final Player player = new Player(0f, 0f, 20f, 20f, new Texture("libgdx.png"), this.aventura.getWorld());
		this.followedObject = player;
		this.aventura.addGameObject(player);
		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		//camera.position.set(player.x, player.y, 0);
		
		camera.update();
		viewport = new ExtendViewport(this.VIEWPORT_WIDTH, this.VIEWPORT_HEIGHT, camera);
		batch = new SpriteBatch();

		Main.frameTimes.add(Instant.now().getEpochSecond());
		Gdx.input.setInputProcessor(new InputProcessorPC(player, camera));
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void render() {
		final Long now = Instant.now().getEpochSecond();
		ScreenUtils.clear(Color.DARK_GRAY);
		this.aventura.logic(now - Main.frameTimes.peek());
		
		viewport.apply();
		//camera.position.x = ((followedObject.x + followedObject.w / 2) - camera.position.x) * lerp;
		//camera.position.y = ((followedObject.y + followedObject.h / 2) - camera.position.y) * lerp;
		camera.update();
		
		
		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);
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
