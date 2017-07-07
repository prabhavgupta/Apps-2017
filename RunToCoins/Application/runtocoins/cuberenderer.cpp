/****************************************************************************
**
** Copyright (C) 2015 The Qt Company Ltd.
** Contact: http://www.qt.io/licensing/
**
** This file is part of the examples of the Qt Toolkit.
**
** $QT_BEGIN_LICENSE:BSD$
** You may use this file under the terms of the BSD license as follows:
**
** "Redistribution and use in source and binary forms, with or without
** modification, are permitted provided that the following conditions are
** met:
**   * Redistributions of source code must retain the above copyright
**     notice, this list of conditions and the following disclaimer.
**   * Redistributions in binary form must reproduce the above copyright
**     notice, this list of conditions and the following disclaimer in
**     the documentation and/or other materials provided with the
**     distribution.
**   * Neither the name of The Qt Company Ltd nor the names of its
**     contributors may be used to endorse or promote products derived
**     from this software without specific prior written permission.
**
**
** THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
** "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
** LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
** A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
** OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
** SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
** LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
** DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
** THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
** (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
** OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE."
**
** $QT_END_LICENSE$
**
****************************************************************************/

#include <QAndroidJniObject>
#include <queue>

#include <QtAndroid>
#include <QtAndroidExtras>

#include "gl_code.h"
#include <jni.h>
#include <android/log.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <EGL/eglext.h>
#include <EGL/egl.h>
#include <QOpenGLContext>
#include <android/sensor.h>
#include <vector>



#include "cuberenderer.h"
#include <QOpenGLContext>
#include <QOpenGLFunctions>
#include <QOpenGLShaderProgram>
#include <QOpenGLVertexArrayObject>
#include <QOpenGLBuffer>
#include <QOpenGLVertexArrayObject>
#include <QOffscreenSurface>
#include <QWindow>

using namespace std;

short coinSize=0;
bool getCoin=false;

GLuint fboId,fboTex,renderBufferId,gfbo,ginc;


vector<Punto> monedas;
bool cargarMonedas=true;
int banderaGPS=0;

float rotarM=0;
 QOpenGLContext* extContext;
 GLuint gProgram,gCamera,gmain,gMapa;
 int sizeDevicew=0,sizeDeviceh=0;
 float bend=0, minimize=0;


