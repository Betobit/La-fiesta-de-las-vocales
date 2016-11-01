package mx.betobit.fiestavocales.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import mx.betobit.fiestavocales.FiestaDeLasVocales;

/**
 * Created by jesusmartinez on 31/10/16.
 * Represents one of many application screens, such as a main menu, a settings menu, the game screen and so on.
 */
public abstract class BaseScreen implements Screen {

	private FiestaDeLasVocales game;
	private Viewport viewport;
	private OrthographicCamera camera;
	private int width;
	private static int height;

	protected SpriteBatch batch;

	/**
	 * Constructor
	 */
	public BaseScreen(FiestaDeLasVocales heroesOfAnzu, int width, int height) {
		game = heroesOfAnzu;
		batch = game.getBatch();
		this.width = width;
		this.height= height;
		camera  = new OrthographicCamera();
		viewport = new FillViewport(width, height, camera);
	}

	/**
	 * Return the camera of the screen.
	 */
	public OrthographicCamera getCamera() {
		return camera;
	}

	/**
	 * Return the viewport.pls
	 */
	public Viewport getViewport() {
		return viewport;
	}

	/**
	 * Return the viewport.
	 */
	public FiestaDeLasVocales getGame() {
		return game;
	}

	/**
	 * Return the viewport width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Return the viewport height.
	 */
	public int getHeight() {
		return height;
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void show() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}