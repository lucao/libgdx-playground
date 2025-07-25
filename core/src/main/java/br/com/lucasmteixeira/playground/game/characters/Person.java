package br.com.lucasmteixeira.playground.game.characters;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import br.com.lucasmteixeira.playground.game.AnimatedMaterialObject;
import br.com.lucasmteixeira.playground.game.CollisionType;
import br.com.lucasmteixeira.playground.game.Physical;
import br.com.lucasmteixeira.playground.game.animation.AnimationType;
import br.com.lucasmteixeira.playground.game.characters.actions.Action;
import br.com.lucasmteixeira.playground.game.characters.actions.ActionType;
import br.com.lucasmteixeira.playground.game.characters.actions.ContinuousAction;
import br.com.lucasmteixeira.playground.game.characters.actions.Direction;
import br.com.lucasmteixeira.playground.game.characters.actions.Idle;
import br.com.lucasmteixeira.playground.game.characters.actions.Jump;
import br.com.lucasmteixeira.playground.game.characters.actions.Stop;
import br.com.lucasmteixeira.playground.game.characters.actions.Walk;
import br.com.lucasmteixeira.playground.game.exceptions.UntreatedCollision;

public abstract class Person extends AnimatedMaterialObject implements Physical {

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

	private static final Integer NORMAL_JUMP_FORCE = 15000;
	private static final Float NORMAL_WALK_SPEED = 50f;// TODO walk

	private final Set<ActionType> actionsPool;
	private final CircularFifoQueue<Action> actionsHistory;

	private final Set<Action> runningActions;
	private final Map<ActionType, ContinuousAction> continuousUnfinishedActions;
	private final Set<Action> actionsToRun;

	private Direction walkDirection;

	private Direction facingDirection;

	protected Person(Float x, Float y, Float w, Float h, Texture texture, World world) {
		// TODO add a enumMap default for any person (could be random)
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
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 0.0f;

		this.fixture = this.body.createFixture(fixtureDef);
		personBox.dispose();

		this.body.setUserData(this);

		MassData mass = new MassData();
		mass.mass = 80.0f;
		this.body.setMassData(mass);

		this.grounded = false;

		this.actionsPool = new HashSet<ActionType>();
		this.actionsHistory = new CircularFifoQueue<Action>(50);

		this.runningActions = new HashSet<Action>();
		this.continuousUnfinishedActions = new HashMap<ActionType, ContinuousAction>();
		this.actionsToRun = new HashSet<Action>();

		this.walkDirection = Direction.NONE;
		this.facingDirection = Direction.RIGHT;
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

	// TODO add maximum velocity
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

			for (ActionType interruptableActionType : actionToRun.getInterruptableActions()) {
				if (this.continuousUnfinishedActions.containsKey(interruptableActionType)) {
					this.continuousUnfinishedActions.get(interruptableActionType).finish(actionToRun.getType(),
							now.plusMillis(deltaTime));
					this.actionsHistory.add(this.continuousUnfinishedActions.remove(interruptableActionType));
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
					runningActionsIterator.remove();
				} else {
					this.actionsToRun.add(action);
				}
			} else {
				this.actionsToRun.add(action);
			}
		}

		final float personVelocity = this.body.getLinearVelocity().x;
		float deltaVelocity;
		float walkForce;

		final float stopForce = this.body.getMass() * (personVelocity / (deltaTime.floatValue() / 1000f));

