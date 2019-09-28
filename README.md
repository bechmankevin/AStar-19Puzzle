# AStar-19Puzzle
Solving the "Nineteen Puzzle" using the A* algorithm as path-finding across a graph of possible states.

 * The Nineteen puzzle is arranged in the following manner:
    		  0  1      
	        2  3      
	  4  5  6  7  8  9
	 10 11 12 13 14 15
	       16 17      
	       18 19
 * The game is set up by switching the "0" (the empty tile) with any other adjacent tile,
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
