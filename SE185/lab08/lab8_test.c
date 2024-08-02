
#include <stdio.h>
#include <math.h>
#include <ncurses/ncurses.h>
#include <unistd.h>
#include <stdlib.h>

/*-----------------------------------------------------------------------------
-								Defines
-----------------------------------------------------------------------------*/
/* Mathmatical constants */
#define PI 3.14159

/* 	Screen geometry
    Use ROWS and COLUMNS for the screen height and width (set by system)
    MAXIMUMS */
#define COLUMNS 100
#define ROWS 80

/* 	Character definitions taken from the ASCII table */
#define AVATAR 'A'
#define WALL '*'
#define EMPTY_SPACE ' '

/*	Number of samples taken to form an moving average for the gyroscope data
    Feel free to tweak this. */
#define NUM_SAMPLES 10


/*-----------------------------------------------------------------------------
-								Static Data
-----------------------------------------------------------------------------*/
/* 2D character array which the maze is mapped into */
char MAZE[COLUMNS][ROWS];


/*-----------------------------------------------------------------------------
-								Prototypes
-----------------------------------------------------------------------------*/
/*	POST: Generates a random maze structure into MAZE[][]
    You will want to use the rand() function and maybe use the output %100.
    You will have to use the argument to the command line to determine how
    difficult the maze is (how many maze characters are on the screen). */
void generate_maze(int difficulty);

/*	PRE: MAZE[][] has been initialized by generate_maze()
    POST: Draws the maze to the screen */
void draw_maze(void);

/*	PRE: 0 < x < COLUMNS, 0 < y < ROWS, 0 < use < 255
    POST: Draws character use to the screen and position x,y */
void draw_character(int x, int y, char use);

/*	PRE: -1.0 < mag < 1.0
    POST: Returns tilt magnitude scaled to -1.0 -> 1.0
    You may want to reuse the roll function written in previous labs. */
double calc_roll(double mag);

/* 	Updates the buffer with the new_item and returns the computed
    moving average of the updated buffer */
double m_avg(double buffer[], int avg_size, double new_item);


/*-----------------------------------------------------------------------------
-								Implementation
-----------------------------------------------------------------------------*/
/*	Main - Run with './ds4rd.exe -t -g -b' piped into STDIN */
void main(int argc, char* argv[])
{
    int time;
    double x_axis, y_axis, z_axis;
    double avg_x, avg_y, avg_z;
    double x[NUM_SAMPLES], y[NUM_SAMPLES], z[NUM_SAMPLES];
    int alarm;
    int player_X = 40;
    int player_Y = 0;
    int last_X;
    int last_Y;
    int difficulty;

  if (argc != 2 )
  {
      printf("You must enter the difficulty level on the command line.");
      return;
  }
  else
  {
      sscanf(argv[1], "%d", &difficulty);
    /* 	Setup screen for Ncurses
        The initscr functionis used to setup the Ncurses environment
        The refreash function needs to be called to refresh the outputs
        to the screen */
    initscr();
    draw_character(player_X, player_Y, AVATAR);
    refresh();


    /* WEEK 2 Generate the Maze */
    //generate_maze(draw_maze)
    /* Read gyroscope data and fill the buffer before continuing */

    /* Event loop */
    do
    {
     

        for (int i = 0; i < NUM_SAMPLES; i++) {
            scanf(" %d, %lf, %lf, %lf", &time, &x_axis, &y_axis, &z_axis);
            x[i] = x_axis;
            y[i] = y_axis;
            z[i] = z_axis;
        }

        avg_x = m_avg(x, NUM_SAMPLES, x_axis);
        avg_x = m_avg(y, NUM_SAMPLES, y_axis);
        avg_x = m_avg(z, NUM_SAMPLES, z_axis);
        

      
        if (x_axis > 0.5) {
            last_X = player_X;
            player_X -= 1;
           /* alarm = time + 1000;
            player_Y += 1;*/
            draw_character(player_X, player_Y, AVATAR);
            draw_character(last_X, player_Y, EMPTY_SPACE);
        }
        else if (x_axis > -0.5) {
            last_X = player_X;
            player_X += 1;
          /*  alarm = time + 1000;
            player_Y += 1;*/
            draw_character(player_X, player_Y, AVATAR);
            draw_character(last_X, player_Y, EMPTY_SPACE);
        }
        if (time > alarm) {
            last_Y = player_Y;
            alarm = time + 1000;
            player_Y += 1;
            draw_character(player_X, player_Y, AVATAR);
            draw_character(player_X, last_Y, EMPTY_SPACE);

        }
    } while(1); // Change this to end game at right time

    endwin();

  }

    printf("YOU WIN!\n");
}

double m_avg(double buffer[], int avg_size, double new_item)
{
    int k;
    for (k = 0; k < avg_size - 1; k++)
        buffer[k] = buffer[k + 1];

    buffer[k] = new_item;
    double sum = 0;
    for (k = 0; k < avg_size; k++) {
        sum += buffer[k];
    }
    return sum / avg_size;

}


/* 	PRE: 0 < x < COLUMNS, 0 < y < ROWS, 0 < use < 255
    POST: Draws character use to the screen and position x,y
    THIS CODE FUNCTIONS FOR PLACING THE AVATAR AS PROVIDED.
    DO NOT NEED TO CHANGE THIS FUNCTION. */
void draw_character(int x, int y, char use)
{
    mvaddch(y,x,use);
    refresh();
}
//void generate_maze(int difficulty) {
//
//    //rand % 100 will yield a value between 0 and 99
//    //???? less than or equal to or greater than or equal to?
//    for (int i = 0; i < ROWS; i++)
//        for (int j = 0; j < COLUMNS; j++) {
//            if ((rand() % 100) <= difficulty) {
//                
//                MAZE[i][j] = WALL;
//            }
//            else {
//                
//                MAZE[i][j] = EMPTY_SPACE;
//            }
//        }
//
//
//}
//void draw_maze(void) {
//
//    for (int i = 0; i < ROWS; i++) {
//        for (int j = 0; j < COLUMNS; j++) {
//            
//            draw_character(i, j, MAZE[i][j]);
//        }
//    }
//}