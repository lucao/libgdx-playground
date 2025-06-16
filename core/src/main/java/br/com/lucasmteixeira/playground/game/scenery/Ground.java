package br.com.lucasmteixeira.playground.game.scenery;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import br.com.lucasmteixeira.playground.game.MaterialObject;
import br.com.lucasmteixeira.playground.game.Physical;

public class Ground extends MaterialObject implements Physical {
	protected Body body;

	protected Fixture fixture;

	public Ground(Float x, Float y, Float w, Float h, Texture texture, World world) {
		super(x, y, w, h, texture);

		// Create our body definition
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vector2(this.x, this.y));
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
	}

	@Override
	public void play(Long deltaTime) {
		// TODO Auto-generated method stub
		Vector2 position = this.body.getPosition();
		this.x = position.x;
		this.y = position.y;
	}

	@Override
	public Body getBody() {
		return this.body;
	}

	@Override
	public Fixture getFixture() {
		return this.fixture;
	}
}
