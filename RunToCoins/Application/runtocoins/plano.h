/*
created with obj2opengl.pl

source file    : plano.obj
vertices       : 4
faces          : 2
normals        : 0
texture coords : 4


// include generated arrays
#import "plano.h"

// set input data to arrays
glVertexPointer(3, GL_FLOAT, 0, planoVerts);
glTexCoordPointer(2, GL_FLOAT, 0, planoTexCoords);

// draw data
glDrawArrays(GL_TRIANGLES, 0, planoNumVerts);
*/

unsigned int planoNumVerts = 4;


#ifndef _QCAR_plano_OBJECT_H_
#define _QCAR_plano_OBJECT_H_
#define NUM_plano_OBJECT_VERTEX 4



static const float planoVertices [12] = {
  -1.0,  1.0,0,
  -1.0,  -1.0,0,
  1.0,  1.0,0,
  1.0,  -1.0,0
};
static const float planoNormals [12] = {
		  0, 1, 0,
		  0, 1, 0,
		  0, 1, 0,
		  0, 1, 0
};
static const float planoTexCoords [8] = {
        0,1,
                0,0,

                1,1,
                1,0,


};

#endif



