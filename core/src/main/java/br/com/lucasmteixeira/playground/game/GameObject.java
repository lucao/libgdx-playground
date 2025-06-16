package br.com.lucasmteixeira.playground.game;

import java.util.Objects;

public abstract class GameObject {
	public Float x;
	public Float y;

	protected GameObject(Float x, Float y) {
		this.x = x;
		this.y = y;
	}

	public abstract void play(Long deltaTime);
	
	@Override
	public int hashCode() {
		return Objects.hash(Math.ceil(x / 10000), Math.ceil(y / 10000));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		GameObject other = (GameObject) obj;
		return Objects.equals(Math.ceil(x / 10000), Math.ceil(other.x / 10000))
				&& Objects.equals(Math.ceil(y / 10000), Math.ceil(other.y / 10000));
	}

}
