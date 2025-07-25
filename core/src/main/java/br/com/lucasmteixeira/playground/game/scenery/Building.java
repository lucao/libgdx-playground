package br.com.lucasmteixeira.playground.game.scenery;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;

import br.com.lucasmteixeira.playground.game.MaterialObject;

public abstract class Building extends MaterialObject {
	protected final List<SceneryObject> objects;

	protected final Foreground foreground;
	protected final Background background;

	private float foregroundAlpha;

	protected boolean isPlayerInside;

	protected Building(Float x, Float y, Float w, Float h, Texture texture, Texture foregroundTexture,
			Texture backgroundTexture) {
		super(x, y, w, h, texture);
		this.objects = new ArrayList<SceneryObject>();

		this.foreground = new Foreground(x, y, w, h, foregroundTexture);
		this.background = new Background(x, y, w, h, backgroundTexture);

		this.isPlayerInside = false;
		this.foregroundAlpha = 0f;
	}

	@Override
	public List<MaterialObject> getChildMaterialObjects() {
		MaterialObject[] childMaterialObjects = this.objects.toArray(new MaterialObject[this.objects.size() + 2]);
		childMaterialObjects[this.objects.size()] = this.background;
		if (this.isPlayerInside) {
			foregroundAlpha = foregroundAlpha > 0f ? foregroundAlpha - 0.1f : 0f;
			this.foreground.setAlpha(foregroundAlpha);
		} else {
			foregroundAlpha = foregroundAlpha < 1f ? foregroundAlpha + 0.1f : 1f;
			this.foreground.setAlpha(foregroundAlpha);
		}
		childMaterialObjects[this.objects.size() + 1] = this.foreground;

		return List.of(childMaterialObjects);
	}

}
