package br.com.lucasmteixeira.playground.game;

import java.time.Instant;

import br.com.lucasmteixeira.playground.Main;

public abstract class GameObject {
	protected Float x;
	protected Float y;

	protected GameObject(Float x, Float y) {
		this.x = x;
		this.y = y;
	}

	public abstract void play(Instant now, Long deltaTime);

	public long[] getQuadrante() {
		return new long[] { Math.round(x / Main.CONSTANTE_DO_QUADRANTE), Math.round(y / Main.CONSTANTE_DO_QUADRANTE) };
	}

	public Float getX() {
		return x;
	}

	public Float getY() {
		return y;
	}
}
