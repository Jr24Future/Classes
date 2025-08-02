
#ifndef ADC_H_
#define ADC_H_

#include "Timer.h"
#include <inc/tm4c123gh6pm.h>

void adc_init();
void adc_init_2();
int adc_read();
int adc_avg();
float dist(int avg);
float calculate_distance_linear(int raw_ir);

#endif /* UART_H_ */
