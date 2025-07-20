package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public abstract class InstantAction extends Action {
	private final Instant end;
	private boolean executed;
	
	protected InstantAction(ActionType type, Instant begin) {
		super(type, begin);
		this.end = begin.plus(this.type.getCooldown().orElse(Duration.ofSeconds(1)));
		this.executed = false;
	}

	protected InstantAction(ActionType type, Instant begin, Duration delay) {
		super(type, begin, delay);
		this.end = begin.plus(this.type.getCooldown().orElse(Duration.ofSeconds(1)).plus(delay));
		this.executed = false;
	}

	@Override
	public Optional<Instant> getEnd() {
		return Optional.of(this.end);
	}
	
	public boolean isExecuted() {
		return executed;
	}

	public void execute() {
		this.executed = true;
	}
}
