/*----------------------------------------------------------------------------
-		         SE 185: Lab 05 - Conditionals (What's up?)	    	         -
-	Name:																	 -
- 	Section:																 -
-	NetID:																     -
-	Date:																	 -
-----------------------------------------------------------------------------*/

/*----------------------------------------------------------------------------
-								Includes									 -
-----------------------------------------------------------------------------*/
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>

/*----------------------------------------------------------------------------
-	                            Prototypes                                   -
-----------------------------------------------------------------------------*/
int close_to(double tolerance, double point, double value) {
    double temp1 = point - tolerance;
    double temp2 = point + tolerance;
    if (value >= temp1 && value <= temp2) {
        return 1;
    }
    else
    {
        return 0;
    }
}
double magnitude(double x, double y, double z)
{
    return sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2));
}
double t = 0.04;
int hey(double gx, double gy, double gz, int lastOutput)
{
    if (close_to(t, 1, gy)) {
            return 1;
    }
    else if (close_to(t, -1, gy)) {
            return 2; 
    }
    else if (close_to(t, -1, gz)) {
        return 3;
    }
    else if (close_to(t, 1, gz)) {
            return 4;
    }
    else if (close_to(t, -1, gx)) {
            return 5;
    }
    else if (close_to(t, 1, gx)) {
            return 6;
    }
    return 0;
} 
/*----------------------------------------------------------------------------
-	                                Notes                                    -
-----------------------------------------------------------------------------*/
// Compile with gcc lab05.c -o lab05
// Run with ./ds4rd.exe -d 054c:05c4 -D DS4_BT -a -g -b | ./lab05

/*----------------------------------------------------------------------------
-								Implementation								 -
-----------------------------------------------------------------------------*/
int main(int argc, char *argv[])
{
    int triangle, circle, x_button, square;
    double ax, ay, az, gx, gy, gz;
    int lastOutput;
    int test;
       

    while (1)
    {
        scanf(" %lf, %lf, %lf, %lf, %lf, %lf, %d, %d, %d, %d",
              &ax, &ay, &az, &gx, &gy, &gz, &triangle, &circle, &x_button, &square);

        
        //printf("Echoing output: %lf, %lf, %lf, %lf, %lf, %lf, %d, %d, %d, %d \n",
              // ax, ay, az, gx, gy, gz, triangle, circle, x_button, square);

        test = hey(gx, gy, gz, lastOutput);
        if (magnitude(ax, ay, az) < 0.05 && test != lastOutput) {
         

          if (test == 1) {
            printf("top\n");
            lastOutput = 1;
        }
        else if (test == 2) {
            printf("bottom\n");
            lastOutput = 2;
        }
        else if (test == 3) {
            printf("front\n");
            lastOutput = 3;
        }
        else if (test == 4) {
            printf("back\n");
            lastOutput = 4;
        }
        else if (test == 5) {
            printf("right\n");
            lastOutput = 5;
        }
        else if (test == 6) {
            printf("left\n");
            lastOutput = 6;
        }
}

        if (triangle) {
            exit(0);
        }
    }

    return 0;
}

/* Put your functions here, and be sure to put prototypes above. */