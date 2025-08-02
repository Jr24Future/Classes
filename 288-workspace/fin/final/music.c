#include "open_interface.h"
#include "music.h"

/// Load three songs onto the iRobot
/**
 * Before calling this method, be sure to initialize the open interface by calling:
 *
 *   oi_t* sensor = oi_alloc();
 *   oi_init(sensor);
 *
 */
void load_songs() {

    // Notes: oi_load_song takes four arguments
    // arg1 - an integer from 0 to 16 identifying this song
    // arg2 - an integer that indicates the number of notes in the song (if greater than 16, it will consume the next song index's storage space)
    // arg3 - an array of integers representing the midi note values (example: 60 = C, 61 = C sharp)
    // arg4 - an array of integers representing the duration of each note (in 1/64ths of a second)
    //
    // For a note sheet, see page 12 of the iRobot Creat Open Interface datasheet

    // Star Spangled Banner
    unsigned char starSpangledNumNotes = 25;
    unsigned char starSpangledNotes[25] = {

        67, 64, 60, 64, 67, 72,
        76, 74, 72, 64, 66, 67,
        67, 67, 76, 74, 72, 71,
        69, 71, 72, 72, 67, 64, 60,
    };

    unsigned char starSpangledDurations[25] = {

        30, 28, 24, 24, 24, 24,
        24, 24, 24, 24, 28, 30,
        30, 30, 30, 24, 21, 24,
        21, 21, 21, 21, 21, 21, 21,

    };

    // Load the song into the system (Part 1 + Part 2 only)
    oi_loadSong(STAR_SPANGLED, starSpangledNumNotes, starSpangledNotes, starSpangledDurations);


}
