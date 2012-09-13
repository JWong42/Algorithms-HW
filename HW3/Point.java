import java.util.Comparator;

public class Point implements Comparable<Point> {

    // Compares points by slope.
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();       

    private final int x;   // x coordinate
    private final int y;   // y coordinate

    // Implements SlopeOrder for sort. 
    private class SlopeOrder implements Comparator<Point> {
    	public int compare(Point v, Point w) {
    		double slope1 = Point.this.slopeTo(v);
    		double slope2 = Point.this.slopeTo(w); 
    		if (slope1 < slope2) {
    			return -1; 
    		} else if (slope1 > slope2) {
    			return 1; 
    		} else { 
    			return 0; 
    		}
    	}
    }
    
    // Creates the point (x, y).
    public Point(int x, int y) {
        this.x = x;
        this.y = y;    
    }

    // Plots this point to standard drawing.
    public void draw() {
        StdDraw.point(x, y);
    }

    // Draws line between this point and that point to standard drawing.
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // Slope between this point and that point.
    public double slopeTo(Point that) {
    	double slope = (double)(that.y - this.y) / (that.x - this.x); 
    	// Horizontal line.
    	if (slope == 0) {   
     		return 0.0; 
     	// Vertical line.
    	} else if (slope == Double.POSITIVE_INFINITY || slope == Double.NEGATIVE_INFINITY) {   
    		return Double.POSITIVE_INFINITY; 
    	// Degenerate line segment. 
    	} else if (Double.isNaN(slope))	{
    		return Double.NEGATIVE_INFINITY; 
    	} else {  
    		return slope;  
    	}
    }

    // Is this point lexicographically smaller than that one?
    // Comparing y-coordinates and breaking ties by x-coordinates.
    public int compareTo(Point that) {
    	if (this.y < that.y) {
    		return -1; 
    	} else if (this.y == that.y) {
    		if (this.x < that.x) {
    			return -1; 
    		} else if (this.x > that.x) {
    			return 1; 
    		}  else {      // this.x == that.x 
    			return 0; 
    		}    			
    	} else {           // that.y > that.y
    		return 1; 
    	}
    }

    // Returns string representation of this point.
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
