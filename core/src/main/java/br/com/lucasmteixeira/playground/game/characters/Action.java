package br.com.lucasmteixeira.playground.game.characters;

import java.time.Instant;
import java.util.Objects;

public class Action {
	private final ActionType type;
	private final Instant begin;
	private final Instant end;
	
	public Action(ActionType type, Instant begin) {
		super();
		this.type = type;
		this.begin = begin;
		this.end = begin.plus(type.getCooldown());
	}

	public ActionType getType() {
		return type;
	}

	public Instant getBegin() {
		return begin;
	}

	public Instant getEnd() {
		return end;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Action other = (Action) obj;
		return type == other.type;
	}
}
