#include<stdio.h>
#include<stdlib.h>
#include<stdbool.h>
#include<string.h>

void main(){
	
	int quiz1Correct = 0;
	int quiz1Total = 0;
	
	int quiz2Correct = 0;
	int quiz2Total = 0;
	
	int quiz3Correct = 0;
	int quiz3Total = 0;
	
	int totalTotal = 0;
	int totalCorrect = 0;

	int tempCorrect = 0;
	int tempTotal = 0;
	bool success = 0;

	
	void quiz1() {
		
		while (!success){
			
			printf("\n\n\n*** Quiz One Has Been Randomly Chosen For You ***\n\n\n");
			//Question 1 ; true or false
			printf("Q1: (Enter T or F)\nCats have been domesticated for approximateley 9,500 years.\n-");
			char answer1;
			scanf(" %c", &answer1);
			if (answer1 == 'T' || answer1 == 't'){
				printf("That's correct.\n\n");
				tempCorrect += 5;
			}
			else{
				printf("That's incorrect.\n\n");
			}
			tempTotal += 5;
			
			//Question 2 ; multiple choice
			printf("Q2: (Enter A, B, C, or D)\nA group of ferrets is called a ___.\n");
			printf("A: Huddle\n");
			printf("B: Gang\n");
			printf("C: Swaddle\n");
			printf("D: Bussiness\n-");
			char answer2;
			scanf(" %c", &answer2);
			if (answer2 == 'D' || answer2 =='d'){
				printf("That's correct.\n\n");
				tempCorrect += 5;
			}
			else{
				printf("That's incorrect.\n\n");
			}
			tempTotal += 5;
			
			//Question 3 ; Fill in the blank
			printf("Q3: (Enter an integer)\nKoalas sleep for about __ hours per day.\n-");
			int answer3;
			scanf(" %d", &answer3);
			if (answer3 == 22){
				printf("That's correct.\n\n");
				tempCorrect += 5;
			}
			else if (answer3 <= 23 && answer3 >= 21){
				printf("That's close enough.\n\n");
				tempCorrect += 5;
			}
			else{
				printf("That's incorrect.\n\n");
			}
			tempTotal += 5;
			
			if (tempCorrect == tempTotal){
				success = 1;
			}
			
			
			quiz1Correct += tempCorrect;
			quiz1Total += tempTotal;
			
			totalCorrect += tempCorrect;
			totalTotal += tempTotal;
		
		printf("Your score for this attempt is: %d/%d\n", tempCorrect, tempTotal);
		printf("Your cummulative score for quiz one is: %d/%d\n", quiz1Correct, quiz1Total);
		printf("Your cummulative score for all quizzes is: %d/%d\n", totalCorrect, totalTotal);
		}
		//quit();
	}
	
	void quiz2(){
		bool success = 0;
		while (!success){
			int tempCorrect = 0;
			int tempTotal = 0;
			
			printf("\n\n\n*** Quiz Two Has Been Randomly Chosen For You ***\n\n\n");
			//Question 1 ; true or false
			printf("Q1: (Enter T or F)\nOctopi have two hearts.\n-");
			char answer1;
			scanf(" %c", &answer1);
			if (answer1 == 'F' || answer1 == 'f'){
				printf("That's correct.\n\n");
				tempCorrect += 5;
			}
			else{
				printf("That's incorrect.\n\n");
			}
			tempTotal += 5;
			
			//Question 2 ; multiple choice
			printf("Q2: (Enter A, B, C, or D)\nGiraffe tongues are ___.\n");
			printf("A: Blue\n");
			printf("B: Black\n");
			printf("C: Purple\n");
			printf("D: Red\n-");
			char answer2;
			scanf(" %c", &answer2);
			if (answer2 == 'B' || answer2 =='b'){
				printf("That's correct.\n\n");
				tempCorrect += 5;
			}
			else{
				printf("That's incorrect.\n\n");
			}
			tempTotal += 5;
			
			//Question 3 ; Fill in the blank
			printf("Q3: (Enter an integer)\nA group of ___ is called a parliament.\n-");
			char answer3[99];
			scanf(" %s", answer3);
			if (!strcmp(answer3, "Owls") || !strcmp(answer3, "owls") || !strcmp(answer3, "Owl") || !strcmp(answer3, "owl")){
				printf("That's correct.\n\n");
				tempCorrect += 5;
			}
			else{
				printf("That's incorrect.\n\n");
			}
			tempTotal += 5;
			
			if (tempCorrect == tempTotal){
				success = 1;
			}
			
			
			quiz2Correct += tempCorrect;
			quiz2Total += tempTotal;
			
			totalCorrect += tempCorrect;
			totalTotal += tempTotal;
		
		printf("Your score for this attempt is: %d/%d\n", tempCorrect, tempTotal);
		printf("Your cummulative score for quiz two is: %d/%d\n", quiz2Correct, quiz2Total);
		printf("Your cummulative score for all quizzes is: %d/%d\n", totalCorrect, totalTotal);
		}
		//quit();
	}
	
	void quiz3(){
		bool success = 0;
		while (!success){
			int tempCorrect = 0;
			int tempTotal = 0;
			
			printf("\n\n\n*** Quiz Three Has Been Randomly Chosen For You ***\n\n\n");
			//Question 3 ; true or false
			printf("Q1: (Enter T or F)\nSnow leaopards can't roar.\n-");
			char answer1;
			scanf(" %c", &answer1);
			if (answer1 == 'T' || answer1 == 't'){
				printf("That's correct.\n\n");
				tempCorrect += 5;
			}
			else{
				printf("That's incorrect.\n\n");
			}
			tempTotal += 5;
			
			//Question 2 ; multiple choice
			printf("Q2: (Enter A, B, C, or D)\n___ can regrow their parts.\n");
			printf("A: Axolotls\n");
			printf("B: Rhinos\n");
			printf("C: Cats\n");
			printf("D: Giraffes\n-");
			char answer2;
			scanf(" %c", &answer2);
			if (answer2 == 'A' || answer2 =='a'){
				printf("That's correct.\n\n");
				tempCorrect += 5;
			}
			else{
				printf("That's incorrect.\n\n");
			}
			tempTotal += 5;
			
			//Question 3 ; Fill in the blank
			printf("Q3: (Enter an integer)\nGiant anteaters have a ___ foot long tongue.\n-");
			int answer3;
			scanf(" %d", &answer3);
			if (answer3 == 2){
				printf("That's correct.\n\n");
				tempCorrect += 5;
			}
			else{
				printf("That's incorrect.\n\n");
			}
			tempTotal += 5;
			
			if (tempCorrect == tempTotal){
				success = 1;
			}
			
			
			quiz3Correct += tempCorrect;
			quiz3Total += tempTotal;
			
			totalCorrect += tempCorrect;
			totalTotal += tempTotal;
		
		printf("Your score for this attempt is: %d/%d\n", tempCorrect, tempTotal);
		printf("Your cummulative score for quiz one is: %d/%d\n", quiz3Correct, quiz3Total);
		printf("Your cummulative score for all quizzes is: %d/%d\n", totalCorrect, totalTotal);
		}
		//quit();
	}
	
	
	//quiz1();
	//quiz2();
	quiz3();
}