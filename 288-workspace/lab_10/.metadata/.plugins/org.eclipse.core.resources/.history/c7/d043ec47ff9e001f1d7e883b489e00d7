/**
 * main.c
 */

#include "button.h"
#include "servo.h"
#include "Timer.h"
#include "lcd.h"
#include "uart.h"

int main(void) {
    // Initialize peripherals
    lcd_init();
    button_init();
    init_button_interrupts();
    uart_init(115200);
    uart_interrupt_init();
    servo_init();

    char message[100];
    int number = 0;
    int clock_amount = 0;
    int button = 0;
    int servo_position = 90;
    int clockwise = 1;
    int location = 0;
    float calc_move = 0;

    int first_var = 0;
    int second_var = 0;
    int third_var = 0;

    while (1) {

        uart_sendStr("Enter amount to move servo: ");

        // 3 digits (Ex. 120 - Probably need to improve this because its hard to send the value into Putty like this)
        first_var = uart_receive() - 48; // ASCII of 0 is 48
        sprintf(message, "%d", first_var);
        uart_sendStr(message);

        second_var = uart_receive() - 48;
        sprintf(message, "%d", second_var);
        uart_sendStr(message);

        third_var = uart_receive() - 48;
        sprintf(message, "%d", third_var);
        uart_sendStr(message);

        number = (first_var * 100) + (second_var * 10) + third_var;

        sprintf(message, "\r\n Moving servo: %d degrees \r\n", number);
        uart_sendStr(message);

        clock_amount = servo_move(number % 181);

        sprintf(message, "Moved servo: %d Clock Cycles \r\n", clock_amount);
        uart_sendStr(message);

        button = button_getButton();

        switch (button) {
        case 1:
            if (clockwise == 1) {
                servo_position--;
                location = servo_move(servo_position);
            } else {
                servo_position++;
                location = servo_move(servo_position);
            }
            break;
        case 2:
            if (clockwise == 1) {
                servo_position -= 5;
                location = servo_move(servo_position);
            } else {
                servo_position += 5;
                location = servo_move(servo_position);
            }
            break;
        case 3:
            if (clockwise == 1) {
                clockwise = 0;
            } else {
                clockwise = 1;
            }
            break;
        case 4:
            if (clockwise == 1) {
                servo_position = 48; // 0 degrees in ASCII
                location = servo_move(servo_position);
            } else {
                servo_position = 203; // 180 degrees in ASCII
                location = servo_move(servo_position);
            }
            break;
        }

        if (clockwise) {
            sprintf(message, "Servo at: %d\n Going lower", location);

        } else {
            sprintf(message, "Servo at: %d\n Going higher", location);
        }

        lcd_printf(message);
        timer_waitMillis(100);
    }
}
