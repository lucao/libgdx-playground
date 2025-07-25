package br.com.lucasmteixeira.playground.game.scenery;

import com.badlogic.gdx.graphics.Texture;

public abstract class Tavern extends Building {

	protected Tavern(Float x, Float y, Float w, Float h, Texture texture, Texture foregroundTexture,
			Texture backgroundTexture) {
		super(x, y, w, h, texture, foregroundTexture, backgroundTexture);
	}

}
