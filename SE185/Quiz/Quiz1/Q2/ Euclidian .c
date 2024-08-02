#include <stdio.h>
#include <math.h>
double distance (double x1, double y1, double x2, double y2) {
    return sqrt(pow(y2 - y1, 2) + pow(x2 - x1, 2));
}
int main()
{
        double schoolX = 15.55, schoolY = 55.15;

        printf("Distance between school and Mike is %lf\n", distance(schoolX, schoolY, 22.05, 85.10));
        printf("Distance between school and Mary is %lf\n", distance(schoolX, schoolY, 43.25, 9.80));
        printf("Distance between school and Gary is %lf\n", distance(schoolX, schoolY, 2.55, 72.86));
        printf("Distance between school and Logan is %lf\n", distance(schoolX, schoolY, 15.15, 40.40));

    return 0;
}
