package mx.betobit.fiestavocales.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ListIterator;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import mx.betobit.fiestavocales.utils.Constants;
import mx.betobit.fiestavocales.FiestaDeLasVocales;
import mx.betobit.fiestavocales.scenes.Hud;
import mx.betobit.fiestavocales.sprites.Balloon;
import mx.betobit.fiestavocales.utils.Tools;
import mx.betobit.fiestavocales.utils.WordGenerator;

/**
 * Created by jesusmartinez on 31/10/16.
 */
public class PlayScreen extends BaseScreen {

	// UI
	private Sprite background;
	private Hud hud;

	// World
	private int width;
	private int height;
	private World world;
	private RayHandler rayHandler;

	// Bodies
	private ArrayList<Balloon> balloons;
	private ArrayList<Light> lights;
	private boolean newBalloon;

	// Debugging
	private Box2DDebugRenderer b2dr;
	private ShapeRenderer shapeRenderer;

	// Others
	private Sound popSound;
	private Socket socket;
	private String id;

	/**
	 * Constructor
	 */
	public PlayScreen(FiestaDeLasVocales game) {
		super(game, 800, 480);
		width = getWidth();
		height= getHeight();

		balloons = new ArrayList<Balloon>();
		lights = new ArrayList<Light>();
		b2dr = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();
		popSound = Gdx.audio.newSound(Gdx.files.internal("sounds/pop.mp3"));

		connectSocket();
		configSocketEvents();
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
		world = new World(new Vector2(0, 4.9f), true);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.7f);

		newBalloon = false;
		for(int i = 0; i < 15; i++) {
			Balloon b = new Balloon(this,
					Tools.getRandomColor(),
					MathUtils.random(0, width), MathUtils.random(0, height));

			balloons.add(b);
			attachLightToBody(b.getBody(), b.getColor(), 90);
		}
		hud = new Hud(getViewport());
	}


	/**
	 * Add balloon with light.
	 */
	private void addBalloon() {
		Balloon b = new Balloon(this,
				Tools.getRandomColor(),
				MathUtils.random(0, width), -50); // Position

		balloons.add(b);
		attachLightToBody(b.getBody(), b.getColor(), 90);
	}

	/**a
	 * Attach a Point light to the given body.
	 *
	 * @param body     The body to attach the light.
	 * @param color    Color of the light.
	 * @param distance Distance of the light.
	 */
	private void attachLightToBody(Body body, Color color, int distance) {
		PointLight light = new PointLight(rayHandler, 10, color, distance, width / 2, height / 2);
		light.attachToBody(body);
		lights.add(light);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		batch.begin();
		background.draw(batch);
		batch.end();

		hud.update(delta);
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		rayHandler.setCombinedMatrix(getCamera());
		rayHandler.updateAndRender();

		if (Constants.DEBUGGING) {
			b2dr.render(world, getCamera().combined);
		}

		// Render balloons
		renderBalloons(delta);

		if(newBalloon) {
			addBalloon();
			newBalloon = false;
		}
	}

	/**
	 * Render balloon and destroy it when is out of the screen.
	 * Iterate over the lights.
	 *
	 * @param delta Delta time
	 */
	private void renderBalloons(float delta) {
		final ListIterator iterator = lights.listIterator();
		while (iterator.hasNext()) {
			final Balloon b = balloons.get(iterator.nextIndex());
			final Light l = (Light)iterator.next();

			if (b.getY() > height - 20) {
				deleteBallon(b, l);
				iterator.remove();
			}
			b.update(delta);
			b.onTap(new Balloon.OnTapListener() {
				@Override
				public void onTap() {
					popSound.play(0.5f);
					Hud.addScore1(20);
					deleteBallon(b, l);
					iterator.remove();
				}
			});

			if(Constants.DEBUGGING) {
				shapeRenderer.setProjectionMatrix(getCamera().combined);
				shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
				shapeRenderer.setColor(Color.FOREST);
				shapeRenderer.rect(b.getX() - 5, b.getY() + 60, 60, 80);
				shapeRenderer.end();
			}
		}
	}

	/**
	 * Delete body and light from world. Set flag newBalloon to true.
	 * @param balloon
	 * @param light
	 */
	private void deleteBallon(Balloon balloon, Light light) {
		world.destroyBody(balloon.getBody()); // Remove body from world
		balloons.remove(balloon); // Remove balloon from list
		light.remove(); // Remove light from world
		newBalloon = true;
	}

	@Override
	public void dispose() {
		rayHandler.dispose();
		b2dr.dispose();
		for(Balloon b : balloons)
			b.getSpriteSheet().dispose();
	}

	/**
	 * Get world of the screen
	 * @return
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Connect to socket
	 */
	public void connectSocket(){
		try {
			socket = IO.socket("http://localhost:8080");
			socket.connect();
		} catch(Exception e){
			System.out.println(e);
		}
	}

	/**
	 * Socket magic
	 */
	public void configSocketEvents(){
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Connected");
			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					id = data.getString("id");
					Gdx.app.log("SocketIO", "My ID: " + id);
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting ID");
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					id = data.getString("id");
					Gdx.app.log("SocketIO", "New Player Connect: " + id);
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting New PlayerID");
				}
			}
		}).on("playerDisconnected", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
			}
		}).on("getPlayers", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				/*JSONArray objects = (JSONArray) args[0];
				try {
					for (int i = 0; i < objects.length(); i++) {
						Starship coopPlayer = new Starship(friendlyShip);
						Vector2 position = new Vector2();
						position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
						position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
						coopPlayer.setPosition(position.x, position.y);

						friendlyPlayers.put(objects.getJSONObject(i).getString("id"), coopPlayer);
					}
				} catch (JSONException e) {

				}*/
			}
		});
	}
}
