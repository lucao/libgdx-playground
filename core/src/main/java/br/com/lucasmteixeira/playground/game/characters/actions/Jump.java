package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Instant;
import java.util.List;

public class Jump extends ContinuousAction {

	public Jump(Instant begin) {
		super(ActionType.JUMP, begin);
		
	}

	@Override
	public List<ActionType> getInterruptableActions() {
		return List.of(ActionType.IDLE);
	}

}
