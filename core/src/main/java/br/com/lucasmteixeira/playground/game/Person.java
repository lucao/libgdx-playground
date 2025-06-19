package br.com.lucasmteixeira.playground.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Person extends MaterialObject implements Physical {
	protected final Body body;

	protected final Fixture fixture;

	protected Person(Float x, Float y, Float w, Float h, Texture texture, World world) {
		super(x, y, w, h, texture);

		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't move we
		// would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set((x + w) - w / 2, (y + h) - h / 2);

		// Create our body in the world using our body definition
		this.body = world.createBody(bodyDef);

		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(w/2);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.0f;

		this.fixture = this.body.createFixture(fixtureDef);
		circle.dispose();
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
	public void play(Long deltaTime) {
		// TODO Auto-generated method stub

	}

	//TODO lembra que o X e Y do box2D são no centro
	public void setX(Float x) {
		this.body.getPosition().x = x;
	}

	public void setY(Float y) {
		this.body.getPosition().y = y;
	}

	@Override
	public Float getX() {
		return this.body.getPosition().x;
	}

	@Override
	public Float getY() {
		return this.body.getPosition().y;
	}

}
