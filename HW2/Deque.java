import java.util.Iterator; 

public class Deque<Item> implements Iterable<Item> {
    private int N;       // Size of deque. 
    private Node pre, post;  // References to sentinels of deque.
    
    // Deque utilizes doubly linked-list.
    private class Node { 
    	private Item item; 
    	private Node next; 
    	private Node prev; 
    }
	
    public Deque() {
        N = 0; 
        pre = new Node();  // Creates the sentinel that's before the first item.
        post = new Node(); // Creates the sentinel that's after the last item. 
        pre.next = post; 
        post.prev = pre; 
    }
	
    // Checks if deque is empty. 
	public boolean isEmpty() {
	    return N == 0; 
	}
	
	// Returns the size of deque. 
	public int size() {
	    return N; 
	}
	
	// Adds an item to the front of the deque.
	public void addFirst(Item item) {
	    if (item == null) throw new java.lang.NullPointerException("Cannot add null items.");
	    Node oldFirst = pre.next; 
	    Node first = new Node();		
	    first.item = item; 
	    first.prev = pre;
     	first.next = oldFirst; 
	    oldFirst.prev = first; 
	    pre.next = first; 
	    N++; 
	}
	
	// Adds an item to the back of the deque. 	
	public void addLast(Item item) {
	    if (item == null) throw new java.lang.NullPointerException("Cannot add null items"); 
	    Node oldLast = post.prev; 
	    Node last = new Node(); 
	    last.item = item; 
	    last.next = post; 
	    last.prev = oldLast; 
	    oldLast.next = last; 
	    post.prev = last; 
        N++; 
	}
	
	// Removes an item from the front of the deque. 
	public Item removeFirst() {
	    if (isEmpty()) throw new java.util.NoSuchElementException("Deque is empty."); 
	    Node first = pre.next; 
	    Item item = first.item; 
	    pre.next = first.next; 
	    first.next.prev = pre; 
	    N--; 
	    return item; 
	}
	
	// Removes an item from the back of the deque. 
	public Item removeLast() { 
	    if (isEmpty()) throw new java.util.NoSuchElementException("Deque is empty."); 
	    Node last = post.prev; 
	    Item item = last.item; 
	    post.prev = last.prev; 
	    last.prev.next = post; 
	    N--; 
	    return item; 
	}
	
	// Returns an iterator object used for iteration through nodes in deque. 
	public Iterator<Item> iterator() {
	    return new DequeIterator(); 		
	}
	
	// Implementation of iterator object. 
	private class DequeIterator implements Iterator<Item> { 		
	    private Node current = pre.next; 
	 
	    public boolean hasNext() {
	        return current != post; 
	    }
	
	    public Item next() { 
		    Item item = current.item; 
		    current = current.next; 
		    return item; 
	    }
		
	    public void remove() { 
		    throw new java.lang.UnsupportedOperationException("Remove operation is not suppored."); 
	    }		
	}
	
}
