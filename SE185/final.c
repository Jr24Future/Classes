#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>


/*

                  Team Members:



   ##########################################
   #                                        #
   #                                        #
   #                                        #
   #             Devon    Meyer             #
   #             Errol   Barker             #
   #             Dameon Johnson             #
   #                                        #
   #                                        #
   #                                        #
   ##########################################
   
   
   
*/









typedef struct weapon_struct{ //Struct that holds the rarity, type (sword, axe, etc), and name (rarity + type) of a weapon
	char rarity[11];
	char weaponName[11];
	char name[23];
}weapon;

/*
typedef struct UltraRare_struct{
	char RareUltraRare[20];
	char UncommonUltraRare[20];
	char CommonUltraRare[20]; 
}UltraRare;

typedef struct SuperRare_struct{
	char RareSuperRare[20]; 
	char UncommonSuperRare[20];
	char CommonSuperRare[20]; 
}SuperRare;

typedef struct Rare_struct{
	char RareRare[20]; 
	char UncommonRare[20];
	char CommonRare[20]; 
}Rare;

typedef struct Uncommon_struct{
	char RareUncommon[20]; 
	char UncommonUncommon[20];
	char CommonUncommon[20];
}Uncommon;
typedef struct Common_struct{
	char RareCommon[20]; 
	char UncommonCommon[20];
	char CommonCommon[20]; 
}Common;
*/

//struct Common_struct getCommon(); 
/*
void getUltraRare(struct UltraRare_struct e); 
void getSuperRare(struct SuperRare_struct q);
void getRare(struct Rare_struct d);
void getUncommon(struct Uncommon_struct a);
void getCommon(struct Common_struct s);
*/

void getRarity(char *s);
void getWeaponName(char *s);
void getName(struct weapon_struct* w);
weapon makeWeapon(struct weapon_struct* w);

// int randomNubmer_Y(void);
// int randomNubmer_X(void);

