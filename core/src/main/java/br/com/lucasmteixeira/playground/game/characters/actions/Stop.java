package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Instant;
import java.util.List;

public class Stop extends ContinuousAction {
	private final Direction direction;

	public Stop(Direction direction, Instant begin) {
		super(Direction.LEFT.equals(direction) ? ActionType.STOP_WALKING_LEFT : ActionType.STOP_WALKING_RIGHT, begin);
		this.direction = direction;
	}

	@Override
	public List<ActionType> getInterruptableActions() {
		if (Direction.LEFT.equals(direction)) {
			return List.of(ActionType.WALKING_LEFT, ActionType.WALKING_RIGHT, ActionType.RUNNING_LEFT,
					ActionType.RUNNING_RIGHT, ActionType.STOP_WALKING_RIGHT);
		} else if (Direction.RIGHT.equals(direction)) {
			return List.of(ActionType.WALKING_LEFT, ActionType.WALKING_RIGHT, ActionType.RUNNING_LEFT,
					ActionType.RUNNING_RIGHT, ActionType.STOP_WALKING_LEFT);
		} else {
			return List.of(ActionType.WALKING_LEFT, ActionType.WALKING_RIGHT, ActionType.RUNNING_LEFT,
					ActionType.RUNNING_RIGHT);
		}
	}

	@Override
	public Direction getDirection() {
		return this.direction;
	}

}
