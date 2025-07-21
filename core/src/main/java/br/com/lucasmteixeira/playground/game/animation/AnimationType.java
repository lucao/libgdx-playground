package br.com.lucasmteixeira.playground.game.animation;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import br.com.lucasmteixeira.playground.game.characters.actions.Direction;

public enum AnimationType {

	IDLE(), WALKING(), RUNNING(), JUMPING(PlayMode.NORMAL), FALLING(), ATTACKING(PlayMode.NORMAL),
	DYING(PlayMode.NORMAL), DEAD();

	private final Direction direction;
	private final PlayMode loop;
	private final float frameInterval;
	private final float delay;

	private static final float DEFAULT_FRAME_INTERVAL = 0.1f;

	private AnimationType() {
		this.loop = PlayMode.LOOP;
		this.direction = Direction.RIGHT;
		this.frameInterval = DEFAULT_FRAME_INTERVAL;
		this.delay = 0;
	}

	private AnimationType(Direction direction) {
		this.loop = PlayMode.LOOP;
		this.direction = direction;
		this.frameInterval = DEFAULT_FRAME_INTERVAL;
		this.delay = 0;
	}

	private AnimationType(PlayMode loop) {
		this.loop = loop;
		this.direction = Direction.RIGHT;
		this.frameInterval = DEFAULT_FRAME_INTERVAL;
		this.delay = 0;
	}

	private AnimationType(PlayMode loop, Direction direction) {
		this.loop = loop;
		this.direction = direction;
		this.frameInterval = DEFAULT_FRAME_INTERVAL;
		this.delay = 0;
	}

	private AnimationType(float frameInterval, float delay) {
		this.loop = PlayMode.LOOP;
		this.direction = Direction.RIGHT;
		this.frameInterval = frameInterval;
		this.delay = delay;
	}

	private AnimationType(PlayMode loop, float frameInterval, float delay) {
		this.loop = loop;
		this.direction = Direction.RIGHT;
		this.frameInterval = frameInterval;
		this.delay = delay;
	}

	private AnimationType(Direction direction, float frameInterval, float delay) {
		this.loop = PlayMode.LOOP;
		this.direction = direction;
		this.frameInterval = frameInterval;
		this.delay = delay;
	}

	private AnimationType(PlayMode loop, Direction direction, float frameInterval, float delay) {
		this.loop = loop;
		this.direction = direction;
		this.frameInterval = frameInterval;
		this.delay = delay;
	}

	private AnimationType(PlayMode loop, float frameInterval) {
		this.loop = loop;
		this.direction = Direction.RIGHT;
		this.frameInterval = frameInterval;
		this.delay = 0;
	}

	private AnimationType(Direction direction, float frameInterval) {
		this.loop = PlayMode.LOOP;
		this.direction = direction;
		this.frameInterval = frameInterval;
		this.delay = 0;
	}

	private AnimationType(PlayMode loop, Direction direction, float frameInterval) {
		this.loop = loop;
		this.direction = direction;
		this.frameInterval = frameInterval;
		this.delay = 0;
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

	public float getFrameInterval() {
		return frameInterval;
	}

	public float getDelay() {
		return delay;
	}
}
