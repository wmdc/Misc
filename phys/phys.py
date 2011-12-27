from pyglet.gl import *

from pyglet.window import key
from pyglet.window import mouse

import random

window = pyglet.window.Window()

n_pts = 35

v = [0] * 2 * n_pts #velocities
p = [0] * 2 * n_pts #positions

cr = 0.8 #coefficient of restitution

for i in range(len(v)):
    v[i] = random.gauss(0, 3)
    p[i] = random.gauss(window.width / 2, window.width / 3)

glPointSize(5.0)

@window.event
def on_mouse_press(x, y, button, modifiers):
    if button == mouse.MIDDLE:
        vertices_gl[0] = x
        vertices_gl[1] = y
        print 'moving to ' + str(x) + ', ' + str(y)
    elif button == mouse.LEFT:
        glColor3f(0,0,0)
        print 'black'
    else:
        glColor3f(255, 255, 255)
        print 'white'

@window.event
def on_key_press(symbol, modifiers):
    if symbol == key.A:
        vertices_gl[0] += 1
        print 'The "A" key was pressed.'
    elif symbol == key.LEFT:
        print 'The left arrow key was pressed.'
    elif symbol == key.ENTER:
        print 'The enter key was pressed.'

@window.event
def on_draw():
    for i in range(len(v)):
        p[i] += v[i]

        if p[i] < 0:
            p[i] = 0
            v[i] = -v[i]*cr

        if (i % 2 == 0) & (p[i] > window.width):
            p[i] = window.width
            v[i] = -v[i]*cr
        elif (i % 2 == 1) & (p[i] > window.height):
            p[i] = window.height
            v[i] = -v[i]*cr


    glClear(GL_COLOR_BUFFER_BIT)
    glLoadIdentity()

    pyglet.graphics.draw(n_pts, pyglet.gl.GL_POINTS, ('v2f', p))

pyglet.app.run()
