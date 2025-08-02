/*
*
*   ping.c
*
*
*
*
*
*   Owen Arnold
*
*/
#include "Timer.h"
#include "ping.h"

/*
 volatile enum {LOW, HIGH, DONE} state;
volatile unsigned int rising_time;
volatile unsigned int falling_time;
volatile unsigned int cm;
volatile unsigned int overflow;
volatile int pulseWidth;
volatile int numOverflow;

void ping_init(void){
    //Port B init
    SYSCTL_RCGCGPIO_R |= 0b10;
    SYSCTL_RCGCTIMER_R |= 0b1000;
    GPIO_PORTB_DEN_R |= 0b1000;

    //Timer init
    TIMER3_CTL_R |= 0xD00;
    TIMER3_CFG_R |= 0x4;
    TIMER3_TBMR_R |= 0x7;
    TIMER3_TBMR_R &= ~0b11000;
    TIMER3_TBPR_R |= 0xFF;
    TIMER3_TBILR_R |=0xFFFF;
    TIMER3_IMR_R |= 0x400;

    NVIC_EN1_R |= 0x10;

    IntRegister(INT_TIMER3B, TIMER3B_Handler);

    IntMasterEnable();

}

unsigned int ping_read(){
    state = LOW;
    TIMER3_CTL_R &= 0xFEFF;
    TIMER3_IMR_R |= 0xFBFF;

    send_pulse();

    TIMER3_CTL_R |= 0x100;
    TIMER3_IMR_R |= 0x400;

    //calculate distance
    pulseWidth = rising_time - falling_time;
    if(pulseWidth < 0){
        pulseWidth = abs(pulseWidth);
        overflow = TIMER3_TBPS_R;
        overflow = (overflow << 24);
        numOverflow++;
    }
    else{
        overflow = TIMER3_TBPS_R;
    }

    cm = (pulseWidth + overflow) * (34000/2);
    cm = cm / 16000000;

    return cm;
}

float send_pulse(){
    GPIO_PORTB_AFSEL_R &= ~0b1000;
    GPIO_PORTB_DIR_R |= 0b1000;
    GPIO_PORTB_DATA_R |= 0b1000;

    timer_waitMicros(25);

    GPIO_PORTB_DATA_R &= 0b0111;
    GPIO_PORTB_DIR_R &= 0b0111;

    GPIO_PORTB_AFSEL_R |= 0b1000;
    GPIO_PORTB_PCTL_R |= 0x7000;
}


void TIMER3B_Handler(){
    if(state == LOW){
        rising_time = TIMER3_TBR_R;
        state = HIGH;
    }
    else if(state == HIGH){
        falling_time = TIMER3_TBR_R;
        state == DONE;
    }

    TIMER3_ICR_R |= 0x400;
}
*/

volatile enum
{
    LOW, HIGH, DONE
} state; // set by TIMER3B_Handler

volatile unsigned int rising_time;
volatile unsigned int falling_time;

#define soundspeed 34300.0 // speed of sound in cm/us
//float trise = 0.00;
//float tfall = 0.00;
volatile float distance;

void ping_init(void)
{

    SYSCTL_RCGCGPIO_R |= 0x2; // enable GPIOB
    GPIO_PORTB_DEN_R |= 0x8; // enable digital functionality for PB3
    GPIO_PORTB_AFSEL_R |= 0x8; // enable alternate functions for PB3
    GPIO_PORTB_PCTL_R |= 0x7000; // enable T3CCP1 peripheral (Timer 3B corresponding to PB3)
//    GPIO_PORTB_DIR_R |= 0x8; // set PB3 as a digital output

    SYSCTL_RCGCTIMER_R |= 0x8;
    TIMER3_CTL_R &= ~(0x100);// disable Timer3b
    TIMER3_CTL_R |= 0xC00; // set Event Mode = both edges
    TIMER3_CFG_R |= 0x4;
    TIMER3_TBMR_R |= 0x7; // capture mode, edge-time mode
    TIMER3_TBMR_R &= ~(0x18); // count down and capture mode
    TIMER3_TBILR_R |= 0xFFFF; // set the start time for the count down
    TIMER3_TBPR_R |= 0xFF; //set the prescale
    TIMER3_IMR_R |= 0x400; // enable Timer B capture mode event
    TIMER3_CTL_R |= 0x100;

    NVIC_EN1_R |= 0x10; // set enable interrupt 36 for 16/32bit timer 3b
   // NVIC_PRI9_R |= 0x20; // give interrupt 36 a priority of 1 (bits 5-7 = interrupt [4n] priority)

    IntRegister(INT_TIMER3B, TIMER3B_Handler);
    IntMasterEnable();
//    state= LOW;
}

void Trigger(void)
{

    state = LOW;
    //disable timer and timer interurrpt
    TIMER3_CTL_R &= ~(0x100); // disable timer 3B
    TIMER3_IMR_R &= ~(0x400); // disable timer interrupts
    GPIO_PORTB_AFSEL_R &= ~(0x8); //disable alternative functions

    GPIO_PORTB_DIR_R |= 0x8; // set PB3 output
    GPIO_PORTB_DATA_R &= ~(0x8); //set PB3 low
    GPIO_PORTB_DATA_R |= 0x8; // set PB3 to high

    timer_waitMicros(5); // wait 5 microseconds
    GPIO_PORTB_DATA_R &= ~(0x8); //set PB3 low
    GPIO_PORTB_DIR_R &= ~(0x8); // set PB3 as input

    GPIO_PORTB_AFSEL_R |= 0x8; // re-enable PB3 alternative functions
    TIMER3_CTL_R |= 0x100; // re-enable Timer 3B
    TIMER3_IMR_R |= 0x400; // re-enable capture mode interrupt event

    TIMER3_ICR_R |= 0x400;
}

unsigned int ping_read(void)
{
    Trigger();
    while (state != DONE)
    {

    }

    distance = soundspeed * (((float)rising_time - (float)falling_time)/(16000000.0))/2.0; // multiply rise and fall time by speed of sound to calculate distance


    return (int) distance;

}

void TIMER3B_Handler(void)
{

    if (state == LOW)
    {
        rising_time = TIMER3_TBR_R;
        state = HIGH;
    }
    else if (state == HIGH)
    {
        falling_time = TIMER3_TBR_R;
        state = DONE;
    }

    TIMER3_ICR_R |= 0x400; // Timer B Capture Mode Event Interrupt Clear

//    state = LOW;
}


int overflow(void) // calculating distance and accounting for overflow
{
    int over;
    if (rising_time - falling_time < 0) // if fall time is greater than rise time, overflow has occurred because the timer has restarted at the high time
    {
        over++;
    }
    return over;
}