int main(void){
   FILE *filePionter = NULL;
   int y;
   int x;
   srand(time(NULL));
   

	filePionter = fopen("FP_Test.txt","a+");
	if(filePionter == NULL){
		printf( "FP_Test file failed to open." );
		fclose(filePionter);
        exit(-1);
	}
	else{
		/*
       struct Common_struct s1;
       strcpy(s1.CommonCommon,malloc(sizeof(char) * 20));
       strcpy(s1.RareCommon, malloc(sizeof(char) * 20));
       strcpy(s1.UncommonCommon, malloc(sizeof(char) * 20));

        struct Uncommon_struct a1;
        strcpy(a1.CommonUncommon, malloc(sizeof(char) * 20));
        strcpy(a1.RareUncommon, malloc(sizeof(char) * 20));
        strcpy(a1.UncommonUncommon, malloc(sizeof(char) * 20));

        struct Rare_struct d1;

        strcpy(d1.CommonRare, malloc(sizeof(char) * 20));
        strcpy(d1.RareRare, malloc(sizeof(char) * 20));
        strcpy(d1.UncommonRare, malloc(sizeof(char) * 20));
        struct SuperRare_struct q1;

        strcpy(q1.CommonSuperRare, malloc(sizeof(char) * 20));
        strcpy(q1.RareSuperRare, malloc(sizeof(char) * 20));
        strcpy(q1.UncommonSuperRare, malloc(sizeof(char) * 20));
        struct UltraRare_struct e1;

        strcpy(e1.CommonUltraRare, malloc(sizeof(char) * 20));
        strcpy(e1.RareUltraRare, malloc(sizeof(char) * 20));
        strcpy(e1.UncommonUltraRare, malloc(sizeof(char) * 20));
		*/
		
		
		int r = (rand()%5)+3; //randomly chooses a number; this will be the number of weapons given to the user
		
		
		weapon* weaponsList = (weapon*)calloc(r, sizeof(weapon)); //allocates enough memory for the randomly selected amount of weapons that will be put in the list
		


for(int i = 0; i<r; ++i){ //Loop put the weapons in the list and print the names to the desired location(s)
        weapon blankWeapon;
		weaponsList[i] = makeWeapon(&blankWeapon);
		//printf("Name: %s\n", weaponsList[i].name); //Uncomment to print to terminal
		fprintf(filePionter, "%s\n", weaponsList[i].name); //Uncomment to print to text file (see line 224)
		
		
		
		/*
       // x = randomNubmer_X();
            x = rand() % 5;
            y = rand() % 3;

		
        if(x==0){ 
            
            //y = randomNubmer_Y();
            if(y == 0){                        // s1.RareCommon = "";
                strcpy(s1.RareCommon,"Csword\n"); // s1.RareCommon = Csword\n                        
            }                                      // & = Csword\n
            if (y == 1){
                strcpy(s1.UncommonCommon,"Caxe\n");
            }
            if(y == 2){
                strcpy(s1.CommonCommon,"Choe\n");
            }
        }
            //----------------------------------
        if(x==1){
           
            //y = randomNubmer_Y();
            if(y == 0){
            strcpy (a1.RareUncommon,"Usword\n");
            }
            if (y == 1){
            strcpy (a1.UncommonUncommon,"Uaxe\n");
            }
            if(y == 2){
            strcpy (a1.CommonUncommon,"Uhoe\n");
            }
        }
            //-----------------------------------
        if(x==2){
           
            //y = randomNubmer_Y();
            if(y == 0){
            strcpy (d1.RareRare,"Rsword\n");
            }
            if (y == 1){
            strcpy (d1.UncommonRare,"Raxe\n");
            }
            if(y == 2){
            strcpy (d1.CommonRare,"Rhoe\n");
            }
        }
            //-----------------------------------
        if(x==3){
          
            //y = randomNubmer_Y();
            if(y == 0){
            strcpy (q1.RareSuperRare,"Ssword\n");
            }
            if (y == 1){
            strcpy (q1.UncommonSuperRare,"Saxe\n");
            }
            if(y == 2){
            strcpy (q1.CommonSuperRare,"Shoe\n");
            }
        }
            //-----------------------------------
        if(x==4){
          
            //y = randomNubmer_Y();
            if(y == 0){ 
            strcpy (e1.RareUltraRare,"Usword\n");
            }
            if (y == 1){
            strcpy (e1.UncommonUltraRare,"Uaxe\n");
            }
            if(y == 2){
            strcpy (e1.CommonUltraRare,"Uhoe\n");
            }
        }
        }
		
                        //prints here
        getCommon(s1);
        getUncommon(a1);
        getRare(d1);
        getSuperRare(q1);
        getUltraRare(e1);  
		
        fprintf(filePionter, "data: \n");
        fprintf(filePionter, "%s", s1.RareCommon);
        fprintf(filePionter, "%s", s1.UncommonCommon);
        fprintf(filePionter, "%s", s1.CommonCommon);
        fprintf(filePionter, "%s", a1.RareUncommon);
        fprintf(filePionter, "%s", a1.UncommonUncommon);
        fprintf(filePionter, "%s", a1.CommonUncommon);
        fprintf(filePionter, "%s", d1.RareRare);
        fprintf(filePionter, "%s", d1.UncommonRare);
        fprintf(filePionter, "%s", d1.CommonRare);
        fprintf(filePionter, "%s", q1.RareSuperRare);
        fprintf(filePionter, "%s", q1.UncommonSuperRare);
        fprintf(filePionter, "%s", q1.CommonSuperRare);
        fprintf(filePionter, "%s", e1.RareUltraRare);
        fprintf(filePionter, "%s", e1.UncommonUltraRare);
        fprintf(filePionter, "%s", e1.CommonUltraRare);

        fclose(filePionter);
		
		*/
    }
	fprintf(filePionter, "\n\n\n\n"); //Uncomment to print to text file (see line 115)
	fclose(filePionter);
	return 0; 
    
}
}
weapon makeWeapon(struct weapon_struct* w){ //Combines three functions, updates the given weapon and returns the updated weapon
	getRarity(w->rarity);
	getWeaponName(w->weaponName);
	getName(w);
	return *w;
}


void getRarity(char* s){ //updates the rarity of a weapon to a random value
	char rarity[11];
	int r;
	
	//	Calculates the rarity
	r = (rand()%31)+1;
	if (r <= 16){                 //Sixteen Possible Numbers
		stpcpy(rarity, "Common");
	}
	else if (r <= 24){            //Eight Possible Numbers
		stpcpy(rarity, "Uncommon");
	}
	else if (r <= 28){            //Four Possible Numbers
		stpcpy(rarity, "Rare");
	}
	else if (r <= 30){            //Two Possible Numbers
		stpcpy(rarity, "Super Rare");
	}
	else if (r == 31){            //One Possible Number
		stpcpy(rarity, "Ultra Rare");
	}
	stpcpy(s, rarity);
}

