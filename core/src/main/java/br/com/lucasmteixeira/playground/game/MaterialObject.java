package br.com.lucasmteixeira.playground.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class MaterialObject extends GameObject {
	protected Float w;
	protected Float h;

	protected final Texture texture;

	protected MaterialObject(Float x, Float y, Float w, Float h, Texture texture) {
		super(x, y);

		this.w = w;
		this.h = h;

		this.texture = texture;
	}

	public TextureRegion getTexture() {
		return new TextureRegion(texture);
	}

	public Float getW() {
		return w;
	}

	public Float getH() {
		return h;
	}
}
