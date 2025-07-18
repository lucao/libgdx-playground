package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Instant;
import java.util.Optional;

public abstract class ContinuousAction extends Action {
	private Optional<ActionType> endActionType;
	protected Optional<Instant> end;

	protected ContinuousAction(ActionType type, Instant begin) {
		super(type, begin);
		if (type.getCooldown().isPresent()) {
			this.end = Optional.of(begin.plus(type.getCooldown().get()));
		} else {
			this.end = Optional.empty();
		}
		this.endActionType = Optional.empty();
	}

	@Override
	public Optional<Instant> getEnd() {
		return this.end;
	}

	public void continueAction(Instant end) {
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
