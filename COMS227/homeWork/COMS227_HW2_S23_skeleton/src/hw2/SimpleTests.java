package hw2;

import static api.BallType.RED;
import static api.BallType.WHITE;
import static api.BallType.YELLOW;
import static api.PlayerPosition.PLAYER_A;
import static api.PlayerPosition.PLAYER_B;

/**
 * Some simple tests that may help you to get started implementing the
 * ThreeCushion class. Warning: these tests do not cover all of the
 * specifications. Perform your own testing in addition to using the
 * specchecker.
 */
public class SimpleTests {
	public static void main(String args[]) {
		ThreeCushion game = new ThreeCushion(PLAYER_B, 40);
	    game.lagWinnerChooses(true, YELLOW);
	    game.cueStickStrike(YELLOW);
	    game.cueBallStrike(RED);
	    game.cueBallImpactCushion();
	    game.cueBallImpactCushion();
	    game.cueBallImpactCushion();
	    game.cueBallStrike(WHITE);
	    game.endShot();
	    game.cueStickStrike(YELLOW);
	    game.foul();
	    game.cueBallImpactCushion();
	    game.cueBallImpactCushion();
	    game.cueBallImpactCushion();
	    game.cueBallStrike(WHITE);
	    game.cueBallStrike(RED);
	    game.endShot();
	    
	    System.out.println();
		//System.out.println("Test 2:");
		//System.out.println("The shot player is " + game.getInningPlayer() + ", expected PLAYER_A");
		//System.out.println("The cue ball is " + game.getCueBall() + ", expected WHITE");
		//System.out.println("This is the break shot " + game.isBreakShot() + ", expected true");
		System.out.println("Stats:    " + game);
		System.out.println("Expected: Player A: 0, Player B*: 1, //Inning: 2 not started");
	    //System.out.println("This is the break shot " + game.isBankShot() + ", expected false");
		// The lag winner chooses to take the break shot and take the white cue ball.
		/*
		 * game.lagWinnerChooses(false, WHITE);
		 * 
		 * System.out.println(); System.out.println("Test 2:");
		 * System.out.println("The shot player is " + game.getInningPlayer() +
		 * ", expected PLAYER_B"); System.out.println("The cue ball is " +
		 * game.getCueBall() + ", expected YELLOW");
		 * System.out.println("This is the break shot " + game.isBreakShot() +
		 * ", expected true"); System.out.println("Stats:    " + game); System.out.
		 * println("Expected: Player A*: 0, Player B: 0, Inning: 1 not started");
		 * 
		 * // Player A takes a shot. game.cueStickStrike(WHITE);
		 */

		/*
		 * System.out.println(); System.out.println("Test 3:");
		 * System.out.println("The shot has started is " + game.isShotStarted() +
		 * ", expected true"); System.out.println("Stats:    " + game);
		 * System.out.println("Expected: Player A*: 0, Player B: 0, Inning: 1 started");
		 * 
		 * // All balls have stopped motion. game.endShot();
		 * 
		 * System.out.println(); System.out.println("Test 4:");
		 * System.out.println("The shot has started is " + game.isShotStarted() +
		 * ", expected false"); System.out.println("This is the break shot " +
		 * game.isBreakShot() + ", expected false");
		 * System.out.println("The cue ball is " + game.getCueBall() +
		 * ", expected YELLOW"); System.out.println("Stats:    " + game); System.out.
		 * println("Expected: Player A: 0, Player B*: 0, Inning: 2 not started");
		 * 
		 * // A foul has been committed by Player B. game.foul();
		 * 
		 * System.out.println(); System.out.println("Test 5:");
		 * System.out.println("The cue ball is " + game.getCueBall() +
		 * ", expected WHITE"); System.out.println("Stats:    " + game); System.out.
		 * println("Expected: Player A*: 0, Player B: 0, Inning: 3 not started");
		 * 
		 * // Player A strikes the wrong cue ball committing a foul.
		 * game.cueStickStrike(YELLOW);
		 * 
		 * System.out.println(); System.out.println("Test 6:");
		 * System.out.println("Stats:    " + game); System.out.
		 * println("Expected: Player A: 0, Player B*: 0, Inning: 4 not started");
		 * 
		 * // All balls stop motion. game.endShot(); // Player B strikes the correct cue
		 * ball starting the next shot. game.cueStickStrike(YELLOW);
		 * 
		 * System.out.println(); System.out.println("Test 7:");
		 * System.out.println("Stats:    " + game);
		 * System.out.println("Expected: Player A: 0, Player B*: 0, Inning: 4 started");
		 * 
		 * // The shot is valid. game.cueBallStrike(RED); game.cueBallImpactCushion();
		 * game.cueBallImpactCushion(); game.cueBallImpactCushion();
		 * game.cueBallStrike(WHITE); game.endShot();
		 * 
		 * System.out.println(); System.out.println("Test 8:");
		 * System.out.println("Stats:    " + game);
		 * System.out.println("Expected: Player A: 0, Player B*: 1, Inning: 4 started");
		 * 
		 * // Player B makes a bank shot game.cueStickStrike(YELLOW);
		 * game.cueBallImpactCushion(); game.cueBallImpactCushion();
		 * game.cueBallImpactCushion(); game.cueBallStrike(RED);
		 * game.cueBallImpactCushion(); game.cueBallStrike(WHITE); game.endShot();
		 * 
		 * System.out.println(); System.out.println("Test 9:");
		 * System.out.println("This is a bank shot " + game.isBankShot() +
		 * ", expected true"); System.out.println("Stats:    " + game);
		 * System.out.println("Expected: Player A: 0, Player B*: 2, Inning: 4 started");
		 * 
		 * // Player B scores a third point and wins the game
		 * game.cueStickStrike(YELLOW); game.cueBallImpactCushion();
		 * game.cueBallStrike(WHITE); game.cueBallImpactCushion();
		 * game.cueBallImpactCushion(); game.cueBallStrike(RED); game.endShot();
		 * 
		 * System.out.println(); System.out.println("Test 10:");
		 * System.out.println("This is a bank shot " + game.isBankShot() +
		 * ", expected false"); System.out.println("Stats:    " + game); System.out.
		 * println("Expected: Player A: 0, Player B*: 3, Inning: 4 started, game result final" );
		 */
	}
}
