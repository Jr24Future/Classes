/**
 * main.c
 */
#include "math.h"
#include <stdint.h>
#include <stdbool.h>
#include <inc/tm4c123gh6pm.h>

#include "Timer.h"
#include "lcd.h"
#include "uart.h"
#include "adc.h"
#include "cyBot_Scan.h"
#include "movement.h"
#include "open_interface.h"

// Define constants
#define SCAN_ANGLE_STEP 2
#define IR_THRESHOLD 50
#define NUM_SAMPLES 3
#define PING_AVERAGE_COUNT 5
#define TARGET_DISTANCE_CM 5

typedef struct {
    int start_angle;
    int end_angle;
    int distance;
} ObjectInfo;

ObjectInfo detected_objects[10];
int object_count = 0;

volatile char uart_data;
volatile char flag = 0;

typedef enum { MANUAL, AUTONOMOUS_SCAN, AUTONOMOUS_TURN, AUTONOMOUS_MOVE } Mode;
Mode current_mode = MANUAL;

float distances[] = {9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45, 48, 50};
int ir_values[] = {1499, 1277, 1147, 1087, 987, 955, 919, 877, 842, 833, 803, 795, 779, 751, 731};

void setup() {

    timer_init();
    lcd_init();
    uart_init(115200);
    uart_interrupt_init();
    cyBOT_init_Scan(0b0001); // ENABLE SERVO ONLY, IR "ENABLED" BY ADC
    adc_init();

    // Set calibration values for your specific CyBot
    right_calibration_value = 238000; // Calibrated for 0 degrees (right)
    left_calibration_value = 1235500; // Calibrated for 180 degrees (left)

}

int adc_distance() {

    int i;
    int raw_ir = adc_read();   // take a reading from the ADC
    if (raw_ir >= ir_values[0]) {
        return distances[0];
    }
    if (raw_ir <= ir_values[14]) {
        return distances[14];
    }
    for (i = 0; i < 14; i++) {
        if (raw_ir <= ir_values[i] && raw_ir > ir_values[i + 1]) {
            float slope = (distances[i + 1] - distances[i]) / (float)(ir_values[i + 1] - ir_values[i]);
            return distances[i] + slope * (raw_ir - ir_values[i]);
        }
    }
    return -1;

    // PART 1 & 2
    // char data[] = "";
    // int distance;
    // int value = adc_read();
    // calculate the distance
    // distance = (155080 * pow(value, -1.215));
    // format string and print to PuTTY and LCD
    // sprintf(data, "ADC = %d\tDist = %d cm \n\r", value, distance);
    // lcd_printf("ADC = %d\nDist = %d cm", value, distance);
    // uart_sendStr(data);
    // timer_waitMillis(100);
}

void control_movement(oi_t *sensor_data, char command) {
    switch (command) {
    case 'w':
        move_forward_with_bump(sensor_data, 5); // Move forward by 10 cm, handle bump
        break;
    case 'a':
        turn_counter_clockwise(sensor_data, 15); // Turn left by 15 degrees
        break;
    case 's':
        move_backwards(sensor_data, 10); // Move backward by 10 cm
        break;
    case 'd':
        turn_clockwise(sensor_data, 15); // Turn right by 15 degrees
        break;
    default:
        uart_sendStr("Invalid movement command.\n");
        break;
    }
}

void perform_180_ir_scan(cyBOT_Scan_t *scanData) {
    int angle;      // Variable declared outside of for loop
    int i;          // Variable declared outside of for loop

    int in_object = 0;
    int start_angle = -1;


    for (angle = 0; angle <= 180; angle += SCAN_ANGLE_STEP) {

        cyBOT_Scan(angle, scanData);  // Scan at the specified angle
        int ir_distance = adc_distance(); // Convert raw IR to distance

        char message[20];
        snprintf(message, sizeof(message), "%d,%d;", angle, ir_distance);
        uart_sendStr(message);

        // Detect objects based on IR distance
        if (ir_distance < IR_THRESHOLD && !in_object) {
            // Detected the start of an object
            in_object = 1;
            start_angle = angle;
        } else if (ir_distance >= IR_THRESHOLD && in_object) {
            // Detected the end of an object
            in_object = 0;
            detected_objects[object_count].start_angle = start_angle;
            detected_objects[object_count].end_angle = angle;
            detected_objects[object_count].distance = ir_distance;
            object_count++;
        }
    }
    uart_sendStr("\n");
    // Display start and end angles of each detected object
    uart_sendStr("\nDetected Objects:\n");
    uart_sendStr("Object\tStart Angle\tEnd Angle\n");
    uart_sendStr("--------------------------------\n");
    for (i = 0; i < object_count; i++) {
        char message[50];
        snprintf(message, sizeof(message), "Object %d\t%d\t\t%d\n", i + 1, detected_objects[i].start_angle, detected_objects[i].end_angle);
        uart_sendStr(message);
    }
    uart_sendStr("\n");
}

