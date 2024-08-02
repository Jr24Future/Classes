package hw2;

import api.PlayerPosition;
import api.BallType;
import static api.PlayerPosition.*;
import static api.BallType.*;

/**
 * Class that models the game of three-cushion billiards.
 * 
 * @author Erroll Barker
 */
public class ThreeCushion {
	private int inning = 1;
	private int scoreA = 0;
	private int scoreB = 0;
	private BallType cueBall;
	private BallType curPlayersBall;
	private BallType otherPlayerBall;
	@SuppressWarnings("unused")
	private BallType ballStrick;
	@SuppressWarnings("unused")
	private BallType ball;
	private PlayerPosition lagWinner;
	private int pointsToWin;
	@SuppressWarnings("unused")
	private boolean selfBreak;
	private boolean Break;
	private boolean InningStarted;
	private PlayerPosition curPlayer;
	private PlayerPosition otherPlayer;
	private boolean BankShot;
	private boolean shotEnds;
	private boolean gameOver;
	private boolean lagIfCalled;
	private boolean hasScore_A;
	private boolean hasScore_B;
	private boolean valid;
	private boolean foul;
	private boolean dontChange = true;
	private boolean startStart;
	private int impactCushion;
	private int redHit;
	private boolean gameStart;
	private int yellowHit;
	private boolean justcuestrike;
	private int whiteHit;
	private int lastBallHit;
	private int bankShot;
	private int objectBall;
	private int endStop;
	
	

	// TODO: EVERTHING ELSE GOES HERE
	// Note that this code will not compile until you have put in stubs for all
	// the required methods.

	// The method below is provided for you and you should not modify it.
	// The compile errors will go away after you have written stubs for the
	// rest of the API methods.

	public ThreeCushion(PlayerPosition lagWinner, int pointsToWin) {
		this.lagWinner = lagWinner;
		this.pointsToWin = pointsToWin;
		redHit = 0;
		yellowHit = 0;
		whiteHit = 0;
		endStop = 0;
		gameStart = false;
	}

	/**
	 * Returns a one-line string representation of the current game state. The
	 * format is:
	 * <p>
	 * <tt>Player A*: X Player B: Y, Inning: Z</tt>
	 * <p>
	 * The asterisks next to the player's name indicates which player is at the
	 * table this inning. The number after the player's name is their score. Z is
	 * the inning number. Other messages will appear at the end of the string.
	 * 
	 * @return one-line string representation of the game state
	 */
	public String toString() {
		String fmt = "Player A%s: %d, Player B%s: %d, Inning: %d %s%s";
		String playerATurn = "";
		String playerBTurn = "";
		String inningStatus = "";
		String gameStatus = "";
		if (getInningPlayer() == PLAYER_A) {
			playerATurn = "*";
		} else if (getInningPlayer() == PLAYER_B) {
			playerBTurn = "*";
		}
		if (isInningStarted()) {
			inningStatus = "started";
		} else {
			inningStatus = "not started";
		}
		if (isGameOver()) {
			gameStatus = ", game result final";
		}
		return String.format(fmt, playerATurn, getPlayerAScore(), playerBTurn, getPlayerBScore(), getInning(),
				inningStatus, gameStatus);
	}

	public PlayerPosition getInningPlayer() {
//		if(roundChange == false)
//			return curPlayer;
//		else {
//			if (curPlayer==PLAYER_A) {
//				return PLAYER_B;
//			}
//			else {
				return curPlayer;
//			}
//		}
	}

	public boolean isInningStarted() {
		
		return InningStarted;
	}

	public boolean isGameOver() {
		
		return gameOver;
	}

	public int getPlayerAScore() {
		
		return scoreA; 
	}


	public int getPlayerBScore() {
		
		return scoreB;
	}

	public void cueBallImpactCushion() {
		if(startStart && !gameOver) {
			impactCushion++;
			bankShot++;
			justcuestrike = false;
			lastBallHit = 0;
			valid = false;
		}
		
	}

	public void cueBallStrike(BallType ballStrick) {
		if(startStart && !gameOver) {
			justcuestrike = false;
			if(curPlayersBall != ballStrick) {
				this.ballStrick = ballStrick;
				if (ballStrick == RED) {
					if(redHit == 0) {
						redHit++;
						if(bankShot >= 3)
						objectBall++;
						if(bankShot >= 3 && objectBall == 2) {
							BankShot = true;
						}
					}
				}
				if	(ballStrick == YELLOW) {
					if(yellowHit == 0) {
						yellowHit++;
						if(bankShot >= 3)
						objectBall++;
						if(bankShot >= 3 && objectBall == 2) {
							BankShot = true;
						}
					}
				}
				if (ballStrick == WHITE) {
					if(whiteHit == 0) {
						whiteHit++;
						if(bankShot >= 3)
						objectBall++;
						if(bankShot >= 3 && objectBall == 2) {
							BankShot = true;
						}
					}
				}
				lastBallHit++;
				if(impactCushion <= 2 ) {
					if(startStart)
						if(endStop != 1)
						if(ballStrick != RED)
						foul();
						if(impactCushion >= redHit) {
							if(redHit != 0)
							foul();
						}
				}
				if(impactCushion>=3) {
					valid = false;
					if(lastBallHit>=1) {
						valid = true;
					}
				}
			}
			else
				foul();
		}
	}

