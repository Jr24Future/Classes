#include<stdio.h>

int main(){

    long number, temp, factor=1;
    long num_arr[10];   //get number from user
    printf("Enter a 10 digit phone number: ");
    scanf("%ld", &number);
    temp=number;
    while (temp) {
        temp=temp/10;
        factor = factor * 10;
    }
int i=0;                    //Inerate through number to get the individual digits
while (factor>1){
    factor = factor/10;
    num_arr[i]=number/factor;
    i=i+1;
    number = number % factor;
}
//get the area code, exchange code and user number
printf("The user number is : ");
for(int j=6;j<10;j++)
{
    printf("%ld", num_arr[j]);
}
printf("\n");
printf("The exchange code is : ");
for(int j=3;j<6;j++)
{
    printf("%ld", num_arr[j]);
}
printf("\n");
printf("The area code is : ");
for(int j=0;j<3;j++)
{
    printf("%ld", num_arr[j]);
}
    return 0;
}