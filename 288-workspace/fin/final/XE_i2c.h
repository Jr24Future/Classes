#ifndef I2C_H_
#define I2C_H_

#include <stdbool.h>
#include <stdint.h>
#include <stddef.h>
#include <inc/tm4c123gh6pm.h>

void i2c_init();
size_t i2c_sendByte(uint8_t addr, uint8_t data);
size_t i2c_sendBytes(uint8_t addr, uint8_t* data, size_t dataLen);
uint8_t i2c_recByte(uint8_t addr);
uint8_t* i2c_recBytes(uint8_t addr, size_t dataLen);
uint8_t i2c_requestByte(uint8_t addr, uint8_t request);

#endif
