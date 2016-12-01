package mx.betobit.fiestavocales.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import org.json.JSONException;
import org.json.JSONObject;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import mx.betobit.fiestavocales.screens.PlayScreen;
import mx.betobit.fiestavocales.utils.BalloonHelper;
import mx.betobit.fiestavocales.utils.Constants;
import mx.betobit.fiestavocales.utils.Word;
import mx.betobit.fiestavocales.utils.WordGenerator;

/**
 * Created by jesusmartinez on 21/11/16.
 */

public class Balloon extends SpriteAnimation {

	private Body body;
	private Light light;

	private String id;
	private Word word;

	private static WordGenerator wordGenerator;

	public Balloon(String id, Texture spriteSheet, int wordId, Color color, float x, float y) {
		super(BalloonHelper.getScreen(), 50, 120, x, y);
		wordGenerator = new WordGenerator();
		word = wordGenerator.getWord(wordId);
		this.batch = BalloonHelper.getScreen().getGame().getBatch();
		this.id = id;

		setSpriteSheet(spriteSheet);
		setColor(color);
		defineBox2dBody();
		setTextureRegion(new TextureRegion(spriteSheet, 57, 458));

		TextureRegion[][] splited = getTextureRegion().split(57, 152);
		TextureRegion[] frames = new TextureRegion[3];

		for (int i = 0; i < 3; i++)
			frames[i] = splited[i][0];
		setAnimation(new Animation(0.15f, frames));
		attachLightToBody();
	}

	/**
	 * Attach a Point light to the given body.
	 */
	private void attachLightToBody() {
		light = new PointLight(BalloonHelper.getRayHandler(), 10, getColor(), 90, 20, 30);
		light.attachToBody(body);
	}

	/**
	 * Get the frame of the animation and draw it. Also draw the word label and set bounds
	 * @param delta Delta time
	 */
	public void update(float delta) {
		TextureRegion frame = getAnimation().getKeyFrame(duration, true);

		float x = body.getPosition().x;
		float y = body.getPosition().y;

		body.setTransform(x, y + 0.8f, body.getAngle());

		// Center sprite in body
		setX(body.getPosition().x - 25);
		setY(body.getPosition().y - 95);
		setBounds(getX(), getY(), 60, 80);

		batch.begin();
		batch.draw(frame, getX(), getY(), 50, 120);
		BalloonHelper.getCustomFont().draw(batch, word.getLabel(),
				getX() + 25 - word.getLabel().length()/2*12,
				getY() + 70);
		batch.end();
	}

	/**
	 * Define box2d body for the balloon.
	 */
	private void defineBox2dBody() {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();

		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.position.set(getX(), getY());

		body = BalloonHelper.getScreen().getWorld().createBody(bdef);
		shape.setRadius(1f);
		fdef.shape = shape;
		fdef.density = 1f;
		fdef.restitution = 0f;
		body.createFixture(fdef);

		//body.setLinearVelocity(MathUtils.random(-5, 5), MathUtils.random(0, 20) );
	}

	/**
	 * Get the sprite sheet of the balloon, split it and create the animation.
	 */
	protected void defineSpriteSheet() {
	}

	/**
	 * Get id of the balloon
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get box2d body
	 * @return
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * Get word attached to body
	 * @return
	 */
	public Word getWord() {
		return word;
	}

	/**
	 * Detect tap or click in sprite custom bounds
	 */
	public void onTap(OnTapListener listener) {
		if(Gdx.input.justTouched()) {
			int x1 = Gdx.input.getX() - 5;
			int y1 = Gdx.input.getY() + 60;
			Vector3 input = new Vector3(x1, y1, 0);
			getScreen().getCamera().unproject(input);
			if (getBoundingRectangle().contains(input.x, input.y)) {
				listener.onTap();
			}
		}
	}

	/**
	 * Custom interface to manage tap listener on body
	 */
	public interface OnTapListener {
		void onTap();
	}

	/**
	 * Remove light from world
	 */
	public void removeLight() {
		if(light != null)
			light.remove();
	}
}
