#include "servo.h"
#include "Timer.h"
#include "inc/tm4c123gh6pm.h"
#include "lcd.h"
#include "button.h"

volatile float current_degrees = 90.0;
volatile int direction = 1;

void servo_init(void) {
    SYSCTL_RCGCGPIO_R |= 0x02; // Turn on clock to GPIO port B
    while ((SYSCTL_PRGPIO_R & 0x02) == 0) {}

    // Configure PB5 as T1CCP1 (Timer 1B PWM)
    GPIO_PORTB_AFSEL_R |= 0x20;
    GPIO_PORTB_PCTL_R &= ~0x00F00000;
    GPIO_PORTB_PCTL_R |= 0x00700000;
    GPIO_PORTB_DIR_R |= 0x20;
    GPIO_PORTB_DEN_R |= 0x20;

    // Enable clock for Timer 1 and wait for it to be ready
    SYSCTL_RCGCTIMER_R |= 0x02;
    while ((SYSCTL_PRTIMER_R & 0x02) == 0) {}

    // Configure Timer 1B for PWM mode
    TIMER1_CTL_R &= ~0x0100;    // Disable Timer 1B during configuration
    TIMER1_CFG_R = 0x04;        // Set to 16-bit timer configuration
    TIMER1_TBMR_R = 0x0A;       // Enable PWM mode and periodic mode

    // Set the period for a 20 ms PWM signal (320,000 ticks)
    TIMER1_TBILR_R = 0xE200;    // Lower 16 bits
    TIMER1_TBPR_R = 0x04;       // Upper 8 bits for a total of 320,000 ticks

    TIMER1_CTL_R |= 0x0100;     // Enable Timer 1B //delete
}

int servo_move(float degrees) {
    if (degrees < 0) degrees = 0;
    if (degrees > 180) degrees = 180;
    current_degrees = degrees;

    unsigned int match_0_deg = 311822;
    unsigned int match_90_deg = 298133;
    unsigned int match_180_deg = 284900;

    unsigned int match_value = match_0_deg - ((match_0_deg - match_180_deg) * (degrees / 180.0));

    TIMER1_TBMATCHR_R = match_value & 0xFFFF;
    TIMER1_TBPMR_R = (match_value >> 16) & 0xFF;

    // on wait off

//   timer_waitMillis(1);
    lcd_printf("Pos: %.1f°\nMatch: %u", degrees, match_value);

    return match_value;
}
