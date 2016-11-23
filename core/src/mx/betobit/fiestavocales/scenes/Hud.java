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
	private int score;

	private static Label scoreLabel2;
	private int score2;

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
		scoreLabel2 = new Label(String.format("Oponente: %04d ", score2),
				new Label.LabelStyle(customFont, Color.WHITE));
		table.left().pad(PADDING).add(scoreLabel).expandX();
		table.add(scoreLabel2).expandX();

		stage.addActor(table);
	}

	// TODO: Refactor similar methods
	/**
	 * Add the given score to the player 1
	 * @param value points
	 */
	public void addScore1(int value){
		score += value;
		scoreLabel.setText(String.format("Tú: %04d ", score));
	}

	/**
	 * Add the given score to the player 2
	 * @param value points
	 */
	public void addScore2(int value){
		score2 += value;
		scoreLabel2.setText(String.format("Oponente: %04d ", score2));
	}

	/**
	 * Update timeCount and score.
	 */
	public void update(float dt){
		stage.draw();
	}

	/**
	 * Get score player 1
	 * @return
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Get score player 2
	 * @return
	 */
	public int getScore2() {
		return score2;
	}

	public void setScore(int score) {
		this.score = score;
		scoreLabel.setText(String.format("Tú: %04d ", score));
	}

	public void setScore2(int score2) {
		this.score2 = score2;
		scoreLabel2.setText(String.format("Oponente: %04d ", score2));
	}
}
