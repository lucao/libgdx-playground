package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Instant;
import java.util.Optional;

public class Walk extends ContinuousAction {
	private final Optional<Instant> end;
	private final Direction direction;

	protected Walk(Direction direction, Instant begin) {
		super(ActionType.WALK, begin);
		this.end = Optional.empty();
		this.direction = direction;
	}
	protected Walk(Direction direction, Instant begin, Instant end) {
		super(ActionType.WALK, begin);
		this.end = Optional.of(end);
		this.direction = direction;
	}
	
	@Override
	public Optional<Instant> getEnd() {
		return this.end;
	}
	
	@Override
	public Direction getDirection() {
		return this.direction;
	}
}
