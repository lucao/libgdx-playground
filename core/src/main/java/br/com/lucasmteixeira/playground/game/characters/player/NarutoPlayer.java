package br.com.lucasmteixeira.playground.game.characters.player;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

import br.com.lucasmteixeira.playground.game.MaterialObject;
import br.com.lucasmteixeira.playground.game.animation.AnimationType;
import br.com.lucasmteixeira.playground.game.characters.actions.Direction;

public class NarutoPlayer extends Player {

	public NarutoPlayer(Float x, Float y, Float w, Float h, World world) {
		super(x, y, w, h,
				MaterialObject.loadTextureWithTransparency("Naruto.jpg", new Color(0.592f, 0.682f, 0.843f, 1), 0.07f),
				world);

		Sprite[] idleFramesRight = new Sprite[] { new Sprite(this.sprite, 30, 5, 36, 55),
				new Sprite(this.sprite, 80, 5, 36, 55), new Sprite(this.sprite, 130, 5, 36, 55),
				new Sprite(this.sprite, 178, 5, 36, 55), new Sprite(this.sprite, 225, 5, 36, 55),
				new Sprite(this.sprite, 275, 5, 36, 55) };

		Sprite[] idleFramesLeft = new Sprite[] { new Sprite(this.sprite, 30, 5, 36, 55),
				new Sprite(this.sprite, 80, 5, 36, 55), new Sprite(this.sprite, 130, 5, 36, 55),
				new Sprite(this.sprite, 178, 5, 36, 55), new Sprite(this.sprite, 225, 5, 36, 55),
				new Sprite(this.sprite, 275, 5, 36, 55) };
		for (int i = 0; i < idleFramesLeft.length; i++)
			idleFramesLeft[i].flip(true, false);

		this.animations.put(AnimationType.IDLE, new EnumMap<Direction, Animation<Sprite>>(Direction.class));
		this.animations.get(AnimationType.IDLE).put(Direction.RIGHT,
				new Animation<Sprite>(AnimationType.IDLE.getFrameInterval(), idleFramesRight));
		this.animations.get(AnimationType.IDLE).put(Direction.NONE,
				new Animation<Sprite>(AnimationType.IDLE.getFrameInterval(), idleFramesRight));
		this.animations.get(AnimationType.IDLE).put(Direction.LEFT,
				new Animation<Sprite>(AnimationType.IDLE.getFrameInterval(), idleFramesLeft));

		Sprite[] walkingFramesRight = new Sprite[] { new Sprite(this.sprite, 28, 70, 33, 52),
				new Sprite(this.sprite, 72, 70, 25, 52), new Sprite(this.sprite, 117, 70, 27, 52),
				new Sprite(this.sprite, 155, 70, 26, 52), new Sprite(this.sprite, 190, 70, 25, 52),
				new Sprite(this.sprite, 221, 70, 24, 52) };

		Sprite[] walkingFramesLeft = new Sprite[] { new Sprite(this.sprite, 28, 70, 33, 52),
				new Sprite(this.sprite, 72, 70, 25, 52), new Sprite(this.sprite, 117, 70, 27, 52),
				new Sprite(this.sprite, 155, 70, 26, 52), new Sprite(this.sprite, 190, 70, 25, 52),
				new Sprite(this.sprite, 221, 70, 24, 52) };
		for (int i = 0; i < walkingFramesLeft.length; i++)
			walkingFramesLeft[i].flip(true, false);

		this.animations.put(AnimationType.WALKING, new EnumMap<Direction, Animation<Sprite>>(Direction.class));
		this.animations.get(AnimationType.WALKING).put(Direction.RIGHT,
				new Animation<Sprite>(AnimationType.WALKING.getFrameInterval(), walkingFramesRight));
		this.animations.get(AnimationType.WALKING).put(Direction.NONE,
				new Animation<Sprite>(AnimationType.WALKING.getFrameInterval(), walkingFramesRight));
		this.animations.get(AnimationType.WALKING).put(Direction.LEFT,
				new Animation<Sprite>(AnimationType.WALKING.getFrameInterval(), walkingFramesLeft));

		Sprite[] fallingFramesRight = new Sprite[] { new Sprite(this.sprite, 139, 330, 31, 54),
				new Sprite(this.sprite, 177, 330, 31, 54) };

		Sprite[] fallingFramesLeft = new Sprite[] { new Sprite(this.sprite, 139, 330, 31, 54),
				new Sprite(this.sprite, 177, 330, 31, 54) };
		for (int i = 0; i < fallingFramesLeft.length; i++)
			fallingFramesLeft[i].flip(true, false);

		this.animations.put(AnimationType.FALLING, new EnumMap<Direction, Animation<Sprite>>(Direction.class));
		this.animations.get(AnimationType.FALLING).put(Direction.RIGHT,
				new Animation<Sprite>(AnimationType.FALLING.getFrameInterval(), fallingFramesRight));
		this.animations.get(AnimationType.FALLING).put(Direction.NONE,
				new Animation<Sprite>(AnimationType.FALLING.getFrameInterval(), fallingFramesRight));
		this.animations.get(AnimationType.FALLING).put(Direction.LEFT,
				new Animation<Sprite>(AnimationType.FALLING.getFrameInterval(), fallingFramesLeft));
		//TODO
		Sprite[] jumpingFramesRight = new Sprite[] { new Sprite(this.sprite, 139, 330, 31, 54),
				new Sprite(this.sprite, 177, 330, 31, 54) };

		Sprite[] jumpingFramesLeft = new Sprite[] { new Sprite(this.sprite, 139, 330, 31, 54),
				new Sprite(this.sprite, 177, 330, 31, 54) };
		for (int i = 0; i < jumpingFramesLeft.length; i++)
			jumpingFramesLeft[i].flip(true, false);

		this.animations.put(AnimationType.JUMPING, new EnumMap<Direction, Animation<Sprite>>(Direction.class));
		this.animations.get(AnimationType.JUMPING).put(Direction.RIGHT,
				new Animation<Sprite>(AnimationType.JUMPING.getFrameInterval(), jumpingFramesRight));
		this.animations.get(AnimationType.JUMPING).put(Direction.NONE,
				new Animation<Sprite>(AnimationType.JUMPING.getFrameInterval(), jumpingFramesRight));
		this.animations.get(AnimationType.JUMPING).put(Direction.LEFT,
				new Animation<Sprite>(AnimationType.JUMPING.getFrameInterval(), jumpingFramesLeft));

	}
}
