package br.com.lucasmteixeira.playground.game.characters.player;

import java.time.Instant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

import br.com.lucasmteixeira.playground.game.characters.Person;
import br.com.lucasmteixeira.playground.game.characters.actions.Direction;

public class Player extends Person {

	public Player(Float x, Float y, Float w, Float h, Texture texture, World world) {
		super(x, y, w, h, texture, world);
		// TODO Auto-generated constructor stub
		// For click/touch events
	}

	@Override
	public void play(Instant now, Long deltaTime) {
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			this.walk(Direction.RIGHT);
		} else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			this.walk(Direction.LEFT);
		}
		super.play(now, deltaTime);

	}
}
