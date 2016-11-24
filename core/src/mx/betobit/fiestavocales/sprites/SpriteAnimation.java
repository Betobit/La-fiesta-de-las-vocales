package mx.betobit.fiestavocales.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import mx.betobit.fiestavocales.screens.PlayScreen;

/**
 * Created by jesusmartinez on 23/11/16.
 */

public abstract class SpriteAnimation extends Sprite {

	private PlayScreen screen;

	private Texture spriteSheet;
	private TextureRegion textureRegion;
	private Animation animation;
	private int width;
	private int height;

	protected float duration;
	protected SpriteBatch batch;

	public SpriteAnimation(PlayScreen playScreen, int width, int height, float x, float y) {
		duration = 0;
		this.screen = playScreen;
		this.batch = screen.getGame().getBatch();
		this.width = width;
		this.height= height;

		setPosition(x, y);
	}


	/**
	 * Get the frame of the animation and draw it.
	 * @param delta Delta time
	 */
	public void update(float delta) {
		duration += delta;
		TextureRegion frame = animation.getKeyFrame(duration, true);

		batch.begin();
		batch.draw(frame, getX(), getY(), width, height);
		batch.end();
	}


	/**
	 * Get the sprite sheet of the sprite, split it and create the animation.
	 */
	protected abstract void defineSpriteSheet();

	/**
	 * Get sprite
	 * @return
	 */
	public Texture getSpriteSheet() {
		return spriteSheet;
	}

	/**
	 * Get animation
	 * @return
	 */
	public Animation getAnimation() {
		return animation;
	}

	/**
	 * Get texture region
	 * @return
	 */
	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

	/**
	 * Get player screen instance
	 * @return
	 */
	public PlayScreen getScreen() {
		return screen;
	}

	/**
	 * Set animation
	 * @return
	 */
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	/**
	 * Set sprite sheet
	 * @return
	 */
	public void setSpriteSheet(Texture spriteSheet) {
		this.spriteSheet = spriteSheet;
	}

	/**
	 * Set texture region
	 * @return
	 */
	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
}