void display_smallest_object() {
    if (object_count == 0) {
        uart_sendStr("\nNo objects detected.\n");
        return;
    }

    int i;          // Variable declared outside of for loop
    int smallest_index = 0;
    float smallest_width = detected_objects[0].end_angle - detected_objects[0].start_angle;

    // Find the smallest object
    for (i = 1; i < object_count; i++) {
        float width = detected_objects[i].end_angle - detected_objects[i].start_angle;
        if (width < smallest_width) {
            smallest_width = width;
            smallest_index = i;
        }
    }

    // Display the smallest object
    uart_sendStr("\nSmallest object detected:\n");
    char message[100];
    snprintf(message, sizeof(message),
             "Start Angle: %d, End Angle: %d, Distance: %d cm\n",
             detected_objects[smallest_index].start_angle,
             detected_objects[smallest_index].end_angle,
             detected_objects[smallest_index].distance);
    uart_sendStr(message);

    // Set next mode to turn towards the smallest object
    current_mode = AUTONOMOUS_TURN;
}

void navigate_to_smallest_object(cyBOT_Scan_t *scanData, oi_t *sensor_data, int smallest_index) {
    if (smallest_index < 0 || smallest_index >= object_count) {
        uart_sendStr("Invalid smallest index. Cannot navigate.\n");
        return;
    }

    // Properly calculate the target angle
    int target_angle = (detected_objects[smallest_index].start_angle + detected_objects[smallest_index].end_angle) / 2;

    // Ensure the target angle is within a valid range (0 to 180 degrees)
    if (target_angle < 0 || target_angle > 180) {
        char error_message[80];
        snprintf(error_message, sizeof(error_message), "Invalid calculated angle: %d degrees. Aborting navigation.\n", target_angle);
        uart_sendStr(error_message);
        return;
    }

    float new_distance = detected_objects[smallest_index].distance - 7; // Adjusting for sensor placement
    char message[80];

    // Determine turning direction based on the target angle
    if (current_mode == AUTONOMOUS_TURN) {
        snprintf(message, sizeof(message), "Turning to Mid Angle: %d degrees\n", target_angle);
        uart_sendStr(message);
        lcd_printf("Turning to %d deg", target_angle);

        if (target_angle < 90) {
            turn_clockwise(sensor_data, 90 - target_angle);  // Turn clockwise if the target is on the right
        } else {
            turn_counter_clockwise(sensor_data, target_angle - 90);  // Turn counter-clockwise if the target is on the left
        }

        // Set next mode to move towards the target
        current_mode = AUTONOMOUS_MOVE;
    } else if (current_mode == AUTONOMOUS_MOVE) {
        snprintf(message, sizeof(message), "Moving towards object at %d degrees\n", target_angle);
        uart_sendStr(message);
        lcd_printf("Moving to %d cm", (int)new_distance);

        if (new_distance > TARGET_DISTANCE_CM) {
            int move_distance = (new_distance > 12) ? 15 : 5;
            move_forward_with_bump(sensor_data, move_distance);

            // After each movement, update current mode to scan again
            current_mode = AUTONOMOUS_SCAN;
        } else {
            uart_sendStr("Arrived within target distance of the smallest object.\n");
            lcd_printf("Arrived at %d cm", new_distance);
            current_mode = MANUAL;  // Stop after reaching the object
        }
    }
}

void handle_bump_sensor(oi_t *sensor_data) {
    oi_update(sensor_data);
    if (sensor_data->bumpLeft) {
        handle_bump(sensor_data, 'L');
    } else if (sensor_data->bumpRight) {
        handle_bump(sensor_data, 'R');
    }
}

void handle_uart_data(char command, oi_t *sensor_data, cyBOT_Scan_t *scanData) {
    switch (command) {
        case 'w': case 'a': case 's': case 'd':
            control_movement(sensor_data, command);
            uart_sendStr("Movement command executed.\n");
            break;
        case 't':
            if (current_mode == MANUAL) {
                current_mode = AUTONOMOUS_SCAN;
                uart_sendStr("Switched to Autonomous Mode. Press 'h' to start scanning.\n");
            } else {
                current_mode = MANUAL;
                uart_sendStr("Switched to Manual Mode.\n");
            }
            break;
        case 'h':
            if (current_mode == AUTONOMOUS_SCAN) {
                perform_180_ir_scan(scanData);
                display_smallest_object();
                uart_sendStr("Press 'h' again to move towards the smallest object.\n");
                current_mode = AUTONOMOUS_TURN;
            } else if (current_mode == AUTONOMOUS_TURN || current_mode == AUTONOMOUS_MOVE) {
                navigate_to_smallest_object(scanData, sensor_data, 0);  // Assume the smallest object for now
            }
            break;
        case 'q':
            uart_sendStr("Disconnecting from Client.\n");
            break;
        default:
            uart_sendStr("Invalid command received.\n");
            break;
    }
}

int main(void) {
    setup();

    cyBOT_Scan_t scanData;
    oi_t *sensor_data = oi_alloc();
    oi_init(sensor_data);

    uart_sendStr("Ready for commands.\n");

    while (1) {
        oi_update(sensor_data);  // Regularly update the sensor data

        handle_bump_sensor(sensor_data);  // Handle any bump sensor activation

        if (flag) {
            flag = 0;  // Reset flag after processing
            handle_uart_data(uart_data, sensor_data, &scanData);
        }
    }

    oi_free(sensor_data);
    return 0;
}
