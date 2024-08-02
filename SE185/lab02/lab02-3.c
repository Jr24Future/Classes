/*----------------------------------------------------------------------------
-		        SE 185: Lab 02 - Solving Simple Problems in C	    	 	 -
-	Name:																	 -
- 	Section:																 -
-	NetID:																     -
-	Date:																	 -
-----------------------------------------------------------------------------*/

/*----------------------------------------------------------------------------
-								Includes									 -
-----------------------------------------------------------------------------*/
#include <stdio.h>

/*----------------------------------------------------------------------------
-								Implementation								 -
-----------------------------------------------------------------------------*/
int main(int argc, char *argv[])
{
    int integer_result;
    double decimal_result;

    integer_result = 77 / 5;
    printf("The value of 77/5 is %d, using integer math.\n", integer_result); //%lf to %d reason becuase lf is for flot and d is for numbers

    integer_result = 2 + 3;
    printf("The value of 2+3 is %d.\n", integer_result);  // interger_result add on

    decimal_result = 1.0 / 22.0;
    printf("The value 1.0/22.0 is %lf.\n", decimal_result);  // %d to %lf reason beacuse d is for numbers and lf is for flot

    return 0;
}
