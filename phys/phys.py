from pyglet.gl import *

from pyglet.window import key
from pyglet.window import mouse

import random
import math

window = pyglet.window.Window()

pt_width = 30

n_pts = 15

v = [0] * 2 * n_pts #velocities
p = [0] * 2 * n_pts #positions

cr = 0.55 #coefficient of restitution
cf = 0.9 #coefficient of friction

g = 0.35

for i in range(len(v)):
    v[i] = random.gauss(0, 3)
    p[i] = random.gauss(window.width / 2, window.width / 3)

glPointSize(pt_width)

def colliding(i, j):
    dist2 = (p[2*i] - p[2*j]) ** 2 + (p[2*i+1] - p[2*j+1]) ** 2
    return dist2 < pt_width ** 2

@window.event
def on_mouse_press(x, y, button, modifiers):
    if button == mouse.MIDDLE:
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
    elif symbol == key.UP:
        for i in range(len(v)//2):
            v[2*i + 1] += random.gauss(9, 3)
        print 'Bump.'
    elif symbol == key.ENTER:
        for i in range(len(v)):
            v[i] = random.gauss(0, 3)
            p[i] = random.gauss(window.width / 2, window.width / 3)
        print 'Generating new points.'

@window.event
def on_draw():
    for i in range(len(v)):
        p[i] += v[i]

        if p[i] < 0:
            p[i] = 0
            v[i] = -v[i]*cr
            v[i-1] = v[i-1]*cf

        if (i % 2 == 1):
            v[i] -= g

        if (i % 2 == 0) & (p[i] > window.width):
            p[i] = window.width
            v[i] = -v[i]*cr
        elif (i % 2 == 1) & (p[i] > window.height):
            p[i] = window.height
            v[i] = -v[i]*cr

    for i in range(n_pts):
        for j in range(n_pts):
            if((i != j) & colliding(i, j)):
                v[2*i] = -v[2*i]
                v[2*i+1] = -v[2*i+1]*cr

                p[2*i] += v[2*i]
                p[2*i+1] += v[2*i+1]


    glClear(GL_COLOR_BUFFER_BIT)
    glLoadIdentity()

    pyglet.graphics.draw(n_pts, pyglet.gl.GL_POINTS, ('v2f', p))

pyglet.app.run()
