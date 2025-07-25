package br.com.lucasmteixeira.playground.game;

import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class MaterialObject extends GameObject implements Comparable<MaterialObject> {
	protected Float w;
	protected Float h;
	
	private Float zIndex;

	protected final Sprite sprite;

	protected MaterialObject(Float x, Float y, Float w, Float h, Texture texture) {
		super(x, y);

		this.w = w;
		this.h = h;
		this.setzIndex(0f);
		this.sprite = new Sprite(texture);
		this.sprite.setBounds(x, y, w, h);
		this.sprite.setAlpha(1f);
	}

	public void draw(SpriteBatch batch) {
		this.sprite.setPosition(x, y);
		this.sprite.draw(batch);
	}

	public void setAlpha(float alpha) {
		this.sprite.setAlpha(alpha);
	}

	public List<MaterialObject> getChildMaterialObjects() {
		return Collections.emptyList();
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
		int tolerance255 = (int) (tolerance * 255);

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
				int distance = Math.abs(r - targetR) + Math.abs(g - targetG) + Math.abs(b - targetB);

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
	
	public void setzIndex(Float zIndex) {
		this.zIndex = zIndex;
	}
	
	@Override
	public int compareTo(MaterialObject m) {
		return this.zIndex.compareTo(m.zIndex);
	}

	
}
