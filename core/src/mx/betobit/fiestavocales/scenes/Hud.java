package mx.betobit.fiestavocales.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import mx.betobit.fiestavocales.sprites.Score;

/**
 * Created by jesusmartinez on 21/11/16.
 * Represents the Hud of the game by all the relevant information
 */

public class Hud {

	public static final int PADDING = 10;
	private Stage stage;
	private Table table;

	private ArrayList<Score> scores;

	// Timer
	private int time;
	private float timeCounter;
	private Label timeLabel;
	private boolean start;

	/**
	 * Constructor
	 * @param viewport
	 */
	public Hud(Viewport viewport) {
		start = false;
		stage = new Stage(viewport);
		table = new Table();
		table.top();
		table.setFillParent(true);
		time = 60;
		timeCounter = 0f;

		BitmapFont customFont = new BitmapFont(Gdx.files.internal("font/font.fnt"));
		timeLabel = new Label(String.format("%02d ", time),
				new Label.LabelStyle(customFont, Color.WHITE));

		scores = new ArrayList<Score>();
		scores.add(new Score("Tú"));
		scores.add(new Score("Oponente"));

		table.add(scores.get(0).getLabel())
				.uniformX().align(Align.center);
		table.pad(PADDING).add(timeLabel)
				.uniformX().align(Align.center);
		table.pad(PADDING).add(scores.get(1).getLabel())
				.uniformX().align(Align.center);

		stage.addActor(table);
	}

	/**
	 * Update timeCount and score.
	 */
	public void update(float dt) {
		if(start) {
			timeCounter += dt;
			if (timeCounter >= 1) {
				if (time > 0) {
					time--;
				}
				timeLabel.setText(String.format("%02d", time));
				timeCounter = -1;
			}
		}

		scores.get(0).update(dt);
		scores.get(1).update(dt);
		stage.draw();
	}

	public Score getScore(int index) {
		return scores.get(index);
	}

	/**
	 * Get time
	 * @return
	 */
	public int getTime() {
		return time;
	}

	public void startTimer() {
		start = true;
	}
/*
	public void setScore(int score) {
		this.score = score;
		scoreLabel.setText(String.format("Tú: %04d ", score));
	}*/

	/*public void setScore2(int score2) {
		this.score2 = score2;
		scoreLabel2.setText(String.format("Oponente: %04d ", score2));
	}*/
}
