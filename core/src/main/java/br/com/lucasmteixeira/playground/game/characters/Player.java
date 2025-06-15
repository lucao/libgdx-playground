package br.com.lucasmteixeira.playground.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import br.com.lucasmteixeira.playground.game.Person;

public class Player extends Person {

	public Player(Float x, Float y, Float w, Float h, Texture texture, World world) {
		super(x, y, w, h, texture, world);
		// TODO Auto-generated constructor stub
		// For click/touch events
		this.addListener(new ClickListener() {
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	//TODO
		        System.out.println("Image clicked!");
		    }
		});
	}

}
