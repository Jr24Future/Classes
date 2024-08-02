#include<stdio.h>
#include<string.h>

    int main(){
        char userName[] = "se185@iastate.edu";
        char passWord[] = "ds4rd";
        char un[30], pass[30];

    printf("Enter your username: ");
    scanf("%s", &un);

    printf("Enter password: ");
    scanf("%s",&pass);

    if (strcmp(userName, un)==0 && strcmp(passWord, pass)==0){
        printf("Success!");
    }
    else{
        printf("Username or password is incorrect.");
    }
    return 0;
}