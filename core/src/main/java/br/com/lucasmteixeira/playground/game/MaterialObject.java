package br.com.lucasmteixeira.playground.game;

import com.badlogic.gdx.graphics.Texture;

public abstract class MaterialObject extends GameObject {
	public Float w;
	public Float h;
	
	private final Texture texture;
	
	protected MaterialObject(Float x, Float y, Float w, Float h, Texture texture) {
		super(x, y);
		
		this.w = w;
		this.h = h;
		
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}

}
