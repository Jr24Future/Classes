#include "ping.h"
#include "Timer.h"
#include "math.h"
#include "inc/tm4c123gh6pm.h"
#include "driverlib/interrupt.h"

#define SYSTEM_CLOCK_FREQUENCY 16000000 // 16 MHz for TM4C123
#define SPEED_OF_SOUND_CM_PER_S 34000.0 // Speed of sound in cm/s
#define TIMEOUT_LIMIT 1000000           // Timeout limit to avoid infinite loop
#define MIN_VALID_PULSE_WIDTH 100       // Minimum valid pulse width to filter noise


volatile unsigned long START_TIME = 0;  // Rising time of the pulse
volatile unsigned long END_TIME = 0;    // Falling time of the pulse
volatile int overflow_count ;
volatile enum {LOW, HIGH, DONE} STATE = LOW; // State of ping echo pulse

void ping_init(void) {
    SYSCTL_RCGCGPIO_R |= 0x2; // Enable PORTB clock
    while ((SYSCTL_PRGPIO_R & 0x2) == 0) {} // Wait for PORTB to be ready

    GPIO_PORTB_AFSEL_R |= 0x8; // Enable alt function for PB3
    GPIO_PORTB_DEN_R |= 0x8; // Enable PB3 as digital
    GPIO_PORTB_PCTL_R |= 0x7000; // Set PB3 to T3CCP1 (Timer)

    SYSCTL_RCGCTIMER_R |= 0x8; // Enable clock for Timer 3
    while ((SYSCTL_PRTIMER_R & 0x8) == 0) {} // Wait for timer to be ready

    TIMER3_CTL_R &= ~0x100; // Disable Timer 3B during config
    TIMER3_CFG_R = 0x4; // 16-bit timer config
    TIMER3_TBMR_R = 0x07; // Capture mode, edge-time, count-up mode
    TIMER3_TBILR_R = 0xFFFE; // Load with 0xFFFE to avoid errata issues
    TIMER3_TBPR_R = 0xFF; // Prescaler for extended range

    TIMER3_ICR_R = 0x400; // Clear capture event interrupt
    TIMER3_IMR_R |= 0x400; // Enable capture event interrupt

    NVIC_EN1_R |= 0x10; // Enable IRQ 36 for Timer 3B
    IntRegister(INT_TIMER3B, TIMER3B_Handler); // Register the handler
    IntMasterEnable(); // Enable global interrupts

    TIMER3_CTL_R |= 0x100; // Enable Timer 3B
}

void send_pulse(void) {
    GPIO_PORTB_AFSEL_R &= ~0x08; // Disable alt function
    GPIO_PORTB_PCTL_R &= ~0x0000F000; // Set PB3 as GPIO
    GPIO_PORTB_DIR_R |= 0x08; // Set PB3 as output
    GPIO_PORTB_DATA_R |= 0x08; // Set PB3 high
    timer_waitMicros(10); // Wait 10 microseconds
    GPIO_PORTB_DATA_R &= ~0x08; // Set PB3 low
    GPIO_PORTB_DIR_R &= ~0x08; // Set PB3 as input
    GPIO_PORTB_AFSEL_R |= 0x08; // Enable alt function
    GPIO_PORTB_PCTL_R |= 0x7000; // Set PB3 to T3CCP1
}

void TIMER3B_Handler(void) {
    if (TIMER3_MIS_R & 0x400) {
        uint32_t timer_value = TIMER3_TBR_R;
        if (STATE == LOW) {
            START_TIME = timer_value;
            STATE = HIGH;
        } else if (STATE == HIGH) {
            END_TIME = timer_value;
            STATE = DONE;

            if (END_TIME > START_TIME) {
                overflow_count++;
            }
        }
        TIMER3_ICR_R = 0x400; // Clear interrupt
    }
}

int ping_read(void) {
    send_pulse(); // Send a pulse to activate PING)))
    STATE = LOW;
    int counter = 0;

    // Wait for data collection with a timeout to prevent infinite loops
    while (STATE != DONE && counter < TIMEOUT_LIMIT) {
        counter++;
    }

    if (counter >= TIMEOUT_LIMIT) {
        // Timeout occurred, return an error code or handle appropriately
        return -1; // Indicate a timeout or error
    }

    int clk = (START_TIME < END_TIME) ? (START_TIME + 0xFFFFFF - END_TIME) : (START_TIME - END_TIME);

    // Validate pulse width to filter out noise
    if (clk < MIN_VALID_PULSE_WIDTH) {
        return -1; // Indicate invalid or noisy measurement
    }

    STATE = LOW; // Reset for next read
    return clk;
}

float calculate_distance(int pulse_width) {
    if (pulse_width <= 0) {
        return -1.0; // Return an error distance for invalid pulse width
    }
    float time_in_seconds = (float)pulse_width / SYSTEM_CLOCK_FREQUENCY; // Time in seconds
    float distance_cm = (time_in_seconds * SPEED_OF_SOUND_CM_PER_S) / 2; // Distance in cm (round-trip)
    return distance_cm;
}

float calculate_time_in_ms(int pulse_width) {
    return (float)pulse_width / (SYSTEM_CLOCK_FREQUENCY / 1000.0); // Convert clock cycles to milliseconds
}
