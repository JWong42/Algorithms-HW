
public class Solver {
	private MinPQ<SearchNode> pqA;
	private MinPQ<SearchNode> pqATwin;
	private SearchNode minA; 
	private SearchNode minATwin; 
	private int moves; 
	private boolean isSolvable; 
	private Stack<Board> solution = new Stack<Board>();

	// Find a solution to the initial board (using the A* algorithm). 
	public Solver(Board initial) {
		Board twin = initial.twin(); 

		pqA = new MinPQ<SearchNode>(); 
		pqATwin = new MinPQ<SearchNode>(); 
		
		SearchNode initialNode = new SearchNode(initial, 0, null);
		SearchNode initialNodeTwin = new SearchNode(twin, 0, null);
		
		pqA.insert(initialNode); 
		pqATwin.insert(initialNodeTwin); 
		
		minA = pqA.delMin(); 
		minATwin = pqATwin.delMin();
		
		while (true) {
			
			if (!minA.board.isGoal()) {

				for (Board neighborBoard : minA.board.neighbors()) {
					SearchNode neighborSN = new SearchNode(neighborBoard, minA.steps+1, minA); 
					if (minA.previous == null || !minA.previous.board.equals(neighborSN.board)) {
						pqA.insert(neighborSN); 	
					} else { 				
						continue; 
					}				
				}								
			} else {
				isSolvable = true; 
				moves = minA.steps; 
				break; 
			}
			minA = pqA.delMin();
			
			if (!minATwin.board.isGoal()) {
				for (Board neighborBoardTwin : minATwin.board.neighbors()) {
					SearchNode neighborSNTwin = new SearchNode(neighborBoardTwin, minATwin.steps+1, minATwin); 
					if (minATwin.previous == null || !minATwin.previous.board.equals(neighborSNTwin.board)) {
						pqATwin.insert(neighborSNTwin); 
					} else { 		 
						continue; 
					}				
				}								
			} else {
				isSolvable = false;
				break; 
			}
			minATwin = pqATwin.delMin(); 
		}	
	}
	
	private class SearchNode implements Comparable<SearchNode> {
		public final Board board; 
		public final int steps; 
		public final SearchNode previous; 
		
		public SearchNode(Board board, int steps, SearchNode previous) {
			this.board = board; 
			this.steps = steps; 
			this.previous = previous; 
		}
		
		@Override public int compareTo(SearchNode that) { 
			if ((this.board.manhattan() + this.steps) < (that.board.manhattan() + that.steps)) {
				return -1; 
			} else if ((this.board.manhattan() + this.steps) > (that.board.manhattan() + that.steps)) {
				return 1; 
			} else {
				return 0; 
			}
		}
	}
	
	
	// Is the initial board solvable? 
	public boolean isSolvable() {
		return isSolvable; 
	}
	
	// Min number of moves to solve initial board; -1 if no solution 
	public int moves() { 
		if (!this.isSolvable()) {
			return -1;
		} else {
			return moves; 
		}
	}
	
	// Sequence of boards in a shortest solution; null if no solution.
	public Iterable<Board> solution() {  
		if (!this.isSolvable()) {
			return null; 
		} else {
			while (minA != null) {
				solution.push(minA.board); 
				minA = minA.previous; 
			}		
			return solution; 
		}
	}
	
	// Solve the slider puzzle. 
	public static void main(String[] args) {
		// create initial board from file 
		In in = new In(args[0]); 
		int N = in.readInt(); 
		int[][] blocks = new int[N][N]; 
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				blocks[i][j] = in.readInt(); 
			}
		}
		Board initial = new Board(blocks); 
		
		// Solve the puzzle. 
		Solver solver = new Solver(initial); 
		
		// Print solution to standard output.
		if (!solver.isSolvable()) {
			StdOut.println("No solution possible"); 
		} else { 
			StdOut.println("Minimum number of moves = " + solver.moves()); 
			for (Board board : solver.solution()) {
				StdOut.println(board); 
			}
		}
	}

}
