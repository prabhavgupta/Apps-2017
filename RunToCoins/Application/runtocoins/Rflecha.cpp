
#include "Rflecha.h"
#include <android/log.h>
#include <math.h>
#include <stdlib.h>

static double PI=3.14159265;
static double p1x=0,p1y=0,p2x=0,p2y=0;
static double Ang=0,Angul0=0;

double Boton::distancia()
{
	                 double latitud1=p1y*(PI/180);
	                 double	 longitud1=p1x*(PI/180);
			         double latitud2=p2y*(PI/180);
			         double	 longitud2=p2x*(PI/180);

					 double Radio= 6378137;
					 double dLat = (latitud2-latitud1);
					 double dLon = (longitud2-longitud1);
					 double a = sin(dLat/2) * sin(dLat/2) +
					         cos(latitud1) * cos(latitud2) *
					         sin(dLon/2) * sin(dLon/2);
					 double c = 2 * atan2(sqrt(a),sqrt(1-a));
					 double d = Radio * c;
					 return d;
}

double Rflecha::myangulo(double Rumbo)
{
if(Rumbo<=0)
	Ang=(Rumbo*-1)+90;
else if(Rumbo>0&&Rumbo<90)
	Ang=90-Rumbo;
else if(Rumbo>=90&&Rumbo<=180)
		Ang=450-Rumbo;

		return Ang;

}
double Rflecha::angulo(double Rumbo,double apunta)
{	if(Rumbo<=0)
	Ang=(Rumbo*-1)+90;
else if(Rumbo>0&&Rumbo<90)
	Ang=90-Rumbo;
else if(Rumbo>=90&&Rumbo<=180)
		Ang=450-Rumbo;

		return (-1*Ang)+apunta;
		//(-1*Ang)-0; norte
		//(-1*Ang)-90;este
		//(-1*Ang)-180; sur
		//(-1*Ang)-270; oeste
}

double Rflecha::calcula(double punto1x,double punto1y,double punto2x,double punto2y,double Rumbo)
{
p1x=punto1x;
p1y=punto1y;
p2x=punto2x;
p2y=punto2y;
	//menu
	//LOG("pendiente");
	double P1=punto1y,P2=punto2y;
	double AnguloPendiente=(atan((punto2y-punto1y)/(punto2x-punto1x)))*(180/ PI);

	if(Rumbo<=0)
		Ang=(Rumbo*-1)+90;
	else if(Rumbo>0&&Rumbo<90)
		Ang=90-Rumbo;
	else if(Rumbo>=90&&Rumbo<=180)
			Ang=450-Rumbo;
	//Ang=referencia-Ang;




	if(AnguloPendiente>0)
	{
	//punto central
	if(P2>P1)
			{
			Angul0=AnguloPendiente-90;
			LOGI("cuadrante: 1 %f",Angul0);

			return 1;
			}
			else if(P1>P2)
			{
				Angul0=AnguloPendiente+180;
				LOGI("cuadrante: 3 %f",Angul0);
				return 3;
			}


	}
	else if(AnguloPendiente<0)
	{
		if(P2>P1)
			{
			Angul0=Ang-AnguloPendiente;
			LOGI("cuadrante: 2 %f",Angul0);
			return 2;

			}
			else if(P1>P2)
			{
				Angul0=-450+AnguloPendiente+360;
				LOGI("cuadrante: 4 %f",Angul0);
				return 4;

			}
	}

	return 0;
}
