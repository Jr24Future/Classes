#include<stdio.h>

void main(){
	int gradeNum;
	printf("Enter your grade: ");
	scanf("%d", &gradeNum);
	
	if (gradeNum < 0 || gradeNum > 100){
		printf("Please enter a valid grade.");
	}
	else{
		printf("Your grade is: ");
		if (gradeNum >= 93 && gradeNum <= 100){
			printf("A");
		}
		else if (gradeNum >= 90 && gradeNum < 93){
			printf("A-");
		}
		else if (gradeNum >= 87 && gradeNum < 90){
			printf("B+");
		}
		else if (gradeNum >= 83 && gradeNum < 87){
			printf("B");
		}
		else if (gradeNum >= 80 && gradeNum < 83){
			printf("B-");
		}
		else if (gradeNum >= 77 && gradeNum < 80){
			printf("C+");
		}
		else if (gradeNum >= 73 && gradeNum < 77){
			printf("C");
		}
		else if (gradeNum >= 70 && gradeNum < 73){
			printf("C-");
		}
		else if (gradeNum >= 67 && gradeNum < 70){
			printf("D+");
		}
		else if (gradeNum >= 63 && gradeNum < 67){
			printf("D");
		}
		else if (gradeNum >= 60 && gradeNum < 63){
			printf("D-");
		}
		else if (gradeNum >= 0 && gradeNum < 60){
			printf("F");
		}
		else{
			printf("Broken");
		}
	}
}