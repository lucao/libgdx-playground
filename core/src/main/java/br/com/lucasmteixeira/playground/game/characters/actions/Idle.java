package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Instant;
import java.util.List;

public class Idle extends ContinuousAction {

	public Idle(Instant begin) {
		super(ActionType.IDLE, begin);
	}

	@Override
	public List<ActionType> getInterruptableActions() {
		return List.of(ActionType.WALKING_LEFT, ActionType.WALKING_RIGHT);
	}

}
