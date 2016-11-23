package mx.betobit.fiestavocales.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;;

/**
 * Created by jesusmartinez on 22/11/16.
 */

public class Tools {

	private static final Color[] colors = {
			Color.SCARLET, Color.CYAN, Color.GREEN
	};

	public static Color getRandomColor() {
		return colors[MathUtils.random(colors.length - 1)];
	}

}
