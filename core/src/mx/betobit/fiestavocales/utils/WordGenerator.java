package mx.betobit.fiestavocales.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

/**
 * Created by jesusmartinez on 21/11/16.
 */

public class WordGenerator {

	private static ArrayList<Word> WORDS;

	public WordGenerator() {
		/*FileHandle handle = ;
		JsonReader jsonReader = new JsonReader();
		JsonValue wordsJSON = jsonReader.parse(handle).child();*/
		WORDS = new ArrayList<Word>();
		Json json= new Json();
		ArrayList<JsonValue> list = json.fromJson(ArrayList.class,
				Gdx.files.external("data/words.json"));

		for (JsonValue v : list) {
			WORDS.add(json.readValue(Word.class, v));
		}
	}

	public Word getWord(int wordId) {
		for(Word w : WORDS) {
			if(w.getId() == wordId)
				return w;
		}
		return WORDS.get(MathUtils.random(WORDS.size() - 1));
	}
}
