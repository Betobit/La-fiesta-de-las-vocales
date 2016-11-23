
var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];

// Run localhost on port 8080
server.listen(8080, function() {
	console.log("Server is now running...");
});

/******************************
      C O N T R O L L E R
******************************/
io.on('connection', function(socket) {
	console.log("Player Connected!");

	// Emitters
	socket.emit('socketId', { id: socket.id });
	socket.emit('getPlayers', players);

	// Receivers
	socket.on('disconnect', function(){
		console.log("Player disconnected");
		socket.broadcast.emit('playerDisconnected', { id: socket.id });
		players = [];
	});

	/*socket.on('newBalloon', function (data) {
		console.log("New balloon! " + JSON.stringify(data));
		balloons.push(new Balloon(data.color, data.x, data.y));
	});*/

	socket.on('balloonTouched', function (data) {
		data.id = socket.id;
		socket.broadcast.emit("balloonTouched", data);

		var len = players.length;
		for(var i = 0; i < len; i++) {
			if(data.id == players[i].id) {
				players[i]["score"] = data.score;
			}
		}
	});

	players.push(new Player(socket.id, 0));
});

/******************************
	      M O D E L
******************************/
function Player(id, score) {
	this.id = id;
	this.score = score;
}
