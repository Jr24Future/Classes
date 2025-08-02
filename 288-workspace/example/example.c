/**
 * @file main.c
 *
 * @author Braedon Giblin <bgiblin@iastate.edu>
 *
 * The purpose of this file is to serve as a basic example for IMU usage.
 */

#include "imu.h"
#include "timer.h"
#include "lcd.h"
#include "open_interface.h"
#include <stdlib.h>
#include <math.h>
#include "i2c.h"

void imu_printChipInfo();
void imu_move_distance(float dis, oi_t *sensor_data, int speed);

int main() {
    timer_init();
    lcd_init();
    imu_init();
    imu_writeReg(IMU_OPR_MODE, NDOF); //0b1100
    imu_setDefaultUnits();

    imu_printChipInfo();


    mag_t* mag;
    float heading;
    while(1){
        mag = imu_getMag();
        heading = mag->heading;
        if (heading >= -45 && heading < 45) {
            lcd_printf("North");
        } else if (heading >= 45 && heading < 135) {
            lcd_printf("East");
        } else if (heading >= -135 && heading < -45) {
            lcd_printf("West");
        } else {
            lcd_printf("South");
        }
        free(mag);
        fflush(stdout);

        timer_waitMillis(1000);   // This is a simple 1 second delay call. 
    }

}


/*
 * This method shows a basic algorithm for calculating movement distance using
 * the IMU. OI represents an iRoomba open interface implementation. However,
 * the actual movement operations are trivial. We simply set a speed value to
 * the wheels (two drive wheels, set a speed to each wheel), and then stop the
 * wheels once we have driven our desired distance.
 */
void imu_move_distance(float dis, oi_t *sensor_data, int speed){
    if (!sensor_data) {
        return;
    }
    float v = 0, x = 0;
    int t = timer_getMicros();  // This is a library call to a running on board timer. Gets current time in micro-Seconds
    int tLast;
    acc_t* acc;
    int direction = (dis > 0) * 2 - 1;
    dis = abs(dis);


    oi_setWheels(speed * direction, speed * direction);
    tLast = t;
    while (x < dis) {
        acc = imu_getAcc();
        t = timer_getMicros();
        v = v + (acc->magnitude * (t/1000000.0f - tLast/1000000.0f));
        x = x + (v * (t/10000.0f - tLast/10000.0f));
        free(acc);
        tLast = t;
    }
    oi_setWheels(0, 0);
}

/*
 * This function uses a simple LCD interface. The library functions should
 * be readily apparant. 
 */
void imu_printChipInfo() {
    lcd_init();
    imu_info_t* chipInfo = imu_getChipInfo();

    lcd_printf("Chip ID: %d\n"
            "ACC ID: %d\n"
            "MAG ID: %d\n"
            "GYRO ID: %d\n",
            chipInfo->chipID,
            chipInfo->accID,
            chipInfo->magID,
            chipInfo->gyrID);

    timer_waitMillis(5000);

    lcd_printf("SW Rev: %d\n"
            "BL Rev: %d\n",
            chipInfo->swRevID,
            chipInfo->blRevID);


    timer_waitMillis(5000);
    lcd_clear();
}


/*
int main (){
timer_init();
uart_init();
lcd_init();

bno_t *bno = bno_alloc();
bno_initCalib(&bno_calibration);

oi_t *oi = oi_alloc();
oi_init(oi);

bno_update(bno);
float target = bno ->euler.headuing /16.;

while (1){
oi_update(oi);
if (oi->wheelDropLeft || oi ->wheelDropRight){
oi_setWheels(0,0);}
else{
bno_update(bno);
float difference = remainderf(target-(bno->euler.heading / 16.) + 360), 360);
difference = difference > 180 ? difference -360 : difference;
difference *= 14;
int16_t power = (int16_t)(
difference < -100 ? -100 : (difference > 100 ? 100 : difference));
oi_setWheels(-power, power);
}
timer_waitMillis(10);
}
}
*/







