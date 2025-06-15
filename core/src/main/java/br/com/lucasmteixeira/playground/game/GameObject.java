package br.com.lucasmteixeira.playground.game;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GameObject extends Actor {
	protected Float x;
	protected Float y;

	protected GameObject(Float x, Float y) {
		this.x = x;
		this.y = y;
	}
	
}
