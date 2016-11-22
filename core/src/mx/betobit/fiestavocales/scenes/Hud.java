package mx.betobit.fiestavocales.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by jesusmartinez on 21/11/16.
 * Represents the Hud of the game by all the relevant information
 */

public class Hud {

	public static final int PADDING = 10;
	private Stage stage;
	private Table table;

	private static Label scoreLabel;
	private static int score;

	private static Label scoreLabel2;
	private static int score2;

	/**
	 * Constructor
	 * @param viewport
	 */
	public Hud(Viewport viewport) {
		score = score2 = 0;

		stage = new Stage(viewport);
		table = new Table();
		table.top();
		table.setFillParent(true);

		BitmapFont customFont = new BitmapFont(Gdx.files.internal("font/font.fnt"));
		scoreLabel = new Label(String.format("Tú: %04d ", score),
				new Label.LabelStyle(customFont, Color.WHITE));
		scoreLabel2 = new Label(String.format("Oponente: %04d ", score),
				new Label.LabelStyle(customFont, Color.WHITE));
		table.left().pad(PADDING).add(scoreLabel).expandX();
		table.add(scoreLabel2).expandX();

		stage.addActor(table);
	}

	public static void addScore(int value){
		score += value;
		scoreLabel.setText(String.format("Tú: %04d ", score));
	}

	/**
	 * Update timeCount and score.
	 */
	public void update(float dt){
		stage.draw();
	}

}
