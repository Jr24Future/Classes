import java.util.Arrays;
import java.util.Random;

import api.ScoreUpdateListener;
import api.ShowDialogListener;
import api.Tile;
import hw3.ConnectGame;
import hw3.GameFileUtil;
import hw3.Grid;
import ui.GameConsole;

/**
 * Examples of using the ConnectGame, GameFileUtil, and Grid classes. The main()
 * method in this class only displays to the console. For the full game GUI, run
 * the ui.GameMain class.
 */
public class SimpleTests {
	public static void main(String args[]) {
		// Example use of Grid
		
  
	    ConnectGame game = new ConnectGame(5, 10, 2, 6, new Random(0));
	    GameConsole gc = new GameConsole();
	    game.setListeners((ShowDialogListener)gc, (ScoreUpdateListener)gc);
	    game.radomizeTiles();
	    Grid grid = game.getGrid();
	    Tile t1 = new Tile(6);
	    Tile t2 = new Tile(6);
	    Tile t3 = new Tile(7);
	    grid.setTile(t1, 1, 2);
	    grid.setTile(t2, 2, 2);
	    grid.setTile(t3, 3, 2);
	    game.tryFirstSelect(1, 2);
	    game.tryContinueSelect(2, 2);
	    game.tryContinueSelect(3, 2);
	    game.tryFinishSelection(3, 2);
	    System.out.println( game.getScore());
	    
	    
	}
}
