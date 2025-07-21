package br.com.lucasmteixeira.playground.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class MaterialObject extends GameObject {
	protected Float w;
	protected Float h;

	protected final Texture texture;

	protected MaterialObject(Float x, Float y, Float w, Float h, Texture texture) {
		super(x, y);

		this.w = w;
		this.h = h;

		this.texture = texture;
	}

	public TextureRegion getTexture() {
		return new TextureRegion(texture);
	}

	public Float getW() {
		return w;
	}

	public Float getH() {
		return h;
	}

	/**
	 * Loads a texture with specific color made transparent
	 * 
	 * @param filePath      Path to texture file
	 * @param colorToRemove The color that should become transparent
	 * @param tolerance     How close colors need to be to be removed (0-1)
	 * @return Texture with transparency applied
	 */
	public static Texture loadTextureWithTransparency(String filePath, Color colorToRemove, float tolerance) {
		Pixmap original = new Pixmap(Gdx.files.internal(filePath));
		Pixmap processed = new Pixmap(original.getWidth(), original.getHeight(), Pixmap.Format.RGBA8888);

		 // Prepare target color in packed int format (RGBA8888)
	    int target = Color.rgba8888(colorToRemove);
	    int targetR = (target & 0xFF);
	    int targetG = ((target >> 8) & 0xFF);
	    int targetB = ((target >> 16) & 0xFF);
	    
	    // Convert tolerance from 0-1 range to 0-255
	    int tolerance255 = (int)(tolerance * 255);
	    
	    // Process all pixels
	    for (int y = 0; y < original.getHeight(); y++) {
	        for (int x = 0; x < original.getWidth(); x++) {
	            int pixel = original.getPixel(x, y);
	            
	            // Extract components using bit operations
	            int r = (pixel & 0xFF);
	            int g = ((pixel >> 8) & 0xFF);
	            int b = ((pixel >> 16) & 0xFF);
	            int a = ((pixel >> 24) & 0xFF);
	            
	            // Calculate color distance (manhattan distance for performance)
	            int distance = Math.abs(r - targetR) 
	                         + Math.abs(g - targetG) 
	                         + Math.abs(b - targetB);
	            
	            if (distance <= tolerance255 * 3) { // 3 channels
	                // Set fully transparent pixel
	                processed.drawPixel(x, y, 0);
	            } else {
	                // Maintain original pixel with proper alpha
	                processed.drawPixel(x, y, (a << 24) | (b << 16) | (g << 8) | r);
	            }
	        }
	    }

		Texture texture = new Texture(processed);
		// texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		original.dispose();
		processed.dispose();
		return texture;
	}
}
