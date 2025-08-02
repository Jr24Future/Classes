#include <stdint.h>
#include "ping.h"
#include "Timer.h"
#include "lcd.h"
#include "uart.h"

volatile int run_measurements = 0;

int main(void) {
    // Initialize peripherals
    lcd_init();
    ping_init();
    uart_init(115200);

    char command;
        while (1) {
            if (uart_data_available()) {
                command = uart_receive();
                if (command == 'm') {
                    run_measurements = 1;  // Start continuous measurements
                } else if (command == 's') {
                    run_measurements = 0;  // Stop measurements
                }
            }

            if (run_measurements) {
                // Trigger the sensor and read the pulse width
                int pulse_width = ping_read();
                float distance = calculate_distance(pulse_width) - 1.0;
                float time_ms = calculate_time_in_ms(pulse_width);
                lcd_clear();

                // Check for valid pulse width
                if (pulse_width > 0) {
                    // Display the pulse width and distance on the LCD
                    lcd_printf("Width: %u\nTime: %.2f ms\nDist: %.2f cm\nOverflows: %d", pulse_width, time_ms, distance, overflow_count);
                    uart_sendStr("Measurement completed\n");
                } else {
                    lcd_printf("No Pulse Detected");
                    uart_sendStr("Error: No pulse detected\n");
                }

                timer_waitMillis(500); // Delay before the next measurement
            }
        }

        return 0;
    }
