#include <jni.h>
#include <android/log.h>
#include <Rflecha.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "Texture.h"
#include "plano.h"
#include "planoD.h"
#include "matriz.h"
#include "flecha.h"
#include <pthread.h>

#include "mon.h"

int tamanoconos=0;
int tframe=0;
int dw,dh;   float *calle=new float[100000];float rmd=0,rmi=0;Punto *menuiconos;Punto mapCenter;
pthread_t thread1,thread2,thread3,threadBall, hilomapa,hilord,hiloro;int puntoscalle=0;int opcionmenu=10;
void *print_message_function( void *ptr );void *Hilomapa( void *ptr );void *coverpositivo( void *ptr );void *covernegativo( void *ptr );
void *launch_ball_thread( void *ptr );
int recorredestino(  );int recorreorigen( );int burbuja(float a[],int b);
float girarx=0,girary=0;
double mygpsx=0,mygpsy=0,myrumbo=0,myvertical=0,myaltitud=0,myinclinacion=0;
 static const float pi      = 3.1416f;int tamanocalle=0;int tamanocover=0;
   int textureCount =0;float *michii;int tamanonomcalle=0;
   void camino(int inicio, int destino);Punto *nombrecalle=new Punto[1000];Punto *covermenu=new Punto[100];
  void  Draw(const GLvoid* vertices,const GLvoid* normales,const GLvoid* texturasc,int numerovertices,int textura,GLuint shader);
  void  DrawNormal(GLuint *objeto3D,int numerovertices,int textura,GLuint shader);

  void  DrawOCL(const GLvoid* vertices,const GLvoid* normales,const GLvoid* texturasc,int numerovertices,int textura,GLuint shader,float *ver);
  void DrawFBO(GLuint *objeto3D,int numerovertices,int textura,GLuint shader,unsigned char * fdY,unsigned char * fdU,unsigned char * fdV,int w ,int h);
  void  iniciarFBO(GLuint *objeto3D,int numerovertices,int textura,GLuint shader,unsigned char * fdY,unsigned char * fdU,unsigned char * fdV,int w ,int h,GLuint fbo_id);
  void DrawFBOVR(GLuint *objeto3D,int numerovertices,int textura,GLuint shader,unsigned char * fdY,unsigned char * fdU,unsigned char * fdV,int w ,int h);
  void  iniciarFBOVR(GLuint *objeto3D,int numerovertices,int textura,GLuint shader,unsigned char * fdY,unsigned char * fdU,unsigned char * fdV,int w ,int h,GLuint fbo_id );
  void  Draw_OES(GLuint *objeto3D,int numerovertices,int textura,GLuint shader,float *scala,GLuint imagen);
  void Drawstar(const GLvoid* vertices, int numerovertices,int textura,GLuint shader,float cr,float cg,float cb);
void iconos(const GLvoid* vertices, int numerovertices,int textura,float tamano,GLuint shader);
void Distancia(double Numero);void Dibujaiconos();
void Letras(const char *frase);
void loadOBJ3D(GLuint *objeto3D,int puntos, const GLvoid* vertices,const GLvoid* normales,const GLvoid* texturasc);
Punto *punto;	float incli=0;float *puntocalle;Punto *menu=new Punto[5];Punto ballFire;

class  Objetos3D
   {
   public:
   	GLuint vBO[3];

   };


  Texture** textures              = 0;int esquin=0;
