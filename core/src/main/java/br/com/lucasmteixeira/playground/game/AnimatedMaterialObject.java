package br.com.lucasmteixeira.playground.game;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import br.com.lucasmteixeira.playground.game.characters.actions.ActionType;

public abstract class AnimatedMaterialObject extends MaterialObject {
	protected final EnumMap<ActionType, Animation<TextureRegion>> animations;

	protected AnimatedMaterialObject(Float x, Float y, Float w, Float h, Texture texture,
			EnumMap<ActionType, Animation<TextureRegion>> animations) {
		super(x, y, w, h, texture);

		this.animations = animations;
	}
}
