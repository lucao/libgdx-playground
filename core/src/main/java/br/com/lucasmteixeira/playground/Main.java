package br.com.lucasmteixeira.playground;

import java.time.Instant;
import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.com.lucasmteixeira.playground.game.GameObject;
import br.com.lucasmteixeira.playground.game.InputProcessorPC;
import br.com.lucasmteixeira.playground.game.MaterialObject;
import br.com.lucasmteixeira.playground.game.aventuras.Aventura;
import br.com.lucasmteixeira.playground.game.aventuras.AventuraPadrao;
import br.com.lucasmteixeira.playground.game.characters.player.NarutoPlayer;
import br.com.lucasmteixeira.playground.game.characters.player.Player;
import br.com.lucasmteixeira.playground.game.scenery.Ground;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
	private OrthographicCamera camera;
	private GameObject followedObject;

	public final static float FOLLOW_LERP = 0.1f;
	public final static float ZOOM_LERP = 0.4f;

	private Viewport viewport;
	private SpriteBatch batch;

	private static final int WORLD_DISTANCE_UNIT = 100;
	public static final int CONSTANTE_DO_QUADRANTE = 10000;

	private int VIEWPORT_WIDTH;
	private int VIEWPORT_HEIGHT;

	private Aventura aventura;

	private Player player;

	public static float frameRate = 1f / 60f;
	private static final CircularFifoQueue<Instant> frameTimes = new CircularFifoQueue<Instant>(10);

	private boolean onStartMenu;
	private boolean onGame;

	// Box2DDebugRenderer debugRenderer;
	// DEBUG
//	private com.badlogic.gdx.scenes.scene2d.ui.List<String> debug_GameObjectsList;
//
	private Stage startMenuStage;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);

		// debugRenderer = new Box2DDebugRenderer();

		// Create camera and viewport
		this.VIEWPORT_WIDTH = WORLD_DISTANCE_UNIT;
		this.VIEWPORT_HEIGHT = Math
				.round(WORLD_DISTANCE_UNIT * ((float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));

		this.onStartMenu = true;
		this.onGame = false;

		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		// camera.position.set(player.x, player.y, 0);

		camera.update();
		viewport = new ExtendViewport(this.VIEWPORT_WIDTH, this.VIEWPORT_HEIGHT, camera);

		Main.frameTimes.add(Instant.now());

		// DEBUG
		startMenuStage = new Stage(new ScreenViewport());
		TextButton startButton = new TextButton("Start game", new Skin(Gdx.files.internal("ui/uiskin.json")));
		startButton.addListener(e -> this.onStartMenu = false);
		startButton.center();
		startMenuStage.addActor(startButton);

		Gdx.input.setInputProcessor(startMenuStage);
	}

	@Override
	public void resize(int width, int height) {
		if (this.onStartMenu) {
			startMenuStage.getViewport().update(width, height, true);
		}
		viewport.update(width, height);
		camera.update();
	}

	@Override
	public void render() {
		final Instant now = Instant.now();
		ScreenUtils.clear(Color.DARK_GRAY);

		if (onStartMenu) {
			startMenuStage.act(now.toEpochMilli() - Main.frameTimes.peek().toEpochMilli());
			startMenuStage.draw();
		} else {
			if (onGame == false) {
				this.aventura = new AventuraPadrao();

				this.player = new NarutoPlayer(0f, 0f, 20f, 20f, this.aventura.getWorld());
				this.followedObject = this.player;
				this.player.setzIndex(2f);
				this.aventura.addGameObject(this.player);

				final Pixmap pixmap = new Pixmap(64, 64, Format.RGBA8888);
				pixmap.setColor(0, 1, 0, 0.75f);
				pixmap.fillRectangle(0, 0, 500, 20);
				Ground ground = new Ground(-30f, -50f, 500f, 20f, new Texture(pixmap), this.aventura.getWorld());
				pixmap.dispose();

				// Ground ground = new Ground(-30f, -50f, 500f, 20f, new Texture("libgdx.png"),
				// this.aventura.getWorld());
				this.aventura.addGameObject(ground);

				batch = new SpriteBatch();

				Gdx.input.setInputProcessor(new InputProcessorPC(this.player, camera));
				onGame = true;
			}
			final List<MaterialObject> drawableObjects = this.aventura.run(this.camera, now,
					now.toEpochMilli() - Main.frameTimes.peek().toEpochMilli());

			if (followedObject instanceof MaterialObject) {
				MaterialObject materialFollowedObject = (MaterialObject) followedObject;
				camera.position.x += ((materialFollowedObject.getX() + materialFollowedObject.getW() / 2)
						- camera.position.x) * FOLLOW_LERP;
				camera.position.y += ((materialFollowedObject.getY() + materialFollowedObject.getH() / 2)
						- camera.position.y) * FOLLOW_LERP;
			} else {
				camera.position.x += (followedObject.getX() - camera.position.x) * FOLLOW_LERP;
				camera.position.y += (followedObject.getY() - camera.position.y) * FOLLOW_LERP;
			}

			camera.update();
			viewport.apply();

			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			// draw all aventura's pertinent objects
			for (MaterialObject materialObject : drawableObjects) {
				materialObject.draw(batch);
				for (MaterialObject childMaterialObject : materialObject.getChildMaterialObjects()) {
					childMaterialObject.draw(batch);
				}
			}

			batch.end();
		}

		Main.frameTimes.offer(now);
		//TODO DEBUG the frameTimes
		// DEBUG
//		debugStage.act(Gdx.graphics.getDeltaTime());
//		debugStage.draw();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
