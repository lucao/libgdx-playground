package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Instant;
import java.util.List;

public class Run extends Walk {

	public Run(Direction direction, Instant begin) {
		super(direction, Direction.LEFT.equals(direction) ? ActionType.RUNNING_LEFT : ActionType.RUNNING_RIGHT, begin);
	}

	@Override
	public List<ActionType> getInterruptableActions() {
		if (Direction.LEFT.equals(direction)) {
			return List.of(ActionType.WALKING_RIGHT, ActionType.WALKING_LEFT, ActionType.RUNNING_RIGHT);
		} else if (Direction.RIGHT.equals(direction)) {
			return List.of(ActionType.WALKING_LEFT, ActionType.WALKING_RIGHT, ActionType.RUNNING_LEFT);
		} else {
			return List.of(ActionType.WALKING_LEFT, ActionType.WALKING_RIGHT, ActionType.RUNNING_RIGHT,
					ActionType.RUNNING_LEFT);
		}
	}

	@Override
	public boolean isRunning() {
		return true;
	}
}
