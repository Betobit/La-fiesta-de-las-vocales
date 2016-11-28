
var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
var balloons= [];
var ready = false;

// Run localhost on port 8080
server.listen(8080, function() {
	console.log("Server is now running...");
});

/******************************
      C O N T R O L L E R
******************************/
io.on('connection', function(socket) {
	console.log("Player Connected!");
	players.push(new Player(socket.id, 0));

	// Emitters
	socket.emit('socketId', { id: socket.id });
	socket.emit('getPlayers', players);
	socket.emit('startGame', { players: players.length });
	socket.broadcast.emit('startGame', { players: players.length });
	socket.broadcast.emit('newPlayer', { });

	socket.on('newBalloon', function(balloon) {
		var founded = false;
		for(var i = 0; i < balloons.length; i++){
			console.log(balloons[i]);
			if(balloons[i].id == balloon.id){
				founded = true;
				return;
			}
		}

		if(!founded) {
			balloons.push(new Balloon(balloon));
			socket.broadcast.emit('getBalloons', balloons);
		}
	});

	socket.on('disconnect', function(){
		console.log("Player disconnected");
		socket.broadcast.emit('playerDisconnected', { id: socket.id });
		balloons = [];
		for(var i = 0; i < players.length; i++){
			if(players[i].id == socket.id){
				players.splice(i, 1);
			}
		}
	});

	socket.on('updateScore', function (data) {
		data.id = socket.id;
		socket.broadcast.emit("updateScore", data);

		var len = players.length;
		for(var i = 0; i < len; i++) {
			if(data.id == players[i].id) {
				players[i]["score"] = data.score;
			}
		}
	});

	socket.on("deleteBalloon", function(balloon) {
		//socket.emit('deleteBalloon', { id: balloon.id });
		socket.broadcast.emit('deleteBalloon', { id: balloon.id });
		for(var i = 0; i < balloons.length; i++) {
			console.log("deleteBalloon " + JSON.stringify(balloons[i]))
			if(balloons[i].id == balloon.id){
				balloons.splice(i, 1);
			}
		}
		socket.broadcast.emit('getBalloons', balloons);
	});
});

/******************************
	      M O D E L
******************************/
function Player(id, score) {
	this.id = id;
	this.score = score;
}

function Balloon(balloon) {
	this.id = balloon.id;
	this.color = balloon.color;
	this.x = balloon.x;
	this.y = balloon.y;
	this.wordId = balloon.wordId;
}
