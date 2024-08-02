/*----------------------------------------------------------------------------
-		                    SE 185: Lab 06 - Bop-It!	    	             -
-	This  code is not mine I only rewrote it and mess around with it											 -
-----------------------------------------------------------------------------*/

/*----------------------------------------------------------------------------
-								Includes									 -
-----------------------------------------------------------------------------*/
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>

/*----------------------------------------------------------------------------
-	                            Prototypes                                   -
-----------------------------------------------------------------------------*/


int triangle, circle, x_button, square;


int correct(int correctAnswer, int t, int c, int x, int s){
	
	if (correctAnswer == 1){
		if (c == 1 || x == 1 || s == 1){
			return 2;
		}
		else if (t == 1){
			return 1;
		}
	}
	else if (correctAnswer == 2){
		if (t == 1 || x == 1 || s == 1){
			return 2;
		}
		else if (c == 1){
			return 1;
		}
	}
	else if (correctAnswer == 3){
		if (t == 1 || c == 1 || s == 1){
			return 2;
		}
		else if (x == 1){
			return 1;
		}
	}
	else if (correctAnswer == 4){
		if (t == 1 || c == 1 || x == 1){
			return 2;
		}
		else if (s == 1){
			return 1;
		}
	}
}

int scan_until_no_press() {
	int t_time, triangle, circle, x_button, square;
	while (triangle || circle || x_button || square) {
		scanf(" %d, %d, %d, %d, %d", &t_time, &triangle, &circle, &x_button, &square);
	} 
	return t_time;
}



void main(int argc, char *argv[])
{	
	int t;
	int score = 0;
	int success = 0;

	
	srand(time(NULL)); 

	
    while (1){
		success = 1;
		score = 0;
int printed = 0;
		if (!printed){
			printf("Press circle to exit\n");
			printf("Press cross to begin\n");
			printed = 1;
		}
		scanf(" %d, %d, %d, %d, %d", &t, &triangle, &circle, &x_button, &square);
int timeLook= 0;
int randomButton;
		if (circle == 1){
				exit(0);
			}
		else if (x_button == 1){
		while (success == 1){
			printf("You have %d miliseconds to respond\n", (3000 - (score * 100)));
			t = scan_until_no_press();
			printed = 0;
			timeLook = t;
			randomButton = (rand() % 4) + 1;
			if (randomButton == 1) { //Triangle
				printf("Please press triangle\n");
			}
			else if (randomButton == 2) { //Circle
				printf("Please press cirlce\n");
			}
			else if (randomButton == 3) { //X Button
				printf("Please press cross\n");
			}
			else if (randomButton == 4) { //Square
				printf("Please press square\n");
			}
			
			scan_until_no_press();
			while (1){
			
				scanf(" %d, %d, %d, %d, %d", &t, &triangle, &circle, &x_button, &square);
				if (3000 > ((t - timeLook) + (score * 100))){
					int test = correct(randomButton, triangle, circle, x_button, square);
					if (test == 1){
						printf("\nCorrect!\n\n");
						score++;
						success = 1;
						scan_until_no_press();
						break;
					}
					else if (test == 2){
						printf("\nWrong!\n\n");
						printf("Previous Score: %d\n", score);
						success = 0;
						scan_until_no_press();
						break;
					}
				}
				else{
					printf("\nToo Slow!\n\n");
					printf("Previous Score: %d\n", score);
					success = 0;
					scan_until_no_press();
					break;
				}
			}
		}
	}}

	
}	





/* Put your functions here, and be sure to put prototypes above. */

