package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class Jump extends InstantAction {
	

	protected Jump(Instant begin) {
		super(ActionType.JUMP, begin);
		
	}

	
}
