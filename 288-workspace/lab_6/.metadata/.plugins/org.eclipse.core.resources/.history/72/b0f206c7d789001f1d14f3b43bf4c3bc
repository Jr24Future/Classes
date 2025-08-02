/**
 * lab6_template_extra_help.c
 *
 * Description: This is file is meant for those that would like a little
 *              extra help with formatting their code.
 *
 */

#define _RESET 0
#define _PART1 0
#define _PART2 0
#define _PART3 0
#define _PART4 1
#define BUFFER_SIZE 20

#include "timer.h"
#include "lcd.h"
#include "uart.h"
#include <stdio.h>
#include <string.h>
#include <math.h>
#include "servo.h"


volatile  char uart_data;
volatile  char flag;


int main(void)
{
    uart_init(115200);
    lcd_init();
    timer_init();
    uart_interrupt_init();


#if _PART1
    lcd_printf("UART Part 1 Test");

    char buffer[20];
    int count = 0;

    while (1)
    {
        char received = uart_receive();

        if (received == '\r' || count == 20)
        {
            buffer[count] = '\0';
            lcd_clear();
            lcd_printf("%s", buffer);
            count = 0;
        }
        else
        {
            buffer[count++] = received;
            lcd_printf("Char: %c\nCount: %d", received, count);
        }
    }

#elif _PART2
    lcd_printf("UART Echo Test");

    while (1)
    {
        char received = uart_receive();
        uart_sendChar(received);

        if (received == '\r')
        {
            uart_sendChar('\n');
        }

        lcd_printf("Received: %c", received);
    }

#elif _PART3
    lcd_printf("UART Interrupt Test");
    char last_displayed = '\0';

    while (1)
       {
           if (flag == 1)
           {
               flag = 0;

               if (uart_data != last_displayed)
               {
                   lcd_clear();
                   lcd_printf("Received: %c", uart_data);
                   last_displayed = uart_data;
               }

               uart_sendChar(uart_data);

               if (uart_data == '\r')
               {
                   uart_sendChar('\n');
               }
           }
       }

#endif


#elif _PART4

#endif
    return 0;
}
