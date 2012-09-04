public class PercolationStats {
	private Percolation pc; 
	private double[] thresholdList;
	
	// N is N in N*N matrix. T is number of experiments ran.
	public PercolationStats(int N, int T) {		
		if (N <= 0 || T <= 0) {
			throw new IllegalArgumentException("N and T cannot be less than or equal to 0.");
		}
		
		thresholdList = new double[T]; // Array used to keep track of thresholds generated in all T experiments.
		
		// Performs Monte Carlo Simulation. 
	    for (int i = 0; i < T; i++) {  
		    double threshold = calculateThreshold(N);  
			thresholdList[i] = threshold;  // Each threshold produced from an experiment is added to the array.
		}
	}
	
	// Main method used in the simulations. 
	private double calculateThreshold(int N) {
		pc = new Percolation(N); 
		double count = 0; 		
		while (!pc.percolates()) {
			int i = StdRandom.uniform(1, N+1);  // Generates a random number from 1 to N+1.
			int j = StdRandom.uniform(1, N+1); 
			if (!pc.isOpen(i, j)) {
				pc.open(i, j); 
				count += 1; 
			}			
		}
		// Threshold is calculated as (# of opened sites required to percolate)/# of sites in matrix.
		return count/(N*N);  
	}
	
	public double mean() {
		return StdStats.mean(thresholdList); 
	}
	
	public double stddev() {
		return StdStats.stddev(thresholdList); 
	}
	
	private String confidenceInterval(double mean, double stddev, int T) {
		double lessThan = mean - ((1.96*stddev)/Math.sqrt(T));
		double greaterThan = mean + ((1.96*stddev)/Math.sqrt(T));
		return String.format("%f, %f", lessThan, greaterThan); 	
	}
	
	public static void main(String[] args) {
	  int N = Integer.parseInt(args[0]);  // N is provided in command line as argument 1.
	  int T = Integer.parseInt(args[1]); // T is provided in command line as argument 2.
	  PercolationStats ps = new PercolationStats(N, T); 
	  double mean = ps.mean(); 
	  double stddev = ps.stddev(); 
	  StdOut.println("mean = " + mean);
	  StdOut.println("stddev = " + stddev);
	  StdOut.println("95% confidence interval = " + ps.confidenceInterval(mean, stddev, T)); 
	}
	
}
