// Lab 09 DS4Talker Skeleton Code       

#include <ctype.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ncurses/ncurses.h>
#include <unistd.h>
#define WORDLENGTH 11
#define MAXWORDS 100
#define DEBUGM 1
#define AVATAR '>'
#define PAST ' '
// Set this to 0 to disable debug output

// Reads words from the file into WL and trims the whitespace off of the end
// DO NOT MODIFY THIS FUNCTION
int read_words(char* WL[MAXWORDS], char* file_name);

// modifies str to trim white space off the right side
// DO NOT MODIFY THIS FUNCTION
//./ds4rd-real.exe -d 054c:05c4 -D DS4_BT -b -bd -bt |  lab09 wordslist.txt
void draw_character(int x, int y, char use);


void trimws(char* str);

int main(int argc, char* argv[]) {
	char* wordlist[MAXWORDS];
	int wordcount;
	int i, j;
	int t, Tb, Cb, Xb, Sb, Jr, Jl, Op, Sh, R2, L2, R1, L1, Du, Dl, Dd, Dr;
	wordcount = read_words(wordlist, argv[1]);
	int x, y, h = 0;
	int N = 0, P = 0;

	scanf("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d",&t, &Tb, &Cb, &Xb, &Sb, &Jr, &Jl, &Op, &Sh, &R2, &L2, &R1, &L1, &Du, &Dl, &Dd, &Dr);

	if (DEBUGM) {
		printf("Read %d words from %s \n", wordcount, argv[1]);
		for (i = 0; i < wordcount; i++) {
			printf("%s,", wordlist[i]);

		}
		printf("\n");
	}

	initscr();

	mvprintw(0, 0, wordlist[0]);
	mvprintw(0, 2, "*");
	i = 1;
	while (i < wordcount){

		if (i + 1 > (h + 1) * 19) {
			h++;
		}
		mvprintw((i - (19 * h)), (h * 12) + 3, wordlist[i]);
		mvprintw((i - (19 * h)), (h * 12) + 2, "*");
		++i;
		
		refresh();
		
		
	}
	mvprintw(N, P, ">");
	do{
		
	scanf("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d",&t, &Tb, &Cb, &Xb, &Sb, &Jr, &Jl, &Op, &Sh, &R2, &L2, &R1, &L1, &Du, &Dl, &Dd, &Dr);
	if ((t % 100) < 8) {
		if (Dd == 1) {
			mvprintw(N, P, " ");
			if (N != 18) {
				mvprintw(N, P, " ");
				++N;
				draw_character(P, N, AVATAR);
			}

			else if (N = 18) {
				mvprintw(N, P, " ");
				N = 0;
				draw_character(P, N, AVATAR);
			}
		}
		if (Du == 1) {
			mvprintw(N, P, " ");
			if (N != -1) {
				mvprintw(N, P, " ");
				--N;
				draw_character(P, N, AVATAR);
			}
			else if (N = -1) {
				mvprintw(N, P, " ");
				N = 18;
				draw_character(P, N, AVATAR);
			}
		}
		if (Dl == 1) {
			mvprintw(N, P, " ");
			if (P > 0) {
				mvprintw(N, P, " ");
				P -= 12;
				draw_character(P, N, AVATAR);
			}
			else{
				mvprintw(N, P, " ");
				P = 36;
				draw_character(P, N, AVATAR);
			}
		}
		if (Dr == 1) {
			mvprintw(N, P, " ");
			if (P != 36) {
				mvprintw(N, P, " ");
				P += 12;
				draw_character(P, N, AVATAR);
			}
			else if (P = 36) {
				mvprintw(N, P, " ");
				P = 0;
				draw_character(P, N, AVATAR);
			}
		}
	}
	refresh();
	} while (1);
	return 0;
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
void draw_character(int x, int y, char use)
{
	mvaddch(y, x, use);
	refresh();
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
