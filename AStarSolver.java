package solver;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Kevin Bechman
 * Solves the Nineteen puzzle using the AStar algorithm.
 * The Nineteen puzzle is arranged in the following manner:
 * 			0  1      
	        2  3      
	  4  5  6  7  8  9
	 10 11 12 13 14 15
	       16 17      
	       18 19
 * The game is set up by switching the "0" (the empty tile) with any other tile,
 * doing this repeatedly until the board is scrambled.
 * Once the board is scrambled, the player attempts to reverse the pattern.
 * 
 * To solve the puzzle, the program treats the sequential moves as Nodes on a graph, where each Node
 * represents a given state reached by advancing from a previous state.  Each state has 2-4 possible moves
 * to be made to reach a new state of the board.
 * 
 * From the start state node, the program generates new nodes representing all possible moves from that state,
 * and assigns a heuristic value to each node (Manhattan Distance of each tile on that state's board to the position
 * the tile would have on the goal state).
 * This heuristic value, combined with the cost to reach that state on the path, allows the program to sequential
 * proceed backwards from the scrambled state, finding the most efficient (shortest) path to the goal state.
 * 
 * This program uses a Min Priority Queue (Sedgewick-modified code) to keep track of discovered nodes (possible moves)
 * ordered by the cost of that Node.
 * It also uses a HashMap to test if a repeated state, when encountered, has a lower path cost than its prevoius twin
 * (the same state can be encountered more than once).
 */
public class AStarSolver {
	static int count = 1; 	// Number of insertions into the frontier - 
						  	// used to assign number to respective nodes
	
	// The goal state for the board to be completed (what the solver is aiming towards)
	static final int[][] GS = {
			{-1, -1, 0,  1,  -1, -1},
			{-1, -1, 2,  3,  -1, -1},
			{ 4,  5, 6,  7,   8,  9},
			{10, 11, 12, 13, 14, 15},
			{-1, -1, 16, 17, -1, -1},
			{-1, -1, 18, 19, -1, -1} };
	static final Board GOAL_BOARD = new Board(GS);
		
	
	public static void main(String[] args) {

		/**
		 * Each test to be solved:
		 */
		
		int[][] testReallyEasy = {
				{-1, -1, 1,  0,  -1, -1},
				{-1, -1, 2,  3,  -1, -1},
				{ 4,  5, 6,  7,   8,  9},
				{10, 11, 12, 13, 14, 15},
				{-1, -1, 16, 17, -1, -1},
				{-1, -1, 18, 19, -1, -1} };
				
			int[][] testFail = {
				{-1, -1, 2,  0,  -1, -1},
				{-1, -1, 1,  3,  -1, -1},
				{ 4,  5, 6,  7,   8,  9},
				{10, 11, 12, 13, 14, 15},
				{-1, -1, 16, 17, -1, -1},
				{-1, -1, 18, 19, -1, -1} };
			
			int[][] test1 = {
				{-1, -1,  2,  1, -1, -1},
				{-1, -1,  6,  3, -1, -1},
				{ 4,  5, 12,  7,  8,  9},
				{10, 11, 13, 17, 14, 15},
				{-1, -1,  0, 16, -1, -1},
				{-1, -1, 18, 19, -1, -1} };
				
			int[][] test2 = {
				{-1, -1,  2,  1, -1, -1},
				{-1, -1,  6,  7, -1, -1},
				{ 5,  3, 0,  17,  8,  9},
				{4, 10, 12, 11, 14, 15},
				{-1, -1,  13, 16, -1, -1},
				{-1, -1, 18, 19, -1, -1} };
				
			int[][] test3 = {
				{-1, -1,  2,  1, -1, -1},
				{-1, -1,  6,  7, -1, -1},
				{ 0,  4, 12,  17,  8,  9},
				{3, 5, 10, 11, 14, 15},
				{-1, -1,  13, 16, -1, -1},
				{-1, -1, 18, 19, -1, -1} };
				
			int[][] test4 = {
				{-1, -1,  1,  6, -1, -1},
				{-1, -1,  2,  0, -1, -1},
				{ 5,  3, 12,  7,  17,  15},
				{4, 10, 13, 16, 9, 8},
				{-1, -1,  18, 14, -1, -1},
				{-1, -1, 19, 11, -1, -1} };
				
			int[][] test5 = {
				{-1, -1, 1, 6, -1, -1},
				{-1, -1, 2, 7, -1, -1},
				{ 4,  13, 10, 15, 9, 12},
				{3, 0, 5, 16, 17, 8},
				{-1, -1, 18, 19, -1, -1},
				{-1, -1, 11, 14, -1, -1} };
				
			int[][] test6 = {
				{-1, -1, 9, 1, -1, -1},
				{-1, -1, 7, 6, -1, -1},
				{ 4,  18, 0, 2, 17, 12},
				{3, 13, 5, 19, 10, 8},
				{-1, -1, 11, 14, -1, -1},
				{-1, -1, 16, 15, -1, -1} };
			
			
			
			// Test the solver
			double startTime = System.nanoTime();
			ArrayList<Board> goalPath = solve(new Board(test4));
			double endTime = System.nanoTime();
			if (goalPath == null)
				System.out.println("Frontier empty; no goal states found.");
			else {
				// Print out states in order showing how it was solved
				System.out.println("Goal Path:");
				for(int i = goalPath.size() - 1; i >= 0; i--) {
					System.out.println(goalPath.get(i));
				}
				System.out.println("Goal achieved in " + (goalPath.size() - 1) + " moves!");
				System.out.println("Took " + (endTime - startTime)/1000000000.0 + " seconds.");
			}
			
			
	}
	
