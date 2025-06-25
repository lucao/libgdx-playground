package br.com.lucasmteixeira.playground.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxLogger;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import br.com.lucasmteixeira.playground.game.exceptions.GameException;
import br.com.lucasmteixeira.playground.game.exceptions.UntreatedCollision;

public class WorldContactListener implements ContactListener {
	private static final GdxLogger COLLISION_LOGGER = new GdxLogger();

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void beginContact(Contact contact) {
		try {
			Physical gameObject1 = (Physical) contact.getFixtureA().getBody().getUserData();
			Physical gameObject2 = (Physical) contact.getFixtureB().getBody().getUserData();
			if (gameObject1 == null || gameObject2 == null) {
				Gdx.app.error("ERROR",
						"Collision detected but not processed due to null userData in Box2D. (gameObject1: "
								.concat(String.valueOf(gameObject1).concat("), (gameObject2: ")
										.concat(String.valueOf(gameObject2)).concat(").")));
			} else {
				Gdx.app.debug("DEBUG",
						"Collision detected for: (gameObject1: "
								.concat(String.valueOf(gameObject1.getClass()).concat("), (gameObject2: ")
										.concat(String.valueOf(gameObject2.getClass())).concat(").")));
				gameObject1.colisao(gameObject2);
			}
		} catch (ClassCastException e) {
			try {
				throw new GameException(
						"Não foi possível verificar colisão porque um dos objetos envolvidos na colisão não é do tipo Physical",
						e);
			} catch (GameException ex) {
				COLLISION_LOGGER.error("COLLISION", ex.getMessage());
			}
		} catch (UntreatedCollision e) {
			COLLISION_LOGGER.error("COLLISION", e.getMessage());
		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
};