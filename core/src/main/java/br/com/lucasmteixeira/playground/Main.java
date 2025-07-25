package br.com.lucasmteixeira.playground;

import java.time.Instant;
import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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

	public static float frameRate = 1f / 60f;
	private static final CircularFifoQueue<Long> frameTimes = new CircularFifoQueue<Long>(4);

	// Box2DDebugRenderer debugRenderer;
	// DEBUG
//	private com.badlogic.gdx.scenes.scene2d.ui.List<String> debug_GameObjectsList;
//
//	private Stage debugStage;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);

		// debugRenderer = new Box2DDebugRenderer();

		this.aventura = new AventuraPadrao();

		// Create camera and viewport
		this.VIEWPORT_WIDTH = WORLD_DISTANCE_UNIT;
		this.VIEWPORT_HEIGHT = Math
				.round(WORLD_DISTANCE_UNIT * ((float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));

		final Player player = new NarutoPlayer(0f, 0f, 20f, 20f, this.aventura.getWorld());
		this.followedObject = player;
		player.setzIndex(2f);
		this.aventura.addGameObject(player);

		final Pixmap pixmap = new Pixmap(64, 64, Format.RGBA8888);
		pixmap.setColor(0, 1, 0, 0.75f);
		pixmap.fillRectangle(0, 0, 500, 20);
		Ground ground = new Ground(-30f, -50f, 500f, 20f, new Texture(pixmap), this.aventura.getWorld());
		pixmap.dispose();
		// Ground ground = new Ground(-30f, -50f, 500f, 20f, new Texture("libgdx.png"),
		// this.aventura.getWorld());
		this.aventura.addGameObject(ground);
		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		// camera.position.set(player.x, player.y, 0);

		camera.update();
		viewport = new ExtendViewport(this.VIEWPORT_WIDTH, this.VIEWPORT_HEIGHT, camera);
		batch = new SpriteBatch();

		Main.frameTimes.add(Instant.now().getEpochSecond());
		Gdx.input.setInputProcessor(new InputProcessorPC(player, camera));

		// DEBUG
//		debugStage = new Stage();
//		long[] debugCameraQuadrante = { Math.round(camera.position.x / Main.CONSTANTE_DO_QUADRANTE),
//				Math.round(camera.position.y / Main.CONSTANTE_DO_QUADRANTE) };
//		debug_GameObjectsList = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(
//				new Skin(Gdx.files.internal("ui/uiskin.json")));
//		debug_GameObjectsList.setItems(String.valueOf(player.getQuadrante()).concat(" player quadrante"),
//				String.valueOf(ground.getQuadrante()).concat(" ground quadrante"),
//				String.valueOf(debugCameraQuadrante).concat(" camera quadrante"));
//		debug_GameObjectsList.setDebug(true);
//		debugStage.addActor(debug_GameObjectsList);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.update();
	}

	@Override
	public void render() {
		final Instant now = Instant.now();
		ScreenUtils.clear(Color.DARK_GRAY);

		final List<MaterialObject> drawableObjects = this.aventura.run(this.camera, now,
				now.toEpochMilli() - Main.frameTimes.peek());
		Main.frameTimes.offer(now.toEpochMilli());

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

		// DEBUG
//		debugStage.act(Gdx.graphics.getDeltaTime());
//		debugStage.draw();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
