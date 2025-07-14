package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Instant;
import java.util.Optional;

public abstract class ContinuousAction extends Action {
	private Optional<ActionType> endActionType;
	private Instant end;

	protected ContinuousAction(ActionType type, Instant begin) {
		super(type, begin);
		this.end = null;
		this.endActionType = Optional.empty();
	}

	@Override
	public Optional<Instant> getEnd() {
		if (this.endActionType.isEmpty())
			return Optional.empty();

		return Optional.of(this.end);
	}

	public void finish(ActionType endActionType, Instant end) {
		if (this.endActionType.isEmpty()) {
			this.endActionType = Optional.of(endActionType);
			this.end = end;
		} else {
			//throw already finished action exception
		}
	}
}
