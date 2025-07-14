package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Instant;
import java.util.List;

public class Stop extends Action {
	private final Direction direction;
	public Stop(Direction direction, Instant begin) {
		super(Direction.LEFT.equals(direction) ? ActionType.STOP_WALKING_LEFT : ActionType.STOP_WALKING_RIGHT, begin);
		this.direction = direction;
	}

	@Override
	public List<ActionType> getInterruptableActions() {
		return List.of(ActionType.WALKING_LEFT, ActionType.WALKING_RIGHT);
	}
	
	@Override
	public Direction getDirection() {
		return this.direction;
	}

}
