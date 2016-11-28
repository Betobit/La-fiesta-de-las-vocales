package mx.betobit.fiestavocales.socket;

import org.json.JSONArray;

/**
 * Created by jesusmartinez on 28/11/16.
 */

public interface SocketInterface {

	void onStartGame();
	void onNewPlayer();
	void onUpdateScore(Boolean player1, int score);
	void onGetPlayers(Boolean player1, int score);
	void onGetBalloons(JSONArray jsonBalloons);
	void onDeleteBalloon(String id);
}
