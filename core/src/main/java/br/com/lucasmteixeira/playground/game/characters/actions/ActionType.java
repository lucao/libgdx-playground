package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Duration;
import java.util.Optional;

public enum ActionType {
	IDLE(Optional.empty()), JUMP(), WALKING_RIGHT(Optional.empty()), WALKING_LEFT(Optional.empty()), STOP_WALKING_RIGHT(Optional.empty()),
	STOP_WALKING_LEFT(Optional.empty());

	ActionType() {
		this.cooldown = Optional.of(Duration.ofSeconds(1));
	}

	ActionType(Optional<Duration> duration) {
		this.cooldown = duration;
	}

	public Optional<Duration> getCooldown() {
		return cooldown;
	}

	private final Optional<Duration> cooldown;
}
