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

unsigned int planoDNumVerts = 8;


#ifndef _QCAR_planoD_OBJECT_H_
#define _QCAR_planoD_OBJECT_H_
#define NUM_planoD_OBJECT_VERTEX 8



static const float planoDVertices [24] = {
  -1.0,  1.0,0,
  -1.0,  -1.0,0,
  0.0,  1.0,0,
  0.0,  -1.0,0,

  0.0,  1.0,0,
   0.0,  -1.0,0,
   1.0,  1.0,0,
   1.0,  -1.0,0

};
static const float planoDNormals [24] = {
		  0, 1, 0,
		  0, 1, 0,
		  0, 1, 0,
		  0, 1, 0,

		 0, 1, 0,
		 0, 1, 0,
		 0, 1, 0,
		 0, 1, 0
};
static const float planoDTexCoords [16] = {
  0.000000, 1,
  0.000000, 0,
  1.000000, 1,
  1.000000, 0,

  0.000000, 1,
  0.000000, 0,
  1.000000, 1,
  1.000000, 0
};

#endif
