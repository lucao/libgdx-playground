package br.com.lucasmteixeira.playground.game;

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
			GameObject gameObject1 = (GameObject) contact.getFixtureA().getUserData();
			gameObject1.colisao((GameObject) contact.getFixtureB().getUserData());
		} catch (ClassCastException e) {
			try {
				throw new GameException(
						"Não foi possível verificar colisão porque um dos objetos envolvidos na colisão não é um GameObject",
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