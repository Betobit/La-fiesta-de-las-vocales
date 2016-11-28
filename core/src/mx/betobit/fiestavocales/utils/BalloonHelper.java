package mx.betobit.fiestavocales.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import box2dLight.RayHandler;
import mx.betobit.fiestavocales.screens.PlayScreen;
import mx.betobit.fiestavocales.sprites.Balloon;

/**
 * Created by jesusmartinez on 24/11/16.
 */

public class BalloonHelper {

	private static RayHandler rayHandler;
	private static BitmapFont customFont;
	private static PlayScreen screen;
	private static Texture spriteSheetPink;
	private static Texture spriteSheetGreen;
	private static Texture spriteSheetBlue;

	public BalloonHelper(PlayScreen playScreen) {
		defineSpriteSheets();
		screen = playScreen;
		customFont = new BitmapFont(Gdx.files.internal("font/regular.fnt"));
		customFont.getData().setScale(0.85f);
		customFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		rayHandler = new RayHandler(screen.getWorld());
		rayHandler.setAmbientLight(0.6f);
	}

	/**
	 * Create new balloon using pre-created sprite sheets.
	 * @param id
	 * @param color
	 * @param wordId
	 * @param x
	 * @param y
	 * @return
	 */
	public Balloon createBalloon(String id, Color color, int wordId, float x, float y) {
		String colorStr = color.toString();
		Balloon balloon;

		if(colorStr.contains(Constants.SCARLET.substring(0,6)))
			balloon = new Balloon(id, spriteSheetPink, wordId, Color.SCARLET, x, y);
		else if(colorStr.contains(Constants.CYAN.substring(0,6)))
			balloon = new Balloon(id, spriteSheetBlue, wordId, Color.CYAN, x, y);
		else {
			balloon = new Balloon(id, spriteSheetGreen, wordId, Color.GREEN, x, y);
		}

		return balloon;
	}
	/**
	 * Get the sprite sheet of the balloon, split it and create the animation.
	 */
	private void defineSpriteSheets() {
		spriteSheetPink = new Texture("balloons_pink.png");
		spriteSheetGreen = new Texture("balloons_green.png");
		spriteSheetBlue = new Texture("balloons_blue.png");
	}

	public static void updateRayHandler() {
		rayHandler.setCombinedMatrix(screen.getCamera());
		rayHandler.updateAndRender();
	}

	public static RayHandler getRayHandler() {
		return rayHandler;
	}

	public static PlayScreen getScreen() {
		return screen;
	}

	public static BitmapFont getCustomFont() {
		return customFont;
	}
}
