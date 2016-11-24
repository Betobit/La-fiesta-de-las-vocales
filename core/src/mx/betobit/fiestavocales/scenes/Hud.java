package mx.betobit.fiestavocales.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

	/**
	 * Constructor
	 * @param viewport
	 */
	public Hud(Viewport viewport) {
		stage = new Stage(viewport);
		table = new Table();
		table.top();
		table.setFillParent(true);

		scores = new ArrayList<Score>();
		scores.add(new Score("Tú"));
		scores.add(new Score("Oponente"));

		table.left().pad(PADDING).add(scores.get(0).getLabel()).expandX();
		table.add(scores.get(1).getLabel()).expandX();

		stage.addActor(table);
	}


	/**
	 * Update timeCount and score.
	 */
	public void update(float dt){
		scores.get(0).update(dt);
		scores.get(1).update(dt);
		stage.draw();
	}

	public Score getScore(int index) {
		return scores.get(index);
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
