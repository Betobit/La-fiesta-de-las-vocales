package mx.betobit.fiestavocales.screens;

/**
 * Created by jesusmartinez on 31/10/16.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import mx.betobit.fiestavocales.FiestaDeLasVocales;

/**
 * Main menu screen. The first screen which the user interacts.
 */
public class MainScreen extends BaseScreen {

	private Sprite background;
	private Stage stage;

	private int width;
	private int height;

	/**
	 * Constructor
	 */
	public MainScreen(FiestaDeLasVocales game) {
		super(game, 800, 480);
		width = getWidth();
		height= getHeight();

	}

	@Override
	public void show() {
		Texture textureBackground = new Texture("menu/clouds.jpg");
		background = new Sprite(textureBackground);
		stage = new Stage(getViewport());

		stage.addActor(setMenuTable());
		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * Get atlas, resources and drawables to draw buttons in a table.
	 * SetInputProcessor of the stage to detect interaction.
	 *
	 * @return Generated table
	 */
	private Table setMenuTable() {
		Table table = new Table();
		TextureAtlas atlas = new TextureAtlas("menu/atlas.txt");

		// Set background
		background.setPosition(0, 0);
		background.setSize(width, height);

		Skin skinButtons = new Skin(atlas);

		// Set big buttons
		ImageButton.ImageButtonStyle dresserStyle = new ImageButton.ImageButtonStyle();
		dresserStyle.up = skinButtons.getDrawable("dresser");
		dresserStyle.over = skinButtons.getDrawable("dresser_hover");
		ImageButton dresser = new ImageButton(dresserStyle);

		ImageButton.ImageButtonStyle storeStyle = new ImageButton.ImageButtonStyle();
		storeStyle.up = skinButtons.getDrawable("store");
		storeStyle.over = skinButtons.getDrawable("store_hover");
		ImageButton store = new ImageButton(storeStyle);

		ImageButton.ImageButtonStyle profileStyle = new ImageButton.ImageButtonStyle();
		profileStyle.up = skinButtons.getDrawable("profile");
		profileStyle.over = skinButtons.getDrawable("profile_hover");
		ImageButton profile = new ImageButton(profileStyle);

		// Set small buttons
		ImageButton.ImageButtonStyle homeStyle = new ImageButton.ImageButtonStyle();
		homeStyle.up = skinButtons.getDrawable("home");
		homeStyle.over = skinButtons.getDrawable("home_hover");
		ImageButton home = new ImageButton(homeStyle);

		ImageButton.ImageButtonStyle scoreStyle = new ImageButton.ImageButtonStyle();
		scoreStyle.up = skinButtons.getDrawable("scoreboard");
		scoreStyle.over = skinButtons.getDrawable("scoreboard_hover");
		ImageButton scoreboard = new ImageButton(scoreStyle);

		ImageButton.ImageButtonStyle helpStyle = new ImageButton.ImageButtonStyle();
		helpStyle.up = skinButtons.getDrawable("help");
		helpStyle.over = skinButtons.getDrawable("help_hover");
		ImageButton help = new ImageButton(helpStyle);

		home.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				getGame().setScreen(new PlayScreen(getGame()));
			}
		});

		// Menu title
		Image menuTitle = new Image(atlas.findRegion("menu"));
		table.add(menuTitle).height(40).width(90).colspan(3).padBottom(70);
		table.row();

		// Set table with the buttons
		table.setFillParent(true);
		table.bottom();

		table.add(dresser).width(150).height(180).padRight(20);
		table.add(store).width(150).height(180).padRight(20);
		table.add(profile).width(150).height(180).padRight(20);
		table.row().padTop(20).padBottom(20);

		table.add(home).width(70).height(70);
		table.add(scoreboard).width(140).height(70);
		table.add(help).width(70).height(70);

		return table;
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		batch.begin();
		background.draw(batch);
		batch.end();

		stage.draw();
		stage.act(delta);
	}

	@Override
	public void dispose() {
		background.getTexture().dispose();
		stage.dispose();
	}
}