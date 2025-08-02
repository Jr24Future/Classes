#include "Timer.h"
#include <inc/tm4c123gh6pm.h>


  typedef struct{
     short objnum;
     short angle;
     short fangle;
     int midangle;
     short width;
     float linwidth;
     float IRdist;
     float Ping;
     float clearance;
     float offset_angle;
   } sensor_info;

   typedef struct{
        short gapnum;
        short angle;
        short fangle;
        int midangle;
        short width;
      } gap;
int scan_180();
int scan_180to360();
int scan_small();
int scan_small_back();
int scan_90to130();
int scan_130to90();
int scan_50to90();
int scan_90to50();
int scan_left();
int scan_right();
int print_scan();
float find_objs();
int find_gaps();
