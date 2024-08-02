/*-----------------------------------------------------------------------------
-					  		SE 185 Lab 08
-             Developed for 185-Rursch by T.Tran and K.Wang
-	Name:
- 	Section:
-	NetID:
-	Date:
-----------------------------------------------------------------------------*/

/*-----------------------------------------------------------------------------
-								Includes
-----------------------------------------------------------------------------*/
#include <stdio.h>


/*-----------------------------------------------------------------------------
-								Defines
-----------------------------------------------------------------------------*/
#define MAXPOINTS 10000


/*-----------------------------------------------------------------------------
-								Prototypes
-----------------------------------------------------------------------------*/
/* Updates the buffer with the new_item and returns the computed
moving average of the updated buffer */
double m_avg(double buffer[], int avg_size, double new_item);


/*-----------------------------------------------------------------------------
-								Implementation
-----------------------------------------------------------------------------*/
int main(int argc, char* argv[]) {

    /* DO NOT CHANGE THIS PART OF THE CODE */
    double x[MAXPOINTS], y[MAXPOINTS], z[MAXPOINTS];
    double new_x, new_y, new_z;
    double avg_x, avg_y, avg_z;
    int lengthofavg = 0;
    if (argc>1) {
        sscanf(argv[1], "%d", &lengthofavg );
        printf("You entered a buffer length of %d\n", lengthofavg);
    }
    else {
        printf("Enter a length on the command line\n");
        return -1;
    }
    if (lengthofavg <1 || lengthofavg >MAXPOINTS) {
       printf("Invalid length\n");
        return -1;
    }

    for(int i = 0; i < lengthofavg; i++)
    {
        scanf("%lf, %lf, %lf", &new_x, &new_y, &new_z);
        x[i] = new_x;
        y[i] = new_y;
        z[i] = new_z;
    }

    while(1)
    {
        scanf("%lf, %lf, %lf", &new_x, &new_y, &new_z);

        avg_x = m_avg(x, lengthofavg, new_x);
        avg_y = m_avg(y, lengthofavg, new_y);
        avg_z = m_avg(z, lengthofavg, new_z);

        printf("RAW, %lf, %lf, %lf, AVG ,%lf, %lf, %lf\n", new_x, new_y, new_z, avg_x, avg_y, avg_z);
        printf("%lf", avg_x);
        fflush(stdout);
    }

}

double m_avg(double buffer[], int avg_size, double new_item)
{
//./ds4rd-real.exe -d 054c:05c4 -D DS4_BT -g | ./lab8_1 4

   int k;
    for (k = 0; k < avg_size - 1; k++)
        buffer[k] = buffer[k + 1];

    buffer[k] = new_item;
    double sum = 0;
    for (k = 0; k < avg_size; k++) {
        sum += buffer[k];
    }
    return sum / avg_size;

   /* double j = 0.0;
    int i;
    for (i = 0; i < avg_size; i++) {
        j += buffer[i];
    }
    
    return j/avg_size;*/
}
