package br.com.lucasmteixeira.playground.game.characters.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

import br.com.lucasmteixeira.playground.game.AnimatedMaterialObject;
import br.com.lucasmteixeira.playground.game.animation.AnimationType;

public class NarutoPlayer extends Player {

	public NarutoPlayer(Float x, Float y, Float w, Float h, World world) {
		super(x, y, w, h, new Texture("Naruto.jpg"), world);

		// Create array of TextureRegions for the first row animation frames
		TextureRegion[] idleFrames = new TextureRegion[6];
		idleFrames[0] = new TextureRegion(this.texture, 30, 5, 36, 55);
		idleFrames[1] = new TextureRegion(this.texture, 80, 5, 36, 55);
		idleFrames[2] = new TextureRegion(this.texture, 130, 5, 36, 55);
		idleFrames[3] = new TextureRegion(this.texture, 178, 5, 36, 55);
		idleFrames[4] = new TextureRegion(this.texture, 225, 5, 36, 55);
		idleFrames[5] = new TextureRegion(this.texture, 275, 5, 36, 55);
		
		this.animations.put(AnimationType.IDLE,
				new Animation<TextureRegion>(AnimatedMaterialObject.FRAME_INTERVAL, idleFrames));
		
	}
}
