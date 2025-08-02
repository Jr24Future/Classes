/*

#include <inc/tm4c123gh6pm.h>
#include <stdint.h>
#include <math.h>
#include "bno055.h"
#include "lcd.h"
#include "uart.h"
#include "timer.h"
#include "open_interface.h"

#define HEADING_GAIN 5// Proportional gain for heading correction

int main() {
    timer_init();
    uart_init(115200);
    lcd_init();

    // Predefined calibration data
    bno_calib_t bno_calibration = {0xFFDF,0xFFDC,0xFFCA,0x0347,0x0049,0xF856,0x0001,0xFFFE,0x0000,0x03E8,0x0295};

    // Allocate the BNO055 structure
    bno_t *bno = bno_alloc();
    bno_initCalib(&bno_calibration);//&bno_calibration
    bno_calibrateInteractive();
    // Robot initialization
    oi_t *oi = oi_alloc();
    oi_init(oi);

    // Target heading
    float target_heading = 184;  // Target direction in degrees

    // Main control loop
    while (1) {
        oi_update(oi);

        // Stop if a wheel is dropped
        if (oi->wheelDropLeft || oi->wheelDropRight) {
            oi_setWheels(0, 0);
        } else {
            // Update BNO sensor to get the current heading
            bno_update(bno);
            float current_heading = bno->euler.heading / 16.0;  // Convert to degrees

            // Calculate heading difference
            float heading_diff = target_heading - current_heading;

            // Normalize heading difference to [-180, 180]
            if (heading_diff > 180) heading_diff -= 360;
            if (heading_diff < -180) heading_diff += 360;

            // Calculate correction power
            int16_t correction = (int16_t)(heading_diff * HEADING_GAIN);

            // Adjust wheel speeds
            int16_t left_power = 100 - correction;  // Base speed is 100
            int16_t right_power = 100 + correction;

            // Clamp wheel powers to the valid range [-255, 255]
            if (left_power > 255) left_power = 255;
            if (left_power < -255) left_power = -255;
            if (right_power > 255) right_power = 255;
            if (right_power < -255) right_power = -255;

            // Set wheel speeds
            oi_setWheels(left_power, right_power);

            // Display the current heading on the LCD
            lcd_printf("Heading: %.2f°", current_heading);

            // Send heading and control information over UART for debugging
            char buffer[100];
            sprintf(buffer, "Heading: %.2f | Target: %.2f | Diff: %.2f | L: %d | R: %d\r\n",
                    current_heading, target_heading, heading_diff, left_power, right_power);
            uart_sendStr(buffer);
        }

        // Delay for stability between each loop iteration
        timer_waitMillis(100);  // Update every 100ms
    }

    // Free the allocated resources (unlikely to reach here in a typical embedded use)
    bno_free(bno);
    oi_free(oi);

    return 0;
}

*/


