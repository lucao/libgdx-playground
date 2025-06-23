package br.com.lucasmteixeira.playground.game;

import java.util.List;

import br.com.lucasmteixeira.playground.Main;
import br.com.lucasmteixeira.playground.game.exceptions.UntreatedCollision;

public abstract class GameObject {
	protected Float x;
	protected Float y;

	protected GameObject(Float x, Float y) {
		this.x = x;
		this.y = y;
	}

	public abstract void play(Long deltaTime);

	public long[] getQuadrante() {
		return new long[] { Math.round(x / Main.CONSTANTE_DO_QUADRANTE), Math.round(y / Main.CONSTANTE_DO_QUADRANTE) };
	}
	
	public GameObjectType getGameObjectType() {
		return GameObjectType.NOTHING;
	}
	
	public void colisao(GameObject gameObject) throws UntreatedCollision {
		if (GameObjectType.NOTHING.equals(gameObject.getGameObjectType())) {
			return;
		}
		throw new UntreatedCollision("Colisão não tratada.", List.of(this, gameObject));
	}

	public Float getX() {
		return x;
	}

	public Float getY() {
		return y;
	}
}
