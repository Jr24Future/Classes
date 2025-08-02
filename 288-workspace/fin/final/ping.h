/*
*
*   uart.h
*
*   Used to set up the UART
*   uses UART1 at 115200
*
*
*   @author Dane Larson
*   @date 07/18/2016
*   Phillip Jones updated 9/2019, removed WiFi.h
*/

#ifndef PING_H_
#define PING_H_

#include "Timer.h"
#include <inc/tm4c123gh6pm.h>

void ping_init(void);

unsigned int ping_read();

float send_pulse();

void TIMER3B_Handler();

int overflow(void);









#endif /* UART_H_ */
