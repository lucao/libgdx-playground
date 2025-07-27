package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Instant;
import java.util.List;

public class Walk extends ContinuousAction {

	protected final Direction direction;

	public Walk(Direction direction, Instant begin) {
		super(Direction.LEFT.equals(direction) ? ActionType.WALKING_LEFT : ActionType.WALKING_RIGHT, begin);
		this.direction = direction;
	}
	
	protected Walk(Direction direction, ActionType actionType, Instant begin) {
		super(actionType, begin);
		this.direction = direction;
	}

	@Override
	public Direction getDirection() {
		return this.direction;
	}

	@Override
	public List<ActionType> getInterruptableActions() {
		if (Direction.LEFT.equals(direction)) {
			return List.of(ActionType.WALKING_RIGHT, ActionType.RUNNING_RIGHT, ActionType.RUNNING_LEFT);
		} else if (Direction.RIGHT.equals(direction)) {
			return List.of(ActionType.WALKING_LEFT, ActionType.RUNNING_RIGHT, ActionType.RUNNING_LEFT);
		} else {
			return List.of(ActionType.WALKING_LEFT, ActionType.WALKING_RIGHT, ActionType.RUNNING_RIGHT,
					ActionType.RUNNING_LEFT);
		}
	}

	public boolean isRunning() {
		return false;
	}
}
