package br.com.lucasmteixeira.playground.game.characters.player;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import br.com.lucasmteixeira.playground.game.MaterialObject;
import br.com.lucasmteixeira.playground.game.animation.AnimationType;
import br.com.lucasmteixeira.playground.game.characters.actions.Direction;

public class NarutoPlayer extends Player {

	public NarutoPlayer(Float x, Float y, Float w, Float h, World world) {
		super(x, y, w, h,
				MaterialObject.loadTextureWithTransparency("Naruto.jpg", new Color(0.592f, 0.682f, 0.843f, 1), 0.07f),
				world);

		TextureRegion[] idleFramesRight = new TextureRegion[] { new TextureRegion(this.texture, 30, 5, 36, 55),
				new TextureRegion(this.texture, 80, 5, 36, 55), new TextureRegion(this.texture, 130, 5, 36, 55),
				new TextureRegion(this.texture, 178, 5, 36, 55), new TextureRegion(this.texture, 225, 5, 36, 55),
				new TextureRegion(this.texture, 275, 5, 36, 55) };

		TextureRegion[] idleFramesLeft = new TextureRegion[] { new TextureRegion(this.texture, 30, 5, 36, 55),
				new TextureRegion(this.texture, 80, 5, 36, 55), new TextureRegion(this.texture, 130, 5, 36, 55),
				new TextureRegion(this.texture, 178, 5, 36, 55), new TextureRegion(this.texture, 225, 5, 36, 55),
				new TextureRegion(this.texture, 275, 5, 36, 55) };
		for (int i = 0; i < idleFramesLeft.length; i++)
			idleFramesLeft[i].flip(true, false);

		this.animations.put(AnimationType.IDLE, new EnumMap<Direction, Animation<TextureRegion>>(Direction.class));
		this.animations.get(AnimationType.IDLE).put(Direction.RIGHT,
				new Animation<TextureRegion>(AnimationType.IDLE.getFrameInterval(), idleFramesRight));
		this.animations.get(AnimationType.IDLE).put(Direction.NONE,
				new Animation<TextureRegion>(AnimationType.IDLE.getFrameInterval(), idleFramesRight));
		this.animations.get(AnimationType.IDLE).put(Direction.LEFT,
				new Animation<TextureRegion>(AnimationType.IDLE.getFrameInterval(), idleFramesLeft));

		TextureRegion[] walkingFramesRight = new TextureRegion[] { new TextureRegion(this.texture, 28, 70, 33, 52),
				new TextureRegion(this.texture, 72, 70, 25, 52), new TextureRegion(this.texture, 117, 70, 27, 52),
				new TextureRegion(this.texture, 155, 70, 26, 52), new TextureRegion(this.texture, 190, 70, 25, 52),
				new TextureRegion(this.texture, 221, 70, 24, 52) };

		TextureRegion[] walkingFramesLeft = new TextureRegion[] { new TextureRegion(this.texture, 28, 70, 33, 52),
				new TextureRegion(this.texture, 72, 70, 25, 52), new TextureRegion(this.texture, 117, 70, 27, 52),
				new TextureRegion(this.texture, 155, 70, 26, 52), new TextureRegion(this.texture, 190, 70, 25, 52),
				new TextureRegion(this.texture, 221, 70, 24, 52) };
		for (int i = 0; i < walkingFramesLeft.length; i++)
			walkingFramesLeft[i].flip(true, false);

		this.animations.put(AnimationType.WALKING, new EnumMap<Direction, Animation<TextureRegion>>(Direction.class));
		this.animations.get(AnimationType.WALKING).put(Direction.RIGHT,
				new Animation<TextureRegion>(AnimationType.WALKING.getFrameInterval(), walkingFramesRight));
		this.animations.get(AnimationType.WALKING).put(Direction.NONE,
				new Animation<TextureRegion>(AnimationType.WALKING.getFrameInterval(), walkingFramesRight));
		this.animations.get(AnimationType.WALKING).put(Direction.LEFT,
				new Animation<TextureRegion>(AnimationType.WALKING.getFrameInterval(), walkingFramesLeft));

		TextureRegion[] fallingFramesRight = new TextureRegion[] { new TextureRegion(this.texture, 139, 330, 31, 54),
				new TextureRegion(this.texture, 177, 330, 31, 54) };

		TextureRegion[] fallingFramesLeft = new TextureRegion[] { new TextureRegion(this.texture, 139, 330, 31, 54),
				new TextureRegion(this.texture, 177, 330, 31, 54) };
		for (int i = 0; i < fallingFramesLeft.length; i++)
			fallingFramesLeft[i].flip(true, false);

		this.animations.put(AnimationType.FALLING, new EnumMap<Direction, Animation<TextureRegion>>(Direction.class));
		this.animations.get(AnimationType.FALLING).put(Direction.RIGHT,
				new Animation<TextureRegion>(AnimationType.FALLING.getFrameInterval(), fallingFramesRight));
		this.animations.get(AnimationType.FALLING).put(Direction.NONE,
				new Animation<TextureRegion>(AnimationType.FALLING.getFrameInterval(), fallingFramesRight));
		this.animations.get(AnimationType.FALLING).put(Direction.LEFT,
				new Animation<TextureRegion>(AnimationType.FALLING.getFrameInterval(), fallingFramesLeft));
		//TODO
		TextureRegion[] jumpingFramesRight = new TextureRegion[] { new TextureRegion(this.texture, 139, 330, 31, 54),
				new TextureRegion(this.texture, 177, 330, 31, 54) };

		TextureRegion[] jumpingFramesLeft = new TextureRegion[] { new TextureRegion(this.texture, 139, 330, 31, 54),
				new TextureRegion(this.texture, 177, 330, 31, 54) };
		for (int i = 0; i < jumpingFramesLeft.length; i++)
			jumpingFramesLeft[i].flip(true, false);

		this.animations.put(AnimationType.JUMPING, new EnumMap<Direction, Animation<TextureRegion>>(Direction.class));
		this.animations.get(AnimationType.JUMPING).put(Direction.RIGHT,
				new Animation<TextureRegion>(AnimationType.JUMPING.getFrameInterval(), jumpingFramesRight));
		this.animations.get(AnimationType.JUMPING).put(Direction.NONE,
				new Animation<TextureRegion>(AnimationType.JUMPING.getFrameInterval(), jumpingFramesRight));
		this.animations.get(AnimationType.JUMPING).put(Direction.LEFT,
				new Animation<TextureRegion>(AnimationType.JUMPING.getFrameInterval(), jumpingFramesLeft));

	}
}