void getWeaponName(char* s){ //updates the type of a weapon to a random type
	char name[11];
	int r;
	
	//	Calculate the weapon
	r = rand()%10;
	switch (r){
		case 0:
			stpcpy(name, "Greatsword"); //Rare
			break;
		case 1:
			strcpy(name, "Shortsword"); //Uncommon
			break;
		case 2:
			strcpy(name, "Battle Axe"); //Uncommon
			break;
		case 3:
			stpcpy(name, "Spear");      //Uncommon
			break;
		case 4:
			strcpy(name, "Shovel");     //Common
			break;
		case 5:
			strcpy(name, "Hoe");        //Common
			break;
		case 6:
			stpcpy(name, "Hammer");     //Common
			break;
		case 7:
			strcpy(name, "Hatchet");    //Common
			break;
		case 8:
			strcpy(name, "Crowbar");    //Common
			break;
		case 9:
			stpcpy(name, "Knife");      //Common
			break;
		default:
			stpcpy(name, "???");        //Common
			printf("Switch statement broke");
	}
	stpcpy(s, name);
	
	/*
	//	Assign the value to the passed variable
	printf("Rarity: %s\n", rarity);
	printf("Name: %s\n", name);
	*w.rarity = rarity;
	*w.name = name;
	printf("W Rarity: %s\n", *w.rarity);
	printf("W Name: %s\n", *w.name);
	*/
	
}
void getName(struct weapon_struct* w){ //combines rarity and weapon name into one string and saves it to the passed weapon's name
	char rarity[11];
	char weaponName[11];
	char temp[23];
	
	stpcpy(rarity, w->rarity);
	stpcpy(weaponName, w->weaponName);
	
	stpcpy(temp, rarity);
	strcat(temp, " ");
	strcat(temp, weaponName);
	
	strcpy(w->name, temp);
}



/*
// Common_struct getCommon(){
void getCommon(struct Common_struct s){
printf("%s",s.RareCommon);
printf("%s",s.UncommonCommon);
printf("%s",s.CommonCommon);
//fprintf(filePionter,"%s",s.RareCommon);
//fprintf(filePionter,"%s",s.UncommonCommon);
//fprintf(filePionter,"%s",s.CommonCommon);
}

void getUncommon(struct Uncommon_struct a){
printf("%s",a.RareUncommon);
printf("%s",a.UncommonUncommon);
printf("%s",a.CommonUncommon);
//fprintf(filePionter,"%s",a.RareUncommon);
//fprintf(filePionter,"%s",a.UncommonUncommon);
//fprintf(filePionter,"%s",a.CommonUncommon);
}

void getRare(struct Rare_struct d){
printf("%s",d.RareRare);
printf("%s",d.UncommonRare);
printf("%s",d.CommonRare);
//fprintf(filePionter,"%s",d.RareRare);
//fprintf(filePionter,"%s",d.UncommonRare);
//fprintf(filePionter,"%s",d.CommonRare);
}

void getSuperRare(struct SuperRare_struct q){
printf("%s",q.RareSuperRare);
printf("%s",q.UncommonSuperRare);
printf("%s",q.CommonSuperRare);
//fprintf(filePionter,"%s",q.RareSuperRare);
//fprintf(filePionter,"%s",q.UncommonSuperRare);
//fprintf(filePionter,"%s",q.CommonSuperRare);
}

void getUltraRare(struct UltraRare_struct e){
printf("%s",e.RareUltraRare);
printf("%s",e.UncommonUltraRare);
printf("%s",e.CommonUltraRare);
//fprintf(filePionter,"%s",e.RareUltraRare);
//fprintf(filePionter,"%s",e.UncommonUltraRare);
//fprintf(filePionter,"%s",e.CommonUltraRare);
}
*/
// int randomNubmer_Y(void){
//     srand(time(NULL));
//     return rand() % 3; 
// }
// int randomNubmer_X(void){
//     srand(time(NULL));
//     return rand() % 5; 
// }