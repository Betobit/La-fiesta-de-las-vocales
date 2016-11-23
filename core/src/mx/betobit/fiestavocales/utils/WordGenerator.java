package mx.betobit.fiestavocales.utils;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by jesusmartinez on 21/11/16.
 */

public class WordGenerator {

	private static final Word[] WORDS = {
		// Hiatus
		// http://www.ejemplode.com/12-clases_de_espanol/49-ejemplo_de_hiato.html
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
		new Word("Héroe", false, true),
		new Word("Caer", false, true),
		new Word("Línea", false, true),
		new Word("Museo", false, true),
		new Word("Teatro", false, true),

		// Diphthong
		// http://www.ejemplode.com/12-clases_de_espanol/41-ejemplo_de_diptongos.html
		new Word("Comedia", true, false),
		new Word("Tierra", true, false),
		new Word("Piojo", true, false),
		new Word("Fuego", true, false),
		new Word("Residuo", true, false),
		new Word("Pausa", true, false),
		new Word("Peine", true, false),
		new Word("Baile", true, false),
		new Word("Laura", true, false),
		new Word("Paisaje", true, false),
		new Word("Deuda", true, false),
		new Word("Piano", true, false),
		new Word("Agua", true, false),
		new Word("Prueba", true, false),
		new Word("Nuevo", true, false),
		new Word("Hueso", true, false),
		new Word("Suave", true, false),
		new Word("Ruido", true, false),

		// Other
		new Word("Bolillo", false, false),
		new Word("Momento", false, false),
		new Word("Lámpara", false, false),
		new Word("Carpeta", false, false),
		new Word("Celular", false, false),
		new Word("Teclado", false, false),
		new Word("Regla", false, false),
		new Word("Borrador", false, false)
	};

	public static Word getWord()
	{
		return WORDS[MathUtils.random(WORDS.length - 1)];
	}
}
