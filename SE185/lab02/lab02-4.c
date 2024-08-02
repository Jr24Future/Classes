/*----------------------------------------------------------------------------
-		        SE 185: Lab 02 - Solving Simple Problems in C	    	 	 
-	Name:       Erroll Barker  																 
- 	Section:    4																 
-	NetID:    	errollb															     
-	Date:		09/07/2022															 
-----------------------------------------------------------------------------*/

/*----------------------------------------------------------------------------
-								Includes									 -
-----------------------------------------------------------------------------*/
#include <stdio.h>
#include <math.h>

/*----------------------------------------------------------------------------
-								Implementation								 -
-----------------------------------------------------------------------------*/
int main(int argc, char *argv[])
{
    int a = 6427 + 1725;
    printf("6427 + 1725 = %d\n", a);
    int b = 6427 + 1725 - 67;
    printf("6427 + 1725 - 97 = %d\n", b);
    double c = 79 + 12 / 5;
    printf("79 + 12 / 5 = %.2lf\n", c);
    double d = 3640.0 / 107.9;
    printf("3640.0 / 107.9 = %.2lf\n", d);
    int e = (22 / 3) * 3;
    printf("(22 / 3) * 3 = %d\n", e);
    int f = 22 / (3 * 3);
    printf("22 / (3 * 3) = %d\n", f);
    double g = 22 / (3 * 3);
    printf("22 / (3 * 3) = %.2lf\n", g);
    double h = 22 / 3 * 3;
    printf("22 / 3 * 3 = %.2lf\n", h);
    double i = (22.0 / 3) * 3.0;
    printf("(22.0/3) * 3.0 = %.2lf\n", i);
    int j = 22.0 / (3 * 3.0);
    printf("22.0 / (3 * 3.0) = %d\n", j);
    double k = 22.0 / 3.0 * 3.0;
    printf("22.0 / 3.0 * 3.0 = %.2lf\n", k);


    //area = pi * r**2
    //r = c /(2*pi)
    const double PI = 3.14159265359;
    const double Circumference = 23.567;
    double r = Circumference / (2 * PI);
    double area = PI * pow((r), 2);
    printf("\n\nArea: %.2lf\n\n", area);

    //m = f /.3048
    const double FEET = 14;
    double m = FEET / .3048;
    printf("\nLenth: %.2lf meters", m);

    //Tc = (Tf - 32) /1.8
    const double Tf = 76.0;
    double Tc = (Tf - 32.0) / 1.8;
    printf("\nTemp: %.2lf Degrees Celcius", Tc);
   return 0;
}
