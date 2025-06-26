package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public abstract class InstantAction extends Action {
	private final Instant end;
	
	protected InstantAction(ActionType type, Instant begin) {
		super(type, begin);
		this.end = begin.plus(this.type.getCooldown().orElse(Duration.ofSeconds(1)));
	}

	@Override
	public Optional<Instant> getEnd() {
		return Optional.of(this.end);
	}
}
