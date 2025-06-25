package br.com.lucasmteixeira.playground.game.characters;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class Jump extends Action {
	private final Instant end;

	protected Jump(Instant begin) {
		super(ActionType.JUMP, begin);
		this.end = begin.plus(this.type.getCooldown().orElse(Duration.ofSeconds(1)));
	}

	@Override
	public Optional<Instant> getEnd() {
		return Optional.of(this.end);
	}
}
