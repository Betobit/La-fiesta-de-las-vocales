package mx.betobit.fiestavocales.utils;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by jesusmartinez on 21/11/16.
 */

public class WordGenerator {

	private static final Word[] WORDS = {
		// Hiatus
		// http://www.ejemplode.com/12-clases_de_espanol/49-ejemplo_de_hiato.html
		new Word(1, "Zoológico", false, true),
		new Word(2, "Día", false, true),
		new Word(3, "Río", false, true),
		new Word(4, "País", false, true),
		new Word(5, "Biología", false, true),
		new Word(6, "Maíz", false, true),
		new Word(7, "Creído", false, true),
		new Word(8, "Leí", false, true),
		new Word(9, "Geografía", false, true),
		new Word(10, "Tío", false, true),
		new Word(11, "Oído", false, true),
		new Word(12, "Oír", false, true),
		new Word(13, "Búho", false, true),
		new Word(14, "Héroe", false, true),
		new Word(15, "Caer", false, true),
		new Word(16, "Línea", false, true),
		new Word(17, "Museo", false, true),
		new Word(18, "Teatro", false, true),

		// Diphthong
		// http://www.ejemplode.com/12-clases_de_espanol/41-ejemplo_de_diptongos.html
		new Word(19, "Comedia", true, false),
		new Word(20, "Tierra", true, false),
		new Word(21, "Piojo", true, false),
		new Word(22, "Fuego", true, false),
		new Word(23, "Residuo", true, false),
		new Word(24, "Pausa", true, false),
		new Word(25, "Peine", true, false),
		new Word(26, "Baile", true, false),
		new Word(27, "Laura", true, false),
		new Word(28, "Paisaje", true, false),
		new Word(29, "Deuda", true, false),
		new Word(30, "Piano", true, false),
		new Word(31, "Agua", true, false),
		new Word(32, "Prueba", true, false),
		new Word(33, "Nuevo", true, false),
		new Word(34, "Hueso", true, false),
		new Word(35, "Suave", true, false),
		new Word(36, "Ruido", true, false),

		// Other
		new Word(37, "Bolillo", false, false),
		new Word(38, "Momento", false, false),
		new Word(39, "Lámpara", false, false),
		new Word(40, "Carpeta", false, false),
		new Word(41, "Celular", false, false),
		new Word(42, "Teclado", false, false),
		new Word(43, "Regla", false, false),
		new Word(44, "Borrador", false, false)
	};

	public static Word getWord(int wordId)
	{
		for(int i = 0; i < WORDS.length; i++) {
			if(WORDS[i].getId() == wordId)
				return WORDS[i];
		}
		return WORDS[MathUtils.random(WORDS.length - 1)];
	}
}
