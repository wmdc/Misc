/* Asteroids */

var canvas = document.getElementById("canvas").getContext("2d");

setInterval(update, 16);

var keys = {
    up: false, 
    down: false, 
    left: false, 
    right: false,

    update: function (event) {
	var kd = (event.type === "keydown");

	switch (event.keyCode) {
	case 38:
	    keys.up = kd;
	case 40:
	    keys.down = kd;
	case 37:
	    keys.left = kd;
	case 39:
	    keys.right = kd;
	}
    }
};

window.addEventListener('keydown', keys.update, false);
window.addEventListener('keyup', keys.update, false);

var ship = {
    x: 200,
    y: 100,
    dx: 0,
    dy: 0,

    accel: 0.5,
    size: 10,

    update: function (keys) {
	if(keys.left) {
	    this.dx -= this.accel;
	}

	if(keys.right) {
	    this.dx += this.accel;
	}

	if(keys.up) {
	    this.dy -= this.accel;
	}

	if(keys.down) {
	    this.dy += this.accel;
	}

/*	if(keys.left) {
	    this.dx = -this.speed;
	} else if(keys.right) {
	    this.dx = this.speed;
	} else if(!keys.left && !keys.right) {
	    this.dx = 0;
	}

	if(keys.up) {
	    this.dy = -this.speed;
	} else if(keys.down) {
	    this.dy = this.speed;
	} else if(!keys.up && !keys.down) {
	    this.dy = 0;
	}*/

	this.x += this.dx;
	this.y += this.dy;
    },

    draw: function() { circle(this.x, this.y, this.size); },

    clear: function () { circle(this.x, this.y, this.size + 2); }
};

var asteroids = {
    count: 0
};

function circle(x,y,r) {
  canvas.beginPath();
  canvas.arc(x, y, r, 0, Math.PI*2, true);
  canvas.fill();
}

function rect(x,y,w,h) {
  canvas.beginPath();
  canvas.rect(x,y,w,h);
  canvas.closePath();
  canvas.fill();
}

function clear() {
  //canvas.clearRect(0, 0, 600, 400);
}

function update() {
    canvas.fillStyle = "#FFFFFF";
    ship.clear();

    ship.update(keys);

    canvas.fillStyle = "#444444";
    ship.draw();
}

document.write("_");