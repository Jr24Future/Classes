#include<stdio.h>

double DrivingCost(double milesDriven, double milesPerGallon, double dollarsPerGallon){
	return (milesDriven / milesPerGallon) * dollarsPerGallon;
}

void main(void){
	double milesDriven;
	double milesPerGallon;
	double dollarsPerGallon;
	
	printf("Miles Driven:       ");
	scanf("%lf", &milesDriven);
	printf("Miles Per Gallon:   ");
	scanf("%lf", &milesPerGallon);
	printf("Dollars Per Gallon: ");
	scanf("%lf", &dollarsPerGallon);
	printf("Driving Cost: %.2lf", DrivingCost(milesDriven, milesPerGallon, dollarsPerGallon));
}