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
	private static Texture spriteSheetPink;
	private static Texture spriteSheetGreen;
	private static Texture spriteSheetBlue;
	private Color color;

	public BalloonHelper(PlayScreen playScreen) {
		defineSpriteSheets();
		screen = playScreen;
		customFont = new BitmapFont(Gdx.files.internal("font/regular.fnt"));
		customFont.getData().setScale(0.85f);
		customFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}

	public Balloon createBalloon(Color color, int wordId, float x, float y) {
		String colorStr = color.toString();
		Balloon balloon;

		if(colorStr.contains(Constants.SCARLET.substring(0,6)))
			balloon = new Balloon(screen, customFont, spriteSheetPink, wordId, color, x, y);
		else if(colorStr.contains(Constants.CYAN.substring(0,6)))
			balloon = new Balloon(screen, customFont, spriteSheetBlue, wordId, color, x, y);
		else {
			balloon = new Balloon(screen, customFont, spriteSheetGreen, wordId, color, x, y);
		}

		return balloon;
	}
	/**
	 * Get the sprite sheet of the balloon, split it and create the animation.
	 */
	protected void defineSpriteSheets() {
		spriteSheetPink = new Texture("balloons_pink.png");
		spriteSheetGreen = new Texture("balloons_blue.png");
		spriteSheetBlue = new Texture("balloons_green.png");
	}
}
