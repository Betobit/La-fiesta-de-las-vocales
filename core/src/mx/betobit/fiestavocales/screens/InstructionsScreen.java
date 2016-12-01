package mx.betobit.fiestavocales.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import mx.betobit.fiestavocales.FiestaDeLasVocales;

/**
 * Created by jesusmartinez on 01/12/16.
 */

public class InstructionsScreen extends BaseScreen {

	private Sprite background;
	private Sprite sprite;
	private float timeCounter;
	private int time;
	public Label timeLabel;
	private Boolean multiplayer;

	/**
	 * Constructor
	 *
	 * @param heroesOfAnzu
	 */
	public InstructionsScreen(FiestaDeLasVocales heroesOfAnzu, boolean multiplayer) {
		super(heroesOfAnzu, 800, 480);
		timeCounter = 0f;
		this.multiplayer = multiplayer;
		time = 4;

		// Background
		Texture textureBackground = new Texture(Gdx.files.internal("bkg_sky.jpg"));
		background = new Sprite(textureBackground);

		// Timer
		BitmapFont customFont = new BitmapFont(Gdx.files.internal("font/font.fnt"));
		timeLabel = new Label(String.format("%01d ", time),
				new Label.LabelStyle(customFont, Color.GOLD));
		timeLabel.setPosition(390, 200);

		// Image
		Texture texture = new Texture(Gdx.files.internal("instructions.png"));
		sprite = new Sprite(texture);
		sprite.setPosition(300, 0);
		sprite.setSize(192, 384);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		batch.begin();
		background.draw(batch);
		sprite.draw(batch);
		timeLabel.draw(batch, 30);
		batch.end();

		timeCounter += delta;
		if (timeCounter >= 1) {
			if (time > 1) {
				time--;
			} else {
				getGame().setScreen(new PlayScreen(getGame(), multiplayer));
			}
			timeLabel.setText(String.format("%01d", time));
			timeCounter = -1;
		}

	}
}
