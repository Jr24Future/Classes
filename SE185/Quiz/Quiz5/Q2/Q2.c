#include <stdio.h>
double averages(double a[4]){
    return(a[0] + a[1] + a[2] + a[3])/4.0;
}
char letterGrade(double average){
    if(average >= 85 && average <= 100)
    return 'A';
    else if(average >= 75 && average <= 84)
    return 'B';
    else if(average >= 60 && average <= 74)
    return 'C';
    else
    return 'F';
}
int main(){
    double grades[2][4];
    printf("Please Enter assignment grades for student 1 and student 2\n ");
    for (int i = 0; i < 2; i++)
    {
        for (int j = 0; j < 4; j++)
        {
            printf("Student-%d Assignment-%d grade: ",i+1, j+1);
            scanf("%lf", &grades[i][j]);
        }
        
    }
    double avg1 = averages(grades[0]);
    double avg2 = averages(grades[1]);
    printf("\nStudent 1 Avg = %.2lf\n", avg1);
    printf("Student 1 Grade = %c\n", letterGrade(avg1));
    printf("Student 2 Avg = %.2lf\n", avg2);
    printf("Student 2 Grade = %c\n", letterGrade(avg2));
}