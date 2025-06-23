package br.com.lucasmteixeira.playground.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import br.com.lucasmteixeira.playground.game.exceptions.UntreatedCollision;

public interface Physical {	
	public Body getBody();
	public Fixture getFixture();
	public void colisao(Physical physicalObject) throws UntreatedCollision;
	public default CollisionType getCollisionType() {
		return CollisionType.NOTHING;
	}
}
