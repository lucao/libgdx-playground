package br.com.lucasmteixeira.playground.game.scenery;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;

import br.com.lucasmteixeira.playground.game.MaterialObject;

public abstract class Building  extends MaterialObject {
	protected final List<SceneryObject> objects;
	
	protected Building(Float x, Float y, Float w, Float h, Texture texture) {
		super(x, y, w, h, texture);
		this.objects = new ArrayList<SceneryObject>();
	}

}
