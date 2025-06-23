package br.com.lucasmteixeira.playground.game.characters;

import java.time.Duration;

public enum ActionType {
	JUMP();
	
	ActionType() {
		this.cooldown = Duration.ofSeconds(1);
	}
	
	ActionType(Duration duration) {
		this.cooldown = duration;
	}

	public Duration getCooldown() {
		return cooldown;
	}

	private final Duration cooldown;
}
