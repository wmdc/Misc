/*
 * Asteroids
 * by Michael Welsman-Dinelle */

var canvas = document.getElementById("canvas").getContext("2d");

setInterval(draw, 16);
window.addEventListener('keydown',doKeyDown,true);

var keys = {
    up: false,
    down: false,
    left: false,
    right: false
};

function doKeyDown(event){
    switch (event.keyCode) {
    case 38:
	keys.up = true;
	break;
    case 40:
	keys.down = true;
	break;
    case 37:
	keys.left = true;
	break;
    case 39:
	keys.right = true;
	break;
    }
}



var ship = {
    x: 200,
    y: 100,
    dx: 0,
    dy: 0,

    speed: 5,
    size: 10,

    update: function (keys) {
	if(keys.up)
	{
	    dx = speed;
	}
	
	if(keys.left)
	{
	    dy = speed;
	}

	x += dx;
	y += dy;
    },

    draw: function() { circle(x, y, size); },

    clear: function () {
    }
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

function draw() {
  canvas.fillStyle = "#FAF7F8";
    ship.draw();


//    for(sprite in sprites)
//    {
//	clear(sprite);
//	sprite.update();
//	draw(sprite);
//    }

//  clear();

//  rect(0,0,600,400);
  canvas.fillStyle = "#444444";
  //circle(x, y, 10);

/*
  if (x + dx > WIDTH || x + dx < 0)
    dx = -dx;
  if (y + dy > HEIGHT || y + dy < 0)
    dy = -dy;

  x += dx;
  y += dy;*/
}

document.write("_");