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

#v = [window.width / 2, window.height / 2]

vertices_gl = (GLfloat * len(p))(*p)

glPointSize(5)

glEnableClientState(GL_VERTEX_ARRAY)
glVertexPointer(2, GL_FLOAT, 0, vertices_gl)

#TODO: glVertexPointer is deprecated -- figure out how to use this style

#positionBufferObject = GLuint()

#glGenBuffers(1, positionBufferObject)
#glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject)
#glBufferData(GL_ARRAY_BUFFER, len(v)*4, vertices_gl, GL_DYNAMIC_DRAW)

#glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, 0)

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
        vertices_gl[i] += v[i]

        if vertices_gl[i] < 0:
            vertices_gl[i] = 0
            v[i] = -v[i]*cr

        if (i % 2 == 0) & (vertices_gl[i] > window.width):
            vertices_gl[i] = window.width
            v[i] = -v[i]*cr
        elif (i % 2 == 1) & (vertices_gl[i] > window.height):
            vertices_gl[i] = window.height
            v[i] = -v[i]*cr


    glClear(GL_COLOR_BUFFER_BIT)
    glLoadIdentity()
    glDrawArrays(GL_POINTS, 0, len(v) // 2)

#    glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject)
#    glEnableVertexAttribArray(0)
#    glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 0, vertices_gl)
#    glDrawArrays(GL_POINTS, 0, len(v) // 2)
#    glDisableVertexAttribArray(0)


pyglet.app.run()
