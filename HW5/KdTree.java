
public class KdTree {
	private Node root;  // Root of KdTree.
	private int N;      // Number of nodes in KdTree.
	
	private static class Node {
		private Point2D p;   // Point in the node. 
		private RectHV rect; // Axis-aligned rectangle corresponding to this node
		private Node lb;     // Left/bottom subtree.
		private Node rt;     // Right/top subtree. 
		
		public Node(Point2D p) {
			this.p = p;  
		}

		public Node(Point2D p, RectHV rect) {
			this.p = p;
			this.rect = rect; 
		}
	}
	
	// Is the tree empty? 
	public boolean isEmpty() {
		return N == 0; 
	}
	
	// Return the number of nodes in tree. 
	public int size() { 
		return N; 
	}
	
	// Insert a new node with the given point if it is not already present. 
	public void insert(Point2D p) {
		boolean success = false; 
		try {
			RectHV rect = root.rect;        // Check to see if there's a root already  
			success = true; 
		} catch (NullPointerException e) {  // If not, insert the root with the initial rect.  
			root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
			N = 1; 
			success = false; 
		} if (success) {                    // If root's present, insert a new point as normally. 
	 		root = insert(root, p, root.p, root.rect, 0, true); 
		}
	}
	
	// Helper method to insert the point in the correct position. 
	private Node insert(Node x, Point2D p, Point2D parentP, RectHV rect, int direction, boolean isEvenLevel ) { 
		// Insert a new node with the specified point if x is null. 
		if (x == null) {
			Node node = new Node(p);  
			if (isEvenLevel) {          // Even level - vertical orientation.      
				if (direction == 1) {         // Direction 1 - left side.
					node.rect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), parentP.y());
				} else if (direction == 2) {  // Direction 2 - right side.
					node.rect = new RectHV(rect.xmin(), parentP.y(), rect.xmax(), rect.ymax());
				}
			} else if (!isEvenLevel) {  // Odd level - horizontal orientation. 
				if (direction == 1) {
					node.rect = new RectHV(rect.xmin(), rect.ymin(), parentP.x(), rect.ymax());
				} else if (direction == 2) {
					node.rect = new RectHV(parentP.x(), rect.ymin(), rect.xmax(), rect.ymax());
				}
			}
			N += 1; 
			return node; 
		}
		if (x.p.equals(p)) return x; 
		// Recursively traverse down the tree following the below conditions. 
		if (isEvenLevel) {             
			if (x.p.x() > p.x()) x.lb = insert(x.lb, p, x.p, x.rect, 1, false);  
			else if (x.p.x() <= p.x()) x.rt = insert(x.rt, p, x.p, x.rect, 2, false); 
		} else if (!isEvenLevel) {   
			if (x.p.y() > p.y()) x.lb = insert(x.lb, p, x.p, x.rect, 1, true);  
			else if (x.p.y() <= p.y()) x.rt = insert(x.rt, p, x.p, x.rect, 2, true);
		}
		return x; 
	}
	
	// Does KdTree contain the point p? 
	public boolean contains(Point2D p) {
		return contains(root, p, 0); 
	}
	
	// Helper method to check the presence of point in tree. 
	private boolean contains(Node x, Point2D p, int level) {	
		if (x == null) return false; 
		if (x.p.equals(p)) return true;
		if (level == 0) {           // level - 0: even (vertical)
			if (x.p.x() > p.x()) return contains(x.lb, p, 1);  
			else if (x.p.x() <= p.x()) return contains(x.rt, p, 1);  
		} else if (level == 1) {    // level - 1: odd (horizontal)
			if (x.p.y() > p.y()) return contains(x.lb, p, 0);  
			else if (x.p.y() <= p.y()) return contains(x.rt, p, 0); 
		}
		return false; 
	}
	
	// Draw all of the points to standard draw. 
	public void draw() {
		StdDraw.square(0.5, 0.5, 0.5); 
		draw(root, true); 
	}
	
	private void draw(Node x, boolean isEvenLevel) {
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.007);
		if (x == null) return; 
		x.p.draw();              // Draw point. 
		if (isEvenLevel) {       // Draw red vertical line. 
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius(0.003);
			StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
			draw(x.lb, false); 
			draw(x.rt, false); 
		} else {                 // Draw horizontal line.
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.setPenRadius(0.003);
			StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
			draw(x.lb, true); 
			draw(x.rt, true); 
		}
	}
	
	
	// All points in the tree that fall inside the rectangle. 
	public Iterable<Point2D> range(RectHV rect) {
		return range(root, rect); 
	}
	
	private Iterable<Point2D> range(Node x, RectHV rect) {
		 // Use a stack to keep track of points that fall in the rectangle.
		Stack<Point2D> stack = new Stack<Point2D>();  
		if (x == null || !rect.intersects(x.rect)) return stack; 
		// First, check to see if the given rectangle intersects a node's axis-aligned rectangle.
		if (rect.intersects(x.rect)) {
			if (rect.contains(x.p)) stack.push(x.p); // If so, add the node's point to the stack. 
			// Recursively check left and right subtrees. 
			Iterable<Point2D> stackLeft = range(x.lb, rect); 
			for (Point2D point : stackLeft) stack.push(point); 
			Iterable<Point2D> stackRight = range(x.rt, rect); 
			for (Point2D point : stackRight) stack.push(point); 
		}
		return stack;
	}
	
	public Point2D nearest(Point2D p) {
		 return nearest(root, p, root.p, true); 
	}
	
	// The nearest neighbor (champion) in the tree to a given point p; null if tree is empty. 
	private Point2D nearest(Node x, Point2D p, Point2D champion, boolean isEvenLevel) {	 
		
		if (x == null || x.rect.distanceSquaredTo(p) >= champion.distanceSquaredTo(p)) return champion;
		
		// Search for nearest point if dist. of node's axis-aligned rect to query point is less than dist. of query point to current closest point. 
		if (x.rect.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
			
			// Update nearest neighbor if node's point's dist. to query point is closer than previous nearest neighbor's dist to query point. 
			if (x.p.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) champion = x.p;  
			
			// Recursively traverse down the tree to check other points. 
			// Always choose the subtree that is on the same side of the splitting line as the query point first. 
			if (isEvenLevel && p.x() <= x.p.x()) {             // Even level and query point left of splitting line.
				champion = nearest(x.lb, p, champion, false);
				champion = nearest(x.rt, p, champion, false);
			} else if (isEvenLevel && p.x() > x.p.x()) {       // Even level and query point right of splitting line.
				champion = nearest(x.rt, p, champion, false);
				champion = nearest(x.lb, p, champion, false);
			} else if (!isEvenLevel && p.y() <= x.p.y()) {     // Odd level and query point below the splitting line.
				champion = nearest(x.lb, p, champion, true); 
				champion = nearest(x.rt, p, champion, true);
			} else if (!isEvenLevel && p.y() > x.p.y()) {      // Odd level and query point right of splitting line.
				champion = nearest(x.rt, p, champion, true);
				champion = nearest(x.lb, p, champion, true);
			}
		}
		return champion; 	
	}
	
}
