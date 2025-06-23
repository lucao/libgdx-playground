package br.com.lucasmteixeira.playground.game.exceptions;

import java.util.List;

import br.com.lucasmteixeira.playground.game.GameObject;

public class UntreatedCollision extends GameException {

	private static final long serialVersionUID = 4985711364225381602L;
	
	private final List<GameObject> gameObjects;

	public UntreatedCollision(String message, List<GameObject> gameObjects) {
		super(message);
		this.gameObjects = gameObjects;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
}
