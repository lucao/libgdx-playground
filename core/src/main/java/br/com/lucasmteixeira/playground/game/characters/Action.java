package br.com.lucasmteixeira.playground.game.characters;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public abstract class Action {
	protected final ActionType type;
	protected final Instant begin;

	protected Action(ActionType type, Instant begin) {
		super();
		this.type = type;
		this.begin = begin;
	}
	
	public static Action create(ActionType type, Instant now, Long deltaTime) {
		switch (type) {
		case JUMP:
			break;
		case WALK_LEFT:
			break;
		case WALK_RIGHT:
			break;
		default:
			break;
		
		}
	}

	public ActionType getType() {
		return type;
	}
	
	public Direction getDirection() {
		return Direction.NONE;
	}

	public Instant getBegin() {
		return begin;
	}

	public Optional<Instant> getEnd() {
		return Optional.empty();
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