	/**
	 * Given the start state of a scrambled board, this method uses the A* algorithm to search a graph
	 * of nodes representing the possible movements a player could make to solve the game board.
	 * @param startState The initial, scrambled state of the board.
	 * @return An ArrayList of Boards representing the path from the scrambled state to the unscrambled state.
	 */
	public static ArrayList<Board> solve(Board startState) {
		
		Node startNode = new Node(startState, null, count);	// Starting position of the puzzle
		MinPQ<Node> frontier = new MinPQ<>();			// Priority queue representing the frontier
		HashMap<Board, Node> nodes = new HashMap<>();	// Hashmap of the Boards (state) to a Node,
													 	//   the explored set is maintained through a field "inFrontier"
		nodes.put(startState, startNode);			// Hash the start board (state) and store the start node
		frontier.add(nodes.get(startState));  		// Initialize the frontier with the starting state
		
		// Loop continuously, checking the node with the lowest cost from the frontier,
		// and then adding its neighbors to the frontier.
		// Either the goal state is reached, or the frontier empties and a failure is returned.
		while(true) {
			// Check if frontier is empty - if not, keep going.
			if(frontier.isEmpty()) {
				return null;
			}
			
			// Remove node from frontier with lowest: (manhatDist + cost from root to path of node)
			Node currNode = frontier.remove();
			
			// Check if current node is a goal state:
			if(currNode.getBoard().isGoalState()) {
				ArrayList<Board> goalPath = new ArrayList<>();
				while(currNode != null) {
					goalPath.add(currNode.getBoard());
					currNode = currNode.getParent();
				}
				return goalPath;  // Return the path from the start state to the goal state
			}
			
			// Add current node to explored set:
			currNode.setExplored();
			
			// Expand current node and add neighbors to frontier:
			// Switch tiles, then see if the switched board is in the hashmap.
			// If not, add it, if so, check HashMap value cost and update if new is lower.
			Board currBoard, switchedBoard;	// current board, current board with switch made
			currBoard = currNode.getBoard();
			int moveX = 0, moveY = 0;	// Moving forward
			// For each possible neighbor, assign the move 
			for (int q=0; q<4; q++) {
				switch (q) {
					case 0: {
						moveX = currBoard.blank()[0] - 1; 
						moveY = currBoard.blank()[1];
					} break;
					case 1: {
						moveX = currBoard.blank()[0] + 1; 
						moveY = currBoard.blank()[1];
					} break;
					case 2: {
						moveX = currBoard.blank()[0]; 
						moveY = currBoard.blank()[1] - 1;
					} break;
					case 3: {
						moveX = currBoard.blank()[0]; 
						moveY = currBoard.blank()[1] + 1;
					} break;
				}
				// If a move can be performed:
				if (currBoard.inBounds(moveX, moveY)) {
					// Copy the current board, and perform the move on the copy
					switchedBoard = currBoard.copyBoard();
					switchedBoard.switchTiles(moveX, moveY);
					// Create a new node from the copied, switched board
					Node switchedNode = new Node(switchedBoard, currNode, count++);
					// If the switched board (neighbor) has already been observed (either in frontier or explored)
					Node storedSwitchedNode = nodes.get(switchedBoard);
					if (storedSwitchedNode != null) {
						// If the stored node is not yet explored (in frontier), 
						// and its cost is greater than the current node's cost
						if (storedSwitchedNode.inFrontier() &&
							storedSwitchedNode.getCost() > 
							currNode.getPathCost() + 1 + switchedNode.getManhatDist()) {
							// Find the appropriate node in the frontier and modify its cost value:
							// Update neighbor node in frontier:
							nodes.put(switchedBoard, 
									  new Node(switchedBoard, currNode, nodes.get(switchedBoard).getNumber()));
							frontier.update(nodes.get(switchedBoard));
						}
						// Else, the node is explored and can be ignored
					}
					else {
						// Node has not been seen yet, so add it in and add to frontier (sets by default).
						nodes.put(switchedBoard, switchedNode);
						frontier.add(switchedNode);
					}
				}
			}
		}
	}

}
