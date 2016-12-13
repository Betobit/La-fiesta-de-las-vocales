package mx.betobit.fiestavocales.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import mx.betobit.fiestavocales.FiestaDeLasVocales;
import mx.betobit.fiestavocales.screens.MenuScreen;
import mx.betobit.fiestavocales.screens.PlayScreen;
import mx.betobit.fiestavocales.sprites.Score;
import mx.betobit.fiestavocales.utils.Constants;

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
	private Sprite timeImage;

	// Table finish
	private boolean multiplayer;

	/**
	 * Constructor
	 * @param viewport
	 */
	public Hud(FiestaDeLasVocales game, Viewport viewport, boolean multiplayer) {
		this.game = game;
		this.viewport = viewport;
		start = false;
		time = 60;
		timeCounter = 0f;
		this.multiplayer = multiplayer;
		setTimerSprite();
		setGameStage();
	}

	/**
	 * Update timeCount and score.
	 */
	public void update(float dt) {
		/* Not now... deadline is coming
		game.getBatch().begin();
		timeImage.draw(game.getBatch());
		game.getBatch().end();
		*/

		if(start) {
			timeCounter += dt;
			scores.get(0).update(dt);
			if(multiplayer)
				scores.get(1).update(dt);

			if (timeCounter >= 1) {
				if (time > 0) {
					time--;
					timeLabel.setText(String.format("%02d", time));
					timeCounter = -1;
				} else {
					start = false;
				}
			}
		}
		stage.draw();
	}

	/**
	 * Create resource for instructions.
	 */
	private void setTimerSprite() {
		Texture texture = new Texture("timer.png");
		timeImage = new Sprite(texture);
		timeImage.setSize(100, 100);
		if(multiplayer)
			timeImage.setPosition(400, 380);
		else
			timeImage.setPosition(400, 380);
	}

	/**
	 * Inflate table with options.
	 */
	private void setGameStage() {
		stage = new Stage(viewport);
		table = new Table();
		table.top();
		table.setFillParent(true);

		BitmapFont customFont = new BitmapFont(Gdx.files.internal("font/font.fnt"));
		timeLabel = new Label(String.format("%02d ", time),
				new Label.LabelStyle(customFont, Color.WHITE));

		// Score player 1
		scores = new ArrayList<Score>();
		scores.add(new Score("P1"));
		table.add(scores.get(0).getLabel())
				.uniformX().align(Align.center);

		// Timer
		table.pad(PADDING).add(timeLabel)
				.uniformX().align(Align.center);

		// Score player 2
		if(multiplayer) {
			scores.add(new Score("P2"));
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
		if(multiplayer) {
			if(scores.get(0).getPoints() != scores.get(1).getPoints()) {
				timeLabel.setText(scores.get(0).getPoints() > scores.get(1).getPoints() ?
						"Gana jugador 1" : "Gana jugador 2");
			} else {
				timeLabel.setText("Empate");
			}
		} else {
			timeLabel.setText("Puntuacion: " + scores.get(0).getPoints());
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


		if(Constants.DEBUGGING) table.setDebug(true);

		table.center();
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * Clear memory
	 */
	public void dispose() {
		stage.dispose();
	}
}