	public void cueStickStrike(BallType cueBall) {
		if(gameOver == false || lagIfCalled == true) {
			if(foul == true)
				foul();
			startStart = true;
			BankShot = false;
			justcuestrike = true;
			
			
			if (hasScore_A == false || hasScore_B == false)
				valid=false;
			else
				valid = true; 
			
				if(curPlayersBall == cueBall) {
					InningStarted = true;
					shotEnds = true;
					Break = true;
					foul = false;
					
			
				}
				else {
					foul();
					shotEnds = true;
					dontChange = true;
				}
		;
			
			}
	}

	public void endShot() {
		if(gameOver == false || lagIfCalled == true) {
			startStart = false;
			endStop = 1;
			
				if(foul == false && valid ) {
					if(curPlayer == PLAYER_A) {
						if(impactCushion>=3) {
							hasScore_A = false;
							if(lastBallHit>=1) {
								hasScore_A = true;
								scoreA++;
								InningStarted = true;
							}
							else
								foul();
						}
						else
							foul();
					}
				
						else {
							if(impactCushion>=3) {
								hasScore_B = false;
								if(lastBallHit>=1) {
									hasScore_B = true;
									scoreB++;
									InningStarted = true;
								}
								else
									foul();
							}
							else
								foul();
						}
				}
				if (hasScore_A == true || hasScore_B == true)
					valid=true;
				else
					valid = false;
				if(shotEnds == true) {
					if(InningStarted && !valid){ 
						if(impactCushion == 0 && redHit == 0 && yellowHit == 0 && whiteHit == 0) {
							inning++;
							
						}	
					}
					impactCushion = 0;
					bankShot = 0;
					lastBallHit = 0;
					redHit = 0;
					yellowHit = 0;
					whiteHit = 0;
					objectBall = 0;       
					
				
				shotEnds = false;
				InningStarted = false;
				if(valid)
					InningStarted = true;
				Break = false;
		//roundChange = true;
				if(scoreB == pointsToWin) {
					gameOver=true;
					endStop = 0;
					lagIfCalled = false;
					InningStarted =false;
					gameStart = false;
				}
				if(scoreA == pointsToWin) {
					gameOver=true;
					endStop = 0;
					lagIfCalled = false;
					InningStarted =false;
					gameStart = false;
				}
				if(!dontChange || justcuestrike) {
					if(curPlayer == PLAYER_A) {
						curPlayer = PLAYER_B;
					}
					else {
						curPlayer = PLAYER_A;
					}
			
					if(cueBall == WHITE) {
						cueBall = YELLOW;
						curPlayersBall=YELLOW;
					}
					else {
						cueBall = WHITE;
						curPlayersBall= WHITE;
					}
					dontChange = false;
					
			}
		}
		
		
		
		}
	}

	public void foul() {
		if(gameOver == false){
			foul = true;
			startStart = false;
			InningStarted = false;
			if(gameStart)
				inning = inning+1;
			
			if(curPlayer == PLAYER_A) {
				curPlayer = PLAYER_B;
			}
			else {
				curPlayer = PLAYER_A;
			}
			
			if(cueBall == WHITE) {
				cueBall = YELLOW;
				curPlayersBall=YELLOW;
				}
			else {
				cueBall = WHITE;
				curPlayersBall= WHITE;
			}
		}
		
		
		
	/*	inning = inning+1;
		//roundChange = true;
		if(cueBall == WHITE) {
			cueBall = YELLOW;
			curPlayersBall=YELLOW;
			}
		else {
			cueBall = WHITE;
			curPlayersBall= WHITE;
		}
		
		if(curPlayer == PLAYER_A) {
			curPlayer = PLAYER_B;
			//curPlayersBall=YELLOW;
		}
		else
		*/	
		
	
		
	}

	public BallType getCueBall() {
		
		return cueBall;
	}
	
	public int getInning() {
		
		return inning;
	}

	

	public boolean isBankShot() {
		
		return BankShot;
	}

	public boolean isBreakShot() {
		
			return Break;
	}

	public boolean isShotStarted() {
		
			return shotEnds;
	}

	public void lagWinnerChooses(boolean selfBreak, BallType cueBall) {
		this.selfBreak = selfBreak;
		this.cueBall = cueBall;
		hasScore_A = false;
		hasScore_B = false;
		startStart = false;
		gameOver = false;
		lagIfCalled = true;
		gameStart = true;
		Break = true;
		if(selfBreak == true) {
			curPlayer = lagWinner;
			curPlayersBall = cueBall;
			
		}
		else {
			playerTrack();
			BallTrack();
			curPlayer = otherPlayer;
			curPlayersBall = otherPlayerBall;
			this.cueBall = curPlayersBall;
			
		}
	}
	private void playerTrack() {
		
			if(lagWinner==PLAYER_A) 
				otherPlayer = PLAYER_B;
			else
				otherPlayer = PLAYER_A;
			
		}
	
	
	private void BallTrack() {
		
			if(cueBall==WHITE) 
				otherPlayerBall = YELLOW;
			else
				otherPlayerBall = WHITE;
				
			}	
		
	
	
	
	
}

