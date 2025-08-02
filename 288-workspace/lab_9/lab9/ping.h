#ifndef PING_H_
#define PING_H_

#include <stdint.h>
#include <stdbool.h>

void ping_init(void);

void send_pulse(void);

int ping_read(void);

float calculate_distance(int pulse_width);

void TIMER3B_Handler(void);

float calculate_time_in_ms(int pulse_width);


extern volatile int overflow_count;


#endif /* PING_H_ */
