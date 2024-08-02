/*----------------------------------------------------------------------------
-		                    SE 185: Lab 06 - Bop-It!	    	             -
-	Name:																	 -
- 	Section:																 -
-	NetID:																     -
-	Date:																	 -
-----------------------------------------------------------------------------*/

/*----------------------------------------------------------------------------
-								Includes									 -
-----------------------------------------------------------------------------*/
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

/*----------------------------------------------------------------------------
-	                            Prototypes                                   -
-----------------------------------------------------------------------------*/
int selectRandom(int x) {
	static int reslt;
	static int count = 0;
	count++;

	if (count == 1) 
	{
		reslt = x;
	}
	else
	{
		count - 1;
			int i = rand() % count;
		if (i == count - 1)
			reslt = x;
	}
	return reslt;
}

/*----------------------------------------------------------------------------
-	                            Notes                                        -
-----------------------------------------------------------------------------*/
// Compile with gcc lab06.c -o lab06
// Run with ./ds4rd.exe -d 054c:05c4 -D DS4_BT -t -b | ./lab06

/*----------------------------------------------------------------------------
-								Implementation								 -
-----------------------------------------------------------------------------*/
int main() 
{
	char *p[] = { "Circle","triangle","square","cross" };
	int mills = 2500;
	int triangle, circle, x_button, square;
	int time;
	int poss[] = {1, 2, 3, 4};
	int i = 0, t = 0, count = 0;
	int startTime;

	scanf("%d, %d, %d, %d, %d", &time, &triangle, &circle, &x_button, &square);
	printf("This is Bob-It Game!\n");
	printf("Please press the Circle Button to Start\n");
	
	do {
		scanf("%d, %d, %d, %d, %d", &time, &triangle, &circle, &x_button, &square);
		// t = selectRandom(poss[4]);
		t = rand() % 4;
		printf("press the %s button!\n", p[t]);
		
		i = (i + 1) % 4;
		// if(i==4){i=0;}
		count++;

		if (t == 0)
		{
			/*
			while (time does not run out)
			*/
			if (circle == 1)
			{
				scanf("%d, %d, %d, %d, %d", &time, &triangle, &circle, &x_button, &square);
				mills = mills - 100;
				printf("You have %d milliseconds to respond\n", mills);
				continue;
			}
			else
			{
				printf("Wrong button!\n");
				printf("You lose!\nYou made it through %d round!\n", count);
				break;
			}
		}
		if (t==3)
		{
			if (x_button == 1)
			{
				scanf("%d, %d, %d, %d, %d", &time, &triangle, &circle, &x_button, &square);
				mills = mills - 100;
				printf("You have %d milliseconds to respond\n", mills);
				continue;
			}
			else
			{
				printf("Wrong button!");
				printf("You lose!\nYou made it through %d round!\n", count);
				break;
			}
		}
		if (t == 1)
		{
			if (triangle == 1)
			{
				scanf("%d, %d, %d, %d, %d", &time, &triangle, &circle, &x_button, &square);
				mills = mills - 100;
				printf("You have %d milliseconds to respond\n", mills);
				continue;
			}
			else
			{
				printf("Wrong button!");
				printf("You lose!\nYou made it through %d round!\n", count);
				break;
			}
		}
		if (t == 2)
		{
			if (square == 1)
			{
				scanf("%d, %d, %d, %d, %d", &time, &triangle, &circle, &x_button, &square);
				mills = mills - 100;
				printf("You have %d milliseconds to respond\n", mills);
				continue;
			}
			else
			{
				printf("Wrong button!");
				printf("You lose!\nYou made it through %d round!\n", count);
				break;
			}
		}
		if (count == 15)
		{
			printf("Thanks for playing!\nYou made it though 15 rounds!\n");
			break;
		}
	} while (1);
	return 0;
}

/* Put your functions here, and be sure to put prototypes above. */

