import java.util.Arrays;

public class Board {
	
	private final int N; 
	private short priorityScoreH;
	private short priorityScoreM; 
	private final short[][] tiles; 

	// Construct a board from an N-by-N array of blocks
	public Board(int[][] blocks) {
		N = blocks.length; 	 
		tiles = new short[N][N];  
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				tiles[i][j] = (short) blocks[i][j]; 				
			}
		}
	}
	
	private Board(short[][] blocks) {
		N = blocks.length; 	 
		tiles = new short[N][N];  
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				tiles[i][j] = (short) blocks[i][j]; 				
			}
		}
	}
	
	// Board dimension N. 
	public int dimension() { 
		return N; 
	}
	
	// Number of blocks out of place. 
	public int hamming() { 
		if (priorityScoreH == 0) {
			int value, positionCorrectness; 
			int incrementValue = 0; 
			for (int i=0; i < N; i++) {
				for (int j=0; j < N; j++) {
					incrementValue += 1; 
					if (i == N-1 && j == N-1) incrementValue = 0; 
					value = tiles[i][j];
					if (value == 0)  continue;
					if (value == incrementValue) {
						positionCorrectness = 0; 
					} else { 
						positionCorrectness = 1; 
					}
					priorityScoreH += positionCorrectness; 
				}
			}
		}
		return priorityScoreH; 
	}
	
	// Sum of Manhattan distances between blocks and goal.
	public int manhattan() { 
		if (priorityScoreM == 0) {
			int distance, row, col, value, remainder;  
			for (int i=0; i < N; i++) {
				for (int j=0; j < N; j++) {
					value = tiles[i][j]; 
					if (value == 0) continue; 
					remainder = value % N; 
					if (remainder == 0) {
						row = (value/N) - 1; 
						col = N-1; 
					} else { 
						row = value/N; 
						col = remainder-1; 
					}
					distance = Math.abs(i-row) + Math.abs(j-col); 
					priorityScoreM += distance; 
				}
			}
		}
		return priorityScoreM; 		
	}
	
	// Is this board the goal board? 
	public boolean isGoal() { 
		int incrementValue = 1; 
		for (int i=0; i < N; i++) {
			for (int j=0; j < N; j++) {
				if (i == N-1 && j == N-1) incrementValue = 0;  
				if (tiles[i][j] != incrementValue) return false;
				incrementValue += 1; 
			}
				
		}
		return true; 	
	}
	
	// A board obtained by exchanging two adjacent blocks in the same row 
	public Board twin() { 		
		Board twinBoard = new Board(this.tiles); 
		for (int i=0; i < twinBoard.N; i++) {
			short checkTile = twinBoard.tiles[i][0]; 
			short checkTileAdjacent = twinBoard.tiles[i][1]; 
			if (checkTile != 0 && checkTileAdjacent != 0) {
				checkTileAdjacent = twinBoard.tiles[i][1]; 
				twinBoard.tiles[i][0] = checkTileAdjacent; 
				twinBoard.tiles[i][1] = checkTile; 
				break; 
			}
		}
		return twinBoard; 	
	}
	
	// Does this board equal y? 
	public boolean equals(Object x) {
		if (x == this) return true; 
		if (x == null) return false; 
		if (x.getClass() != this.getClass()) return false; 
		Board that = (Board) x; 
		return (this.N == that.N) && (Arrays.deepEquals(this.tiles, that.tiles)); 				
	}
	
	// Return all neighboring board in an iterable.
	public Iterable<Board> neighbors() {
		Stack<Board> neighborBoards = new Stack<Board>(); 
		int row = 0;  // Row of the blank tile.
		int col = 0;  // Column of the blank tile. 
		// Find the bank tile's position
		for (int i=0; i < N; i++) {
			for (int j=0; j < N; j++) {
				int checkTile = tiles[i][j]; 
				if (checkTile == 0) {
					row = i; 
					col = j; 
					break; 
				}
			}
		}			
		// top left corner
		if (row == 0 && col == 0) {   
			neighborBoards.push(neighborBoard(row, col, 0, 1));	 // board with blank tile exchanged with right one
			neighborBoards.push(neighborBoard(row, col, 1, 0));  // board with blank tile exchanged with below one				
		// top right corner
		} else if (row == 0 && col == N-1) {   
			neighborBoards.push(neighborBoard(row, col, 0, -1)); // board with blank tile exchanged with left one
			neighborBoards.push(neighborBoard(row, col, 1, 0));	 
		// bottom left corner
		} else if (row == N-1 && col == 0) {  
			neighborBoards.push(neighborBoard(row, col, -1, 0)); // board with blank tile exchanged with top one
			neighborBoards.push(neighborBoard(row, col, 0, 1));	
		// bottom right corner 
		} else if (row == N-1 && col == N-1) {  
			neighborBoards.push(neighborBoard(row, col, -1, 0));
			neighborBoards.push(neighborBoard(row, col, 0, -1));
		// top side
		} else if (row == 0 && col > 0 && col < N-1) {    
			neighborBoards.push(neighborBoard(row, col, 0, -1));
			neighborBoards.push(neighborBoard(row, col, 0, 1));
			neighborBoards.push(neighborBoard(row, col, 1, 0));	
		// bottom side 
		} else if (row == N-1 && col > 0 && col < N-1) {  
			neighborBoards.push(neighborBoard(row, col, 0, -1));
			neighborBoards.push(neighborBoard(row, col, 0, 1));
			neighborBoards.push(neighborBoard(row, col, -1, 0));
		// right side 
		} else if (row > 0 && row < N-1 && col == N-1) {  
			neighborBoards.push(neighborBoard(row, col, -1, 0));
			neighborBoards.push(neighborBoard(row, col, 0, -1));
			neighborBoards.push(neighborBoard(row, col, 1, 0));
		// left side 
		} else if (row > 0 && row < N-1 && col == 0) {  
			neighborBoards.push(neighborBoard(row, col, -1, 0));
			neighborBoards.push(neighborBoard(row, col, 0, 1));
			neighborBoards.push(neighborBoard(row, col, 1, 0));
		// anywhere else 
		} else {
			neighborBoards.push(neighborBoard(row, col, -1, 0));
			neighborBoards.push(neighborBoard(row, col, 0, 1));
			neighborBoards.push(neighborBoard(row, col, 0, -1));
			neighborBoards.push(neighborBoard(row, col, 1, 0));		
		}
 		return neighborBoards; 	
	}
	
	// Helper method for neighbors(). 
	private Board neighborBoard(int row, int col, int i, int j) {
		// Makes a copy of the original 
		short[][] neighborTiles = new short[N][N]; 
		for (int x=0; x < N; x++) {
			for (int y=0; y < N; y++) {
				neighborTiles[x][y] = tiles[x][y]; 
			}
		}
		short newTile = neighborTiles[row+i][col+j]; 
		neighborTiles[row+i][col+j] =  neighborTiles[row][col]; 
		neighborTiles[row][col] = newTile; 
		Board neighborBoard = new Board(neighborTiles);
		return neighborBoard; 
	}
	
	// String representation of the board.
	public String toString() { 
		StringBuilder s = new StringBuilder(); 
		s.append(N + "\n"); 
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				s.append(String.format("%2d ", tiles[i][j])); 
			}
			s.append("\n"); 
		}
		return s.toString(); 
	}
	
}
