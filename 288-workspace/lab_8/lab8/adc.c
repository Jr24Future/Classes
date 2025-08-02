/*
 * adc.c
 *
 *  Created on: Oct 23, 2024
 *      Author: ryanchin
 */
#include "adc.h"
#include "uart.h"

void adc_init(void){

    /*
     * For ADC#, # is replaced with either 0 or 1. For SS...#, # is replaced with 0, 1, 2, or 3. You choose the number to use.
     * ADC Active Sample Sequencer (ADCACTSS): ADC#_ACTSS_R
     * ADC Raw Interrupt Status (ADCRIS): ADC#_RIS_R
     * ADC Interrupt Mask (ADCIM): ADC#_IM_R
     * ADC Interrupt Status and Clear (ADCISC): ADC#_ISC_R
     * ADC Event Multiplexer Select (ADCEMUX): ADC#_EMUX_R
     * ADC Processor Sample Sequence Initiate (ADCPSSI): ADC#_PSSI_R
     * ADC SampleSequence Input Multiplexer Select (0-3) (ADCSSMUX0-3): ADC#_SSMUX#_R
     * ADC Sample Sequence Control (0-3) (ADCSSCTL0-3): ADC#_SSCTL#_R
     * ADC Sample Sequence Result FIFO (0-3) (ADCSSFIFO0-3): ADC#_SSFIFO#_R
     * ADC Clock Configuration (ADCCC): ADC#_CC_R
     * ADC Run Mode Clock Gating Control (RCGCADC): SYSCTL_RCGCADC_R
     * ADC Peripheral Ready (PRADC): SYSCTL_PRADC_R
     */

    SYSCTL_RCGCADC_R |= 0x0001; // activate ADC0
    SYSCTL_RCGCGPIO_R |= 0x02;  // activate PORTB clock
    while((SYSCTL_PRGPIO_R & 0x02) != 0x02) {}; // wait until ready

    GPIO_PORTB_DIR_R &= ~0x10;  // make PB4 as input
    GPIO_PORTB_AFSEL_R |= 0x10; // enable PB4 alt function
    GPIO_PORTB_DEN_R &= ~0x10;  // disable digital IO on PB4
    GPIO_PORTB_AMSEL_R |= 0x10; // enable analog functionality on PB4
    while((SYSCTL_PRADC_R & 0x0001) != 0x0001) {};  // wait until ready

    ADC0_PC_R &= ~0xF;
    ADC0_PC_R |= 0x1;   // set 125k samples/second
    ADC0_SSPRI_R = 0x0123;  // sequencer 3 highest priority
    ADC0_ACTSS_R &= ~0x0008;    // disable sequencer 3
    ADC0_EMUX_R &= ~0xF000; //sequencer 3 software trigger
    ADC0_SSMUX3_R &= ~0x000F;
    ADC0_SAC_R = ADC_SAC_AVG_16X;
    ADC0_SSMUX3_R += 10; // set channel
    ADC0_SSCTL3_R = 0x0006;  // no TS0 D0, yes IE0 END0
    ADC0_IM_R &= ~0x0008;   // disable SS3 interrupts
    ADC0_ACTSS_R |= 0x0008; // enable sequencer 3

}

int adc_read(void){
    int result;

    ADC0_PSSI_R = 0x0008;   // initiate SS3
    while((ADC0_RIS_R & 0x08) == 0) {}; // wait for conversion
    result = ADC0_SSFIFO3_R & 0xFFF;   // read result
    ADC0_ISC_R = 0x0008;    // acknowledge completion

    return result;
}



