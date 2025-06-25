package br.com.lucasmteixeira.playground.game.scenery;

import java.time.Instant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import br.com.lucasmteixeira.playground.game.CollisionType;
import br.com.lucasmteixeira.playground.game.MaterialObject;
import br.com.lucasmteixeira.playground.game.Physical;
import br.com.lucasmteixeira.playground.game.exceptions.UntreatedCollision;

public class Ground extends MaterialObject implements Physical {
	protected Body body;

	protected Fixture fixture;

	public Ground(Float x, Float y, Float w, Float h, Texture texture, World world) {
		super(x, y, w, h, texture);

		// Create our body definition
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vector2((x + w) - w / 2, (y + h) - h / 2));
		groundBodyDef.type = BodyType.StaticBody;
		// Create a body from the definition and add it to the world
		this.body = world.createBody(groundBodyDef);

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(w / 2, h / 2);
		// Create a fixture from our polygon shape and add it to our ground body
		this.fixture = this.body.createFixture(groundBox, 0.0f);
		// Clean up after ourselves
		groundBox.dispose();

		this.body.setUserData(this);
	}
	
	@Override
	public CollisionType getCollisionType() {
		return CollisionType.GROUND;
	}

	@Override
	public void colisao(Physical physicalObject) throws UntreatedCollision {
		switch (physicalObject.getCollisionType()) {
		case GROUND:
			;
			break;
		case PERSON:
			Gdx.app.debug("DEBUG", "collision detected ground is dispactching action for player");
			physicalObject.colisao(this);
			break;
		default:
			break;
		}
	}

	@Override
	public void play(Instant now, Long deltaTime) {
//		// TODO Auto-generated method stub
//		Vector2 position = this.body.getPosition();
//		this.x = position.x;
//		this.y = position.y;
	}

	@Override
	public Body getBody() {
		return this.body;
	}

	@Override
	public Fixture getFixture() {
		return this.fixture;
	}

	@Override
	public Float getX() {
		return this.x = this.body.getPosition().x - (w / 2);
	}

	@Override
	public Float getY() {
		return this.y = this.body.getPosition().y - (h / 2);
	}
}
