package mx.betobit.fiestavocales.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Random;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import mx.betobit.fiestavocales.FiestaDeLasVocales;

/**
 * Created by jesusmartinez on 31/10/16.
 */
public class PlayScreen extends BaseScreen {

	// World
	private int width;
	private int height;
	private World world;
	private RayHandler rayHandler;

	// Bodies
	private ArrayList<Body> balloons;

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

		balloons = new ArrayList<Body>();
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
			bdef.position.set(ran.nextInt(width) , ran.nextInt(height));

			body = world.createBody(bdef);
			shape.setRadius(7f);
			fdef.shape = shape;
			fdef.density = 0f;
			fdef.restitution = 0f;
			body.createFixture(fdef);

			body.setLinearVelocity(10, 5);
			bodies.add(body);
		}
		/*for (MapObject object : tiledMap.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = bodyType;
			bdef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));

			body = world.createBody(bdef);
			shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
			fdef.shape = shape;
			body.createFixture(fdef);

			bodies.add(body);
		}*/

		return bodies;
	}

	@Override
	public void show() {
		world = new World(new Vector2(0, 0), true);
		// Set light world.
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.7f);

		balloons = createBodies(1, BodyDef.BodyType.DynamicBody);

		for (Body b : balloons) {
			PointLight light = new PointLight(rayHandler, 200, Color.RED, 80, b.getPosition().x, height / 2);
			light.attachToBody(b);
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

		b2dr.render(world, getCamera().combined);
		rayHandler.setCombinedMatrix(getCamera());
		rayHandler.updateAndRender();

	}

	@Override
	public void dispose() {
	}
}
