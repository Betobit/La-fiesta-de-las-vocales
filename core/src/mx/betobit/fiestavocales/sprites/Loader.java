package mx.betobit.fiestavocales.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import mx.betobit.fiestavocales.screens.PlayScreen;

/**
 * Created by jesusmartinez on 23/11/16.
 */

public class Loader extends SpriteAnimation {

	public Loader(PlayScreen playScreen, int width, int height, float x, float y) {
		super(playScreen, width, height, x, y);
		defineSpriteSheet();
	}

	/**
	 * Get the sprite sheet of the loader, split it and create the animation.
	 */
	protected void defineSpriteSheet() {
		setSpriteSheet(new Texture("loader.png"));
		setTextureRegion(new TextureRegion(getSpriteSheet(), 480, 420));

		TextureRegion[][] splited = getTextureRegion().split(60, 60);
		TextureRegion[] frames = new TextureRegion[56];

		for (int i = 0; i < 7; i++)
			for(int j = 0; j < 8; j++)
				frames[8*i+j] = splited[i][j];

		setAnimation(new Animation(0.015f, frames));
	}

}
