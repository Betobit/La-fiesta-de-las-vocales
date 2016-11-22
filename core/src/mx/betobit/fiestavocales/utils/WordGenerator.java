package mx.betobit.fiestavocales.utils;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by jesusmartinez on 21/11/16.
 */

public class WordGenerator {

	private static final Word[] WORDS = {
		new Word("Hiato", false, true),
		new Word("Aula", false, true),
		new Word("Virtual", false, true),
		new Word("Bolillo", false, true),
		new Word("Momento", false, true),
		new Word("Zoológico", false, true),
		new Word("Día", false, true),
		new Word("Río", false, true),
		new Word("País", false, true),
		new Word("Biología", false, true),
		new Word("Maíz", false, true),
		new Word("Creído", false, true),
		new Word("Leí", false, true),
		new Word("Geografía", false, true),
		new Word("Tío", false, true),
		new Word("Oído", false, true),
		new Word("Oír", false, true),
		new Word("Búho", false, true),
		new Word("Héroe", false, true)
	};

	public static Word getWord()
	{
		return WORDS[MathUtils.random(WORDS.length - 1)];
	}
}
