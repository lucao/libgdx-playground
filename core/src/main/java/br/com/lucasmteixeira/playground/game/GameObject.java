package br.com.lucasmteixeira.playground.game;

public abstract class GameObject {
	public Float x;
	public Float y;

	public static final int CONSTANTE_DO_QUADRANTE = 10000;

	protected GameObject(Float x, Float y) {
		this.x = x;
		this.y = y;
	}

	public abstract void play(Long deltaTime);

	public long[] getQuadrante() {
		return new long[] { Math.round(x / CONSTANTE_DO_QUADRANTE), Math.round(y / CONSTANTE_DO_QUADRANTE) };
	}

}
