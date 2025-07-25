package br.com.lucasmteixeira.playground.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

import br.com.lucasmteixeira.playground.Main;
import br.com.lucasmteixeira.playground.game.characters.actions.Direction;
import br.com.lucasmteixeira.playground.game.characters.player.Player;

public class InputProcessorPC implements InputProcessor {

	private final Player player;
	private final OrthographicCamera camera;

	public InputProcessorPC(Player player, OrthographicCamera camera) {
		this.player = player;
		this.camera = camera;
	}

	@Override
	public boolean keyDown(int keycode) {

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.D) {
			player.stop(Direction.RIGHT);
		} else if (keycode == Input.Keys.A) {
			player.stop(Direction.LEFT);
		}

		if (keycode == Input.Keys.SPACE) {
			player.jump();
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
//		Vector3 mouseVector = camera.unproject(new Vector3(screenX, screenY, 1));
//		player.x = mouseVector.x;
//		player.y = mouseVector.y;
		return true;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		float zoomValue = camera.zoom + amountY * Main.ZOOM_LERP;
		if (zoomValue < 0.5f) {
			camera.zoom = 0.5f;
		} else if (zoomValue > 10f) {
			camera.zoom = 10f;
		} else {
			camera.zoom = zoomValue;
		}

		return true;
	}

}
