/* Client program that reads in N strings from standard input and
 * prints out k number (specified from command-line) of them in a random fashion. 
 * 
 * Execution: echo A B C D E | java Subset 4 
 * Result example - C E B 
 */

public class Subset {
	
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]); 
		// Uses randomized queue as the primary data structure for this program. 
		RandomizedQueue<String> rq = new RandomizedQueue<String>(); 
		while(!StdIn.isEmpty()) {
		    rq.enqueue(StdIn.readString()); 
		}
		for (int i = 0; i < k; i ++) {
			StdOut.println(rq.dequeue()); 
		}
	}
	
}
