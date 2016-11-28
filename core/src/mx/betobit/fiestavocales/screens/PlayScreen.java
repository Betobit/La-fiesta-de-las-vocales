package mx.betobit.fiestavocales.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import mx.betobit.fiestavocales.FiestaDeLasVocales;
import mx.betobit.fiestavocales.scenes.Hud;
import mx.betobit.fiestavocales.socket.SocketClient;
import mx.betobit.fiestavocales.socket.SocketInterface;
import mx.betobit.fiestavocales.sprites.Balloon;
import mx.betobit.fiestavocales.sprites.Loader;
import mx.betobit.fiestavocales.utils.BalloonHelper;
import mx.betobit.fiestavocales.utils.Constants;
import mx.betobit.fiestavocales.utils.Tools;

/**
 * Created by jesusmartinez on 31/10/16.
 */
public class PlayScreen extends BaseScreen implements SocketInterface {

	// UI
	private Sprite background;
	private Hud hud;
	private Loader loader;
	private BitmapFont customFont;

	// World
	private int width;
	private int height;
	private World world;

	// Bodies
	private ConcurrentHashMap<String, Balloon> balloons;
	private boolean newBalloon;

	// Debugging
	private Box2DDebugRenderer b2dr;
	private ShapeRenderer shapeRenderer;

	// Others
	private Sound popSound;
	private char gameState;
	private BalloonHelper balloonHelper;
	private SocketClient socketClient;

	/**
	 * Constructor
	 */
	public PlayScreen(FiestaDeLasVocales game, boolean multiplayer) {
		super(game, 800, 480);
		width = getWidth();
		height= getHeight();

		gameState = multiplayer ? 'w' : 'p';
		balloons = new ConcurrentHashMap<String, Balloon>();
		b2dr = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();
		popSound = Gdx.audio.newSound(Gdx.files.internal("sounds/pop.mp3"));
		customFont = new BitmapFont(Gdx.files.internal("font/font.fnt"));
		socketClient = new SocketClient();
		socketClient.setSocketInterface(this);
	}

	/**
	 * Init world, lights and create balloons.
	 */
	@Override
	public void show() {
		// Background
		Texture textureBackground = new Texture("bkg_sky.jpg");
		background = new Sprite(textureBackground);

		// World
		world = new World(new Vector2(0, 0), false);

		balloonHelper = new BalloonHelper(this);

		newBalloon = false;
		hud = new Hud(getViewport());
		loader = new Loader(this, 120, 120, width/2, height/2);
	}

	/**
	 * Add balloon with light.
	 */
	private void addBalloon(int positionY) {
		Balloon b = balloonHelper.createBalloon(
				socketClient.playerId + MathUtils.random(1000), // Id
				Tools.getRandomColor(), -1, // Color, wordId
				MathUtils.random(0, width), positionY); // Position

		balloons.put(b.getId(), b);

		// Sending balloon to server
		try {
			JSONObject json = new JSONObject();
			json.put("id", b.getId());
			json.put("color", "#" + b.getColor().toString());
			json.put("wordId", b.getWord().getId());
			json.put("x", b.getBody().getPosition().x);
			json.put("y", b.getBody().getPosition().y);
			socketClient.socket.emit("newBalloon", json);
		} catch (JSONException e) {
			Gdx.app.log("Error", "Error creating new balloon json");
		}

	}

	@Override
	public void render(float delta) {
		super.render(delta);

		batch.begin();
		background.draw(batch);
		batch.end();

		switch(gameState) {
			case 'w' : // waiting
				loader.update(delta);
				batch.begin();
				customFont.draw(batch, "Esperando segundo jugador...", width/2 - 170, height/2 - 80);
				batch.end();
				break;
			case 'p': // play
				world.step(Gdx.graphics.getDeltaTime(), 6, 2);
				BalloonHelper.updateRayHandler();
				hud.update(delta);

				if (Constants.DEBUGGING) {
					b2dr.render(world, getCamera().combined);
				}

				// Render balloons
				renderBalloons(delta);

				if(newBalloon) {
					addBalloon(-50);
					newBalloon = false;
				}

				if(hud.getTime() <= 0) {
					//gameState = 'w';
				}
		}
	}

