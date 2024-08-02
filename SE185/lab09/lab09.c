// Lab 09 DS4Talker Skeleton Code       

#include <ctype.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ncurses.h>
#define WORDLENGTH 11
#define MAXWORDS 100
#define DEBUGM 1   // Set this to 0 to disable debug output

// Reads words from the file into WL and trims the whitespace off of the end
// DO NOT MODIFY THIS FUNCTION
int read_words(char* WL[MAXWORDS], char* file_name);

// modifies str to trim white space off the right side
// DO NOT MODIFY THIS FUNCTION
void trimws(char* str);

void draw_character(int x, int y, char use);
int calclength(char num[80]);


// --------------------------------------------------------------------------------------------- \\

int main(int argc, char* argv[]) {
	char* wordlist[MAXWORDS];
	int wordcount = read_words(wordlist, argv[1]);
    initscr();
    refresh();
    int quit = 0,cursur = 7, k = 0, previous_Value = 0, pos = 30, b[1000], val = 0;
    int triangle, circle, xButton, square, leftJoyX, leftJoyY, rightJoyX, rightJoyY, time, i, joyL, joyR, O, S, R2, L2, R1, L1;
    mvprintw(28,0, "Your sentence:");
    while(quit == 0)
    {
        //scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &RightJoyButton, &LeftJoyButton, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
        scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
        for (i = 0; i < 19; i++){
            mvprintw(pos,previous_Value, "_");
            mvprintw(i,0, "%s", wordlist[i]);
            refresh();
            }
        for (i = 19; i < 38; i++){
            mvprintw((i-19),15, "%s", wordlist[i]);
            refresh();
            }
        for (i = 38; i < 57; i++){
            mvprintw((i-38),30, "%s", wordlist[i]);
            refresh();
            }
        for (i = 57; i < 76; i++){
            mvprintw((i-57),45, "%s", wordlist[i]);
            refresh();
            }
            draw_character(cursur, k, '<');
        if (leftJoyY > 125){
            k++;
            draw_character(cursur, (k-1), ' ');
            if ((k == 19) && (leftJoyY > 125)) {
                cursur += 15;
                k = 0;
                }
            if (cursur > 52) {
                cursur = 7;
            }
            while (leftJoyY > 10) {
                scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
            }
        if (leftJoyY < -125){
                k--;
                draw_character(cursur, (k+1), ' ');
            if ((k < 0) && (leftJoyY < -125) && (cursur == 7)) {
                cursur = 52;
                k = 18;
                }
            if ((k < 0) && (leftJoyY < -125) && (cursur != 7)) {
                cursur -= 15;
                k = 18;
                }
            while (leftJoyY < -10) {
                scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
        }
        if (leftJoyX > 120) {
            cursur += 15;
            draw_character((cursur-15), k, ' ');
            if (cursur > 52) {
                cursur = 7;
            }
            while (leftJoyX > 10) {
                scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
        }
        if (leftJoyX < -120) {
            cursur -= 15;
            draw_character((cursur+15), k, ' ');
            if (cursur < 7) {
                cursur = 52;
            }
            while (leftJoyX < -10) {
                scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
        }
        if (triangle == 1) {
            if (cursur == 7) {
                mvprintw(pos,previous_Value, " ");
                previous_Value++;
                mvprintw(pos,previous_Value, "%s", wordlist[k]);
                previous_Value += (calclength(wordlist[k]));
                b[val] = (calclength(wordlist[k]));
                val++;
                while (triangle == 1) {
                    scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
            }
            if (cursur == 22) {
                mvprintw(pos,previous_Value, " ");
                previous_Value++;
                mvprintw(pos,previous_Value, "%s", wordlist[(k+19)]);
                previous_Value += (calclength(wordlist[k+19]));
                b[val] = (calclength(wordlist[k+19]));
                val++;
                while (triangle == 1) {
                    scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
            }
            if (cursur == 37) {
                mvprintw(pos,previous_Value, " ");
                previous_Value++;
                mvprintw(pos,previous_Value, "%s", wordlist[(k+38)]);
                previous_Value += (calclength(wordlist[k+38]));
                b[val] = (calclength(wordlist[k+38]));
                val++;
                while (triangle == 1) {
                    scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
            }
            if (cursur == 52) {
                mvprintw(pos,previous_Value, " ");
                previous_Value++;
                mvprintw(pos,previous_Value, "%s", wordlist[(k+57)]);
                previous_Value += (calclength(wordlist[k+57]));
                b[val] = (calclength(wordlist[k+57]));
                val++;
                while (triangle == 1) {
                    scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
            }
        }
        if (square == 1) {
            if (cursur == 7) {
                mvprintw(pos,previous_Value, "%s", wordlist[k]);
                previous_Value += (calclength(wordlist[k]));
                b[val] = (calclength(wordlist[k]));
                val++;
                while (square == 1) {
                    scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
            }
            if (cursur == 22) {
                mvprintw(pos,previous_Value, "%s", wordlist[(k+19)]);
                previous_Value += (calclength(wordlist[k+19]));
                b[val] = (calclength(wordlist[k+19]));
                val++;
                while (square == 1) {
                    scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
            }
            if (cursur == 37) {
                mvprintw(pos,previous_Value, "%s", wordlist[(k+38)]);
                previous_Value += (calclength(wordlist[k+38]));
                b[val] = (calclength(wordlist[k+38]));
                val++;
                while (square == 1) {
                    scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
            }
            if (cursur == 52) {
                mvprintw(pos,previous_Value, "%s", wordlist[(k+57)]);
                previous_Value += (calclength(wordlist[k+57]));
                b[val] = (calclength(wordlist[k+57]));
                val++;
                while (square == 1) {
                    scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
            }
            }
        }
        if (previous_Value > 80) {
            pos++;
            previous_Value = 0;
        }
        if (previous_Value < 0) {
            previous_Value = 0;
        }
        if (xButton == 1) {
            val--;
            if (b[val] == 1) {
                    mvprintw(pos,previous_Value, " ");
                    previous_Value--;
                    while (xButton == 1) {
                        scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
                }
            }
            if (b[val] != 1) {
                for (i = 0; i < (b[val] + 1); i++) {
                    mvprintw(pos,previous_Value, " ");
                    previous_Value--;
                    while (xButton == 1) {
                        scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
                    }
                }
            }
            
    }
        if (joyL == 1) {
            if (b[val] == 1) {
                mvprintw(pos, previous_Value, "                                                        ");
                previous_Value--;
                while (joyL == 1) {
                    scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
                }
            }
            if (b[val] != 1) {
                for (i = 0; i < (b[val] + 200); i++) {
                    mvprintw(pos, previous_Value, "                                                     ");
                    previous_Value--;
                    while (joyL == 1) {
                        scanf(" %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d", &time, &triangle, &circle, &xButton, &square, &joyL, &joyR, &O, &S, &R2, &L2, &R1, &L1, &leftJoyX, &leftJoyY, &rightJoyX, &rightJoyY);
                    }
                }
            }
        }
       /* if (LeftJoyButton == 1) {
            for (i = 0; i < 81; i++) {
                mvprintw(pos, i, " ");
                previous_Value = 0;
            }
        } */
        
    }
	// most of your code goes here. Do not forget to include the ncurses library 
	endwin();
	return 0;
}

void draw_character(int x, int y, char use) {
	mvprintw(y,x,"%c",use);
	refresh();
}

// DO NOT MODIFY THIS FUNCTION!
void trimws(char* str) {
	int length = strlen(str);
	int x;
	if (length == 0) return;
	x = length - 1;
	while (isspace(str[x]) && (x >= 0)) {
		str[x] = '\0';
		x -= 1;
	}
}


// DO NOT MODIFY THIS FUNCTION!
int read_words(char* WL[MAXWORDS], char* file_name)
{
	int numread = 0;
	char line[WORDLENGTH];
	char *p;
	FILE* fp = fopen(file_name, "r");
	while (!feof(fp)) {
		p = fgets(line, WORDLENGTH, fp);
		if (p != NULL) 
		{
			trimws(line);
			WL[numread] = (char *)malloc(strlen(line) + 1);
			strcpy(WL[numread], line);
			numread++;
		}
	}
	fclose(fp);
	return numread;
}

int calclength(char num[80]) {
	int val = strlen(num);
	return val;
}
