package mx.betobit.fiestavocales.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import mx.betobit.fiestavocales.Constants;
import mx.betobit.fiestavocales.FiestaDeLasVocales;
import mx.betobit.fiestavocales.sprites.Balloon;

/**
 * Created by jesusmartinez on 31/10/16.
 */
public class PlayScreen extends BaseScreen {

	private Sprite background;

	// World
	private int width;
	private int height;
	private World world;
	private RayHandler rayHandler;

	// Bodies
	private ArrayList<Balloon> balloons;
	private ArrayList<Light> lights;

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
	 * Create box2d bodies.
	 *
	 * @param index    The layer to draw.
	 * @param bodyType Type of the body to draw.
	 * @return ArrayList of created bodies.
	 */
	private ArrayList<Body> createBodies(int index, BodyDef.BodyType bodyType) {
		Body body;
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		ArrayList<Body> bodies = new ArrayList<Body>();
		Random ran = new Random();

		for(int i = 0; i < 10; i++) {
			bdef.type = bodyType;
			bdef.position.set(ran.nextInt(width) , ran.nextInt(100));

			body = world.createBody(bdef);
			shape.setRadius(20f);
			fdef.shape = shape;
			fdef.density = 1f;
			fdef.restitution = 0f;
			body.createFixture(fdef);

			body.setLinearVelocity(0, ran.nextFloat() * -5);
			bodies.add(body);
		}

		shape.dispose();
		return bodies;
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
		world = new World(new Vector2(0, 0f), true);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.7f);

		for(int i = 0; i < 15; i++)
			balloons.add(new Balloon(this, MathUtils.random(0, width), MathUtils.random(0, height/2)));

		// Attach lights
		for (Balloon b : balloons) {
			PointLight light = new PointLight(rayHandler, 200, Color.PURPLE, 80, b.getBody().getPosition().x, height / 2);
			light.attachToBody(b.getBody());
			lights.add(light);
		}
	}

	/**
	 * Attach a Point light to the given body.
	 *
	 * @param body     The body to attach the light.
	 * @param color    Color of the light.
	 * @param distance Distance of the light.
	 */
	private void attachLightToBody(Body body, Color color, int distance) {
		new PointLight(rayHandler, 200, color, distance, width / 2, height / 2)
				.attachToBody(body);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		batch.begin();
		background.draw(batch);
		batch.end();

		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		rayHandler.setCombinedMatrix(getCamera());
		rayHandler.updateAndRender();

		if (Constants.DEBUGGING) {
			b2dr.render(world, getCamera().combined);
		}

		// Render balloon and destroy it if is out.
		/*ListIterator iterator = balloons.listIterator();
		while (iterator.hasNext()) {
			Balloon b = (Balloon)iterator.next();

			if (b.getBody().getPosition().y > height/2) {
				world.destroyBody(b.getBody());
				//lights.get(iterator.nextIndex()).remove();
				iterator.remove();
			}
			b.update(delta);
		}*/

		ListIterator iterator = lights.listIterator();
		while (iterator.hasNext()) {
			Balloon b = balloons.get(iterator.nextIndex());
			Light l = (Light)iterator.next();

			if (b.getY() > height) {
				world.destroyBody(b.getBody()); // Remove body from world
				balloons.remove(b); // Remove balloon from list
				l.remove(); // Remove light from world
				iterator.remove(); // Remove light from list
			}
			b.update(delta);
		}
	}

	@Override
	public void dispose() {
	}

	public World getWorld() {
		return world;
	}
}
