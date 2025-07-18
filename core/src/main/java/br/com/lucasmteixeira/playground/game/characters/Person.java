package br.com.lucasmteixeira.playground.game.characters;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import br.com.lucasmteixeira.playground.game.CollisionType;
import br.com.lucasmteixeira.playground.game.MaterialObject;
import br.com.lucasmteixeira.playground.game.Physical;
import br.com.lucasmteixeira.playground.game.characters.actions.Action;
import br.com.lucasmteixeira.playground.game.characters.actions.ActionType;
import br.com.lucasmteixeira.playground.game.characters.actions.ContinuousAction;
import br.com.lucasmteixeira.playground.game.characters.actions.Direction;
import br.com.lucasmteixeira.playground.game.characters.actions.Idle;
import br.com.lucasmteixeira.playground.game.characters.actions.Jump;
import br.com.lucasmteixeira.playground.game.characters.actions.Stop;
import br.com.lucasmteixeira.playground.game.characters.actions.Walk;
import br.com.lucasmteixeira.playground.game.exceptions.UntreatedCollision;

public abstract class Person extends MaterialObject implements Physical {
	private static final GdxLogger LOGGER = new GdxLogger();

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

	private static final Integer NORMAL_JUMP_FORCE = 7000;
	private static final Float NORMAL_WALK_SPEED = 50f;// TODO walk

	private final List<ActionType> actionsPool;
	private final CircularFifoQueue<Action> actionsHistory;

	private final Set<Action> runningActions;
	private final Map<ActionType, ContinuousAction> continuousUnfinishedActions;
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
		PolygonShape personBox = new PolygonShape();
		personBox.setAsBox(w / 2, h / 2);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = personBox;
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 1.0f;
		fixtureDef.restitution = 0.0f;

		this.fixture = this.body.createFixture(fixtureDef);
		personBox.dispose();

		this.body.setUserData(this);

		MassData mass = new MassData();
		mass.mass = 80.0f;
		this.body.setMassData(mass);

		this.grounded = false;

		this.actionsPool = new ArrayList<ActionType>();
		this.actionsHistory = new CircularFifoQueue<Action>(50);

		this.runningActions = new HashSet<Action>();
		this.continuousUnfinishedActions = new HashMap<ActionType, ContinuousAction>();
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
	public CollisionType getCollisionType() {
		return CollisionType.PERSON;
	}

	@Override
	public void colisao(Physical physicalObject) throws UntreatedCollision {
		switch (physicalObject.getCollisionType()) {
		case GROUND:
			Gdx.app.debug("DEBUG", "player grounded");
			this.grounded = true;
			break;
		case PERSON:
			// TODO treat the object within range for interactions
			;
			break;
		default:
			break;
		}
	}

	@Override
	public void play(Instant now, Long deltaTime) {
		final Iterator<ActionType> actionsPoolIterator = this.actionsPool.iterator();

		while (actionsPoolIterator.hasNext()) {
			final Action actionToRun;
			switch (actionsPoolIterator.next()) {
			case JUMP:
				actionToRun = new Jump(now);
				break;
			case STOP_WALKING_LEFT:
				actionToRun = new Stop(Direction.LEFT, now);
				break;
			case STOP_WALKING_RIGHT:
				actionToRun = new Stop(Direction.RIGHT, now);
				break;
			case WALKING_LEFT:
				actionToRun = new Walk(Direction.LEFT, now);
				break;
			case WALKING_RIGHT:
				actionToRun = new Walk(Direction.RIGHT, now);
				break;
			default:
				actionToRun = new Idle(now);
				break;
			}

			for (ActionType actionType : actionToRun.getInterruptableActions()) {
				if (this.continuousUnfinishedActions.containsKey(actionType)) {
					Gdx.app.debug("DEBUG", "interrupting action ".concat(String.valueOf(actionType)));
					this.continuousUnfinishedActions.get(actionType).finish(actionToRun.getType(),
							now.plusMillis(deltaTime));
					this.actionsHistory.add(this.continuousUnfinishedActions.remove(actionType));
				}
			}

			if (this.continuousUnfinishedActions.containsKey(actionToRun.getType())) {
				this.continuousUnfinishedActions.get(actionToRun.getType()).continueAction(now.plusMillis(deltaTime));
			}

			if (!this.runningActions.add(actionToRun))
				continue;

			if (actionToRun instanceof ContinuousAction) {
				this.continuousUnfinishedActions.put(actionToRun.getType(), (ContinuousAction) actionToRun);
			}
			this.actionsToRun.add(actionToRun);
		}

		this.actionsPool.clear();

		// clear actions that are already executed TODO continue with continuous actions
		final Iterator<Action> runningActionsIterator = this.runningActions.iterator();
		while (runningActionsIterator.hasNext()) {
			Action action = runningActionsIterator.next();
			if (action.getEnd().isPresent()) {
				// action with cooldown
				if (now.isAfter(action.getEnd().get())) {
					Gdx.app.debug("DEBUG", "ending continuous action ".concat(String.valueOf(action.getType())));
					runningActionsIterator.remove();
				} else {
					this.actionsToRun.add(action);
				}
			} else {
				//Gdx.app.debug("DEBUG", "ending instant action ".concat(String.valueOf(action.getType())));
				this.actionsToRun.add(action);
			}
		}

		final float deltaVelocity = NORMAL_WALK_SPEED - Math.abs(this.body.getLinearVelocity().x);
		final float walkForce = this.body.getMass() * (deltaVelocity / (deltaTime.floatValue() / 1000f));

		final float stopDeltaVelocity = Math.abs(this.body.getLinearVelocity().x);
		final float stopForce = this.body.getMass() * (stopDeltaVelocity / (deltaTime.floatValue() / 1000f));
		for (final Action actionToRun : this.actionsToRun) {

			this.actionsHistory.add(actionToRun);
			switch (actionToRun.getType()) {
			case JUMP:
				if (grounded) {
					Gdx.app.debug("DEBUG", "running action JUMP, for: ".concat(this.getClass().toString()));
					this.body.applyLinearImpulse(new Vector2(0, NORMAL_JUMP_FORCE), this.body.getLocalCenter(), true);
					grounded = false;
				}
				break;
			case STOP_WALKING_LEFT:
				this.body.applyForceToCenter(new Vector2(stopForce, 0), true);
				break;
			case STOP_WALKING_RIGHT:

				this.body.applyForceToCenter(new Vector2(-stopForce, 0), true);
				break;
			case WALKING_RIGHT:
				this.body.applyForceToCenter(new Vector2(walkForce, 0), true);
				break;
			case WALKING_LEFT:
				this.body.applyForceToCenter(new Vector2(-walkForce, 0), true);
				break;
			default:
				break;
			}
		}

		// TODO execute actions

		this.actionsToRun.clear();
	}

	public void jump() {
		this.actionsPool.add(ActionType.JUMP);
	}

	public void walk(Direction direction) {
		this.actionsPool.add(direction.equals(Direction.LEFT) ? ActionType.WALKING_LEFT : ActionType.WALKING_RIGHT);
	}

	public void stop(Direction direction) {
		this.actionsPool
				.add(direction.equals(Direction.LEFT) ? ActionType.STOP_WALKING_LEFT : ActionType.STOP_WALKING_RIGHT);
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
