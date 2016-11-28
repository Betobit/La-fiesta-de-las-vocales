package mx.betobit.fiestavocales.socket;

import com.badlogic.gdx.Gdx;

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
	 * Send balloon to server
	 * @param b Balloon
	 */
	public void emitBalloon(Balloon b) {
		try {
			JSONObject json = new JSONObject();
			json.put("id", b.getId());
			json.put("color", "#" + b.getColor().toString());
			json.put("wordId", b.getWord().getId());
			json.put("x", b.getBody().getPosition().x);
			json.put("y", b.getBody().getPosition().y);
			socket.emit("newBalloon", json);
		} catch (JSONException e) {
			Gdx.app.log("Error", "Error creating new balloon json");
		}
	}

	public void emitDeleteBalloon(Balloon balloon) {
		JSONObject json = new JSONObject();
		try {
			json.put("id", balloon.getId());
			socket.emit("deleteBalloon", json);
		} catch (JSONException e) {
			Gdx.app.log("Error", "Error sending new score");
		}
	}

	/**
	 * Send player score to server.
	 * @param score
	 */
	public void emitScore(int score) {
		JSONObject json = new JSONObject();
		try {
			json.put("id", playerId);
			json.put("score", score);
			socket.emit("updateScore", json);
		} catch (JSONException e) {
			Gdx.app.log("Error", "Error sending new score");
		}
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
