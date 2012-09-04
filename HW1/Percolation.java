public class Percolation {	
	private int gridSize; 
	private int virtualTop; 
	private int virtualBottom; 
	private int[] openSites; 
	private WeightedQuickUnionUF ufOne;
	private WeightedQuickUnionUF ufTwo;
	
	public Percolation(int N) {
		// First site in N*N matrix starts at index 0. 
		
		gridSize = N;              
		virtualTop = N*N;          // For 4x4 matrix, the virtualTop will be at index 16.
		virtualBottom = (N*N)+1;   // For 4x4 matrix, the virtualBottom will be at index 17.
		openSites = new int[N*N];  // Matrix used to keep track of opened/closed sites using 1's (opened) and 0's (closed). 
		
		ufOne = new WeightedQuickUnionUF((N*N)+1);  // Obj used in checking for isFull - no virtual bottom.
		ufTwo = new WeightedQuickUnionUF((N*N)+2);	// Obj used in checking for percolation.
	
		for (int i = 0; i < N; i++) { 
			ufOne.union(i, virtualTop);               // Establish virtual top for uf object 1. 
			ufTwo.union(i, virtualTop);               // Establish virtual top for uf object 2.
			ufTwo.union((N*N)-(i+1), virtualBottom);  // Establish virtual bottom for uf object 2.
		}		
	}
	
	// Opens a given site at i, j coordinates.
	public void open(int i, int j) {
		validateIndices(i, j);
		int newSite1D = xyTo1D(i, j);		
		openSites[newSite1D] = 1; 
		connectNeighborOf(newSite1D, i-1, j);  // top 
		connectNeighborOf(newSite1D, i, j+1);  // right
		connectNeighborOf(newSite1D, i+1, j);  // bottom 
		connectNeighborOf(newSite1D, i, j-1);  // left
	}
	
	// Converts i, j coordinates to 1D index.	
	private int xyTo1D(int i, int j) {
		int a = i-1; 
		int b = j-1; 
		return (gridSize*a)+b; 
	}
	
	// Connects a site to its neighbor if it is opened. 	
	private void connectNeighborOf(int site, int i, int j) {
		if ((i > 0 && i <= gridSize) && (j > 0 && j <= gridSize)) {
			if (isOpen(i, j)) {
				int neighborOfSite = xyTo1D(i, j); 
				ufOne.union(site, neighborOfSite); 
				ufTwo.union(site, neighborOfSite);
			}
		}	
	}
	
	// Checks whether a site at i, j is opened. 	
	public boolean isOpen(int i, int j) {
		// an open site is not blocked
		validateIndices(i, j); 
		int siteToCheck = xyTo1D(i, j); 		 
		return openSites[siteToCheck] == 1; 		
	}
	
	// Checks whether a site at i, j is opened and connected to another site in top row. 	
	public boolean isFull(int i, int j) {
		validateIndices(i, j); 
		int siteToCheck = xyTo1D(i, j);
		if (isOpen(i, j) && ufOne.connected(siteToCheck, virtualTop)) {
			return true; 
		}
		return false; 		
	}
	
	// Checks if any cell in bottom row is connected to another cell in top row through virtual sites.
	public boolean percolates() {
		if (gridSize == 1 && !isOpen(1, 1)) {
			return false; 
		}
		return ufTwo.connected(virtualTop, virtualBottom); 		
	}		
	
	// Throws exception when given i and j coordinates are out of bounds. 	
	private void validateIndices(int i, int j) {
		if (i <= 0 || i > gridSize) throw new IndexOutOfBoundsException("row index i out of bounds");
		if (j <= 0 || j > gridSize) throw new IndexOutOfBoundsException("row index j out of bounds");
	}
	
}