#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

 GLuint texturasOES[1];
 GLuint texturasOESMAP[1];

 QAndroidJniObject m_surfaceTexture;
 QAndroidJniObject m_camera;


 extern "C" {
          JNIEXPORT jint  JNICALL Java_com_totos_run_to_coins_NotificationClient_numtextura(JNIEnv * env, jobject obj);
          JNIEXPORT jint  JNICALL Java_com_totos_run_to_coins_NotificationClient_numtexturaMap(JNIEnv * env, jobject obj);
          JNIEXPORT void  JNICALL Java_com_totos_run_to_coins_NotificationClient_loadTexture(JNIEnv* env, jobject obj);
          JNIEXPORT void JNICALL Java_com_totos_run_to_coins_NotificationClient_valores(JNIEnv*, jobject,jdouble longitud,jdouble latitud,  jfloat angulito,jfloat vertical,jfloat inclinacion);
          JNIEXPORT void  Java_com_totos_run_to_coins_NotificationClient_setPos(JNIEnv * env, jobject obj,double mx,double my);
          JNIEXPORT void  Java_com_totos_run_to_coins_NotificationClient_flagGPS(JNIEnv * env, jobject obj,int x);
          JNIEXPORT void  Java_com_totos_run_to_coins_NotificationClient_addCoin(JNIEnv * env, jobject obj,float x,float y, double mx,double my);



 };


 JNIEXPORT void  Java_com_totos_run_to_coins_NotificationClient_addCoin(JNIEnv * env, jobject obj,float x,float y,double mx,double my)
{

     Punto p;


     if(x< sizeDevicew/2)
     {
         if(y> sizeDeviceh/2)// 3
         {

             p.y=my-((y-(sizeDeviceh/2)));
             p.x=mx-((x-(sizeDevicew/2)));


         }

         else //cuadrante 2
         {
             p.y=my-((y+(sizeDeviceh/2)));
             p.x=mx-((x-(sizeDevicew/2)));

         }

     }

     else
     {
         if(y> sizeDeviceh/2) // 4
         {
             p.y=my-((y-(sizeDeviceh/2)));
             p.x=mx-((x+(sizeDevicew/2)));

         }

         else // 1
         {

             p.y=my-((y+(sizeDeviceh/2)));
             p.x=mx-((x+(sizeDevicew/2)));
         }

     }


      monedas.push_back(p);

      coinSize=monedas.size();


 }



 JNIEXPORT void  Java_com_totos_run_to_coins_NotificationClient_setPos(JNIEnv * env, jobject obj,double mx,double my)
 {

     mapCenter.x=mx;
     mapCenter.y=my;

 }




  void *measure_distances(void*)
  {



      while(1)
      {
        Punto aux;
        for(int i=0;i<monedas.size();i++)
        {

            Rflecha::calcula(mygpsx,mygpsy,monedas[i].x,monedas[i].y,myrumbo);
            monedas[i].distancia=Rflecha::distancia();

        }

      }

  }




 JNIEXPORT void JNICALL Java_com_totos_run_to_coins_NotificationClient_valores(JNIEnv*, jobject,jdouble longitud,jdouble latitud, jfloat angulito,jfloat vertical,jfloat inclinacion)
 {
     mygpsx=longitud;
     mygpsy=latitud;
     myrumbo=angulito;
     myvertical=vertical;
     myinclinacion=inclinacion;

   cargarMonedas=false;

 }



 JNIEXPORT jint   JNICALL Java_com_totos_run_to_coins_NotificationClient_numtextura(JNIEnv * env, jobject obj)
 {     glGenTextures(1, texturasOES);
 glBindTexture(GL_TEXTURE_EXTERNAL_OES,texturasOES[0]);
 glTexParameterf(GL_TEXTURE_EXTERNAL_OES,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
 glTexParameterf(GL_TEXTURE_EXTERNAL_OES,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
 glTexParameteri(GL_TEXTURE_EXTERNAL_OES,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
 glTexParameteri(GL_TEXTURE_EXTERNAL_OES,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
     return texturasOES[0];
 }

 JNIEXPORT jint   JNICALL Java_com_totos_run_to_coins_NotificationClient_numtexturaMap(JNIEnv * env, jobject obj)
 {     glGenTextures(1, texturasOESMAP);
 glBindTexture(GL_TEXTURE_EXTERNAL_OES,texturasOESMAP[0]);
 glTexParameterf(GL_TEXTURE_EXTERNAL_OES,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
 glTexParameterf(GL_TEXTURE_EXTERNAL_OES,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
 glTexParameteri(GL_TEXTURE_EXTERNAL_OES,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
 glTexParameteri(GL_TEXTURE_EXTERNAL_OES,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
     return texturasOESMAP[0];
 }


 JNIEXPORT void JNICALL Java_com_totos_run_to_coins_NotificationClient_loadTexture(JNIEnv* env, jobject obj)
 {



     // Handle to the activity class:
     jclass activityClass = env->GetObjectClass(obj);

     jmethodID getTextureCountMethodID = env->GetMethodID(activityClass,
                                                     "getTextureCount", "()I");
     if (getTextureCountMethodID == 0)
     {
         LOGI("Function getTextureCount() not found.");
         return;
     }

     textureCount = env->CallIntMethod(obj, getTextureCountMethodID);
     if (!textureCount)
     {
         LOGI("getTextureCount() returned zero.");
         return;
     }

     textures = new Texture*[textureCount];

     jmethodID getTextureMethodID = env->GetMethodID(activityClass,
         "getTexture", "(I)Lcom/totos/run/to/coins/Texture;");

     if (getTextureMethodID == 0)
     {
         LOGI("Function getTexture() not found.");
         return;
     }

     LOGI("numero de texturas %d",textureCount);


     // Register the textures
     for (int y = 0; y < textureCount; ++y)
     {

         jobject textureObject = env->CallObjectMethod(obj, getTextureMethodID, y);
         if (textureObject == NULL)
         {
             LOGI("GetTexture() returned zero pointer");
             return;
         }

         textures[y] = Texture::create(env, textureObject);
     }




 }


static void printGLString(const char *name, GLenum s) {
    const char *v = (const char *) glGetString(s);
    LOGI("GL %s = %s\n", name, v);
}

static void checkGlError(const char* op) {
    for (GLint error = glGetError(); error; error
            = glGetError()) {
        LOGI("after %s() glError (0x%x)\n", op, error);
    }
}



GLuint loadShader(GLenum shaderType, const char* pSource) {
    GLuint shader = glCreateShader(shaderType);
    if (shader) {
        glShaderSource(shader, 1, &pSource, NULL);
        glCompileShader(shader);
        GLint compiled = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
        if (!compiled) {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen) {
                char* buf = (char*) malloc(infoLen);
                if (buf) {
                    glGetShaderInfoLog(shader, infoLen, NULL, buf);
                    LOGE("Could not compile shader %d:\n%s\n",
                            shaderType, buf);
                    free(buf);
                }
                glDeleteShader(shader);
                shader = 0;
            }
        }
    }
    return shader;
}


void  DrawFBO(GLuint *objeto3D,int numerovertices,int textura,GLuint shader,unsigned char * fdY,unsigned char * fdU,unsigned char * fdV,int w ,int h)
{



    float *algo=new float[16];
        for(int t=0;t<16;t++)
        {
            algo[t]=matriz::regresa(t);

        }

            glUseProgram( shader);


                      GLuint matriz     = glGetUniformLocation(shader, "matrizp");
                                              glUniformMatrix4fv(matriz, 1, GL_FALSE,(GLfloat*)&algo[0] );

                      GLuint puntitos    = glGetAttribLocation(shader,"vertexPosition");
                      GLuint pscale    = glGetUniformLocation(shader,"scale");

                      glUniform3f(pscale,.5,.5,.5);

                                                  GLuint normalitos    = glGetAttribLocation(shader,"vertexNormal");
                                                  GLuint fotito    = glGetAttribLocation(shader,"vertexTexCoord");
                                                    GLuint resolucion=glGetUniformLocation(shader,"resolution");
                                                GLuint time=glGetUniformLocation(shader,"time");
                                                                    glUniform1f(time,(GLfloat)tiempo);

                                                glUniform2f(resolucion,w,h);

                                            glEnableVertexAttribArray(puntitos);
                                            glEnableVertexAttribArray(normalitos);
                                            glEnableVertexAttribArray(fotito);


                                            glBindBuffer(GL_ARRAY_BUFFER, objeto3D[0]);
                                            glVertexAttribPointer(puntitos, 3, GL_FLOAT, GL_FALSE, 0, 0);
                                            glBindBuffer(GL_ARRAY_BUFFER, objeto3D[1]);
                                            glVertexAttribPointer(normalitos, 3, GL_FLOAT, GL_FALSE, 0, 0);
                                            glBindBuffer(GL_ARRAY_BUFFER, objeto3D[2]);
                                            glVertexAttribPointer(fotito, 2, GL_FLOAT, GL_FALSE, 0, 0);

                                               glActiveTexture(GL_TEXTURE0);
                                              glBindTexture( GL_TEXTURE_2D,fboTex );
                                              glEnable(GL_DEPTH_TEST);
                                              glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);




            glDrawArrays(GL_TRIANGLES, 0,numerovertices);
            delete(algo);
            glDisable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);

   


}
void  iniciarFBO(GLuint *objeto3D,int numerovertices,int textura,GLuint shader,unsigned char * fdY,unsigned char * fdU,unsigned char * fdV,int w ,int h,GLuint fbo_id )
{
    glBindFramebuffer(GL_FRAMEBUFFER, fbo_id);
    glViewport(0, 0, w, h);
   glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
   glClearColor(0, 0, 0, 0);

   matriz::R(0,1,0,0);
   matriz::escala(1,1,1);
   matriz::longitud(0,0,0);

    float *algo=new float[16];
        for(int t=0;t<16;t++)
        {
            algo[t]=matriz::regresa(t);

        }

        glUseProgram( shader);
                         const Texture* const thisTexture = textures[textura];

                                 GLuint matriz     = glGetUniformLocation(shader, "matrizp");
                                 glUniformMatrix4fv(matriz, 1, GL_FALSE,(GLfloat*)&algo[0] );

                                  GLuint puntitos    = glGetAttribLocation(shader,"vertexPosition");
                                  GLuint pscale    = glGetUniformLocation(shader,"scale");



                                   GLuint normalitos    = glGetAttribLocation(shader,"vertexNormal");
                                   GLuint fotito    = glGetAttribLocation(shader,"vertexTexCoord");
                                   GLuint resolucion=glGetUniformLocation(shader,"resolution");
                                   GLuint time=glGetUniformLocation(shader,"time");
                                   glUniform1f(time,(GLfloat)tiempo);

                                   glEnableVertexAttribArray(puntitos);
                                   glEnableVertexAttribArray(normalitos);
                                   glEnableVertexAttribArray(fotito);


                                        glBindBuffer(GL_ARRAY_BUFFER, objeto3D[0]);
                                        glVertexAttribPointer(puntitos, 3, GL_FLOAT, GL_FALSE, 0, 0);
                                        glBindBuffer(GL_ARRAY_BUFFER, objeto3D[1]);
                                        glVertexAttribPointer(normalitos, 3, GL_FLOAT, GL_FALSE, 0, 0);
                                        glBindBuffer(GL_ARRAY_BUFFER, objeto3D[2]);
                                        glVertexAttribPointer(fotito, 2, GL_FLOAT, GL_FALSE, 0, 0);

                                                                      glActiveTexture(GL_TEXTURE0);

                                                                       glBindTexture(GL_TEXTURE_2D,thisTexture->mTextureID);



    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);


    glBindFramebuffer(GL_FRAMEBUFFER, 0);

      glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
       glClearColor(0, 0, 0, 0);
    delete (algo);

}


GLuint createProgram(const char* pVertexSource, const char* pFragmentSource) {
    GLuint vertexShader = loadShader(GL_VERTEX_SHADER, pVertexSource);
    if (!vertexShader) {
        return 0;
    }

    GLuint pixelShader = loadShader(GL_FRAGMENT_SHADER, pFragmentSource);
    if (!pixelShader) {
        return 0;
    }

    GLuint program = glCreateProgram();
    if (program) {
        glAttachShader(program, vertexShader);
        checkGlError("glAttachShader");
        glAttachShader(program, pixelShader);
        checkGlError("glAttachShader");
        glLinkProgram(program);
        GLint linkStatus = GL_FALSE;
        glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
        if (linkStatus != GL_TRUE) {
            GLint bufLength = 0;
            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength) {
                char* buf = (char*) malloc(bufLength);
                if (buf) {
                    glGetProgramInfoLog(program, bufLength, NULL, buf);
                    LOGE("Could not link program:\n%s\n", buf);
                    free(buf);
                }
            }
            glDeleteProgram(program);
            program = 0;
        }
    }
    return program;
}


bool setupGraphics(int w, int h) {
    printGLString("Version", GL_VERSION);
    printGLString("Vendor", GL_VENDOR);
    printGLString("Renderer", GL_RENDERER);
    printGLString("Extensions", GL_EXTENSIONS);

    LOGI("setupGraphics(%d, %d)", w, h);
    gProgram = createProgram(gVertexShader, gFragmentShader);
    if (!gProgram) {
        LOGE("Could not create program.");
        return false;
    }

    gCamera= createProgram(gVertexShaderCamera, gFragmentShaderCamera);
      if (!gCamera) {
          LOGE("Could not create gCamera.");
          return false;
      }

      gmain = createProgram(gVertexShaderMain, gFragmentShaderMain);
      if (!gmain) {
          LOGE("Could not create program.");
          return false;
      }


      gMapa= createProgram(gVertexShaderMap, gFragmentShaderMap);
      if (!gMapa) {
          LOGE("Could not create program.");
          return false;
      }

      gfbo = createProgram(gVertexShaderFBO, gFragmentShaderFBO);
                     if (!gfbo) {
                         LOGE("Could not create program.");
                         return false;
                     }






    glViewport(0, 0, w, h);
    checkGlError("glViewport");
    return true;
}

Objetos3D *objetos3D=new Objetos3D[4];

void loadOBJ3D(GLuint *objeto3D,int puntos, const GLvoid* vertices,const GLvoid* normales,const GLvoid* texturasc)
{
    glGenBuffers(3, objeto3D);

    glBindBuffer(GL_ARRAY_BUFFER, objeto3D[0]);
    glBufferData(GL_ARRAY_BUFFER,puntos*3*sizeof(float), vertices, GL_STREAM_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, 0 );

    glBindBuffer(GL_ARRAY_BUFFER, objeto3D[1]);
    glBufferData(GL_ARRAY_BUFFER,puntos*3*sizeof(float), normales, GL_STREAM_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, 0 );

    glBindBuffer(GL_ARRAY_BUFFER, objeto3D[2]);
    glBufferData(GL_ARRAY_BUFFER,puntos*2*sizeof(float), texturasc, GL_STREAM_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, 0 );
}

const GLfloat gTriangleVertices[] = { 0.0f, 0.5f, -0.5f, -0.5f,
        0.5f, -0.5f };

void renderFrame() {

//Render principal

    glClearColor(0, 0, 0, 0.0f);
       checkGlError("glClearColor");
       glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
       checkGlError("glClear");

       iniciarFBO(objetos3D[0].vBO,NUM_plano_OBJECT_VERTEX,0,gfbo,NULL,NULL,NULL,256,256,fboId);

       glViewport(0, 0, sizeDevicew,sizeDeviceh);

       float *scala=new float[3];

       matriz::yxz(180,0,0);
               matriz::escala(1,1,1);
               matriz::longitud(0,0,0.);
               scala[0]=1.0;
               scala[1]=1.0;
               scala[2]=1.0;
               Draw_OES(objetos3D[0].vBO,NUM_plano_OBJECT_VERTEX,9,gCamera,scala,texturasOES[0]);




              Punto aux;

               for(int i=0;i<monedas.size();i++)
               {

                   Rflecha::calcula(mygpsx,mygpsy,monedas[i].x,monedas[i].y,myrumbo);
                   monedas[i].distancia=Rflecha::distancia();

                   if(Rflecha::distancia()<5)
                   {
                       monedas.erase(monedas.begin()+i);
                       getCoin=true;
                   }



               }

            



               if(myvertical>1.1)
               {

                   matriz::yxz(180,0,0);
                   matriz::escala(1,1,1);
                   matriz::longitud(0,0,0.);
                   scala[0]=1.0;
                   scala[1]=1.0;
                   scala[2]=1.0;
                   Draw_OES(objetos3D[0].vBO,NUM_plano_OBJECT_VERTEX,9,gMapa,scala,texturasOESMAP[0]);
               }
               else if(!cargarMonedas && monedas.size()>0)
               {

                   //arrow
                  Rflecha::calcula(mygpsx,mygpsy,monedas[0].x,monedas[0].y,myrumbo);

                  matriz::yxz(myvertical*-50,Rflecha::angulo(),0);


                  matriz::escala(.5,.5,.5);
                  matriz::longitud(0,.8,0);
                  matriz::multiply();

                  DrawNormal(objetos3D[2].vBO,NUM_flecha_OBJECT_VERTEX,1,gmain);
               }

               for(int m=0;m<monedas.size();m++)
                           {




                               double mx=0;
                               double my=0;
                               double md=10;
                               double mz=0;
               //phone down
                               if(myvertical>1.1)
                               {



                               matriz::yxz(90,rotarM,0);
                               matriz::escala(.1,.1,.1);
                               Rflecha::calcula(mapCenter.x,mapCenter.y,monedas[m].x,mapCenter.y,myrumbo);
                               double mxinc=Rflecha::distancia();

                               Rflecha::calcula(mapCenter.x,mapCenter.y,mapCenter.x,monedas[m].y,myrumbo);
                               double myinc=Rflecha::distancia();

                               matriz::longitud(mxinc,myinc,0.0);


                               DrawNormal(objetos3D[1].vBO,NUM_mon_OBJECT_VERTEX,0,gmain);

                               Rflecha::calcula(mapCenter.x,mapCenter.y,monedas[m].x,monedas[m].y,myrumbo);


                               }

                               else
                               {
                                   matriz::R(rotarM,0,1,0);
                                   matriz::escala(1,1,1);
                                   double mx=(Rflecha::calcula(mygpsx,mygpsy,monedas[m].x,monedas[m].y,myrumbo));
                                   double my=myvertical;
                                   double md=Rflecha::distancia();
                                   double mz=(md/20.0)*-1;
                                   double mdr=(md/50);
                                   matriz::longitud(mx*mdr,(my)+(md*2.0/50.0),mz);
                                   matriz::multiply();

                                   DrawNormal(objetos3D[1].vBO,NUM_mon_OBJECT_VERTEX,0,gmain);

                               }

}
               rotarM+=5;


if(monedas.size()>0)
{
               matriz::R(rotarM,0,1,0);
               matriz::escala(1,1,1);
               double mx=(Rflecha::calcula(mygpsx,mygpsy,monedas[monedas.size()-1].x,monedas[monedas.size()-1].y,myrumbo));


               double my=myvertical;
               double md=Rflecha::distancia();
               double mz=(md/20.0)*-1;
               double mdr=(md/50);
               matriz::longitud(mx*mdr,(my)+(md*2.0/50.0),mz);
               matriz::multiply();

               DrawFBO(objetos3D[3].vBO,NUM_pelota_OBJECT_VERTEX,0,gProgram,NULL,NULL,NULL,  256,256);


}
//menu
  matriz::R(0,1,0,0);
  matriz::escala(1,1,1);
  matriz::longitud(0,0,0);

  DrawNormal(objetos3D[0].vBO,NUM_plano_OBJECT_VERTEX,0,gmain);



                                               tiempo=tiempo+1.0;
                                               tframe ++;

}

void  DrawNormal(GLuint *objeto3D,int numerovertices,int textura,GLuint shader)
{
    glViewport(0, 0, sizeDevicew,sizeDeviceh);

      float *algo=new float[16];
            for(int t=0;t<16;t++)
            {
                algo[t]=matriz::regresa(t);

            }

                glUseProgram( shader);
                 const Texture* const thisTexture = textures[textura];

                          GLuint matriz     = glGetUniformLocation(shader, "matrizp");
                                                  glUniformMatrix4fv(matriz, 1, GL_FALSE,(GLfloat*)&algo[0] );

                          GLuint puntitos    = glGetAttribLocation(shader,"vertexPosition");
                          GLuint pscale    = glGetUniformLocation(shader,"scale");

                          glUniform3f(pscale,0.5,0.5,0.5);

                                                      GLuint normalitos    = glGetAttribLocation(shader,"vertexNormal");
                                                      GLuint fotito    = glGetAttribLocation(shader,"vertexTexCoord");
                                                        GLuint resolucion=glGetUniformLocation(shader,"resolution");
                                                    GLuint time=glGetUniformLocation(shader,"time");
                                                                        glUniform1f(time,(GLfloat)tiempo);

                                                glEnableVertexAttribArray(puntitos);
                                                glEnableVertexAttribArray(normalitos);
                                                glEnableVertexAttribArray(fotito);





                                                glBindBuffer(GL_ARRAY_BUFFER, objeto3D[0]);
                                            glVertexAttribPointer(puntitos, 3, GL_FLOAT, GL_FALSE, 0, 0);
                                            glBindBuffer(GL_ARRAY_BUFFER, objeto3D[1]);
                                            glVertexAttribPointer(normalitos, 3, GL_FLOAT, GL_FALSE, 0, 0);
                                            glBindBuffer(GL_ARRAY_BUFFER, objeto3D[2]);
                                            glVertexAttribPointer(fotito, 2, GL_FLOAT, GL_FALSE, 0, 0);


                                            glEnable (GL_BLEND);
                                            //glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                                            glEnable(GL_DEPTH_TEST);

                                            glActiveTexture(GL_TEXTURE0);

glBindTexture(GL_TEXTURE_2D,thisTexture->mTextureID);


    glDrawArrays(GL_TRIANGLES, 0,numerovertices);


    glDisable(GL_BLEND);
    glDisable(GL_DEPTH_TEST);

delete(algo);


}

void  Draw_OES(GLuint *objeto3D,int numerovertices,int textura,GLuint shader,float *scala,GLuint imagen)
{
    glViewport(0, 0, sizeDevicew,sizeDeviceh);

      float *algo=new float[16];
            for(int t=0;t<16;t++)
            {
                algo[t]=matriz::regresa(t);

            }

                glUseProgram( shader);
                // const Texture* const thisTexture = textures[textura];

                          GLuint matriz     = glGetUniformLocation(shader, "matrizp");
                                                  glUniformMatrix4fv(matriz, 1, GL_FALSE,(GLfloat*)&algo[0] );

                          GLuint puntitos    = glGetAttribLocation(shader,"vertexPosition");
                          GLuint pscale    = glGetUniformLocation(shader,"scale");

                          glUniform3f(pscale,scala[0],scala[1],scala[2]);

                                                      GLuint normalitos    = glGetAttribLocation(shader,"vertexNormal");
                                                      GLuint fotito    = glGetAttribLocation(shader,"vertexTexCoord");
                                                        GLuint resolucion=glGetUniformLocation(shader,"resolution");
                                                    GLuint time=glGetUniformLocation(shader,"time");
                                                                        glUniform1f(time,(GLfloat)tiempo);

                                                glEnableVertexAttribArray(puntitos);
                                                glEnableVertexAttribArray(normalitos);
                                                glEnableVertexAttribArray(fotito);




                                glBindBuffer(GL_ARRAY_BUFFER, objeto3D[0]);
                                glVertexAttribPointer(puntitos, 3, GL_FLOAT, GL_FALSE, 0, 0);
                                glBindBuffer(GL_ARRAY_BUFFER, objeto3D[1]);
                                glVertexAttribPointer(normalitos, 3, GL_FLOAT, GL_FALSE, 0, 0);
                                glBindBuffer(GL_ARRAY_BUFFER, objeto3D[2]);
                                glVertexAttribPointer(fotito, 2, GL_FLOAT, GL_FALSE, 0, 0);


                                           glActiveTexture(GL_TEXTURE0);

                                          glBindTexture(GL_TEXTURE_EXTERNAL_OES,imagen);

                                       glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

glDisableVertexAttribArray(puntitos);
                   glDisableVertexAttribArray(normalitos);
                   glDisableVertexAttribArray(fotito);
                   glBindBuffer(GL_ARRAY_BUFFER, NULL);

}




CubeRenderer::CubeRenderer(QOffscreenSurface *offscreenSurface)
    : m_offscreenSurface(offscreenSurface),
      m_context(0),
      m_program(0),
      m_vbo(0),
      m_vao(0),
      m_matrixLoc(0)
{
}

CubeRenderer::~CubeRenderer()
{
    // Use a temporary offscreen surface to do the cleanup.
    // There may not be a native window surface available anymore at this stage.
    m_context->makeCurrent(m_offscreenSurface);

    delete m_program;
    delete m_vbo;
    delete m_vao;

    m_context->doneCurrent();
    delete m_context;
}

int CubeRenderer::score()
{

 return coinSize-monedas.size();

}

int CubeRenderer::flagGPS()
{

 return banderaGPS;

}

int CubeRenderer::totalCoins()
{

    return monedas.size();
}

bool CubeRenderer::startCoin()
{

    return getCoin;
}

void CubeRenderer::stopCoin()
{
    getCoin=false;
}

int CubeRenderer::distance()
{


if(monedas.size()>0)
{
   Rflecha::calcula(mygpsx,mygpsy,monedas[0].x,monedas[0].y,myrumbo);

    return Rflecha::distancia();
}
    else
    return 0;
}

int * CubeRenderer::posCoin()
{
    int *pos=new int[2];



    if(myvertical>1.1)
    {

        pos[0]=sizeDevicew/(2.0);
        pos[1]=sizeDeviceh/2.0;

    }
    else if(monedas.size()>0)
    {

        double mx=(Rflecha::calcula(mygpsx,mygpsy,monedas[0].x,monedas[0].y,myrumbo));
        double my=myvertical;
        double md=Rflecha::distancia();
        double mz=(md/20.0)*-1;
        double mdr=(md/50);
       // matriz::longitud(mx*mdr,(my)+(md*2.0/50.0),mz);


    pos[0]=(((mx))+2.0)*mdr/sizeDevicew;
    pos[1]=((my)+(md*2.0/50.0))*mdr/sizeDeviceh;;
    //LOGI("pos %d %d %f %f",pos[0],pos[1],(mx*mdr),((my)+(md*2.0/50.0)));

    }
    else
    {
        pos[0]=sizeDevicew/(2.0);
        pos[1]=sizeDeviceh/2.0;

    }

    return pos;
}

void CubeRenderer::moveMenu(double a, double b)
{
    bend=a;
    minimize=b;

}

void CubeRenderer::init(QWindow *W, QOpenGLContext *share)
{
    m_context = new QOpenGLContext;
    m_context->setShareContext(share);
    m_context->setFormat(W->requestedFormat());
    m_context->create();
    if (!m_context->makeCurrent(W))
        return;

    QOpenGLFunctions *f = m_context->functions();
    f->glClearColor(0.0f, 0.1f, 0.25f, 1.0f);
    f->glViewport(0, 0, W->width() * W->devicePixelRatio(), W->height() * W->devicePixelRatio());

    static const char *vertexShaderSource =
        "attribute highp vec4 vertex;\n"
        "attribute lowp vec2 coord;\n"
        "varying lowp vec2 v_coord;\n"
        "uniform highp mat4 matrix;\n"
        "void main() {\n"
        "   v_coord = coord;\n"
        "   gl_Position = matrix * vertex;\n"
        "}\n";
    static const char *fragmentShaderSource =
        "varying lowp vec2 v_coord;\n"
        "uniform sampler2D sampler;\n"
        "void main() {\n"
        "   gl_FragColor = vec4(texture2D(sampler, v_coord).rgb, 0.5);\n"
        "}\n";
    m_program = new QOpenGLShaderProgram;
    m_program->addShaderFromSourceCode(QOpenGLShader::Vertex, vertexShaderSource);
    m_program->addShaderFromSourceCode(QOpenGLShader::Fragment, fragmentShaderSource);
    m_program->bindAttributeLocation("vertex", 0);
    m_program->bindAttributeLocation("coord", 1);
    m_program->link();
    m_matrixLoc = m_program->uniformLocation("matrix");

    m_vao = new QOpenGLVertexArrayObject;
    m_vao->create();
    QOpenGLVertexArrayObject::Binder vaoBinder(m_vao);

    m_vbo = new QOpenGLBuffer;
    m_vbo->create();
    m_vbo->bind();

    GLfloat v[] = {
        -0.5, 0.5, 0.5, 0.5,-0.5,0.5,-0.5,-0.5,0.5,
        0.5, -0.5, 0.5, -0.5,0.5,0.5,0.5,0.5,0.5,
        -0.5, -0.5, -0.5, 0.5,-0.5,-0.5,-0.5,0.5,-0.5,
        0.5, 0.5, -0.5, -0.5,0.5,-0.5,0.5,-0.5,-0.5,

        0.5, -0.5, -0.5, 0.5,-0.5,0.5,0.5,0.5,-0.5,
        0.5, 0.5, 0.5, 0.5,0.5,-0.5,0.5,-0.5,0.5,
        -0.5, 0.5, -0.5, -0.5,-0.5,0.5,-0.5,-0.5,-0.5,
        -0.5, -0.5, 0.5, -0.5,0.5,-0.5,-0.5,0.5,0.5,

        0.5, 0.5,  -0.5, -0.5, 0.5,  0.5,  -0.5,  0.5,  -0.5,
        -0.5,  0.5,  0.5,  0.5,  0.5,  -0.5, 0.5, 0.5,  0.5,
        -0.5,  -0.5, -0.5, -0.5, -0.5, 0.5,  0.5, -0.5, -0.5,
        0.5, -0.5, 0.5,  0.5,  -0.5, -0.5, -0.5,  -0.5, 0.5
    };
    GLfloat texCoords[] = {
        0.0f,0.0f, 1.0f,1.0f, 1.0f,0.0f,
        1.0f,1.0f, 0.0f,0.0f, 0.0f,1.0f,
        1.0f,1.0f, 1.0f,0.0f, 0.0f,1.0f,
        0.0f,0.0f, 0.0f,1.0f, 1.0f,0.0f,

        1.0f,1.0f, 1.0f,0.0f, 0.0f,1.0f,
        0.0f,0.0f, 0.0f,1.0f, 1.0f,0.0f,
        0.0f,0.0f, 1.0f,1.0f, 1.0f,0.0f,
        1.0f,1.0f, 0.0f,0.0f, 0.0f,1.0f,

        0.0f,1.0f, 1.0f,0.0f, 1.0f,1.0f,
        1.0f,0.0f, 0.0f,1.0f, 0.0f,0.0f,
        1.0f,0.0f, 1.0f,1.0f, 0.0f,0.0f,
        0.0f,1.0f, 0.0f,0.0f, 1.0f,1.0f
    };

    const int vertexCount = 36;
    m_vbo->allocate(sizeof(GLfloat) * vertexCount * 5);
    m_vbo->write(0, v, sizeof(GLfloat) * vertexCount * 3);
    m_vbo->write(sizeof(GLfloat) * vertexCount * 3, texCoords, sizeof(GLfloat) * vertexCount * 2);
    m_vbo->release();

    if (m_vao->isCreated())
        setupVertexAttribs();

sizeDevicew=W->width();
 sizeDeviceh=W->height();
     setupGraphics(sizeDevicew, sizeDeviceh);

     loadOBJ3D(objetos3D[0].vBO,NUM_plano_OBJECT_VERTEX,&planoVertices ,&planoNormals,&planoTexCoords);
     loadOBJ3D(objetos3D[1].vBO,NUM_mon_OBJECT_VERTEX,&monVertices ,&monNormals,&monTexCoords);
     loadOBJ3D(objetos3D[2].vBO,NUM_flecha_OBJECT_VERTEX,&flechaVertices ,&flechaNormals,&flechaTexCoords);
     loadOBJ3D(objetos3D[3].vBO,NUM_pelota_OBJECT_VERTEX,&pelotaVertices ,&pelotaNormals,&pelotaTexCoords);



    int w=256;
   int      h=256;
            //////////////////////////////////////////////////
         glGenFramebuffers(1,&fboId);
                     glGenTextures(1,&fboTex);
                     glGenRenderbuffers(1,&renderBufferId);


                     glBindFramebuffer(GL_FRAMEBUFFER, fboId);
                         //
                         glBindTexture(GL_TEXTURE_2D, fboTex);

                         glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);

                         glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                         glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                         glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                         glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

                         glBindRenderbuffer(GL_RENDERBUFFER, renderBufferId);
                         glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, w, h);

                         glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, fboTex, 0);
                         glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBufferId);

                         glBindTexture(GL_TEXTURE_2D, 0);
                         glBindRenderbuffer(GL_RENDERBUFFER, 0);
                         glBindFramebuffer(GL_FRAMEBUFFER, 0);

                                                 // FBO status check
                                                 GLenum status;
                                                 status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
                                                 switch(status) {
                                                     case GL_FRAMEBUFFER_COMPLETE:
                                                         LOGI("fbo complete");
                                                         break;

                                                     case GL_FRAMEBUFFER_UNSUPPORTED:
                                                    LOGI("fbo unsupported");
                                                    exit(1);
                                                         break;

                                                     default:
                                                    LOGI("Framebuffer Error");
                                                    exit(1);
                                                         break;
                                                 }





 QtAndroid::androidActivity().callMethod<void>("checkGPS", "()V");




 QtAndroid::androidActivity().callMethod<void>("Inicia", "()V");



     for (int i = 0; i < textureCount; ++i)
            {
                glGenTextures(1, &(textures[i]->mTextureID));
                glBindTexture(GL_TEXTURE_2D, textures[i]->mTextureID);
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, textures[i]->mWidth,
                        textures[i]->mHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                        (GLvoid*)  textures[i]->mData);
            }
}

void CubeRenderer::resize(int w, int h)
{
    m_proj.setToIdentity();
    m_proj.perspective(45, w / float(h), 0.01f, 100.0f);
}

void CubeRenderer::setupVertexAttribs()
{
    m_vbo->bind();
    m_program->enableAttributeArray(0);
    m_program->enableAttributeArray(1);
    m_context->functions()->glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, 0);
    m_context->functions()->glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 0,
                                                  (const void *)(36 * 3 * sizeof(GLfloat)));
    m_vbo->release();
}

void CubeRenderer::render(QWindow *w, QOpenGLContext *share, uint texture)
{
    if (!m_context)
        init(w, share);

    if (!m_context->makeCurrent(w))
        return;
<void>("Paso", "()V");


    renderFrame();
    m_context->swapBuffers(w);
}
