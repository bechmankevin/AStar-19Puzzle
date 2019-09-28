package solver;

import java.util.ArrayList;

/**
 * @author Kevin Bechman
 *
 * This class is used to represent a given state on the path from the start state to the goal state.
 * A Node object most importantly contains a Board (a state of the game board) and the path cost to get
 * to that state.  In addition, a Node contains a reference to its parent Node - the state just prior to
 * the current object's state.
 * 
 * The class also keeps track of whether or not this Node has been explored, so as to distinguish Nodes
 * still in the frontier from Nodes that have yet to be visited.
 */
public class Node implements Comparable<Node>, Denumerable {
	private int num;      // Identifying number for Denumerable interface and MinPQ
	private Board board;  // state of a board
	private Node parent;  // Parent node
	private int totalCost; 	  // f-value : sum of pathCost and manhatDist
	private int pathCost; // g-value
	private boolean inFrontier;  // Whether or not this node has been visited
	
	Node(Board board, Node parent, int num) {
		this.board = board;
		this.parent = parent;
		this.num = num;
		inFrontier = true;
		if (parent != null)
			pathCost = parent.getPathCost() + 1;
		else pathCost = 0;
		totalCost = getManhatDist() + pathCost;
	}

	
	/**
	 * Finds the Manhattan Distance, or the heuristic for the A* pathfinding algorithm.
	 * Takes each tile's X and Y coordinate distance from the goal location (the unscrambled state of the board)
	 * and adds them together, providing the heuristic value.
	 * @return The heuristic value of this Node's board state.
	 */
	int getManhatDist() {
		// For each tile, find the manhattan distance to its goal state and
		// add in to the total.
		int dist = 0;
		int[] loc;
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 6; x++) {
				if (board.getTileNumber(y, x) != -1 && board.getTileNumber(y, x) != 0) {
					loc = AStarSolver.GOAL_BOARD.findLoc(board.getTileNumber(y, x));
					dist += Math.abs(y - loc[0]);
					dist += Math.abs(x - loc[1]);
				}
			}
		}
		return dist;
	}

	
	/**
	 * Iterates through the path formed by the node's ancestors, going back
	 * to the start state from the goal state.
	 * @return ArrayList of states, starting with the goal state.
	 */
	ArrayList<Board> getPath() {
		ArrayList<Board> path = new ArrayList<>();
		Node n = this;
		while (parent != null) {
			path.add(n.board);
			n = this.parent;
		}
		return path;
	}

	Board getBoard() {
		return board;
	}

	int getCost() {
		return totalCost;
	}

	Node getParent() {
		return parent;
	}

	int getPathCost() {
		return pathCost;
	}

	boolean inFrontier() {
		return inFrontier;
	}

	void setExplored() {
		inFrontier = false;
	}
	
	// Overrides:
	
	@Override
	public int getNumber() {
		return num;
	}
	
	@Override
	public void setNumber(int x) {
		num = x;
	}
	
	@Override
	public String toString() {
		return board.toString() + "cost: " + getManhatDist() + " + " + pathCost + " = " + totalCost + " and inFrontier = "
				+ inFrontier + "\nID: " + getNumber() + "\n";
	}
	
	@Override
	public boolean equals(Object o) {
		Node n = (Node) o;
		if (this.getBoard().equals(n.getBoard()) && this.getCost() == (n.getCost())) {
			return true;
		}
		return false;
	}
	
	@Override
	public int compareTo(Node n) {
		if (n == null)
			return 1;
		if (n.equals(this))
			return 0;
		if (this.getCost() > n.getCost())
			return 1;
		if (this.getCost() < n.getCost())
			return -1;
		return 0;
	}
}
