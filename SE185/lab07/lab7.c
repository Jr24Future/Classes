/*-----------------------------------------------------------------------------
-					          SE 185 Lab 07 - The DS4 Equalizer
-             Developed for 185-Rursch by T.Tran and K.Wang
-	Name:
- 	Section:
-	NetID:
-	Date:
-
-   This file provides the outline for your program
-   Please implement the functions given by the prototypes below and
-   complete the main function to make the program complete.
-   You must implement the functions which are prototyped below exactly
-   as they are requested.
-----------------------------------------------------------------------------*/

/*-----------------------------------------------------------------------------
-								Includes
-----------------------------------------------------------------------------*/
#include <stdio.h>
#include <math.h>


/*-----------------------------------------------------------------------------
-								Defines
-----------------------------------------------------------------------------*/
#define PI 3.141592653589

/* NO GLOBAL VARIABLES ALLOWED */


/*-----------------------------------------------------------------------------
-								Prototypes
-----------------------------------------------------------------------------*/


/*------------------------------------------------------------------------------------
    PRE: Arguments must point to double variables or int variables as appropriate
    This function scans a line of DS4 data, and returns
    True when left button is pressed
    False Otherwise
    POST: it modifies its arguments to return values read from the input line.
------------------------------------------------------------------------------------*/
int read_input( int* time,
                double* g_x, double* g_y, double* g_z,
                int* button_T, int* button_C, int* button_X, int* button_S,
                int* l_joy_x, int* l_joy_y, int* r_joy_x, int* r_joy_y );

/*-----------------------------------------------------------------------------
    PRE: ~(-1.0) <= mag <= ~(1.0)
    This function scales the roll/pitch value to fit on the screen.
    Input should be capped at either -1.0 or 1.0 before the rest of your
    conversion.
    POST: -39 <= return value <= 39
-----------------------------------------------------------------------------*/
int scaleMagForScreen(double rad);
/* {
    if (rad < -1) {
        rad = -1;
        return(rad * 39);
    }
    else if (rad > 1) {
        rad = 1;
        return(rad * 39);
    }
}*/
/*-----------------------------------------------------------------------------
    PRE: -128 <= mag <= 127
    This function scales the joystick value to fit on the screen.
    POST: -39 <= return value <= 39
-----------------------------------------------------------------------------*/
int scaleJoyForScreen(int rad);/* {
    if (rad < -128) {
        rad = -1;
        return(rad * 39);
    }
    else if (rad > 127) {
        rad = 1;
        return(rad * 39);
    }
}*/
/*----------------------------------------------------------------------------
    PRE: -39 <= number <= 39
    Uses print_chars to graph a number from -39 to 39 on the screen.
    You may assume that the screen is 80 characters wide.
----------------------------------------------------------------------------*/
void graph_line(int number); 
/*
{
    if (number == 0) {
        print_chars(40, ' ');
        printf("0");
    }
    else if (number > 0){
        print_chars(40 - number, ' ');
        print_chars(number, 'L');
    }
    else
    {
        print_chars(40, ' ');
        print_chars(number, 'R');
    }
    printf("\n");
}*/
/*-----------------------------------------------------------------------------
    PRE: num >= 0
    This function prints the character "use" to the screen "num" times
    This function is the ONLY place printf is allowed to be used
    POST: nothing is returned, but "use" has been printed "num" times
-----------------------------------------------------------------------------*/
void print_chars(int num, char use);
   /*{ int count = 0;
    while (count < num) {
        count++;
        printf("%c", use);
    }
}
*/

/*-----------------------------------------------------------------------------
-								Implementation
-----------------------------------------------------------------------------*/
int main()
{
    double x, y, z;                     /* Values of x, y, and z axis*/
    int t;                              /* Variable to hold the time value */
    int b_Up, b_Down, b_Left, b_Right;  /* Variables to hold the button statuses */
    int j_LX, j_LY, j_RX, j_RY;         /* Variables to hold the joystick statuses */
    int scaled_pitch, scaled_roll; 	    /* Value of the roll/pitch adjusted to fit screen display */
    int scaled_joy;                     /* Value of joystick adjusted to fit screen display */
    int mood = 0;
    /* Put pre-loop preparation code here */

    do
    {
        scanf(" %d, %lf, %lf, %lf, %d, %d, %d, %d, %d, %d, %d, %d",
            &t, &x, &y, &z, &b_Up, &b_Right, &b_Down, &b_Left, &j_LX, &j_LY, &j_RX, &j_RY);
        //printf(" %d, %lf, %lf, %lf, %d, %d, %d, %d, %d, %d, %d, %d\n",
            //t, x, y, z, b_Up, b_Down, b_Left, b_Right, j_LX, j_LY, j_RX, j_RY);
       // graph_line(scaleMagForScreen(x));
       // graph_line(scaleMagForScreen(z));

        /* Calculate and scale for pitch AND roll AND joystick */

        if (b_Up == 1) {
            mood = 1;
        }
        if (b_Down == 1) {
            mood = 0;
        }
        if (b_Right == 1)
            mood = 2;
        /* Switch between roll, pitch, and joystick with the up, down, and right button, respectivly */
        if (mood == 1)
            graph_line(scaleMagForScreen(z));
        else if (mood == 0)
            graph_line(scaleMagForScreen(x));
        else
        graph_line(scaleJoyForScreen(j_RX));
        
        

        /* Output your graph line */


        fflush(stdout);

    } while (!(b_Left)); /* Modify to stop when left button is pressed */

    return 0;

}

int scaleMagForScreen(double rad) {
    if (rad < -1) {
        rad = -1;
    }
    else if (rad > 1) {
        rad = 1;
    }
    return(rad * 39);
}

int scaleJoyForScreen(int rad) {
    return (rad / (128 / 39));

}
void graph_line(int number) {
   // printf("%d", number);
    if (number == 0) {
        print_chars(40, ' ');
        printf("0");
    }
    else if (number > 0) {
        print_chars(40 - number, ' ');
        print_chars(number, 'L');
    }
    else if( number < 0)
    {
        print_chars(41, ' ');
        print_chars(number * -1, 'R');
    }
    printf("\n");
}
void print_chars(int num, char use) {
    int count = 0;
    while (count < num) {
        count++;
         printf("%c", use);
    }
}