		if (grounded) {
			this.currentAnimation = AnimationType.IDLE;
		} else {
			this.currentAnimation = AnimationType.FALLING;
		}
		for (final Action actionToRun : this.actionsToRun) {
			this.actionsHistory.add(actionToRun);
			switch (actionToRun.getType()) {
			case JUMP:
				if (now.isAfter(actionToRun.getDelay().get())) {
					Jump jumpAction = (Jump) actionToRun;
					if (!jumpAction.isExecuted()) {
						this.body.applyLinearImpulse(new Vector2(0, NORMAL_JUMP_FORCE), this.body.getLocalCenter(),
								true);
						grounded = false;
						jumpAction.execute();
					}
				} else {
					this.body.setLinearVelocity(0f, this.body.getLinearVelocity().y);
				}
				break;
			case STOP_WALKING_LEFT:
				if (Direction.LEFT.equals(this.walkDirection) || Direction.NONE.equals(this.walkDirection)) {
					this.body.applyForceToCenter(new Vector2(-stopForce, 0), true);
					this.walkDirection = Direction.NONE;
				}
				break;
			case STOP_WALKING_RIGHT:
				if (Direction.RIGHT.equals(this.walkDirection) || Direction.NONE.equals(this.walkDirection)) {
					this.body.applyForceToCenter(new Vector2(-stopForce, 0), true);
					this.walkDirection = Direction.NONE;
				}
				break;
			case WALKING_RIGHT:
				if (Direction.RIGHT.equals(this.walkDirection) || Direction.NONE.equals(this.walkDirection)) {
					deltaVelocity = NORMAL_WALK_SPEED - Math.abs(personVelocity);
					walkForce = this.body.getMass() * (deltaVelocity / (deltaTime.floatValue() / 1000f));
					this.body.applyForceToCenter(new Vector2(walkForce, 0), true);
					this.walkDirection = Direction.RIGHT;
					this.facingDirection = Direction.RIGHT;
					if (grounded) {
						this.currentAnimation = AnimationType.WALKING;
					}
				}
				break;
			case WALKING_LEFT:
				if (Direction.LEFT.equals(this.walkDirection) || Direction.NONE.equals(this.walkDirection)) {
					deltaVelocity = NORMAL_WALK_SPEED - Math.abs(personVelocity);
					walkForce = this.body.getMass() * (deltaVelocity / (deltaTime.floatValue() / 1000f));
					this.body.applyForceToCenter(new Vector2(-walkForce, 0), true);
					this.walkDirection = Direction.LEFT;
					this.facingDirection = Direction.LEFT;
					if (grounded) {
						this.currentAnimation = AnimationType.WALKING;
					}
				}
				break;
			case RUNNING_RIGHT:
				if (Direction.LEFT.equals(this.walkDirection) || Direction.NONE.equals(this.walkDirection)) {
					deltaVelocity = (3 * NORMAL_WALK_SPEED) - Math.abs(personVelocity);
					walkForce = this.body.getMass() * (deltaVelocity / (deltaTime.floatValue() / 1000f));
					this.body.applyForceToCenter(new Vector2(-walkForce, 0), true);
					this.walkDirection = Direction.LEFT;
					this.facingDirection = Direction.LEFT;
					if (grounded) {
						this.currentAnimation = AnimationType.WALKING;
					}
				}
				break;
			case RUNNING_LEFT:
				if (Direction.LEFT.equals(this.walkDirection) || Direction.NONE.equals(this.walkDirection)) {
					deltaVelocity = (3 * NORMAL_WALK_SPEED) - Math.abs(personVelocity);
					walkForce = this.body.getMass() * (deltaVelocity / (deltaTime.floatValue() / 1000f));
					this.body.applyForceToCenter(new Vector2(-walkForce, 0), true);
					this.walkDirection = Direction.LEFT;
					this.facingDirection = Direction.LEFT;
					if (grounded) {
						this.currentAnimation = AnimationType.WALKING;
					}
				}
				break;
			default:
				break;
			}
		}

		this.actionsToRun.clear();

		// update positioning
		this.x = this.body.getPosition().x - (w / 2);
		this.y = this.body.getPosition().y - (h / 2);
	}

	@Override
	public void draw(SpriteBatch batch) {
		this.animationStateTime += Gdx.graphics.getDeltaTime();
		Sprite spriteToDraw = this.animations.get(this.currentAnimation).get(this.facingDirection)
				.getKeyFrame(this.animationStateTime, this.currentAnimation.isLoop());

		spriteToDraw.setBounds(x, y, w, h);
		spriteToDraw.draw(batch);
	}

	public void jump() {
		if (grounded) {
			this.actionsPool.add(ActionType.JUMP);
		}
	}

	public void walk(Direction direction) {
		if (Direction.RIGHT.equals(direction) && Direction.LEFT.equals(this.walkDirection)) {
			this.actionsPool.add(ActionType.STOP_WALKING_LEFT);
			return;
		}
		if (Direction.LEFT.equals(direction) && Direction.RIGHT.equals(this.walkDirection)) {
			this.actionsPool.add(ActionType.STOP_WALKING_RIGHT);
			return;
		}
		this.actionsPool.add(direction.equals(Direction.LEFT) ? ActionType.WALKING_LEFT : ActionType.WALKING_RIGHT);
	}

	public void stop(Direction direction) {
		if (this.actionsPool.contains(ActionType.WALKING_RIGHT) && direction.equals(Direction.LEFT)) {
			return;
		}
		if (this.actionsPool.contains(ActionType.WALKING_LEFT) && direction.equals(Direction.RIGHT)) {
			return;
		}
		if (this.actionsPool.contains(ActionType.STOP_WALKING_LEFT)
				|| this.actionsPool.contains(ActionType.STOP_WALKING_RIGHT)) {
			return;
		}
		this.actionsPool
				.add(direction.equals(Direction.LEFT) ? ActionType.STOP_WALKING_LEFT : ActionType.STOP_WALKING_RIGHT);
	}

	// X e Y do box2D são no centro
	public void setX(Float x) {
		this.body.getPosition().x = (x + w) - w / 2;
	}

	public void setY(Float y) {
		this.body.getPosition().y = (y + h) - h / 2;
	}
}
