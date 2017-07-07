class matriz
{
public:
     static void  yxz(double An,double Bn,double Cn );
     static void  identidad();
     static float regresa(int a);
     static void longitud(double X,double Y, double Z);
     static void  escala(double sx,double sy,double sz );
     static void  R(double angle, double x, double y, double z);
     static void  multiply();
};


class Punto
{
public:
	double x;
	double y;
	double z;
const	char *titulo;
int textura;
int tipo;
bool touch;
double distancia;
};
