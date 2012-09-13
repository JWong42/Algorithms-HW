import java.util.Arrays;

public class Brute {
	
    public static void main(String[] args) {
    	
        // Rescales coordinates and turns on animation mode.
    	StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);

        // Reads in the input.        
    	String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();   // Total # of points that are in input file.        
        
        // Adds all points from the input file to an array. 
        Point[] points = new Point[N];        
        for (int i=0; i < N; i++) {
        	int x = in.readInt(); 
        	int y = in.readInt(); 
        	Point p = new Point(x, y); 
        	points[i] = p; 
        }
       
        // Sorts the points array in descending order. 
        Arrays.sort(points); 
         
        // Brute forcing through to get all combinations of 4 points (N choose 4). 
        for (int i=0; i < N-3; i++) {
        	for (int j=i+1; j < N-2; j++) {
        		for (int k=j+1; k < N-1; k++) {
        			for (int l=k+1; l < N; l++) {     				
        				double slope1 = points[i].slopeTo(points[j]); 
        				double slope2 = points[j].slopeTo(points[k]); 
        				double slope3 = points[k].slopeTo(points[l]);
        				if (slope1 == slope2 && slope2 == slope3 && slope1 == slope3) {
        					// Prints out line segment order. 
        					StdOut.println(points[i] + " -> " + points[j] + " -> " + points[k] + " -> " + points[l]);
        					// Draws a line segment connecting the two ends. 
        					points[i].drawTo(points[l]);  
        				}
        			}
        		}
        	}
        }
       
        // Displays to screen all at once.
        StdDraw.show(0);
    }
}