	@Override
	public void dispose() {
		b2dr.dispose();
		// TODO: Dispoes balloons
	}

	/**
	 * Send player score to server.
	 */
	private void sendNewScore() {
		JSONObject json = new JSONObject();
		try {
			json.put("id", socketClient.playerId);
			json.put("score", hud.getScore(0).getPoints());
			socketClient.socket.emit("updateScore", json);
		} catch (JSONException e) {
			Gdx.app.log("Error", "Error sending new score");
		}
	}

	/**
	 * Render balloon and destroy it when is out of the screen.
	 * Listen for a tap.
	 * Iterate over the lights.
	 *
	 * @param delta Delta time
	 */
	private void renderBalloons(float delta) {
		for(final HashMap.Entry<String, Balloon> entry : balloons.entrySet()) {
			final Balloon b = entry.getValue();
			if (b.getY() > height - 20) {
				deleteBalloon(entry.getKey());
			}

			b.onTap(new Balloon.OnTapListener() {
				@Override
				public void onTap() {
					popSound.play(0.5f);
					if(b.getWord().isDiphthong())
						hud.getScore(0).addPoints(20);
					else
						hud.getScore(0).addPoints(-20);
					sendNewScore();
					deleteBalloon(entry.getKey());
					triggerDeleteBalloon(b);
				}
			});

			if(Constants.DEBUGGING) {
				shapeRenderer.setProjectionMatrix(getCamera().combined);
				shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
				shapeRenderer.setColor(Color.FOREST);
				shapeRenderer.rect(b.getX() - 5, b.getY() + 60, 60, 80);
				shapeRenderer.end();
			}

			b.update(delta);
		}
	}

	private void triggerDeleteBalloon(Balloon balloon) {
		newBalloon = true;

		JSONObject json = new JSONObject();
		try {
			json.put("id", balloon.getId());
			socketClient.socket.emit("deleteBalloon", json);
		} catch (JSONException e) {
			Gdx.app.log("Error", "Error sending new score");
		}
	}

	/**
	 */
	private void deleteBalloon(String key) {
		Balloon balloon = balloons.get(key);
		if(balloons.size() > 0) {
			world.destroyBody(balloon.getBody()); // Remove body from world
			balloons.remove(key); // Remove from hash map
			balloon.removeLight(); // Remove light form world
		}
	}

	/**
	 * Get world of the screen
	 * @return
	 */
	public World getWorld() {
		return world;
	}

	/********************
	 * INTERFACE METHODS
	 ********************/
	@Override
	public void onStartGame() {
		hud.startTimer();
		gameState = 'p';
	}

	@Override
	public void onNewPlayer() {
		for(int i = 0; i < 4; i++)
			addBalloon(MathUtils.random(0, height/2));
	}

	@Override
	public void onUpdateScore(Boolean player1, int score) {
		if(player1)
			hud.getScore(0).setLabel(score);
		else
			hud.getScore(1).setLabel(score);
	}

	@Override
	public void onGetPlayers(Boolean player1, int score) {
		if(player1)
			hud.getScore(0).setLabel(score);
		else
			hud.getScore(1).setLabel(score);

	}

	@Override
	public void onGetBalloons(JSONArray jsonBalloons) {
		try {
			for(int i = 0; i < jsonBalloons.length(); i++) {
				JSONObject jsonBalloon = jsonBalloons.getJSONObject(i);
				Vector2 position = new Vector2();
				position.x = ((Double) jsonBalloon.getDouble("x")).floatValue();
				position.y = ((Double) jsonBalloon.getDouble("y")).floatValue();

				Balloon b = balloonHelper.createBalloon(
						jsonBalloon.getString("id"),
						Color.valueOf(jsonBalloon.getString("color")), // Color
						jsonBalloon.getInt("wordId"), // Word id
						position.x, position.y); // Position
				if(!balloons.containsKey(b.getId()))
					balloons.put(b.getId(), b);
			}
		} catch (JSONException e) {

		}
	}

	@Override
	public void onDeleteBalloon(String id) {
		deleteBalloon(id);
	}
}
