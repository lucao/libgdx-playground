package br.com.lucasmteixeira.playground.game;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import br.com.lucasmteixeira.playground.game.animation.AnimationType;

public abstract class AnimatedMaterialObject extends MaterialObject {
	public static final float FRAME_INTERVAL = 0.1f;
	protected final EnumMap<AnimationType, Animation<TextureRegion>> animations;

	protected AnimationType currentAnimation;

	private float animationStateTime;

	protected AnimatedMaterialObject(Float x, Float y, Float w, Float h, Texture texture) {
		super(x, y, w, h, texture);

		this.animations = new EnumMap<AnimationType, Animation<TextureRegion>>(AnimationType.class);
		this.currentAnimation = AnimationType.IDLE;

		this.animationStateTime = 0f;
	}

	@Override
	public TextureRegion getTexture() {
		this.animationStateTime += Gdx.graphics.getDeltaTime();
		return this.animations.get(this.currentAnimation).getKeyFrame(this.animationStateTime, this.currentAnimation.isLoop());
	}
}
