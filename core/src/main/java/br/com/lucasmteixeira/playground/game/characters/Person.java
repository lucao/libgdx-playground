package br.com.lucasmteixeira.playground.game.characters;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import br.com.lucasmteixeira.playground.game.GameObject;
import br.com.lucasmteixeira.playground.game.MaterialObject;
import br.com.lucasmteixeira.playground.game.Physical;
import br.com.lucasmteixeira.playground.game.exceptions.UntreatedCollision;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Person extends MaterialObject implements Physical {
	protected final Body body;

	protected final Fixture fixture;

	protected Integer strength;
	protected Integer dexterity;
	protected Integer vitality;
	protected Integer inteligence;
	protected Integer wisdom;
	protected Integer charisma;

	protected Integer lifePoints;
	protected Integer energyPoints;

	protected boolean grounded;

	private static final Integer NORMAL_JUMP_FORCE = 10;

	private final List<ActionType> actionsPool;
	private final CircularFifoQueue<Action> actionsHistory;

	private final Set<Action> runningActions;
	private final Set<Action> actionsToRun;

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
		circle.setRadius(w / 2);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.0f;

		this.fixture = this.body.createFixture(fixtureDef);
		circle.dispose();

		this.body.setUserData(this);

		this.grounded = false;

		this.actionsPool = new ArrayList<ActionType>();
		this.actionsHistory = new CircularFifoQueue<Action>(50);

		this.runningActions = new HashSet<Action>();
		this.actionsToRun = new HashSet<Action>();
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
	public void colisao(Physical physicalObject) throws UntreatedCollision {
		switch (physicalObject.getCollisionType()) {
		case GROUND:
			this.grounded = true;
			break;
		case PERSON:
			// TODO treat the object within range
			;
			break;
		default:
			break;
		}
	}

	@Override
	public void play(Instant now, Long deltaTime) {
		// TODO clear actions that are already executed
		final Iterator<Action> runningActionsIterator = this.runningActions.iterator();
		while (runningActionsIterator.hasNext()) {
			Action action = runningActionsIterator.next();
			if (now.isBefore(action.getEnd())) {
				runningActionsIterator.remove();
			}
		}

		final Iterator<ActionType> actionsPoolIterator = this.actionsPool.iterator();

		while (actionsPoolIterator.hasNext()) {
			final Action actionToRun = new Action(actionsPoolIterator.next(), now);
			if (!this.runningActions.add(actionToRun))
				continue;
			this.actionsToRun.add(actionToRun);
		}

		for (Action actionToRun : this.actionsToRun) {
			switch (actionToRun.getType()) {
			case JUMP:
				if (grounded) {
					this.body.applyLinearImpulse(new Vector2(0, NORMAL_JUMP_FORCE), this.body.getLocalCenter(), true);
					grounded = false;
				}
				continue;
			}
		}

		// TODO execute actions
		this.actionsToRun.clear();
	}

	public void jump() {
		this.actionsPool.add(ActionType.JUMP);
	}

	// TODO lembra que o X e Y do box2D são no centro
	public void setX(Float x) {
		this.body.getPosition().x = (x + w) - w / 2;
	}

	public void setY(Float y) {
		this.body.getPosition().y = (y + h) - h / 2;
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
