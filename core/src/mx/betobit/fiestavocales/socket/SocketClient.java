package mx.betobit.fiestavocales.socket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import mx.betobit.fiestavocales.sprites.Balloon;
import mx.betobit.fiestavocales.utils.Constants;

/**
 * Created by jesusmartinez on 28/11/16.
 */

public class SocketClient {

	public Socket socket;
	public String playerId;
	public SocketInterface socketInterface;

	/**
	 * Constructor.
	 * Connect to server
	 */
	public SocketClient() {
		try {
			socket = IO.socket(Constants.SERVER_IP);
			socket.connect();
		} catch(Exception e){
			System.out.println(e);
		}

		configSocketEvents();
	}

	/**
	 * Set socket events
	 */
	public void setSocketInterface(SocketInterface socketInterface) {
		this.socketInterface = socketInterface;
	}

	/**
	 * Socket communication
	 */
	public void configSocketEvents(){
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
			}
		}).on("socketId", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					playerId = data.getString("id");
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error: " + e.getMessage());
				}
			}
		}).on("startGame", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject response = (JSONObject) args[0];
				try {
					if(response.getInt("players") > 1) {
						socketInterface.onStartGame();
					}
				} catch (JSONException e) {

				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				socketInterface.onNewPlayer();
			}
		}).on("updateScore", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject player = (JSONObject) args[0];

				try {
					if(playerId.equals(player.getString("id")))
						socketInterface.onUpdateScore(true, player.getInt("score"));
					else
						socketInterface.onUpdateScore(false, player.getInt("score"));
				} catch (JSONException e) {

				}
			}
		}).on("getPlayers", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONArray jsonPlayers = (JSONArray) args[0];
				try {
					for(int i = 0; i < jsonPlayers.length(); i++) {
						JSONObject player = jsonPlayers.getJSONObject(i);
						if(playerId.equals(player.getString("id")))
							socketInterface.onGetPlayers(true, player.getInt("score"));
						else
							socketInterface.onGetPlayers(true, player.getInt("score"));
					}
				} catch (JSONException e) {

				}
			}
		}).on("getBalloons", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONArray jsonBalloons = (JSONArray) args[0];
				socketInterface.onGetBalloons(jsonBalloons);
			}
		}).on("deleteBalloon", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject jsonBalloon = (JSONObject) args[0];
				try {
					socketInterface.onDeleteBalloon(jsonBalloon.getString("id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
