package mx.betobit.fiestavocales.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.ListIterator;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import mx.betobit.fiestavocales.Constants;
import mx.betobit.fiestavocales.FiestaDeLasVocales;
import mx.betobit.fiestavocales.scenes.Hud;
import mx.betobit.fiestavocales.sprites.Balloon;

/**
 * Created by jesusmartinez on 31/10/16.
 */
public class PlayScreen extends BaseScreen {

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
					MathUtils.randomBoolean() ? Color.CYAN : Color.SCARLET,
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
				MathUtils.randomBoolean() ? Color.CYAN : Color.SCARLET,
				MathUtils.random(0, width), -30);

		balloons.add(b);
		attachLightToBody(b.getBody(), b.getColor(), 90);
	}

	/**
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

		// Render balloon and destroy it when is out of the screen.
		ListIterator iterator = lights.listIterator();
		while (iterator.hasNext()) {
			Balloon b = balloons.get(iterator.nextIndex());
			Light l = (Light)iterator.next();

			if (b.getY() > height) {
				world.destroyBody(b.getBody()); // Remove body from world
				balloons.remove(b); // Remove balloon from list
				l.remove(); // Remove light from world
				iterator.remove(); // Remove light from list
				newBalloon = true;
			}
			b.update(delta);
		}

		if(newBalloon) {
			Hud.addScore(20);
			addBalloon();
			newBalloon = false;
		}
	}

	@Override
	public void dispose() {
		rayHandler.dispose();
		b2dr.dispose();
		for(Balloon b : balloons)
			b.getSpriteSheet().dispose();
	}

	public World getWorld() {
		return world;
	}
}
