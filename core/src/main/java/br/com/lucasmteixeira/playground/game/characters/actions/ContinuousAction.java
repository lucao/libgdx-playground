package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Instant;

public class ContinuousAction extends Action {

	protected ContinuousAction(ActionType type, Instant begin) {
		super(type, begin);
	}

}
