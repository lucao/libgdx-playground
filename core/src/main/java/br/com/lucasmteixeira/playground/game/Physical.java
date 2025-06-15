package br.com.lucasmteixeira.playground.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public interface Physical {
	public void doPhysics(World world);
	
	public Body getBody();
	public Fixture getFixture();
}
