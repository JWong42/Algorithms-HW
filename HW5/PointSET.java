import java.util.*;

public class PointSET {
	
	private TreeSet<Point2D> tree; 

	// Construct an empty set of points. 
	public PointSET() { 
	    tree = new TreeSet<Point2D>(); 
	}
	
	// Is the set empty? 
	public boolean isEmpty() { 
		return tree.isEmpty();
	}
	
	// Number of points in the set. 
	public int size() {
		return tree.size(); 
	}
	
	// Add the point p to the set (if it is not already in the set).
	public void insert(Point2D p) {
		tree.add(p); 
	}
	
	// Does the set contain the point p? 
	public boolean contains(Point2D p) {
		return tree.contains(p); 
	}
	
	// Draw all of the points to standard draw. 
	public void draw() {
		StdDraw.square(0.5, 0.5, 0.5); 
		Iterator<Point2D> iterator = tree.descendingIterator(); 
		while (iterator.hasNext()) {
			Point2D p = iterator.next(); 
			StdOut.println(p); 
			StdDraw.setPenRadius(0.007);
			p.draw(); 
		}
	}
	
	// All points in the set that fall inside the rectangle. 
	public Iterable<Point2D> range(RectHV rect) {
		Stack<Point2D> stack =  new Stack<Point2D>(); 	
		for (Point2D p : tree) {
			if (rect.contains(p)) stack.push(p); 
		}
		return stack; 
	}
	
	// The nearest neighbor in the set to a given point p; null if set is empty. 
	public Point2D nearest(Point2D p) {
		Point2D nearest = null; 
		double shortestDistance = p.distanceTo(tree.first());
		for (Point2D q : tree) {
			if (p.distanceTo(q) <= shortestDistance) {
				shortestDistance = p.distanceTo(q); 
				nearest = q; 
			}
		}
		return nearest; 
	}
	
}
