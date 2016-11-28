package mx.betobit.fiestavocales.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import mx.betobit.fiestavocales.FiestaDeLasVocales;
import mx.betobit.fiestavocales.screens.MenuScreen;
import mx.betobit.fiestavocales.screens.PlayScreen;
import mx.betobit.fiestavocales.sprites.Score;

/**
 * Created by jesusmartinez on 21/11/16.
 * Represents the Hud of the game by all the relevant information
 */

public class Hud {

	public static final int PADDING = 10;
	private static Viewport viewport;
	private static FiestaDeLasVocales game;
	private Stage stage;
	private Table table;

	private ArrayList<Score> scores;

	// Timer
	private int time;
	private float timeCounter;
	private Label timeLabel;
	private boolean start;

	// Table finish
	private boolean finish;
	private boolean multiplayer;

	/**
	 * Constructor
	 * @param viewport
	 */
	public Hud(FiestaDeLasVocales game, Viewport viewport, boolean multiplayer) {
		this.game = game;
		this.viewport = viewport;
		start = false;
		finish = false;
		time = 10;
		timeCounter = 0f;
		this.multiplayer = multiplayer;
		setGameStage();
	}

	/**
	 * Update timeCount and score.
	 */
	public void update(float dt) {
		if(finish) {
			stage.draw();
		} else {
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
			if(multiplayer)
				scores.get(1).update(dt);
			stage.draw();
		}
	}

	private void setGameStage() {
		stage = new Stage(viewport);
		table = new Table();
		table.top();
		table.setFillParent(true);

		BitmapFont customFont = new BitmapFont(Gdx.files.internal("font/font.fnt"));
		timeLabel = new Label(String.format("%02d ", time),
				new Label.LabelStyle(customFont, Color.WHITE));

		scores = new ArrayList<Score>();
		scores.add(new Score("Tú"));
		table.add(scores.get(0).getLabel())
				.uniformX().align(Align.center);
		table.pad(PADDING).add(timeLabel)
				.uniformX().align(Align.center);

		if(multiplayer) {
			scores.add(new Score("Oponente"));
			table.pad(PADDING).add(scores.get(1).getLabel())
					.uniformX().align(Align.center);
		}

		stage.addActor(table);
	}
	/**
	 * Get score
	 * @param index
	 * @return
	 */
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

	/**
	 * Start counting timer
	 */
	public void startTimer() {
		start = true;
	}

	/**
	 * Redraw stage to show winner and restart button.
	 */
	public void finishGame() {
		finish = true;

		if(multiplayer) {
			if(scores.get(0).getPoints() != scores.get(1).getPoints()) {
				timeLabel.setText(scores.get(0).getPoints() > scores.get(1).getPoints() ?
						"Tu ganaste :D" : "Gana el oponente :C");
			} else {
				timeLabel.setText("Empate");
			}
		} else {
			timeLabel.setText("Puntuación: " + scores.get(0).getPoints());
		}

		stage.dispose();
		stage = new Stage(viewport);
		table = new Table();
		table.setFillParent(true);

		TextureAtlas atlas = new TextureAtlas("menu/finish_menu.txt");
		Skin skinButtons = new Skin(atlas);

		ImageButton.ImageButtonStyle resetStyle = new ImageButton.ImageButtonStyle();
		resetStyle.up = skinButtons.getDrawable("reset");
		resetStyle.over = skinButtons.getDrawable("reset");
		ImageButton reset = new ImageButton(resetStyle);

		reset.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new PlayScreen(game, multiplayer));
			}
		});

		ImageButton.ImageButtonStyle homeStyle = new ImageButton.ImageButtonStyle();
		homeStyle.up = skinButtons.getDrawable("home_small");
		homeStyle.over = skinButtons.getDrawable("home_small");
		ImageButton home = new ImageButton(homeStyle);

		home.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				 game.setScreen(new MenuScreen(game));
			}
		});

		table.add(timeLabel).width(150).center().align(Align.center).padBottom(60).colspan(2).row();
		table.add(reset).width(80).height(80).uniform();
		table.add(home).width(80).height(80).uniform();

		table.center();
		table.setDebug(true);
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
	}
}
