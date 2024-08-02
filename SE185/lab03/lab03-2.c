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
int numButtons(int triangle, int circle, int cross, int square);

/*----------------------------------------------------------------------------
-	                                Notes                                    -
-----------------------------------------------------------------------------*/
// Compile with gcc lab03-2.c -o lab03-2
// Run with ./ds4rd.exe -d 054c:05c4 -D DS4_BT -b | ./lab03-2

/*----------------------------------------------------------------------------
-								Implementation								 -
-----------------------------------------------------------------------------*/
int main(int argc, char *argv[])
{
    int t;
    int c;
    int x;
    int s;


    while (1)
    {
        scanf("%d, %d, %d, %d", &t, &c, &x, &s);
        printf("Number of buttons pressed:  %d\n", numButtons(t, c, x, s));
        fflush(stdout);
    }

    return 0;
}

int numButtons(int triangle, int circle, int cross, int square) {
    int totalButton;
    totalButton = triangle + circle + cross + square;
    return totalButton;
}


