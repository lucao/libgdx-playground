package br.com.lucasmteixeira.playground.game.scenery;

import java.time.Instant;

import com.badlogic.gdx.graphics.Texture;

public abstract class Chair extends SceneryObject {

	protected Chair(Float x, Float y, Float w, Float h, Texture texture) {
		super(x, y, w, h, texture);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void play(Instant now, Long deltaTime) {
		// TODO Auto-generated method stub

	}

}
