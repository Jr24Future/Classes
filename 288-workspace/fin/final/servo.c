
#include "servo.h"
#include "Timer.h"
#include "lcd.h"
#include "button.h"

float right_val = 313010;
float left_val = 283588;
float mid_val;
float current_degrees;


void servo_init(){
    extern float current_degrees;
    extern float right_val;

    SYSCTL_RCGCGPIO_R |= 0b010; //enable GPIOB
    GPIO_PORTB_DEN_R |= 0b0100000; //enable digital functionality for PB5
    GPIO_PORTB_AFSEL_R |= 0b0100000; //enable alternate function for PB5
    GPIO_PORTB_PCTL_R |= 0x0700000; //set PMC5 to 7, T1CCP1 (timer 1)

    SYSCTL_RCGCTIMER_R |= 0b010; //enable timer module 1

    TIMER1_CTL_R &= ~(0b0100000000); //disable timerB

    TIMER1_CTL_R |= 0b0110000000000; //set to both edges
    TIMER1_CFG_R |= 0x04;
    TIMER1_TBMR_R |= 0b01010; //enable PWM mode, periodic Timer mode
    TIMER1_TBMR_R &= 0b1011; //set to edge time mode
    TIMER1_TBPR_R = 0x04; //first 8 bits of period in clock cycles
    TIMER1_TBILR_R = 0xE200; //last 16 bits of period in clock cycles



    TIMER1_TBPMR_R = 0x04; //first 8 bits of LOW PW

    TIMER1_TBMATCHR_R = (0xFFFF & (int)right_val);

    TIMER1_CTL_R |= 0b0100000000; //enable timerB

    current_degrees = 0;
    timer_waitMillis(740);
}


int servo_move(float degrees){


    extern float right_val, left_val;
    float pulsewidth;
    float move_degrees = degrees - current_degrees;
    int wait_time;

    wait_time = abs(move_degrees) * (725.0 / 180.0);


    float m = (right_val - left_val) / (180.0);
    pulsewidth = right_val - (degrees * (m));


    TIMER1_CTL_R &= ~(0b0100000000); //disable timerB

    TIMER1_TBMATCHR_R = (0xFFFF & (int)pulsewidth); //last 16 bits of LOW PW
    TIMER1_CTL_R |= 0b0100000000; //enable timerB


    current_degrees = degrees;
    timer_waitMillis(wait_time);
    return (int)pulsewidth;
}

int servo_move_fast(float degrees){


    extern float right_val, left_val;
    float pulsewidth;


    float m = (right_val - left_val) / (180.0);
    pulsewidth = right_val - (degrees * (m));


    TIMER1_CTL_R &= ~(0b0100000000); //disable timerB

    TIMER1_TBMATCHR_R = (0xFFFF & (int)pulsewidth); //last 16 bits of LOW PW
    TIMER1_CTL_R |= 0b0100000000; //enable timerB

    timer_waitMillis(100);

    current_degrees = degrees;

    return (int)pulsewidth;
}

/*
void lcd_printservoinfo(int x){
    extern short mode;
    extern float current_degrees;

    char print[10];
    sprintf(print, "%d   %f  %d", x, current_degrees, mode);
    lcd_puts(print);
}
*/

void servo_cal(){

    short mode;
    int x;
    mode = 0;
    x = servo_move(90);
    char print[10];
    float degrees = 90;
    lcd_init();
    button_init();

    while(1){

            if ((GPIO_PORTE_DATA_R & 0b00000001) == 0){ //check if button 1 is pressed
                lcd_clear();
                if(mode == 0){
                    degrees++;
                }
                else if(mode == 1){
                    degrees--;
                }
                    x = servo_move(degrees);
                    sprintf(print, "%d   %f   %d", x, degrees, mode);
                    lcd_puts(print);
            }
            else if ((GPIO_PORTE_DATA_R & 0b00000010) == 0){ //check if button 2 is pressed
                lcd_clear();
                if(mode == 0){
                    degrees += 5;
                }
                else if(mode == 1){
                    degrees -= 5;
                }
                x = servo_move(degrees);
                sprintf(print, "%d   %f   %d", x, degrees, mode);
                lcd_puts(print);
            }
            else if ((GPIO_PORTE_DATA_R & 0b00000100) == 0){  //check if button three is pressed

               lcd_clear();
               if(mode == 0){
                   mode = 1;
               }
               else{
                   mode = 0;
               }
               sprintf(print, "%d   %f   %d", x, degrees, mode);
               lcd_puts(print);
               timer_waitMillis(200);
                   }
            else if ((GPIO_PORTE_DATA_R & 0b00001000) == 0){   //check if button 4 is pressed
                lcd_clear();
                if(mode == 0){
                    degrees = 180;
                }
                else if(mode == 1){
                    degrees = 0;
                }
                x = servo_move(degrees);
                sprintf(print, "%d   %f   %d", x, degrees, mode);
                lcd_puts(print);
                           }

        }
}

void servo_stop(){
    TIMER1_CTL_R &= ~(0b0100000000);
}