/*

#define HEADING_GAIN 5 // Proportional gain for heading correction
#define MAX_POSITION_DELTA 50 // Maximum allowed change in cm
#define DEG_TO_RAD (M_PI / 180.0)



// Global position tracking
int current_x = 0;
int current_y = 0;
float current_heading = 0;
//return to exit
int start_x = 0;
int start_y = 0;

float current_orientation = 0;

// Function prototypes
void align_to_heading(oi_t *oi, bno_t *bno, float target_heading);
void move_distance(oi_t *oi, int distance_cm, bno_t *bno);
int get_user_input(const char *prompt, int max_value, int is_multiple_of_60);
void navigate_to_target(oi_t *oi, bno_t *bno, int target_x, int target_y);
void enter_border(oi_t *oi, bno_t *bno);
void exit_border(oi_t *oi, bno_t *bno);


int main() {
    timer_init();
    uart_init(115200);
    lcd_init();

    // Predefined calibration data
    bno_calib_t bno_calibration = {0xFFDF,0xFFDC,0xFFCA,0x0347,0x0049,0xF856,0x0001,0xFFFE,0x0000,0x03E8,0x0295};

    // Allocate the BNO055 structure
    bno_t *bno = bno_alloc();
    bno_initCalib(&bno_calibration);
    bno_calibrateInteractive();

    // Robot initialization
    oi_t *oi = oi_alloc();
    oi_init(oi);

    // Load the song
    load_songs();

    // Ask user for starting X position
    start_x = get_user_input("Enter starting X position (0, 60, 120, 180, 240): ", 240, 1);
    start_y = 0; // Starting position is always Y = 0
    current_x = start_x;
    current_y = start_y;

    // Ask user for the number of targets
    int num_targets = get_user_input("Enter the number of targets (1-5): ", 5, 0);

    int *target_x = malloc(num_targets * sizeof(int));
    int *target_y = malloc(num_targets * sizeof(int));


    if (!target_x || !target_y) {
        uart_sendStr("Memory allocation failed!\r\n");
        if (target_x) free(target_x); // Free partially allocated memory
        if (target_y) free(target_y); // Free partially allocated memory
        return -1; // Exit with an error
    }

    char debug_msg2[50];
    sprintf(debug_msg2, "Starting Position: (%d, %d)\r\n", current_x, current_y);
    uart_sendStr(debug_msg2);

    int i;
    for (i = 0; i < num_targets; i++) {
        char prompt_x[50];
        char prompt_y[50];
        sprintf(prompt_x, "Enter X coordinate for target %d (0, 60, 120, 180, 240): \r\n", i + 1);
        sprintf(prompt_y, "Enter Y coordinate for target %d (0, 60, 120, ..., 420): ", i + 1);
        target_x[i] = get_user_input(prompt_x, 240, 1);
        target_y[i] = get_user_input(prompt_y, 420, 1);
    }

    // Enter the grid by crossing two white tapes
   // enter_border(oi, bno);

    // Navigate to each target
    for (i = 0; i < num_targets; i++) {
        uart_sendStr("Navigating to target...\r\n");
        char buffer[50];
        sprintf(buffer, "Target %d: (%d, %d)\r\n", i + 1, target_x[i], target_y[i]);
        uart_sendStr(buffer);

        // Navigate to the target
        navigate_to_target(oi, bno, target_x[i], target_y[i]);

        // If the target is off the main axis, move to (X, 0) after reaching (X, Y)
        if (target_y[i] != 0) {
            uart_sendStr("Moving to align with main axis...\r\n");
            navigate_to_target(oi, bno, target_x[i], 0);
        }

        // Stop for 2 seconds
        lcd_printf("Target %d Reached", i + 1);
        timer_waitMillis(2000);
    }


    // Return to starting position
    uart_sendStr("Returning to starting position...\r\n");
    navigate_to_target(oi, bno, start_x, start_y);

    // Exit the border
    uart_sendStr("Exiting the border...\r\n");
    //exit_border(oi, bno);

    // Play music
    uart_sendStr("Playing music...\r\n");
    //oi_play_song(STAR_SPANGLED);

    // Cleanup
    free(target_x);
    free(target_y);
    oi_free(oi);
    uart_sendStr("All targets reached.\r\n");
    lcd_printf("All Targets Done");



    return 0;
}


void enter_border(oi_t *oi, bno_t *bno) {
    uart_sendStr("Starting outside the border. Aligning to heading 135ï¿½...\r\n");
    align_to_heading(oi, bno, 80); // Align to 135ï¿½ for initial entry direction

    int white_tape_count = 0; // Counter for white tape crossings
    int floor_state = 0; // 0 = normal floor, 1 = white tape

    uart_sendStr("Entering border detection mode...\r\n");

    while (white_tape_count < 2) {
        oi_update(oi);

        // Move forward while checking cliff sensors
        oi_setWheels(100, 100);

        // Read cliff sensor values
        int cliff_right = oi->cliffFrontRightSignal;

        if (cliff_right > 2700 && floor_state == 0) {
            // Detected white tape
            floor_state = 1; // Update floor state to white tape
            white_tape_count++;
            uart_sendStr("White tape detected.\r\n");
            timer_waitMillis(300);

            // Stop briefly on white tape
            oi_setWheels(0, 0);


            // Resume forward movement
            if (white_tape_count < 2) {
                uart_sendStr("Continuing to next border...\r\n");
                oi_setWheels(200, 200);
            }
        } else if (cliff_right < 2000 && floor_state == 1) {
            // Detected transition to normal floor
            floor_state = 0; // Update floor state to normal floor
            uart_sendStr("Normal floor detected.\r\n");
        }

        // Optional safety: Ensure it doesn't move indefinitely
        if (white_tape_count == 2 && cliff_right < 2000) {
            // Confirmed second white tape followed by normal floor
            oi_setWheels(0, 0); // Stop the robot
            uart_sendStr("Inside the border. Stopping movement.\r\n");
            break;
        }
    }

    // Perform a 180ï¿½ scan (optional)
    uart_sendStr("Performing 180ï¿½ scan...\r\n");
    //scan_180();
}



  Moves the robot out of the border by reversing the entry process.


void exit_border(oi_t *oi, bno_t *bno) {
    uart_sendStr("Aligning to exit the border. Aligning to heading 315ï¿½...\r\n");
    align_to_heading(oi, bno, 260); // Align to 315ï¿½ for exit direction

    int white_tape_count = 0; // Counter for white tape crossings
    int floor_state = 0; // 0 = normal floor, 1 = white tape

    uart_sendStr("Exiting border detection mode...\r\n");

    while (white_tape_count < 2) {
        oi_update(oi);

        // Move forward while checking cliff sensors
        oi_setWheels(200, 200);

        // Read cliff sensor values
        int cliff_right = oi->cliffRightSignal;

        if (cliff_right > 2700 && floor_state == 0) {
            // Detected white tape
            floor_state = 1; // Update floor state to white tape
            white_tape_count++;
            uart_sendStr("White tape detected.\r\n");

            // Stop briefly on white tape
            oi_setWheels(0, 0);
            timer_waitMillis(500);

            // Resume forward movement
            if (white_tape_count < 2) {
                uart_sendStr("Continuing to next border...\r\n");
                oi_setWheels(200, 200);
            }
        } else if (cliff_right < 2000 && floor_state == 1) {
            // Detected transition to normal floor
            floor_state = 0; // Update floor state to normal floor
            uart_sendStr("Normal floor detected.\r\n");
        }

        // Optional safety: Ensure it doesn't move indefinitely
        if (white_tape_count == 2 && cliff_right < 2000) {
            // Confirmed second white tape followed by normal floor
            oi_setWheels(0, 0); // Stop the robot
            uart_sendStr("Outside the border. Exiting zone complete.\r\n");
            break;
        }
    }
}





*
 * Align the robot to the specified heading using the BNO055.


void align_to_heading(oi_t *oi, bno_t *bno, float target_heading) {
    bno_update(bno);
    float current_heading = bno->euler.heading / 16.0;
    float heading_diff = target_heading - current_heading;

    // Normalize heading difference to [-180, 180]
    if (heading_diff > 180) heading_diff -= 360;
    if (heading_diff < -180) heading_diff += 360;

    const int16_t MIN_TURN_SPEED = 20; // Minimum speed to avoid stalling

    // While the robot's heading is outside the tolerance
    while (fabs(heading_diff) > 0.5) { // Adjust as needed for tolerance
        bno_update(bno);
        current_heading = bno->euler.heading / 16.0;

        // Recalculate the difference
        heading_diff = target_heading - current_heading;
        if (heading_diff > 180) heading_diff -= 360;
        if (heading_diff < -180) heading_diff += 360;

        // Calculate correction speed
        int16_t turn_speed = (int16_t)(heading_diff * HEADING_GAIN);

        // Ensure turn speed is above the minimum threshold
        if (turn_speed > 0 && turn_speed < MIN_TURN_SPEED) {
            turn_speed = MIN_TURN_SPEED;
        } else if (turn_speed < 0 && turn_speed > -MIN_TURN_SPEED) {
            turn_speed = -MIN_TURN_SPEED;
        }

        // Clamp turn speed to a maximum value
        if (turn_speed > 100) turn_speed = 100;
        if (turn_speed < -100) turn_speed = -100;

        // Turn the robot
        oi_setWheels(-turn_speed, turn_speed);

        // Debugging
        char debug_msg[100];
        sprintf(debug_msg, "Aligning: Heading: %.2f|Target: %.2f|Diff: %.2f\r",
                current_heading, target_heading, heading_diff);
        uart_sendStr(debug_msg);

        timer_waitMillis(50); // Delay for stability
    }

    // Stop the robot once aligned
    oi_setWheels(0, 0);
}


*
 * Move the robot a specified distance while maintaining its heading.


void move_distance(oi_t *oi, int distance_cm, bno_t *bno) {
    int total_distance = 0; // Reset total distance
    int initial_x = current_x; // Save initial X position
    int initial_y = current_y; // Save initial Y position


    // Reset oi->distance
    oi_update(oi);
    oi->distance = 0;

    // Get the current heading
    bno_update(bno);
    float current_heading  = bno->euler.heading / 16.0;
    float target_heading = current_heading;

    oi_setWheels(200, 200); // Start moving forward

    while (total_distance < distance_cm * 10) { // Convert cm to mm
        oi_update(oi);

        // Check for bumpers
        if (oi->bumpLeft || oi->bumpRight) {
            oi_setWheels(0, 0); // Stop movement
            char bump_direction = oi->bumpLeft ? 'L' : 'R';



            // Back up 15 cm
            move_backwards(oi, 15);
            // Update position after backing up based on the current heading
            if ((current_heading >= 350 || current_heading <= 10)) { // Facing North
                current_y -= 15; // Move down in the Y-axis
            } else if (current_heading >= 170 && current_heading <= 190) { // Facing South
                current_y += 15; // Move up in the Y-axis
            } else if (current_heading >= 80 && current_heading <= 100) { // Facing East
                current_x -= 15; // Move left in the X-axis
            } else if (current_heading >= 260 && current_heading <= 280) { // Facing West
                current_x += 15; // Move right in the X-axis
            }
            // Debugging after backup
            char backup_debug_msg[100];
            sprintf(backup_debug_msg, "Position after backup: (%d, %d), Heading: %.2f\n",
                    current_x, current_y, current_heading);
            uart_sendStr(backup_debug_msg);


            // Determine the new heading
            bno_update(bno);
            float current_heading = bno->euler.heading / 16.0; // Get current heading
            float target_heading;
            if (bump_direction == 'R') {
                target_heading = fmod((current_heading - 90 + 360), 360); // Turn left
            } else {
                target_heading = fmod((current_heading + 90), 360); // Turn right
            }

            // Align to the target heading
            align_to_heading(oi, bno, target_heading);
            oi->distance = 0; // Reset distance tracking


            // Move forward 15 cm
            move_forward(oi, 30);
            // Update position after moving forward
            if ((target_heading >= 350 || target_heading <= 10)) { // Facing North
                current_y += 30; // Move up in the Y-axis
            } else if (target_heading >= 170 && target_heading <= 190) { // Facing South
                current_y -= 30; // Move down in the Y-axis
            } else if (target_heading >= 80 && target_heading <= 100) { // Facing East
                current_x += 30; // Move right in the X-axis
            } else if (target_heading >= 260 && target_heading <= 280) { // Facing West
                current_x -= 30; // Move left in the X-axis
            }




            // Turn back to the original heading
            align_to_heading(oi, bno, current_heading);

            // Continue toward the target
            oi_update(oi);
            oi->distance = 0;
            continue;
        }

        // Check for wheel drops or obstacles
        if (oi->wheelDropLeft || oi->wheelDropRight) {
            oi_setWheels(0, 0); // Stop
            uart_sendStr("Wheel drop detected. Halting movement.\r\n");
            break;
        }

        // Sanity check for oi->distance
        if (oi->distance < -500 || oi->distance > 500) {
            char debug_msg[100];
            sprintf(debug_msg, "Unexpected distance: %d | Skipping this update.\n", oi->distance);
            uart_sendStr(debug_msg);
            oi->distance = 0; // Reset erratic value
            continue;
        }

        // Update total distance traveled
        total_distance += oi->distance;


        // Update the current heading
        bno_update(bno);
        float current_heading = bno->euler.heading / 16.0;
        float heading_diff = target_heading - current_heading;

        // Normalize heading difference to [-180, 180]
        if (heading_diff > 180) heading_diff -= 360;
        if (heading_diff < -180) heading_diff += 360;

        // Calculate correction power
        int16_t correction = (int16_t)(heading_diff * HEADING_GAIN);
        int16_t left_power = 200 - correction;  // Adjust base speed
        int16_t right_power = 200 + correction;

        // Clamp wheel powers to the valid range [-255, 255]
        left_power = left_power > 255 ? 255 : (left_power < -255 ? -255 : left_power);
        right_power = right_power > 255 ? 255 : (right_power < -255 ? -255 : right_power);

        // Set wheel speeds
        oi_setWheels(left_power, right_power);

        // Update live position based on heading
        if ((target_heading >= 330 || target_heading <= 10)) { // North (340ï¿½)
            int new_y = initial_y + (total_distance / 10);
            if (abs(new_y - current_y) <= MAX_POSITION_DELTA) {
                current_y = new_y;
            } else {
                uart_sendStr("Unexpected Y position change. Skipping update.\r");
            }
        } else if (target_heading >= 150 && target_heading <= 170) { // South (160ï¿½)
            int new_y = initial_y - (total_distance / 10);
            if (abs(new_y - current_y) <= MAX_POSITION_DELTA) {
                current_y = new_y;
            } else {
                uart_sendStr("Unexpected Y position change. Skipping update.\r");
            }
        } else if (target_heading >= 60 && target_heading <= 80) { // East (70ï¿½)
            int new_x = initial_x + (total_distance / 10);
            if (abs(new_x - current_x) <= MAX_POSITION_DELTA) {
                current_x = new_x;
            } else {
                uart_sendStr("Unexpected X position change. Skipping update.\r");
            }
        } else if (target_heading >= 240 && target_heading <= 260) { // West (250ï¿½)
            int new_x = initial_x - (total_distance / 10);
            if (abs(new_x - current_x) <= MAX_POSITION_DELTA) {
                current_x = new_x;
            } else {
                uart_sendStr("Unexpected X position change. Skipping update.\r");
            }
        }

        if (current_x < -1000 || current_x > 1000 || current_y < -1000 || current_y > 1000) {
            uart_sendStr("Position overflow detected.\r\n");
            current_x = initial_x;
            current_y = initial_y;
        }

        // Display live position and heading on LCD
        lcd_printf("Loc: (%d, %d)\nHeading: %.2fï¿½", current_x, current_y, target_heading);

        // Debugging:
        char debug_msg[100];
        sprintf(debug_msg, "Live Pos: (%d, %d), Heading: %.2f, Distance: %d\n", current_x, current_y, target_heading, oi->distance);
        uart_sendStr(debug_msg);

        timer_waitMillis(50); // Delay for stability
    }

    oi_setWheels(0, 0); // Stop at the end
}


*
 * Navigate the robot to a target position in the grid.


void navigate_to_target(oi_t *oi, bno_t *bno, int target_x, int target_y) {
    oi->distance = 0;

    // Move to the X-coordinate first
    if (current_x != target_x) {
        int target_heading = (target_x > current_x) ? 70 : 250; // East or West
        align_to_heading(oi, bno, target_heading);
        move_distance(oi, abs(target_x - current_x), bno);
    }

    // Move to the Y-coordinate
    if (current_y != target_y) {
        int target_heading = (target_y > current_y) ? 340 : 160; // North or South
        align_to_heading(oi, bno, target_heading);
        move_distance(oi, abs(target_y - current_y), bno);
    }

    // Final position update
    current_x = target_x;
    current_y = target_y;

    // Debugging: Confirm arrival at the target
    char buffer[100];
    sprintf(buffer, "Arrived at (%d, %d)\r\n", current_x, current_y);
    uart_sendStr(buffer);
}









*/








