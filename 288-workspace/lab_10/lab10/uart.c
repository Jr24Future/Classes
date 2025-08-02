#include "uart.h"
#include "timer.h"
#include "inc/tm4c123gh6pm.h"


volatile char uart_data = 0;
volatile char flag = 0;

#define UART1_CLOCK GPIO_PORTB
#define GPIO_PORTB_ENABLE SYSCTL_RCGCGPIO_R
#define UART1_ENABLE SYSCTL_RCGCUART_R

void uart_init(int baud)
{
    // Enable clock for GPIO port B and UART1
    SYSCTL_RCGCGPIO_R |= 0x02;     // Enable clock for PORTB (bit 1)
    SYSCTL_RCGCUART_R |= 0x02;     // Enable clock for UART1 (bit 1)

    // Configure GPIO pins for UART1 (PB0 = U1RX, PB1 = U1TX)
    GPIO_PORTB_AFSEL_R |= 0x03;    // Enable alternate functions for PB0 and PB1
    GPIO_PORTB_PCTL_R |= (1 << 0) | (1 << 4);  // PMC0 and PMC1 as UART (page 688)
    GPIO_PORTB_DEN_R |= 0x03;      // Enable digital on PB0 and PB1
    GPIO_PORTB_DIR_R |= 0x02;      // Set PB1 as output, PB0 as input

    // Configure UART1 baud rate, data bits, parity, and stop bits
    int ibrd = 16000000 / (16 * baud);  // Integer part of the baud rate divisor
    int fbrd = ((16000000 % (16 * baud)) * 64 + baud / 2) / baud;  // Fraction part

    UART1_CTL_R &= ~0x01;          // Disable UART1 for configuration
    UART1_IBRD_R = ibrd;           // Set integer baud rate divisor
    UART1_FBRD_R = fbrd;           // Set fractional baud rate divisor
    UART1_LCRH_R = 0x60;           // Set data length to 8 bits, no parity, 1 stop bit
    UART1_CC_R = 0x0;              // Use system clock
    UART1_CTL_R |= 0x301;          // Enable UART1, TXE, RXE
}

void uart_sendChar(char data)
{
    // Wait until there is space in the FIFO
    while ((UART1_FR_R & 0x20) != 0);
    // Transmit data
    UART1_DR_R = data;
}

char uart_receive(void)
{
    // Wait until there is data in the receive FIFO
    while ((UART1_FR_R & 0x10) != 0);
    // Read received data
    return (char)(UART1_DR_R & 0xFF);
}

void uart_sendStr(const char *data)
{
    while (*data != '\0')
    {
        uart_sendChar(*data);
        data++;
    }
}

// Interrupt Initialization
void uart_interrupt_init()
{
    // Enable interrupts for receiving bytes through UART1
    UART1_IM_R |= 0x10;  // Enable interrupt on receive
        NVIC_EN0_R |= 0x40;  // Enable UART1 interrupt in NVIC (bit 6 for UART1)
        IntRegister(INT_UART1, uart_interrupt_handler);  // Register your interrupt handler
        IntMasterEnable();  // Enable global interrupts
}

void uart_interrupt_handler()
{
    // Check the Masked Interrupt Status
    if (UART1_MIS_R & 0x10) {  // Check if receive interrupt occurred
            uart_data = (char)(UART1_DR_R & 0xFF);  // Read the received character
            flag = 1;  // Set flag to indicate new data has been received
            UART1_ICR_R |= 0x10;  // Clear the interrupt
        }
}

int uart_data_available(void) {
    return !(UART1_FR_R & 0x10); // Check if the receive FIFO is empty
}
