#include "adc.h"
#include "math.h"


float distances[] = {9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45, 48, 50};
//int ir_values[] = {1499, 1277, 1147, 1087, 987, 955, 919, 877, 842, 833, 803, 795, 779, 751, 731};
int ir_values[] = {3445, 3185, 3009, 2924, 2822, 2763, 2713, 2685, 2632, 2600, 2571, 2558, 2533, 2518, 2507};

void adc_init(void) {
    SYSCTL_RCGCGPIO_R |= 0b00000010; //Enable clock GPIOB
    SYSCTL_RCGCADC_R |= 0b01; //enable clock ADC module 0
    GPIO_PORTB_AFSEL_R |= 0b00010000; //PB4 is set
    GPIO_PORTB_DIR_R &= 0b11101111; //Enable PB4 as input
    GPIO_PORTB_DEN_R &= 0b11101111; //disable PB4 digital
    GPIO_PORTB_AMSEL_R |= 0b00010000; //Enable PB4 Analog Function
    GPIO_PORTB_ADCCTL_R = 0x00; //pins dont trigger ADC conversion

    //Initialize ADC0 and SS0
    ADC0_ACTSS_R |= 0b0001; //Sample Sequencer 0
    ADC0_EMUX_R &= 0xF1110; //Set EM0 to default trigger
    ADC0_SSMUX0_R |= 0x055555555; //user ASC input 10
    ADC0_SSCTL0_R |= 0x011111117; //samples use DX sample 8 is the end
    ADC0_IM_R |= 0b01;
}

void adc_init_2(void){
    SYSCTL_RCGCGPIO_R |= 0b11010;
    GPIO_PORTB_AFSEL_R |= 0x10;
    GPIO_PORTB_DIR_R &= ~(0x10);
    GPIO_PORTB_DEN_R &= ~(0x10);
    GPIO_PORTB_AMSEL_R |= 0x10;
    GPIO_PORTB_ADCCTL_R &= 0x00;

    SYSCTL_RCGCADC_R |= 0x1; // provide clock to ADC0
    SYSCTL_RCGCADC_R &= ~(0x2); // disable ADC1
    ADC0_ACTSS_R &= ~(0x1); // disable SS0 for config
    ADC0_EMUX_R = 0x0; // select SS0 trigger
    ADC0_SSMUX0_R |= 0xA; // select the first sample to convert in SS0
    ADC0_SSCTL0_R |= 0x6; // Interrupt enable and end of sequence maybe: ADC0_SSCTL0_R |= 0x2;
    ADC0_SAC_R |= 0x4; // 8x hardware oversample?
    ADC0_ACTSS_R |= 0x1; // re-enable SS0 after config
//    ADC0_IM_R |= 0x1;
}

int adc_read(void) {

    //adc_init();
    ADC0_PSSI_R |= 0b01; //Trigger start of conversion
    int data = 0;
    if(ADC0_RIS_R & 0b01){
        data = (ADC0_SSFIFO0_R & 0xFFF);
    }
    ADC0_ISC_R |= 0b01;

    return data;
}

int adc_avg(void){
    int d0 = adc_read();
    int d1 = adc_read();
    int d2 = adc_read();
    int d3 = adc_read();
    int d4 = adc_read();
    int d5 = adc_read();
    int d6 = adc_read();
    int d7 = adc_read();


    int avg = (d0 + d1 + d2 + d3 + d4 + d5 + d6 + d7) / 8 ;


    return avg;
}


float calculate_distance_linear(int raw_ir) {
    extern float distances[];
    extern int ir_values[];
    int i;

    if (raw_ir >= ir_values[0]) {
        return distances[0];
    }
    if (raw_ir <= ir_values[14]) {
        return distances[14];
    }
    for (i = 0; i < 14; i++) {
        if (raw_ir <= ir_values[i] && raw_ir > ir_values[i + 1]) {
            float slope = (distances[i + 1] - distances[i]) / (ir_values[i + 1] - ir_values[i]);
            return distances[i] + slope * (raw_ir - ir_values[i]);
        }
    }
    return -1;
}


