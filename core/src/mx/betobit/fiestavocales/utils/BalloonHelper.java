package mx.betobit.fiestavocales.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import mx.betobit.fiestavocales.screens.PlayScreen;
import mx.betobit.fiestavocales.sprites.Balloon;

/**
 * Created by jesusmartinez on 24/11/16.
 */

public class BalloonHelper {

	private static World world;
	private static BitmapFont customFont;
	private static PlayScreen screen;
	private Texture spriteSheet;
	private TextureRegion textureRegion;
	private Color color;

	public BalloonHelper(PlayScreen playScreen) {
		defineSpriteSheet();
		screen = playScreen;
		customFont = new BitmapFont(Gdx.files.internal("font/regular.fnt"));
		customFont.getData().setScale(0.85f);
		customFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}

	public Balloon createBalloon(Color color, int wordId, float x, float y) {
		return new Balloon(screen, customFont, spriteSheet, wordId, color, x, y);
	}
	/**
	 * Get the sprite sheet of the balloon, split it and create the animation.
	 */
	protected void defineSpriteSheet() {

		switch (MathUtils.random(2)) {
			case 0:
				spriteSheet = new Texture("balloons_pink.png");
				break;
			case 1:
				spriteSheet = new Texture("balloons_blue.png");
				break;
			case 2:
				spriteSheet = new Texture("balloons_green.png");
				break;
		}

		textureRegion = new TextureRegion(spriteSheet, 57, 458);
	}
}
