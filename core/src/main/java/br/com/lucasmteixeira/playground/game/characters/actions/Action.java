package br.com.lucasmteixeira.playground.game.characters.actions;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class Action {
	protected final ActionType type;
	protected final Instant begin;
	private final Duration delay;

	protected Action(ActionType type, Instant begin) {
		super();
		this.type = type;
		this.begin = begin;
		this.delay = Duration.ZERO;
	}

	protected Action(ActionType type, Instant begin, Duration delay) {
		this.type = type;
		this.begin = begin;
		this.delay = delay;
	}

	public abstract List<ActionType> getInterruptableActions();

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

	public Optional<Instant> getDelay() {
		return Optional.of(begin.plus(delay));
	}

	public boolean onCooldown(Instant now) {
		return this.type.getCooldown().isPresent() ? now.isBefore(this.begin.plus(this.type.getCooldown().get()))
				: false;
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
		if (obj instanceof Action) {
			Action other = (Action) obj;
			return type == other.type;
		} else {
			return false;
		}
	}
}
