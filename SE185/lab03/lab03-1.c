/*----------------------------------------------------------------------------
-		    SE 185: Lab 03 - Introduction to the DS4 and Functions	    	 -
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

/*----------------------------------------------------------------------------
-	                            Prototypes                                   -
-----------------------------------------------------------------------------*/
double magnitude(double x, double y, double z);
int minutes(int ms) {
    int m = ms / 60000;
    return (m);
}

int seconds(int ms) {
    int s = ms - (minutes(ms) * 60000);
    s = s / 1000;
    return (s);
}
int milliseconds(int ms) {
    int milli = ms - (seconds(ms)*1000 + minutes(ms)*60000);
    milli % 1000;
    return (milli);
}
/*----------------------------------------------------------------------------
-	                                Notes                                    -
-----------------------------------------------------------------------------*/
// Compile with gcc lab03-1.c -o lab03-1
// Run with ./ds4rd.exe -d 054c:05c4 -D DS4_BT -t -a | ./lab03-1

/*----------------------------------------------------------------------------
-								Implementation								 -
-----------------------------------------------------------------------------*/
int main(int argc, char *argv[])
{
    /* DO NOT MODIFY THESE VARIABLE DECLARATIONS */
    int t;
    double ax, ay, az;

    while (1)
    {
        scanf("%d, %lf, %lf, %lf", &t, &ax, &ay, &az);

        /* CODE SECTION 0 */
        double secouds = t / 1000.0;
        printf("Echoing output: %8.3lf, %.4lf, %.4lf, %.4lf\n", secouds, ax, ay, az);


        /* 	CODE SECTION 1 */
       // printf("At %d ms, the acceleration's magnitude was: %lf\n", t, magnitude(ax, ay, az));


        /* 	CODE SECTION 2 */
        
            printf("At %d minutes, %d seconds, and %d milliseconds it was: %lf\n",
            minutes(t), seconds(t), milliseconds(t), magnitude(ax, ay, az));
        
    }

    return 0;
}

/* Put your functions here */


double magnitude(double x, double y, double z)
{
    // Step 8, uncomment and modify the next line
     //return sqrt(pow(x,2) + pow(y,2) + pow(z,2));
}