float tiempo=0.0;
   GLint vertexHandle              = 0;
   GLint normalHandle              = 0;
   GLint textureCoordHandle        = 0;
   GLint mvpMatrixHandle           = 0;

   GLint colorLoc = 0;
     GLint samplerLoc = 0;
     GLint mz          = 0;

     GLint timeLoc = 0;



     static const char gVertexShader[] = " \
     		  \
     		attribute vec4 vertexPosition; \
    		 uniform vec3 scale; \
     		attribute vec4 vertexNormal; \
     		attribute vec2 vertexTexCoord; \
     		varying vec2 texCoord; \
     		varying vec4 normal;\
     		 \
             uniform mediump float bend;\
             uniform mediump float minimize;\
             float side=-1.0;\
             float width=0.1;\
             float height=0.1;\
            \
     		uniform mat4 matrizp; \
     		 \
     		void main() \
     		{ \
    		 \
             mediump vec4 pos=vertexPosition;\
             pos.y = mix(vertexPosition.y, height, minimize);\
             mediump float t = pos.y / height;\
             t = (3. - 2. * t) * t * t;\
             pos.x = mix(vertexPosition.x, side * width, t * bend);\
             gl_Position=matrizp*(pos*vec4(scale,1.0));\
     		 normal = vertexNormal; \
            texCoord = vertexTexCoord; \
     		} \
     		";

            //
     static const char gFragmentShader[] = " \
     		 \
     		precision mediump float; \
     		 \
     		varying vec2 texCoord; \
     		varying vec4 normal; \
     		 \
     		uniform sampler2D texSampler2D; \
     		 \
     		void main() \
     		{ \
                   vec4 v=texture2D(texSampler2D, texCoord);\
                   if(v.x==0.0&&v.y==0.0&&v.z==0.0)\
                   {\
                    v=vec4(0.0,0.0,0.0,0.0); \
                   }\
               gl_FragColor = v*vec4(1.0,1.0,1.0,0.5); \
     		} \
     		";
     static const char gVertexShaderFBO[] = " \
     		  \
     		attribute vec4 vertexPosition; \
     		attribute vec4 vertexNormal; \
     		attribute vec2 vertexTexCoord; \
     		varying vec2 texCoord; \
     		varying vec4 normal; \
     		 \
     		uniform mat4 matrizp; \
     		 \
     		void main() \
     		{ \
     		   gl_Position =matrizp*vertexPosition; \
     		 normal = vertexNormal; \
        texCoord = vertexTexCoord; \
     		} \
     		";



     static const char gFragmentShaderFBO[] = " \
 \
     		precision mediump float; \
     		 vec2 resolution=vec2(1.0,1.0);uniform float     time;\
     		varying vec2 texCoord; \
     		varying vec4 normal; \
     		 \
     		uniform sampler2D texSampler2D; \
     		 \
     		void main() \
    		 {vec2 v_texCoord = texCoord.xy / resolution; \
    		 vec2 p =  v_texCoord * 8.0 - vec2(20.0);\
    		             vec2 i = p;\
    		             float c = 1.0;\
    		             float inten = .05;\
\
    		             for (int n = 0; n < 3; n++)\
    		             {\
                             float t = sin(time*4.0*3.1416/720.0) * (1.0 - (3.0 / float(n+1)));\
\
    		                 i = p + vec2(cos(t - i.x) + sin(t + i.y),\
    		                 sin(t - i.y) + cos(t + i.x));\
\
    		                 c += 1.0/length(vec2(p.x / (sin(i.x+t)/inten),\
    		                 p.y / (cos(i.y+t)/inten)));\
    		             }\
\
    		             c /= float(3);\
    		             c = 1.5 - sqrt(c);\
\
    		            vec4 texColor = vec4(.30, .1, 0.0, 1.);\
\
            texColor.rgb *= (1.0 / (1.0 - (c + 0.05)));\
		float gray = dot(texColor.rgb, vec3(0.598, 1.174, 0.228));\
            gl_FragColor = vec4(texColor.rgb,gray);\
     		} \
     		";




     static const char gVertexShaderCamera[] = " \
     		  \
     		attribute vec4 vertexPosition; \
    		 uniform vec3 scale; \
     		attribute vec4 vertexNormal; \
     		attribute vec2 vertexTexCoord; \
     		varying vec2 texCoord; \
     		varying vec4 normal;\
     		 \
     		uniform mat4 matrizp; \
     		 \
     		void main() \
     		{ \
    		 \
     		  gl_Position=matrizp*(vertexPosition*vec4(scale,1.0));\
     		 normal = vertexNormal; \
        texCoord = vertexTexCoord; \
     		} \
     		";


     static const char gFragmentShaderCamera[] = ""
         		"#extension GL_OES_EGL_image_external : require\n"
         		"precision mediump float;\n "
    "           uniform float     time;\n"
         		"varying vec2 texCoord;\n "
         	"	varying vec4 normal;\n "
            "vec2 resolution=vec2(640.,480.);\n"
         		"uniform samplerExternalOES texSampler2D;\n "

         		"void main()\n "
                "{\n "
             "gl_FragColor= texture2D(texSampler2D, texCoord)*vec4(1.0,1.0,1.0,1.0);\n"
         		"}\n"
         		"";
   
     static const char gVertexShaderMain[] = " \
            		  \
            		attribute vec4 vertexPosition; \
            		attribute vec4 vertexNormal; \
            		attribute vec2 vertexTexCoord; \
            		varying vec2 texCoord; \
            		varying vec4 normal; \
            		 \
            		uniform mat4 matrizp; \
            		 \
            		void main() \
            		{ \
            		   gl_Position =matrizp*vertexPosition; \
            		 normal = vertexNormal; \
               texCoord = vertexTexCoord; \
            		} \
            		";

     static const char gFragmentShaderMain[] = " \
         		 \
         		precision mediump float; \
         		 uniform vec4 u_color;		                           \
         		varying vec2 texCoord; \
         		varying vec4 normal; \
         		 \
         		uniform sampler2D texSampler2D; \
         		 \
         		void main() \
         		{ \
                   gl_FragColor = texture2D(texSampler2D, texCoord); \
         		} \
         		";

     static const char gVertexShaderParticula[]= " \
    		 uniform float u_time;	\
    		attribute vec4 vertexPosition; \
    		 varying float v_lifetime;\
    		uniform mat4 mz;\
    		 \
    		void main() \
    		{ \
    		 float x = 0.0;\
    float y = 0.0;\
 \
    gl_Position = mz *vertexPosition;\
    gl_PointSize = 120.0;\
    		} \
    		";

     static const char gFragmentShaderParticula[] = " \
        		 \
        		precision mediump float;                             \
              uniform vec4 u_color;		                           \
              varying float v_lifetime;                            \
              uniform sampler2D texSampler2D;                         \
              void main()                                          \
              {                                                    \
                gl_FragColor = texture2D(texSampler2D, gl_PointCoord )*vec4( u_color ) ;         \
                                  \
        		} \
        		";




     static const char gVertexShaderMap[] = " \
     		  \
     		attribute vec4 vertexPosition; \
    		 uniform vec3 scale; \
     		attribute vec4 vertexNormal; \
     		attribute vec2 vertexTexCoord; \
     		varying vec2 texCoord; \
     		varying vec4 normal;\
     		 \
     		uniform mat4 matrizp; \
     		 \
     		void main() \
     		{ \
    		 \
     		  gl_Position=matrizp*(vertexPosition*vec4(scale,1.0));\
     		 normal = vertexNormal; \
        texCoord = vertexTexCoord; \
     		} \
     		";


     static const char gFragmentShaderMap[] = ""
             		"#extension GL_OES_EGL_image_external : require\n"
             		"precision mediump float;\n "

             		"varying vec2 texCoord;\n "
             	    "varying vec4 normal;\n "
             		"uniform samplerExternalOES texSampler2D;\n "
        		 	 "uniform float     time;"
             		"void main()\n "
             		"{\n "
                    "gl_FragColor =texture2D(texSampler2D, texCoord)*vec4(1.0,1.0,1.0,0.6);\n "
             		"}\n"
             		"";




     extern "C" {
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNILib_init(JNIEnv * env, jobject obj,  jint width, jint height);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNILib_step(JNIEnv * env, jobject obj);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_initApplicationNative(JNIEnv * env, jobject obj);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_nativeTouchEvent(JNIEnv * env, jobject obj, jfloat w, jfloat h);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_scrooll(JNIEnv * env, jobject obj, jfloat w, jfloat h);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_valores(JNIEnv*, jobject,jdouble longitud,jdouble latitud, jdouble altitud, jfloat angulito,jfloat vertical,jfloat inclinacion);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_loadicons(JNIEnv * env, jobject obj,jfloat longitud,jfloat latitud,jstring title);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_construyecalle(JNIEnv*, jobject);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_cargarcalle(JNIEnv*, jobject,jfloat longitud,jfloat latitud);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_loadesquinas(JNIEnv*, jobject,jfloat longitud,jfloat latitud);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_loadpesos(JNIEnv*, jobject,jint fin,jint inicio);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_colocacamino(JNIEnv*, jobject);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_recorremenu(JNIEnv*, jobject,jfloat x,jfloat y);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_nombrescalles(JNIEnv * env, jobject obj,jfloat longitud,jfloat latitud,jstring title);
         JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNIActivity_muevecover(JNIEnv * env, jobject obj, jfloat op);

         JNIEXPORT jint  JNICALL Java_com_android_gl2jni_GL2JNILib_numtextura(JNIEnv * env, jobject obj);
         JNIEXPORT jint  JNICALL Java_com_android_gl2jni_GL2JNILib_getTexturaMapa(JNIEnv * env, jobject obj);
         JNIEXPORT jint  JNICALL Java_com_android_gl2jni_GL2JNILib_getTexturaScore(JNIEnv * env, jobject obj);
         JNIEXPORT jint  JNICALL Java_com_android_gl2jni_GL2JNILib_getCoinsLeft(JNIEnv * env, jobject obj);
         JNIEXPORT void  JNICALL Java_com_android_gl2jni_GL2JNILib_setPos(JNIEnv * env, jobject obj,double mx,double my);



     };

