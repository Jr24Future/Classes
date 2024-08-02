#include <stdio.h>
#include <stdlib.h>
void quit()
{
	printf("Do you want to exit out of this qiuzzes?\n");
	printf("             Y or N\n     ");
	char Quit_Q;
	char Sure;
	scanf(" %c", &Quit_Q);
	if (Quit_Q == 'Y' || Quit_Q == 'y') {
		printf("Are you sure you want to quit from this quiz\n");
		printf("NOTE!!  If you quit from this quiz you will lose all process and will stat from the begining.\n");
		printf("To quit Type Q  ---//---  To continue Type anything other latter else\n\n\n");
		scanf(" %c", &Sure);
		if (Sure == 'Q' || Sure == 'q') {
			exit(0);
		}
		else
		{
			printf("Thank you for continue");
		}
	}
	else
	{
		printf("Thank you for continue");
	}
}

