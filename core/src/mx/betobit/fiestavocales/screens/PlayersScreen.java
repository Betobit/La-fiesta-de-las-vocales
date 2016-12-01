package mx.betobit.fiestavocales.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import mx.betobit.fiestavocales.FiestaDeLasVocales;

/**
 * Created by jesusmartinez on 24/11/16.
 */

public class PlayersScreen extends BaseScreen {

	// UI
	private Sprite background;
	private Stage stage;

	/**
	 * Constructor
	 */
	public PlayersScreen(FiestaDeLasVocales game) {
		super(game, 800, 480);

		Texture textureBackground = new Texture("bkg_sky.jpg");
		background = new Sprite(textureBackground);
		stage = new Stage(getViewport());

		Table table = new Table();
		TextureAtlas atlas = new TextureAtlas("menu/menu.txt");
		Skin skinButtons = new Skin(atlas);

		ImageButton.ImageButtonStyle player1Style = new ImageButton.ImageButtonStyle();
		player1Style.up = skinButtons.getDrawable("1_player");
		player1Style.over = skinButtons.getDrawable("1_player_hover");
		ImageButton player1 = new ImageButton(player1Style);

		player1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				getGame().setScreen(new InstructionsScreen(getGame(), false));
			}
		});

		ImageButton.ImageButtonStyle player2Style = new ImageButton.ImageButtonStyle();
		player2Style.up = skinButtons.getDrawable("2_players");
		player2Style.over = skinButtons.getDrawable("2_players_hover");
		ImageButton player2 = new ImageButton(player2Style);

		player2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				getGame().setScreen(new PlayScreen(getGame(), true));
			}
		});

		// Set table with the buttons
		table.setFillParent(true);
		table.center();

		//table.row().padTop(10).padBottom(20);

		table.add(player1).width(160).height(70).padBottom(30);
		table.row();
		table.add(player2).width(160).height(70);

		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
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

}
