/*
 * Asteroids
 * by Michael Welsman-Dinelle */

var canvas = document.getElementById("canvas").getContext("2d");

var keys = {
    up: false,
    down: false,
    left: false,
    right: false
}

var ship = {
    x: 0,
    y: 0,
    dx: 0,
    dy: 0,

    speed: 5,
    size: 10
}

setInterval(draw, 16);

function circle(x,y,r) {
  canvas.beginPath();
  canvas.arc(x, y, r, 0, Math.PI*2, true);
  canvas.fill();
}

function rect(x,y,w,h) {
  ctx.beginPath();
  ctx.rect(x,y,w,h);
  ctx.closePath();
  ctx.fill();
}

function clear() {
  ctx.clearRect(0, 0, WIDTH, HEIGHT);
}

function init() {
  canvas = document.getElementById("canvas");
  ctx = canvas.getContext("2d");
  return setInterval(draw, 10);
}

function draw() {
  //clear all sprite rects

  //draw new sprites

  clear();
  ctx.fillStyle = "#FAF7F8";
  rect(0,0,WIDTH,HEIGHT);
  ctx.fillStyle = "#444444";
  circle(x, y, 10);

  if (x + dx > WIDTH || x + dx < 0)
    dx = -dx;
  if (y + dy > HEIGHT || y + dy < 0)
    dy = -dy;

  x += dx;
  y += dy;
}

init();