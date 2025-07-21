package br.com.lucasmteixeira.playground.game;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import br.com.lucasmteixeira.playground.game.animation.AnimationType;
import br.com.lucasmteixeira.playground.game.characters.actions.Direction;

public abstract class AnimatedMaterialObject extends MaterialObject {
	protected final EnumMap<AnimationType, EnumMap<Direction, Animation<TextureRegion>>> animations;

	protected AnimationType currentAnimation;

	protected float animationStateTime;

	protected AnimatedMaterialObject(Float x, Float y, Float w, Float h, Texture texture) {
		super(x, y, w, h, texture);

		this.animations = new EnumMap<AnimationType, EnumMap<Direction, Animation<TextureRegion>>>(AnimationType.class);
		this.currentAnimation = AnimationType.IDLE;

		this.animationStateTime = 0f;
	}

	@Override
	public TextureRegion getTexture() {
		this.animationStateTime += Gdx.graphics.getDeltaTime();
		return this.animations.get(this.currentAnimation).get(Direction.NONE).getKeyFrame(this.animationStateTime,
				this.currentAnimation.isLoop());
	}
}
