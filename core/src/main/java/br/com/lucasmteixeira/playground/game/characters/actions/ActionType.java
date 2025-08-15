package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Duration;
import java.util.Optional;

public enum ActionType {
	IDLE(Optional.empty()), START_JUMP(), JUMP(Optional.of(Duration.ofMillis(400))), WALKING_RIGHT(Optional.empty()),
	WALKING_LEFT(Optional.empty()), RUNNING_RIGHT(Optional.empty()), RUNNING_LEFT(Optional.empty()),
	STOP_WALKING_RIGHT(Optional.of(Duration.ofMillis(200))), STOP_WALKING_LEFT(Optional.of(Duration.ofMillis(200)));

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
