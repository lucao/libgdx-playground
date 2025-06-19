package br.com.lucasmteixeira.playground.game;

import com.badlogic.gdx.graphics.Texture;

public abstract class MaterialObject extends GameObject {
	protected Float w;
	protected Float h;
	
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

	public Float getW() {
		return w;
	}

	public Float getH() {
		return h;
	}
}
