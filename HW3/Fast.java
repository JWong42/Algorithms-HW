import java.util.Arrays;

public class Fast {

	public static void main(String[] args) {
		
        // Rescales coordinates and turn on animation mode.
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

        // For each point, do a secondary sort using SLOPE_ORDER. 
        for (int i=0; i < N; i++) {
        	// First, makes an array copy of the original, sorted points array.
        	Point[] pointsCopy = new Point[N];
        	for (int j=0; j < N; j++) {
        		pointsCopy[j] = points[j]; 
        	}
        	
        	// SLOPE_ORDER sort the array. 
        	// Increment the low point in the sort range and move forward the pivot point with each pass.
        	Arrays.sort(pointsCopy, i, pointsCopy.length, pointsCopy[i].SLOPE_ORDER); 
        	
        	double prevSlope = pointsCopy[i].slopeTo(pointsCopy[i]);
        	double currentSlope; 
        	int collinearPts = 1; 
        	
        	// Checks for groupings of collinear points starting at a point adjacent to the pivot point. 
        	for (int k=i+1; k < N; k ++) {            	
        		currentSlope = pointsCopy[i].slopeTo(pointsCopy[k]);
        		if (prevSlope == currentSlope) {
        			collinearPts++; 
        			// For a set of collinear points at the end, probe from beginning of array for another point with same slope
        			// to check if these collinear points are part of a larger line segment. 
        			if (collinearPts >= 3 && k == N-1) {    				
        				boolean flag = false; 
        				// Checks from beginning of array to the left of pivot point 
        				for (int l=0; l < i; l++) {   
        					if (flag == true) break; 
        					double slopeToCheck = pointsCopy[i].slopeTo(pointsCopy[l]);
        					if (prevSlope == slopeToCheck ) flag = true; 
        				}
        				// If no larger line segment exists.
        				if (flag == false) {  
        					Point[] lineSegment = new Point[collinearPts+1];
        					// Adds the pivot point as last item in lineSegment. 
        					lineSegment[collinearPts] = pointsCopy[i];   
        					// Items are added to lineSegment in reverse order from pointsCopy.
        					for (int m=0; m < collinearPts; m++) {
        						lineSegment[m] = pointsCopy[k-m];        
        					}
        					// Sorts lineSegment to get collinear points in order.
        					Arrays.sort(lineSegment);   
        					// Prints out line segment order.
        					for (int n=0; n<collinearPts; n++){
        						StdOut.print(lineSegment[n] + " -> ");    
        					}
        					StdOut.print(lineSegment[collinearPts]); 
        					StdOut.println(); 
        					// Draws a line connecting the two collinear points at the end.
        					lineSegment[0].drawTo(lineSegment[collinearPts]);   
        				}         				
        			}
       			      			
        		} else {        			
        			if (collinearPts >= 3) {  
        				boolean flag = false; 
        				for (int l=0; l < i; l++) {    
        					if (flag == true) break; 
        					double slopeToCheck = pointsCopy[i].slopeTo(pointsCopy[l]);
        					if (prevSlope == slopeToCheck ) flag = true; 
        				}
        				if (flag == false) {  
        					Point[] lineSegment = new Point[collinearPts+1];
        					lineSegment[0] = pointsCopy[i]; 
        					for (int m=1; m < collinearPts+1; m++) {
        						lineSegment[m] = pointsCopy[k-m]; 
        					}
        					Arrays.sort(lineSegment); 
        					for (int n=0; n<collinearPts; n++){
        						StdOut.print(lineSegment[n] + " -> "); 
        					}
        					StdOut.print(lineSegment[collinearPts]); 
        					StdOut.println(); 
        					lineSegment[0].drawTo(lineSegment[collinearPts]);
        				}        				
        			}       			  
        			collinearPts = 1;
        			prevSlope = currentSlope;
        		}       		
        	}      	
        }

        // Displays to screen all at once.
        StdDraw.show(0);
	}
	
}
