package br.com.lucasmteixeira.playground.game.aventuras;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

import br.com.lucasmteixeira.playground.game.Aventura;
import br.com.lucasmteixeira.playground.game.GameObject;
import br.com.lucasmteixeira.playground.game.characters.Player;
import br.com.lucasmteixeira.playground.game.scenery.Ground;

public class AventuraPadrao extends Aventura {
	
	private static List<GameObject> criarGameObjects(World world) {
		List<GameObject> gameObjects = new ArrayList<GameObject>();
		gameObjects.add(new Player(0f, 0f, 1f, 2f, new Texture("libgdx.png"), world));
		gameObjects.add(new Ground(-100f, 20f, 400f, 20f, new Texture(""), world));
		
		return gameObjects;
	}

	public AventuraPadrao(Camera camera) {
		super(camera);
		
		this.gameObjects.addAll(criarGameObjects(this.world));
		// TODO Auto-generated constructor stub
	}
}
