
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <ncurses/ncurses.h>
#include <unistd.h>


#define PI 3.14159


#define COLUMNS 100
#define ROWS 80


#define AVATAR 'A'
#define WALL '¤'
#define EMPTY_SPACE ' '

#define NUM_SAMPLES 10

char MAZE[ROWS][COLUMNS];


// POST: Generates a random maze structure into MAZE[][]
//You will want to use the rand() function and maybe use the output %100.  
//You will have to use the argument to the command line to determine how 
//difficult the maze is (how many maze characters are on the screen).
void generate_maze(int difficulty);

// PRE: MAZE[][] has been initialized by generate_maze()
// POST: Draws the maze to the screen 
void draw_maze(void);

// PRE: 0 < x < COLS, 0 < y < ROWS, 0 < use < 255
// POST: Draws character use to the screen and position x,y
void draw_character(int x, int y, char use);


double calc_roll(double mag);

double m_avg(double buffer, int avg_size, double new_item);


int main(int argc, char* argv[])
{
	double gx, gy, gz;
	int button_T, button_X, button_S, button_C;
	int time;
	int avatar_column = ROWS / 2; 
	int avatar_row = 0; 
	int avatar_p_c, avatar_p_r, avatar_c_c, avatar_c_r;
	
	if (argc <2) { 
		printf("You must enter the difficulty level on the command line."); 
		return 1;
	}
	int difficulty = atoi(argv[1]); 
	   
	initscr();
	refresh();
	
	
	generate_maze(difficulty);
	
	MAZE[avatar_column][avatar_row] = AVATAR;
	
	draw_maze();
	
	avatar_p_c = avatar_column;
	avatar_p_r = avatar_row;
	avatar_c_c = avatar_column;
	avatar_c_r = avatar_row;
	
	
	scanf("%d, %lf, %lf, %lf", &time, &gx, &gy, &gz);
	
	
	do
	{
		scanf("%d, %lf, %lf, %lf", &time, &gx, &gy, &gz);
	
		if ((time % 100) < 4){
			
			
			if(!m_avg(0.35, 0, gx)){
				if(gx < 0 && avatar_c_c < ROWS && MAZE[avatar_c_c + 1][avatar_c_r] != WALL){
					avatar_c_c += 1;
				}
				else if(gx > 0 && avatar_column > 2 && MAZE[avatar_c_c - 1][avatar_c_r] != WALL){
					avatar_c_c -= 1;
				}
			}
			
			/*
			if(MAZE[avatar_c_c][avatar_c_r + 1] == WALL && MAZE[avatar_c_c - 1][avatar_c_r] == WALL && MAZE[avatar_c_c  + 1][avatar_c_r] == WALL){
				break;
			}
			*/
			int j;
			int left = 0;
			int right = COLUMNS;
			int safe = 0;
			for (j = avatar_c_c; j > 0; j--) {
				if (MAZE[j][avatar_c_r] == WALL) {
					left = j;
					break;
				}
			}
			for (j = avatar_c_c; j < COLUMNS; j++) {
				if (MAZE[j][avatar_c_r] == WALL) {
					right = j;
					break;
				}
			}
			for (j = left + 1; j < right; j++) {
				if (MAZE[j][avatar_c_r + 1] == EMPTY_SPACE) {
					safe = 1;
				}
			}
			if (!safe) {
				break;
			}
			
			
			if(MAZE[avatar_c_c][avatar_c_r + 1] != WALL){ 
				avatar_c_r += 1;
				draw_character(avatar_c_c, avatar_c_r, AVATAR);
				draw_character(avatar_p_c, avatar_p_r, EMPTY_SPACE);
				avatar_p_c = avatar_c_c;
				avatar_p_r = avatar_c_r;
			}
			
		}
		

	} while(avatar_c_r < COLUMNS - 1);

	endwin();
	
	if(avatar_c_r == COLUMNS - 1){
		printf("YOU WIN!\n");
	} else{
		printf("You lose, sorry!\n");
	}


	return 0;
}




void draw_character(int x, int y, char use)
{
	mvaddch(y,x,use);    
	refresh();
}

void generate_maze(int difficulty){
	
	
	for(int i = 0; i < ROWS; i++)
		for(int j = 0; j < COLUMNS; j++){
			if((rand() % 100) <= difficulty){
				
				MAZE[i][j] = WALL;
			} else{
				
				MAZE[i][j] = EMPTY_SPACE;
			}
		}
	
	
}

void draw_maze(void){
	
	for(int i = 0; i < ROWS; i++){
		for(int j = 0; j < COLUMNS; j++){
			
			draw_character(i,j,MAZE[i][j]); 
		}
	}
}

double calc_roll(double mag){
	
	
	if(mag < -1){
		mag = -1;
	} else if (mag > 1){
		mag = 1;
	}
	
	double x_rads = asin(mag);
	
	return x_rads; 
}

double m_avg(double buffer, int avg_size, double new_item){
	
	if(fabs(avg_size - new_item) < buffer){
		return 1; 
	} else {
		return 0; 
	}
	
}