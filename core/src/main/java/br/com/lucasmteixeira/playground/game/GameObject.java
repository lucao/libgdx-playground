package br.com.lucasmteixeira.playground.game;

import br.com.lucasmteixeira.playground.Main;

public abstract class GameObject {
	public Float x;
	public Float y;

	protected GameObject(Float x, Float y) {
		this.x = x;
		this.y = y;
	}

	public abstract void play(Long deltaTime);

	public long[] getQuadrante() {
		return new long[] { Math.round(x / Main.CONSTANTE_DO_QUADRANTE), Math.round(y / Main.CONSTANTE_DO_QUADRANTE) };
	}

}
