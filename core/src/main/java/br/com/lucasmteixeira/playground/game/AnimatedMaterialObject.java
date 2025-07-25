package br.com.lucasmteixeira.playground.game;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.com.lucasmteixeira.playground.game.animation.AnimationType;
import br.com.lucasmteixeira.playground.game.characters.actions.Direction;

public abstract class AnimatedMaterialObject extends MaterialObject {
	protected final EnumMap<AnimationType, EnumMap<Direction, Animation<Sprite>>> animations;

	protected AnimationType currentAnimation;

	protected float animationStateTime;

	protected AnimatedMaterialObject(Float x, Float y, Float w, Float h, Texture texture) {
		super(x, y, w, h, texture);

		this.animations = new EnumMap<AnimationType, EnumMap<Direction, Animation<Sprite>>>(AnimationType.class);
		this.currentAnimation = AnimationType.IDLE;
		
		for (EnumMap<Direction, Animation<Sprite>> animation: animations.values()) {
			for(Animation<Sprite> animationSprite: animation.values()) {
				for(Sprite sprite: animationSprite.getKeyFrames()) {
					sprite.setAlpha(1f);
				}
			}
		}

		this.animationStateTime = 0f;
	}

	@Override
	public void draw(SpriteBatch batch) {
		this.animationStateTime += Gdx.graphics.getDeltaTime();
		Sprite spriteToDraw = this.animations.get(this.currentAnimation).get(Direction.NONE).getKeyFrame(this.animationStateTime,
				this.currentAnimation.isLoop());
		
		spriteToDraw.setBounds(x, y, w, h);
		spriteToDraw.draw(batch);
	}
}
