package br.com.lucasmteixeira.playground.game.characters.player;

import java.time.Instant;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

import br.com.lucasmteixeira.playground.game.characters.Person;

public class Player extends Person {

	public Player(Float x, Float y, Float w, Float h, Texture texture, World world) {
		super(x, y, w, h, texture, world);
		// TODO Auto-generated constructor stub
		// For click/touch events
	}

	@Override
	public void play(Instant now, Long deltaTime) {
		super.play(now, deltaTime);
		
	}
}
