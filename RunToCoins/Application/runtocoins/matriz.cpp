#include "matriz.h"
#include <math.h>
#include <android/log.h>
#include <jni.h>

#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,"matrix",__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,"matrix",__VA_ARGS__)

float *Monumento=new float[16];


void matriz::longitud(double X,double Y,double Z)
{
Monumento[12]=X;Monumento[13]=Y;Monumento[14]=Z;Monumento[15]= 1.000000f;
}
void matriz::yxz(double An,double Bn,double Cn){
  Monumento[0]=cos(((Bn)*2*M_PI)/360)*cos(((Cn)*2*M_PI)/360);		Monumento[1]=-cos(((Bn)*2*M_PI)/360)*sin(((Cn)*2*M_PI)/360)*sin(((An)*2*M_PI)/360)+sin(((Bn)*2*M_PI)/360)*sin(((An)*2*M_PI)/360);				Monumento[2]=cos(((Bn)*2*M_PI)/360)*sin(((Cn)*2*M_PI)/360)*sin(((An)*2*M_PI)/360)+sin(((Bn)*2*M_PI)/360)*cos(((An)*2*M_PI)/360);				Monumento[3]=  0.000000f;
  	Monumento[4]=sin(((Cn)*2*M_PI)/360);							  	Monumento[5]=cos(((Cn)*2*M_PI)/360)*cos(((An)*2*M_PI)/360);																						Monumento[6]=-cos(((Cn)*2*M_PI)/360)*sin(((An)*2*M_PI)/360);																						Monumento[7]=  0.000000f;
  	Monumento[8]=-sin(((Bn)*2*M_PI)/360)*cos(((Cn)*2*M_PI)/360);		Monumento[9]=sin(((Bn)*2*M_PI)/360)*sin(((Cn)*2*M_PI)/360)*cos(((An)*2*M_PI)/360)+cos(((Bn)*2*M_PI)/360)*sin(((An)*2*M_PI)/360);				Monumento[10]=-sin(((Bn)*2*M_PI)/360)*sin(((Cn)*2*M_PI)/360)*sin(((An)*2*M_PI)/360)+cos(((Bn)*2*M_PI)/360)*cos(((An)*2*M_PI)/360);			Monumento[11]= 0.000000f;


}

void
matriz::R(double angle, double x, double y, double z)
{
    double radians, c, s, c1, u[3], length;
    int i, j;

    radians = (angle * M_PI) / 180.0;

    c = cos(radians);
    s = sin(radians);

    c1 = 1.0 - cos(radians);

    length = sqrt(x * x + y * y + z * z);

    u[0] = x / length;
    u[1] = y / length;
    u[2] = z / length;

    for (i = 0; i < 16; i++)
    	Monumento[i] = 0.0;

    Monumento[15] = 1.0;

    for (i = 0; i < 3; i++)
    {
    	Monumento[i * 4 + (i + 1) % 3] = u[(i + 2) % 3] * s;
    	Monumento[i * 4 + (i + 2) % 3] = -u[(i + 1) % 3] * s;
    }

    for (i = 0; i < 3; i++)
    {
        for (j = 0; j < 3; j++)
        	Monumento[i * 4 + j] += c1 * u[i] * u[j] + (i == j ? c : 0.0);
    }
}
void matriz::escala(double sx,double sy,double sz)
{
	Monumento[0]  *= sx;
	Monumento[1]  *= sx;
	Monumento[2]  *= sx;
	Monumento[3]  *= sx;

	Monumento[4]  *= sy;
	Monumento[5]  *= sy;
	Monumento[6]  *= sy;
	Monumento[7]  *= sy;

	Monumento[8]  *= sz;
	Monumento[9]  *= sz;
	Monumento[10] *= sz;
	Monumento[11] *= sz;
//LOGI("matrix %f",Monumento[0]);;

}
void   matriz::identidad()
   {
   Monumento[0]=1; Monumento[1]=0; Monumento[2]=0; Monumento[3]=0;
   Monumento[4]=0; Monumento[5]=1; Monumento[6]=0; Monumento[7]=0;
   Monumento[8]=0; Monumento[9]=0; Monumento[10]=1; Monumento[11]=0;
   Monumento[15]=1;

   }

float matriz::regresa(int a)
{
return Monumento[a];
}




void
matriz::multiply()
{
	float *modelViewMatrix=new float[16];

	 float l = -1.f;
	 float r = 1.0f;
	 float b = -1.0f;
	 float t = 1.0f;
	float n = 1.0f;
    float f = 10.0f;//10

	modelViewMatrix[0]=1; modelViewMatrix[1]=0; modelViewMatrix[2]=0; modelViewMatrix[3]=0;
	modelViewMatrix[4]=0; modelViewMatrix[5]=1; modelViewMatrix[6]=0; modelViewMatrix[7]=0;
	modelViewMatrix[8]=0; modelViewMatrix[9]=0; modelViewMatrix[10]=1; modelViewMatrix[11]=0;
	modelViewMatrix[12]=0;modelViewMatrix[13]=0;modelViewMatrix[14]=0;modelViewMatrix[15]=1;
	modelViewMatrix[0]  = 2 / (r - l);
	modelViewMatrix[3]  = -(r + l) / (r - l);
	modelViewMatrix[5]  = 2 / (t - b);
	modelViewMatrix[7]  = -(t + b) / (t - b);
	modelViewMatrix[10] = -2 / (f - n);
	modelViewMatrix[11] = -(f + n) / (f - n);
    int i, j, k;
    float aTmp[16];

    for (i = 0; i < 4; i++)
    {
        for (j = 0; j < 4; j++)
        {
            aTmp[j * 4 + i] = 0.0;

            for (k = 0; k < 4; k++)
                aTmp[j * 4 + i] += modelViewMatrix[k * 4 + i] * Monumento[j * 4 + k];
        }
    }

    for (i = 0; i < 16; i++)
    	Monumento[i] = aTmp[i];
}
