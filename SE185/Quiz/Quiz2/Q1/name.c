#include <stdio.h>
#include <string.h>

int main()
{
    char firstname[2000];
    char lastname[2000];
    char fullname[4000];
    char major[2000];
    char year[2000];
    int gradYear;

    printf("Enter your first name: ");
    gets(firstname);
printf("Enter your last name: ");
gets(lastname);
    printf("Enter your major: ");
    gets(major);
printf("Enter year in college (ex. Freshman, sophomore, etc.): ");
gets(year);
    printf("Enter your graduation year: ");
    scanf("%d", &gradYear);

        strcpy(fullname, lastname);
        strcat(fullname, ",");
        strcat(firstname, ")");
        strcat(fullname, firstname );

    printf("Name (last, first): Name ( %s\n", fullname);
    printf("Major: %s\n", major);
    printf("Year: %s\n", year);
    printf("Graduation year: %d\n", gradYear);
return 0;
}