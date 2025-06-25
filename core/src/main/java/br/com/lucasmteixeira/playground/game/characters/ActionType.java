package br.com.lucasmteixeira.playground.game.characters;

import java.time.Duration;
import java.util.Optional;

public enum ActionType {
	JUMP(), WALK_RIGHT(Optional.empty()), WALK_LEFT(Optional.empty());

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
