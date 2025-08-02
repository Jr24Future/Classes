/*
 * servo.c
 *
 *  Created on: Nov 4, 2024
 *      Author: ryanchin
 */

#include "servo.h"
#include "Timer.h"

void servo_init(void){

    SYSCTL_RCGCGPIO_R |= 0b00000010; //turn on clock to gpio port b
    while((SYSCTL_PRGPIO_R & 0b10)==0){};
    GPIO_PORTB_AFSEL_R |= 0x20; //wire 5 to output
    GPIO_PORTB_PCTL_R &= 0x0FFFFF; //force 0 in pctl5
    GPIO_PORTB_PCTL_R |= 0x700000; //force 1 in pctl5
    GPIO_PORTB_DIR_R |= 0x20;  //wire 5 to output
    GPIO_PORTB_DEN_R |= 0x20;

    SYSCTL_RCGCTIMER_R |= 0x02; //timer 1 clock start
    while((SYSCTL_PRTIMER_R & 0x02)==0){};
    TIMER1_CTL_R &= ~0x0100; //disable timer before config
    TIMER1_CFG_R = 0x4; //set size of timer to 16
    TIMER1_TBMR_R = 0x00A; //periodic and PWM enable
    TIMER1_TBILR_R = 0xE200; //lower 16 bits of the interval with pulse
    TIMER1_TBPR_R = 0x04; //set upper 8 bits of the interval with pulse
    TIMER1_CTL_R |= 0x0100; //enable timer
}

int servo_move(float degrees){

    // 0 degrees = 1 millisecond
    // 90 degrees = 1.5 milliseconds
    // 180 degrees = 2 milliseconds
    // 320,000 = timer ticks in 20ms duration
    // 0 - 32,000 clock cycles
    unsigned int move_amount = (320000 - ((degrees / 180) * 32000));
    TIMER1_TBMATCHR_R = move_amount & 0xFFFF;
    TIMER1_TBPMR_R = (move_amount >> 16) & 0xFF;

    timer_waitMillis(300); //wait for servo to move

    return TIMER1_TBMATCHR_R;
}
