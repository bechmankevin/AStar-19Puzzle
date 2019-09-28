package solver;

/**
 * @author Kevin Bechman
 * 
 * This class represents a state of the board - it holds a 2D array, and contains
 * various methods for manipulating and gathering information about the position of tiles on the board.
 */
public class Board {
	public int[][] board;		// 2D array representing game board
	private int[] blank;		// location of blank spot where blank[0] is y and blank[1] is x
								//   saved for convenience
	
	/**
	 * Initializes board with the supplied 2D array representation of one.
	 * @param board A 2-D array representing the game board.
	 */
	Board(int[][] board) {
		this.board = board;
		blank = findLoc(0);
	}
	
	/**
	 * Tests if the given coordinate resides within the game board.
	 * @param y Y Coordinate value
	 * @param x X Coordinate value
	 * @return True if the coordinate is in-bounds, false otherwise.
	 */
	boolean inBounds(int y, int x) {
		if( ((y == 2 || y == 3) && (x >= 0 && x < 6)) || ((x == 2 || x == 3) && (y >= 0 && y < 6)) ) {
			return true;
		} else { 
			return false;
		}
	}
	
	/**
	 * Finds the location of the tile numbered by the parameter 'k' on the board
	 * and returns its coordinates.
	 * @param k The number of the tile to be located
	 * @return 2-cell int array, with [0] and [1] being the 'y' and 'x' coordinates, respectively
	 */
	int[] findLoc(int k) {
		int[] loc = new int[2];
		for(int y = 0; y < 6; y++) {
			for(int x = 0; x < 6; x++) {
				if(board[y][x] == k) {
					loc[0] = y;
					loc[1] = x;
					return loc;
				}
			}
		}
		System.out.println("Error: Location of tile " + k + " not found!");
		loc[0] = loc[1] = -1;
		return loc;
	}
	
	/**
	 * Provides a deep clone of the board with no lingering references.
	 * @return Board object with same values as this Board
	 */
	public Board copyBoard() {
		int[][] ret = new int[6][6];
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				ret[i][j] = board[i][j];
			}
		}
		return new Board(ret);
	}
	
	/**
	 * Copies and returns the location of the blank tile (y, x).
	 * @return 2-cell array where blank[0] is y-value and blank[1] is x-value
	 */
	public int[] blank() {
		return new int[] { blank[0], blank[1] };
	}
	
	/**
	 * Provides the tile's number (0-19) given coordinates 'y' and 'x'.
	 * @param y y-value of tile
	 * @param x x-value of tile
	 * @return The tile number
	 */
	public int getTileNumber(int y, int x) {
		return board[y][x];
	}
	
	/**
	 * Tests whether this board is equal to the board representing the goal state
	 * @return True if board is a goal state, false otherwise
	 */
	public boolean isGoalState() {
		if(this.equals(new Board(AStarSolver.GS)))
			return true;
		return false;
	}
	
	/**
	 * Switches values of tiles between [by][bx] (the blank tile) and [y][x] (tile to be switched w/ blank tile).
	 * @param iy y-value of tile to swap with blank tile
	 * @param ix x-value of tile to swap with blank tile
	 * @return The newly swapped board, or null if the tiles can't be swapped
	 */
	public Board switchTiles(int y, int x) {
		int by = blank[0], bx = blank[1];
		if(!inBounds(by, bx) || !inBounds(y, x) || !areAdj(by, bx, y, x)) {
			System.out.println(by + " " + bx + " " + y + " " + x);
			System.out.println("Cannot switch between tiles that are out of bounds or adjacent.");
			return null;
		}
		// Swap the tile values
		int temp = board[by][bx];
		board[by][bx] = board[y][x];
		board[y][x] = temp;
		blank[0] = y; blank[1] = x;
		return this;
	}
	
	/**
	 *  Tests if the two coordinates are adjacent
	 * @param y1  y value of first tile
	 * @param x1  x value of first tile
	 * @param y2  y value of second tile
	 * @param x2  x value of second tile
	 * @return True if the given coordinates are adjacent, false otherwise
	 */
	public boolean areAdj(int y1, int x1, int y2, int x2) {
		// Check that the two tiles are adjacent horizontally or vertically
		// If the y values are 1 apart and x are the same,
		// or if y values are the same and x values are 1 apart, return true.  
		// Otherwise, return false.
		if ((Math.abs(y1 - y2) == 1 && Math.abs(x1 - x2) == 0) ||
		    (Math.abs(y1 - y2) == 0 && Math.abs(x1 - x2) == 1)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		for(int y = 0; y < 6; y++) {
			for(int x = 0; x < 6; x++) {
				if (board[y][x] != -1) {
					hash = hash * 31 + board[y][x];
				}
			}
		}
		return Math.abs(hash);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!this.getClass().equals(o.getClass()))
			return false;
		Board b = (Board) o;
		for(int y = 0; y < 6; y++) {
			for(int x = 0; x < 6; x++) {
				if(board[y][x] != b.getTileNumber(y, x))
					return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		String str = "\n";
		for(int y = 0; y < 6; y++) {
			for(int x = 0; x < 6; x++) {
				if(board[y][x] == -1) { str += "   "; }
				else if(board[y][x] < 10) { str += "  " + board[y][x]; }
				else { str+= " " + board[y][x]; }
					
			}
			str += "\n";
		}
		return str;
	}
}
