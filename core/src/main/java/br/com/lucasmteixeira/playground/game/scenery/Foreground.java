package br.com.lucasmteixeira.playground.game.scenery;

import java.time.Instant;

import com.badlogic.gdx.graphics.Texture;

import br.com.lucasmteixeira.playground.game.MaterialObject;

public class Foreground extends MaterialObject {

	protected Foreground(Float x, Float y, Float w, Float h, Texture texture) {
		super(x, y, w, h, texture);
	}

	@Override
	public void play(Instant now, Long deltaTime) {
		// TODO Auto-generated method stub

	}

}
