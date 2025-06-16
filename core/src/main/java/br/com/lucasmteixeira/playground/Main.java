package br.com.lucasmteixeira.playground;

import java.time.Clock;
import java.time.Instant;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import br.com.lucasmteixeira.playground.game.Aventura;
import br.com.lucasmteixeira.playground.game.aventuras.AventuraPadrao;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private Aventura aventura;

	private static final CircularFifoQueue<Long> frameTimes = new CircularFifoQueue<Long>(4);

	@Override
	public void create() {
		batch = new SpriteBatch();
		aventura = new AventuraPadrao(new OrthographicCamera());

		Main.frameTimes.add(Instant.now().getEpochSecond());
	}

	@Override
	public void render() {
		final Long now = Instant.now().getEpochSecond();		
		
		this.aventura.logic(now - Main.frameTimes.peek());
		
		ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
		batch.setProjectionMatrix(this.aventura.camera.combined);
		batch.begin();
		// TODO draw all aventura's pertinent objects
		// batch.draw(image, 140, 210);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

}
