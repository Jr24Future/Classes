#include "uart.h"
#include "timer.h"
#include "inc/tm4c123gh6pm.h"

volatile char uart_data = 0;
volatile char flag = 0;

void uart_init(int baud) {
    // Enable clock for GPIO port B and UART1
    SYSCTL_RCGCGPIO_R |= 0x02;     // Enable clock for PORTB (bit 1)
    SYSCTL_RCGCUART_R |= 0x02;     // Enable clock for UART1 (bit 1)

    // Configure GPIO pins for UART1 (PB0 = U1RX, PB1 = U1TX)
    GPIO_PORTB_AFSEL_R |= 0x03;    // Enable alternate functions for PB0 and PB1
    GPIO_PORTB_PCTL_R |= (1 << 0) | (1 << 4);  // PMC0 and PMC1 as UART
    GPIO_PORTB_DEN_R |= 0x03;      // Enable digital on PB0 and PB1
    GPIO_PORTB_DIR_R |= 0x02;      // Set PB1 as output, PB0 as input

    // Configure UART1 baud rate, data bits, parity, and stop bits
    int ibrd = 16000000 / (16 * baud);
    int fbrd = ((16000000 % (16 * baud)) * 64 + baud / 2) / baud;

    UART1_CTL_R &= ~0x01;
    UART1_IBRD_R = ibrd;
    UART1_FBRD_R = fbrd;
    UART1_LCRH_R = 0x60;
    UART1_CC_R = 0x0;
    UART1_CTL_R |= 0x301;
}

void uart_sendChar(char data) {
    while ((UART1_FR_R & 0x20) != 0);
    UART1_DR_R = data;
}

char uart_receive(void) {
    while ((UART1_FR_R & 0x10) != 0);
    return (char)(UART1_DR_R & 0xFF);
}

void uart_sendStr(const char *data) {
    while (*data != '\0') {
        uart_sendChar(*data);
        data++;
    }
}

void uart_receiveStr(char *buffer, int max_length) {
    int i = 0;
    char c;
    do {
        c = uart_receive();
        if (c == '\r' || c == '\n') break;
        if (i < max_length - 1) buffer[i++] = c;
    } while (1);
    buffer[i] = '\0';
}

void uart_interrupt_init() {
    UART1_IM_R |= 0x10;
    NVIC_EN0_R |= 0x40;
    IntRegister(INT_UART1, uart_interrupt_handler);
    IntMasterEnable();
}

void uart_interrupt_handler() {
    if (UART1_MIS_R & 0x10) {
        uart_data = (char)(UART1_DR_R & 0xFF);
        flag = 1;
        UART1_ICR_R |= 0x10;
    }
}

int uart_data_available(void) {
    return !(UART1_FR_R & 0x10);
}


/**
 * Get user input via UART for the bot's position, limited by max_value.
 */
int get_user_input(const char *prompt, int max_value, int is_multiple_of_60) {
    uart_sendStr(prompt);
    uart_sendStr("\r\n");

    char buffer[10];
    uart_receiveStr(buffer, sizeof(buffer)); // Use uart_receiveStr for input

    // Convert input to integer
    int value = atoi(buffer);

    // Validate input
    if ((is_multiple_of_60 && value % 60 != 0) || value < 0 || value > max_value) {
        uart_sendStr("\r\nInvalid input. Please try again.\r\n");
        return get_user_input(prompt, max_value, is_multiple_of_60);
    }

    return value;
}
