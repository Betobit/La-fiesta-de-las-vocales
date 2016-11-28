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

import box2dLight.RayHandler;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import mx.betobit.fiestavocales.FiestaDeLasVocales;
import mx.betobit.fiestavocales.scenes.Hud;
import mx.betobit.fiestavocales.sprites.Balloon;
import mx.betobit.fiestavocales.sprites.Loader;
import mx.betobit.fiestavocales.utils.BalloonHelper;
import mx.betobit.fiestavocales.utils.Constants;
import mx.betobit.fiestavocales.utils.Tools;

/**
 * Created by jesusmartinez on 31/10/16.
 */
public class PlayScreen extends BaseScreen {

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
	private Socket socket;
	private String playerId;
	private char gameState;
	private BalloonHelper balloonHelper;

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

		connectSocket();
		configSocketEvents();
	}

	/**
	 * Add balloon with light.
	 */
	private void addBalloon(int positionY) {
		Balloon b = balloonHelper.createBalloon(
				playerId + MathUtils.random(1000), // Id
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
			socket.emit("newBalloon", json);
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

	/**
	 * Send player score to server.
	 */
	private void sendNewScore() {
		JSONObject json = new JSONObject();
		try {
			json.put("id", playerId);
			json.put("score", hud.getScore(0).getPoints());
			socket.emit("updateScore", json);
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
				deleteBalloon(b, entry.getKey());
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
					deleteBalloon(b, entry.getKey());
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

	/**
	 * Delete body and light from world. Set flag newBalloon to true.
	 * Also indicate to server eliminate
	 * @param balloon
	 */
	private void deleteBalloon(Balloon balloon, String key) {
		world.destroyBody(balloon.getBody()); // Remove body from world
		balloons.remove(key); // Remove from hash map
		balloon.removeLight(); // Remove light form world
		newBalloon = true;

		JSONObject json = new JSONObject();
		try {
			json.put("id", balloon.getId());
			socket.emit("deleteBalloon", json);
		} catch (JSONException e) {
			Gdx.app.log("Error", "Error sending new score");
		}
	}

	@Override
	public void dispose() {
		b2dr.dispose();
		// TODO: Dispoes balloons
	}

	/**
	 * Get world of the screen
	 * @return
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Connect to server
	 */
	public void connectSocket(){
		try {
			socket = IO.socket(Constants.SERVER_IP);
			socket.connect();
		} catch(Exception e){
			System.out.println(e);
		}
	}

	/**
	 * Socket communication
	 */
	public void configSocketEvents(){
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
			}
		}).on("socketId", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					playerId = data.getString("id");
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error: " + e.getMessage());
				}
			}
		}).on("playerDisconnected", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				//hud.getScore(1).setPoints(0);
			}
		}).on("startGame", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject response = (JSONObject) args[0];
				try {
					if(response.getInt("players") > 1) {
						hud.startTimer();
						gameState = 'p';
					}
				} catch (JSONException e) {

				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				for(int i = 0; i < 4; i++)
					addBalloon(MathUtils.random(0, height/2));
			}
		}).on("updateScore", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject player = (JSONObject) args[0];

				try {
					if(playerId.equals(player.getString("id")))
						hud.getScore(0).setLabel(player.getInt("score"));
					else
						hud.getScore(1).setLabel(player.getInt("score"));
				} catch (JSONException e) {

				}
			}
		}).on("getPlayers", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONArray jsonPlayers = (JSONArray) args[0];
				try {
					for(int i = 0; i < jsonPlayers.length(); i++) {
						JSONObject player = jsonPlayers.getJSONObject(i);
						if(playerId.equals(player.getString("id")))
							hud.getScore(0).setLabel(player.getInt("score"));
						else
							hud.getScore(1).setLabel(player.getInt("score"));
					}
				} catch (JSONException e) {

				}
			}
		}).on("getBalloons", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONArray jsonBalloons = (JSONArray) args[0];
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
		}).on("deleteBalloon", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject jsonBalloon = (JSONObject) args[0];
				try {
					balloons.remove(jsonBalloon.getString("id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
