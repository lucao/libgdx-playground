package br.com.lucasmteixeira.playground.game.animation;

public enum AnimationType {
	
	IDLE(), WALKING(), RUNNING(), JUMPING(false), FALLING(), ATTACKING(false), DYING(false), DEAD();
	
	private final boolean loop;
	
	private AnimationType() {
		this.loop = true;
	}
	
	private AnimationType(boolean loop) {
		this.loop = loop;
	}

	public boolean isLoop() {
		return loop;
	}
}
