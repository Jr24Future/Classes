#include<stdio.h>
int main(){
    int areaCode, exchangeCode, userNumber;
    long int phoneNumber;

    printf("please enter a 10 digit phonenumber: ");
    scanf("%ld", &phoneNumber);

    userNumber = phoneNumber % 10000;
    areaCode = phoneNumber / 10000000;
    exchangeCode = (phoneNumber / 10000) % 1000;

printf("Area code is : %d \n", areaCode);
printf("Exhange code is : %d \n", exchangeCode);
printf("User number is : %d \n", userNumber);
 return 0;   
}