//#include "movement.h"
//#include "timer.h"
//#include "lcd.h"
//#include "uart.h"
//#include <math.h>
//
//extern float reference_heading;
//extern int current_x, current_y;
//
//#define HEADING_GAIN 5
//#define DEG_TO_RAD (M_PI / 180.0)
//extern float current_orientation;
//
//void move_forward(oi_t *sensor, int centimeters) {
//    double total_distance = 0;
//    int goal = centimeters * 10; // Convert cm to mm
//
//    oi_setWheels(200, 200); // Move forward
//    while (total_distance < goal) {
//        oi_update(sensor);
//        total_distance += sensor->distance; // Update total distance
//    }
//    oi_setWheels(0, 0); // Stop
//}
//// Backup movement
//void move_backward(oi_t *sensor, int centimeters) {
//    int total_distance = 0;
//    int goal = centimeters * 10; // Convert cm to mm
//
//    oi_setWheels(-200, -200); // Move backwards
//    while (total_distance < goal) {
//        oi_update(sensor);
//        total_distance -= sensor->distance; // Negative distance
//        sensor->distance = 0; // Reset
//    }
//    oi_setWheels(0, 0); // Stop
//}
//
//// Turn clockwise
//void turn_clockwise(oi_t *sensor, int degrees) {
//    int sum = 0;
//    oi_setWheels(-100, 100); // Turn clockwise
//
//    while (sum > -degrees) {
//        oi_update(sensor);
//        sum += sensor->angle;
//        current_orientation = fmod(current_orientation + sensor->angle + 360.0, 360.0); // Update orientation
//    }
//
//    oi_setWheels(0, 0); // Stop
//}
//
//// Turn counter-clockwise
//void turn_counter_clockwise(oi_t *sensor, int degrees) {
//    int sum = 0;
//    oi_setWheels(100, -100); // Turn counter-clockwise
//
//    while (sum < degrees) {
//        oi_update(sensor);
//        sum += sensor->angle;
//        current_orientation = fmod(current_orientation + sensor->angle + 360.0, 360.0); // Update orientation
//    }
//
//    oi_setWheels(0, 0); // Stop
//}
//
//// Handle bump
//void handle_bump(oi_t *sensor, char bump_direction) {
//    // Backup 15 cm
//    move_backward(sensor, 15);
//
//    // Adjust heading based on the bumper triggered
//    if (bump_direction == 'L') {
//        turn_clockwise(sensor, 20); // Move away from the object to the right
//        current_orientation = fmod(current_orientation + 20.0, 360.0); // Update orientation
//    } else if (bump_direction == 'R') {
//        turn_counter_clockwise(sensor, 20); // Move away from the object to the left
//        current_orientation = fmod(current_orientation - 20.0 + 360.0, 360.0); // Update orientation
//    }
//
//    // Update position after backup
//    current_x -= (int)(15 * cos(current_orientation * DEG_TO_RAD));
//    current_y -= (int)(15 * sin(current_orientation * DEG_TO_RAD));
//
//    // Debugging: Log updated position
//    char position_msg[100];
//    sprintf(position_msg, "Bump Updated Pos: X = %d, Y = %d, Orientation = %.2f\n",
//            current_x, current_y, current_orientation);
//    uart_sendStr(position_msg);
//
//    // Move forward 30 cm to avoid obstacle
//    move_forward(sensor, 30);
//
//    // Adjust Y position to 0 if needed
//    if (current_y != 0) {
//        int target_orientation = (current_y > 0) ? 225 : 135; // Choose direction to return to Y = 0
//        int rotation_angle = target_orientation - (int)current_orientation;
//        if (rotation_angle > 180) rotation_angle -= 360;
//        if (rotation_angle < -180) rotation_angle += 360;
//
//        if (rotation_angle > 0) {
//            turn_clockwise(sensor, rotation_angle);
//        } else {
//            turn_counter_clockwise(sensor, -rotation_angle);
//        }
//        current_orientation = target_orientation;
//
//        while (abs(current_y) > 0) {
//            oi_update(sensor);
//            int distance_to_move = abs(current_y) < 30 ? abs(current_y) : 30; // Break into smaller moves
//            move_forward(sensor, distance_to_move);
//        }
//    }
//}
//
//// Forward movement with bump handling
//void move_forward_with_bump(oi_t *sensor, int centimeters) {
//    double total_distance = 0;
//    int total_goal = centimeters * 10; // Convert cm to mm
//    oi_setWheels(200, 200); // Move forward
//
//    while (total_distance < total_goal) {
//        oi_update(sensor);
//
//        // Check for bumpers
//        if (sensor->bumpLeft || sensor->bumpRight) {
//            oi_setWheels(0, 0); // Stop
//            handle_bump(sensor, sensor->bumpLeft ? 'L' : 'R');
//            total_distance = 0; // Reset distance after handling the bump
//            continue;
//        }
//
//        // Update total distance traveled
//        total_distance += sensor->distance;
//
//        // Update position based on heading
//        current_x += (int)((sensor->distance / 10.0) * cos(current_orientation * DEG_TO_RAD));
//        current_y += (int)((sensor->distance / 10.0) * sin(current_orientation * DEG_TO_RAD));
//
//        char position_msg[100];
//        sprintf(position_msg, "Live Pos: (%d, %d)\n", current_x, current_y);
//        uart_sendStr(position_msg);
//
//        sensor->distance = 0; // Prevent accumulation of distance
//    }
//
//    oi_setWheels(0, 0);
//}
//
float move_forward(oi_t *sensor, float input_centimeters) {
    float mm_traveled = 0;
    float angle_deviated = 0;
    float target_mm = input_centimeters * 10; // Convert to mm for internal calculation

    while (mm_traveled < target_mm) { // Loop until target distance is reached
        oi_update(sensor);
        obstacle_check(sensor); // Handle obstacles dynamically

        float distance_this_step = sensor->distance; // Get the distance traveled in this step
        mm_traveled += distance_this_step;          // Increment total distance traveled
        angle_deviated += sensor->angle;            // Track deviation

        update_live_position(distance_this_step);   // Update live position

        // Dynamic wheel adjustments to maintain straight line
        if (angle_deviated > degree_accuracy) { // Correct for veering left
            forward_RWP -= 1; // Reduce right wheel power
            angle_deviated = 0; // Reset angle deviation
        } else if (angle_deviated < -degree_accuracy) { // Correct for veering right
            forward_RWP += 1; // Increase right wheel power
            angle_deviated = 0; // Reset angle deviation
        }

        oi_setWheels(forward_RWP, forward_LWP); // Set wheel speeds
        timer_waitMillis(50); // Small delay for stability
    }

    oi_setWheels(0, 0); // Stop the robot

    // Calculate the remaining distance (if interrupted)
    float remaining_distance_cm = (target_mm - mm_traveled) / 10;

    // Debugging: Log completion or interruption
    char debug_msg[100];
    sprintf(debug_msg, "Move Completed: Target: %.2f cm, Traveled: %.2f cm, Remaining: %.2f cm\n",
            input_centimeters, mm_traveled / 10, remaining_distance_cm);
    uart_sendStr(debug_msg);

    return remaining_distance_cm; // Return remaining distance (0 if fully traveled)
}