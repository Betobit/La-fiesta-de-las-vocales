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

import mx.betobit.fiestavocales.screens.PlayScreen;
import mx.betobit.fiestavocales.utils.Constants;
import mx.betobit.fiestavocales.utils.Word;
import mx.betobit.fiestavocales.utils.WordGenerator;

/**
 * Created by jesusmartinez on 21/11/16.
 */

public class Balloon extends SpriteAnimation {

	private World world;
	private Body body;

	private Word word;
	private BitmapFont customFont;

	public Balloon(PlayScreen playScreen, Color color, float x, float y) {
		super(playScreen, 50, 120, x, y);
		this.word = WordGenerator.getWord();
		this.world = playScreen.getWorld();
		this.batch = playScreen.getGame().getBatch();

		customFont = new BitmapFont(Gdx.files.internal("font/regular.fnt"));
		customFont.getData().setScale(0.85f);
		customFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		setColor(color);
		defineBox2dBody();
		defineSpriteSheet();
	}


	/**
	 * Get the frame of the animation and draw it. Also draw the word label and set bounds
	 * @param delta Delta time
	 */
	public void update(float delta) {
		TextureRegion frame = getAnimation().getKeyFrame(duration, true);

		// Center sprite in body
		setX(body.getPosition().x - 25);
		setY(body.getPosition().y - 95);
		setBounds(getX(), getY(), 60, 80);

		batch.begin();
		batch.draw(frame, getX(), getY(), 50, 120);
		customFont.draw(batch, word.getLabel(),
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

		body = world.createBody(bdef);
		shape.setRadius(25f);
		fdef.shape = shape;
		fdef.density = 1f;
		fdef.restitution = 0f;
		body.createFixture(fdef);

		body.setLinearVelocity(MathUtils.random(-5, 5), MathUtils.random(0, 20) );
	}

	/**
	 * Get the sprite sheet of the balloon, split it and create the animation.
	 */
	protected void defineSpriteSheet() {
		String color = getColor().toString();

		if(color.contains(Constants.SCARLET.substring(0,6)))
			setSpriteSheet(new Texture("balloons_pink.png"));
		else if(color.contains(Constants.CYAN.substring(0,6)))
			setSpriteSheet(new Texture("balloons_blue.png"));
		else
			setSpriteSheet(new Texture("balloons_green.png"));

		setTextureRegion(new TextureRegion(getSpriteSheet(), 57, 458));

		TextureRegion[][] splited = getTextureRegion().split(57, 152);
		TextureRegion[] frames = new TextureRegion[3];

		for (int i = 0; i < 3; i++)
			frames[i] = splited[i][0];
		setAnimation(new Animation(0.15f, frames));
	}

	/**
	 * Get box2d body
	 * @return
	 */
	public Body getBody() {
		return body;
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

	public interface OnTapListener {
		void onTap();
	}
}
