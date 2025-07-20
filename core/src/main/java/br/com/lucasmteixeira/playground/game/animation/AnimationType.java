package br.com.lucasmteixeira.playground.game.animation;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import br.com.lucasmteixeira.playground.game.characters.actions.Direction;

public enum AnimationType {

	IDLE(), WALKING(), RUNNING(), JUMPING(PlayMode.NORMAL), FALLING(), ATTACKING(PlayMode.NORMAL),
	DYING(PlayMode.NORMAL), DEAD();

	private final Direction direction;
	private final PlayMode loop;

	private AnimationType() {
		this.loop = PlayMode.LOOP;
		this.direction = Direction.RIGHT;
	}

	private AnimationType(Direction direction) {
		this.loop = PlayMode.LOOP;
		this.direction = direction;
	}

	private AnimationType(PlayMode loop) {
		this.loop = loop;
		this.direction = Direction.RIGHT;
	}

	private AnimationType(PlayMode loop, Direction direction) {
		this.loop = loop;
		this.direction = direction;
	}

	public PlayMode getLoopMode() {
		return loop;
	}

	public boolean isLoop() {
		return loop == PlayMode.LOOP;
	}

	public Direction getDirection() {
		return direction;
	}
}
