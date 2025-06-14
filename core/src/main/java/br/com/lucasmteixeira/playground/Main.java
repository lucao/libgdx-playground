package br.com.lucasmteixeira.playground;

import java.time.Clock;
import java.time.Instant;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    
    private static final CircularFifoQueue<Instant> frameTimes = new CircularFifoQueue<Instant>(4);

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        Main.frameTimes.add(Instant.now());
    }

    @Override
    public void render() {
    	final Instant now = Instant.now();
    	this.logic(Main.frameTimes.peek(), now);
    	Main.frameTimes.offer(now);
    	
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.end();
    }
    
    public void logic(Instant begin, Instant end) {
    	
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
