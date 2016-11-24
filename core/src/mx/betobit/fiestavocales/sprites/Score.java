package mx.betobit.fiestavocales.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by jesusmartinez on 24/11/16.
 */
public class Score {

	private char status;
	private float timer;

	private Label label;
	private String name;
	private int points;

	public Score(String name) {
		points = 0;
		timer = 0f;
		status = 'u'; // undefined
		this.name = name;
		BitmapFont customFont = new BitmapFont(Gdx.files.internal("font/font.fnt"));
		label = new Label(String.format("%s: %04d ", name, points),
				new Label.LabelStyle(customFont, Color.WHITE));
	}
	/**
	 * Add points to the current score
	 * @param value points
	 */
	public void addPoints(int value){
		if(value > 0)
			status = 'c'; // correct
		else if( value < 0)
			status = 'i'; // incorrect

		// Avoid negative points
		if(points + value >= 0)
			points += value;

		label.setText(String.format("%s: %04d ", name, points));
	}


	public void update(float delta) {
		timer += delta;

		if(timer > 2) {
			timer -= 2;
			status = 'u';
		}

		switch (status) {
			case 'u':
				label.setColor(Color.WHITE);
				break;
			case 'c':
				label.setColor(Color.GOLD);
				break;
			case 'e':
				label.setColor(Color.BLUE);
				break;
			case 'i':
				label.setColor(Color.RED);
				break;
		}
	}

	/**
	 * Get int points
	 * @return
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Get label
	 * @return
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * Set int points
	 * @return
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * Set label
	 * @return
	 */
	public void setLabel(int points) {
		this.points = points;
		status = 'e'; // extern

		label.setText(String.format("%s: %04d ", name, this.points));
	}
}
