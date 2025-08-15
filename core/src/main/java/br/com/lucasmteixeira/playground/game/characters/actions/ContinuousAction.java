package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import br.com.lucasmteixeira.playground.game.characters.actions.exceptions.ActionCantContinue;

public abstract class ContinuousAction extends Action {
	private Optional<ActionType> endActionType;
	protected Optional<Instant> end;
	protected final Optional<Duration> maxDuration;

	protected ContinuousAction(ActionType type, Instant begin) {
		super(type, begin);
		if (type.getCooldown().isPresent()) {
			this.end = Optional.of(begin.plus(type.getCooldown().get()));
		} else {
			this.end = Optional.empty();
		}
		this.endActionType = Optional.empty();
		this.maxDuration = Optional.empty();
	}
	
	protected ContinuousAction(ActionType type, Instant begin, Duration maxDuration) {
		super(type, begin);
		if (type.getCooldown().isPresent()) {
			this.end = Optional.of(begin.plus(type.getCooldown().get()));
		} else {
			this.end = Optional.empty();
		}
		this.endActionType = Optional.empty();
		this.maxDuration = Optional.of(maxDuration);
	}
	
	@Override
	public Optional<Instant> getDelay() {
		return Optional.empty();
	}
	@Override
	public Optional<Instant> getEnd() {
		return this.end;
	}

	public void continueAction(Instant end) throws ActionCantContinue {
		if (this.maxDuration.isPresent()) {
			if (Duration.between(begin, end).compareTo(this.maxDuration.get()) >= 0) {
				throw new ActionCantContinue();
			}
		}
		this.end = Optional.of(end);
		
	}

	public void finish(ActionType endActionType, Instant end) {
		if (this.endActionType.isEmpty()) {
			this.endActionType = Optional.of(endActionType);
			this.end = Optional.of(end);
		} else {
			// throw already finished action exception
		}
	}
}
