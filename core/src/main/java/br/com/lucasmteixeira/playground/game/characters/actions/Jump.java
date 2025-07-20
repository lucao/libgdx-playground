package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Jump extends InstantAction {

	public Jump(Instant begin) {
		super(ActionType.JUMP, begin, Duration.ofMillis(100));

	}

	@Override
	public List<ActionType> getInterruptableActions() {
		return List.of(ActionType.IDLE);
	}

}
