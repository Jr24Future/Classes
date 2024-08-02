package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * @author Erroll Barker
 */

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E>
{
	  /**
	   * Default number of elements that may be stored in each node.
	   */
	  private static final int DEFAULT_NODESIZE = 4;
	  
	  /**
	   * Number of elements that can be stored in each node.
	   */
	  private final int nodeSize;
	  
	  /**
	   * Dummy node for head.  It should be private but set to public here only  
	   * for grading purpose.  In practice, you should always make the head of a 
	   * linked list a private instance variable.  
	   */
	  public Node head;
	  
	  /**
	   * Dummy node for tail.
	   */
	  private Node tail;
	  
	  /**
	   * Number of elements in the list.
	   */
	  private int size;
	  
	  /**
	   * Constructs an empty list with the default node size.
	   */
	  public StoutList()
	  {
		  this(DEFAULT_NODESIZE);
	  }
	
	  /**
	   * Constructs an empty list with the given node size.
	   * @param nodeSize number of elements that may be stored in each node, must be an even number
	   */
	  public StoutList(int nodeSize)
	  {
		  if (nodeSize <= 0 || nodeSize % 2 != 0) throw new IllegalArgumentException();
	    
		  // dummy nodes
		  head = new Node();
		  tail = new Node();
		  head.next = tail;
		  tail.previous = head;
		  this.nodeSize = nodeSize;
	  }
	  
	  /**
	   * Constructor for grading only.  Fully implemented. 
	   * @param head
	   * @param tail
	   * @param nodeSize
	   * @param size
	   */
	  public StoutList(Node head, Node tail, int nodeSize, int size)
	  {
		  this.head = head; 	
		  this.tail = tail; 
          this.nodeSize = nodeSize; 
          this.size = size; 
	  }
	
	  @Override
	  public int size()
	  {
		  return size;
	  }
	  
	  @Override
	  public boolean add(E item)
	  {
		  if (item == null) {
			  throw new NullPointerException(); //Throws NullPointerException if the item is null. Ensure the item is not null.
		  }
		  
		  if (contains(item)) {
			  return false;	// If item already exists, return false.
		  }
		  
		  if (size == 0) { //If the list is empty,
			  Node node = new Node();
			  node.addItem(item);	//Add the item to a new node.
			  head.next = node;
			  node.previous = head;
			  node.next = tail;
			  tail.previous = node;
		  } else {
			  if (tail.previous.count < nodeSize) { //Adds the item to the last node if it is not full.
				  tail.previous.addItem(item);
			  } else { //Creates another node at the end if the last node is full.
				  Node node = new Node();
				  node.addItem(item);
				  Node tempNode = tail.previous;
				  tempNode.next = node;
				  node.previous = tempNode;
				  node.next = tail;
				  tail.previous = node;
			  }
		  }
		  
		  size++; //Size of the list increases because a new item has been added.
		  return true;
	  }
	  
	   /**
		* Checks for a duplicate before adding.
		*
		* @param item to search for
		* @return whether the list contains the item or not
		*/
		public boolean contains(E item) 
		{
			if(size < 1) {
				return false;
			}
		
			Node tempNode = head.next;
			while(tempNode != tail) {
				for (int i = 0; i < tempNode.count; i++) {
					if(tempNode.data[i].equals(item)) {
						return true;
					}
						
					tempNode = tempNode.next;
				}
			}
			
			return false;
	 	
	    }
	
	  @Override
	  public void add(int pos, E item)
	  {
		  if (pos < 0 || pos > size) { //Throws IndexOutOfBoundsException if pos is out of bounds.
			  throw new IndexOutOfBoundsException(); //Check if position is valid.
		  }
		  
		  if (head.next == tail) {
			  add(item); //If the list is empty, simply add the item.
			  return;
		  } else if (contains(item)) {
			  return;	// If item already exists, return false.
		  }

		  NodeInfo nodeInfo = find(pos);	//Find the node to insert the item.
		  Node tempNode = nodeInfo.node;
		  int offset = nodeInfo.offset;
		  
		  if (offset == 0) {
			  if (tempNode.previous.count < nodeSize && tempNode.previous != head) {
				  tempNode.previous.addItem(item);	//Insert at the beginning if the previous node is not full.
				  size++;
				  return;
			  } else if (tempNode == tail) {
				  add(item);	//Add to the end if at the last node.
				  size++;
				  return;
			  }
		  }
		  
		  if (tempNode.count < nodeSize) {
			  tempNode.addItem(offset, item);	//Insert within a non-full node.
		  } else {
			  Node newSuccesor = new Node();
			  int halfPoint = nodeSize / 2;
			  int count = 0;
			  
			  //Split the full node into two nodes.
			  while (count < halfPoint) {
				  newSuccesor.addItem(tempNode.data[halfPoint]);
				  tempNode.removeItem(halfPoint);
				  count++;
			  }
			  
			  Node oldSuccesor = tempNode.next;
			  tempNode.next = newSuccesor;
			  newSuccesor.previous = tempNode;
			  newSuccesor.next = oldSuccesor;
			  oldSuccesor.previous = newSuccesor;
			  
			  if (offset <= nodeSize / 2) {
				  tempNode.addItem(offset, item);	//Insert into the first part of the split node.
			  }
			  
			  if (offset > nodeSize / 2) {
				  newSuccesor.addItem((offset - nodeSize / 2), item); //Insert into the second part of the split node.
			  }
		  }
		  
		  size++;	//Size of the list increases because a new item has been added.
	  }
	
	  @Override
	  public E remove(int pos)
	  {
		  if (pos < 0 || pos > size) { //Throws IndexOutOfBoundsException if pos is out of bounds.
			  throw new IndexOutOfBoundsException();	//Check if position is valid.
		  }
		  
		  NodeInfo nodeInfo = find(pos);	//Find the node to insert the item.
		  Node tempNode = nodeInfo.node;
		  int offset = nodeInfo.offset;
		  E nodeValue = tempNode.data[offset];
		  
		  if (tempNode.next == tail && tempNode.count == 1) {
			  Node predecessor = tempNode.previous;
			  predecessor.next = tempNode.previous;
			  tempNode.next.previous = predecessor;
			  tempNode = null;
		  } else if (tempNode.next == tail || tempNode.count > nodeSize / 2) {
			  tempNode.removeItem(offset);	//Remove from the current node.
		  } else {
			  tempNode.removeItem(offset);
			  Node Succesor = tempNode.next;
			  
			  if (Succesor.count > nodeSize / 2) {
				  tempNode.addItem(Succesor.data[0]);	//Move an element from the next node to the current node.
				  Succesor.removeItem(0);
			  } else if (Succesor.count <= nodeSize / 2) {
				  for (int i = 0; i <Succesor.count; i++) {
					  tempNode.addItem(Succesor.data[i]);	//Merge the next node into the current node.
				  }
				  
				  tempNode.next = Succesor.next;
				  Succesor.next.previous = tempNode;
				  Succesor = null;
			  }
		  }
		  
		  size--;	//Size of the list decreases because an item has been removed.
		  return nodeValue;
	  }
	
	  /**
	   * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
	   * Traverse the list and copy its elements into an array, deleting every visited node along 
	   * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
	   * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
	   * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
	   * (possibly) the last one must be full of elements.  
	   *  
	   * Comparator<E> must have been implemented for calling insertionSort().    
	   */
	  public void sort()
	  {
		  E[] sortDataList = (E[]) new Comparable[size];	//Create an array to store elements.
		  
		  int index = 0;
		  Node tempNode = head.next;
		  while (tempNode != tail) {
			  for (int i = 0; i < tempNode.count; i++) {
				  sortDataList[index] = tempNode.data[i];	//Copy elements to the array.
				  index++;
			  }
			  
			  tempNode = tempNode.next;
		  }
		  
		  head.next = tail;
		  tail.previous = head;
		  
		  insertionSort(sortDataList, new ElementComparator());	//Sort the array.
		  size = 0;
		  for (int i = 0; i < sortDataList.length; i++) {
			  add(sortDataList[i]);	//Update the stout list with sorted elements.
		  }
	  }
	  
	  /**
	   * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
	   * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
	   *  
	   * Comparable<? super E> must be implemented for calling bubbleSort(). 
	   */
	  public void sortReverse() 
	  {
		  E[] sortReverseDataList = (E[]) new Comparable[size];	//Create an array to store elements.
		  
		  int index = 0;
		  Node tempNode = head.next;
		  while (tempNode != tail) {
			  for (int i = 0; i < tempNode.count; i++) {
				  sortReverseDataList[index] = tempNode.data[i];	//Copy elements to the array.
				  index++;
			  }
			  
			  tempNode = tempNode.next;
		  }
		  
		  head.next = tail;
		  tail.previous = head;
		  
		  bubbleSort(sortReverseDataList);	//Sort the array in reverse order.
		  size = 0;
		  for (int i = 0; i < sortReverseDataList.length; i++) {
			  add(sortReverseDataList[i]);	//Update the stout list with sorted elements.
		  }
	  }
	  
	  @Override
	  public Iterator<E> iterator()
	  {
		  return new StoutListIterator();
	  }
	
	  @Override
	  public ListIterator<E> listIterator()
	  {
		  return new StoutListIterator();
	  }
	
	  @Override
	  public ListIterator<E> listIterator(int index)
	  {
		  return new StoutListIterator(index);
	  }
	  
	  /**
	   * Returns a string representation of this list showing
	   * the internal structure of the nodes.
	   */
	  public String toStringInternal()
	  {
		  return toStringInternal(null);
	  }
	
	  /**
	   * Returns a string representation of this list showing the internal
	   * structure of the nodes and the position of the iterator.
	   *
	   * @param iter an iterator for this list
	   */
	  public String toStringInternal(ListIterator<E> iter) 
	  {
	      int count = 0;
	      int position = -1;
	      
	      if (iter != null) {
	          position = iter.nextIndex();
	      }
	
	      StringBuilder sb = new StringBuilder();
	      sb.append('[');
	      Node current = head.next;
	      while (current != tail) {
	    	  sb.append('(');
	    	  E data = current.data[0];
	    	  if (data == null) {
	    		  sb.append("-");
	    	  } else {
	    		  if (position == count) {
	    			  sb.append("| ");
	    			  position = -1;
	    		  }
	    	      sb.append(data.toString());
	    	      ++count;
	    	  }
	
			  for (int i = 1; i < nodeSize; ++i) {
			     sb.append(", ");
			      data = current.data[i];
			      if (data == null) {
			          sb.append("-");
			      } else {
			          if (position == count) {
			              sb.append("| ");
			              position = -1;
			          }
			          sb.append(data.toString());
			          ++count;
			
			          // iterator at end
			          if (position == size && count == size) {
			              sb.append(" |");
			              position = -1;
			          }
			     }
			  }
			  
			  sb.append(')');
			  current = current.next;
			  
			  if (current != tail) {
				  sb.append(", ");
			  }
			      
	      }  
	      
	      sb.append("]");
	      return sb.toString();
	  }
	
	  /**
	   * @author Erroll Barker
	   * 
	   * Node type for this list.  Each node holds a maximum
	   * of nodeSize elements in an array. Empty slots are null.
	   */
	  private class Node
	  {
		 /**
		  * Array of actual data elements.
		  */
		 // Unchecked warning unavoidable.
		 public E[] data = (E[]) new Comparable[nodeSize];
	
		 /**
		  * Link to next node.
		  */
		 public Node next;
	
		 /**
		  * Link to previous node;
		  */
		 public Node previous;
	
		 /**
		  * Index of the next available offset in this node, also 
		  * equal to the number of elements in this node.
		  */
		 public int count;
	
		/**
		 * Adds an item to this node at the first available offset.
		 * Precondition: count < nodeSize
		 * @param item element to be added
		 */
		void addItem(E item)
		{
			if (count >= nodeSize)
		    {
				return; //Node is full, cannot add more.
		    }
		  
			data[count++] = item;	//Add the item to the node.
		    //useful for debugging
			//System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
		}
		  
		/**
		 * Adds an item to this node at the indicated offset, shifting
		 * elements to the right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * @param offset array index at which to put the new element
		 * @param item element to be added
		 */
		void addItem(int offset, E item)
		{
			if (count >= nodeSize)
		    {
				return;	//Node is full, cannot add more.
		    }
			
		    for (int i = count - 1; i >= offset; --i)
		    {
		    	data[i + 1] = data[i];	//Shift elements to the right.
		    }	
		    
		    ++count;	//Increment the count.
		    data[offset] = item;	//Insert the item at the offset.
		    //useful for debugging 
		    //System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
		    
		    
		}
		
		/**
		 * Deletes an element from this node at the indicated offset, 
		 * shifting elements left as necessary.
		 * Precondition: 0 <= offset < count
		 * @param offset
		 */
		void removeItem(int offset)
		{
			E item = data[offset];	//Retrieve the item to be removed.
		    for (int i = offset + 1; i < nodeSize; ++i)
		    {
		    	data[i - 1] = data[i];	//Shift elements to the left.
		    }
		    	data[count - 1] = null;	//Clear the last element.
		        --count;	//Decrement the count.
		}    
	  }
	  
	  /**
	   * @author Erroll Barker
	   * 
	   * An iterator implementation for traversing a StoutList.
	   *
	   * This iterator allows both forward and backward traversal through the elements
	   * of a StoutList.
       *
	   * @param <E> The type of elements in the StoutList	
	   */
	  private class StoutListIterator implements ListIterator<E>
	  {
		  // constants you possibly use
		  
		  final int LAST_ACTION_PREV = 0;
		  final int LAST_ACTION_NEXT = 1;
		  
	      // instance variables
		  
		  /**
		   * Pointer for iterator.
		   */
		  int currentPosition;
		  
		  /**
		   * Tracks the last action.
		   */
		  int lastAction;
		  
		  /**
		   * Data Structure of the iterator.
		   */
		  public E[] dataList;
		    
		/**
		 * Default constructor 
		 */
		public StoutListIterator()
		{
			currentPosition = 0;	//Start at the beginning.
			lastAction = -1;	//No last action yet.
			setup(); //Setup the iterator.
		}
	
		/**
		 * Constructor finds node at a given position.
		 * @param pos
		 */
		public StoutListIterator(int pos)
		{
			currentPosition = pos;	//Start at the specified position.
			lastAction = -1;	//No last action yet.
			setup(); //Setup the iterator.
		}
		
		@Override
		public boolean hasNext()
		{
			//Check if there's a next element.
			if (currentPosition >= size) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public E next()
		{
			if (!hasNext()) {
				throw new NoSuchElementException();	//Throw an exception if no more elements.
			}
			
			lastAction = LAST_ACTION_NEXT;	//Set the last action to "next".
			return dataList[currentPosition++];	//Return and move to the next element.
		}	
		
		@Override
		public void remove()
		{
			if (lastAction == LAST_ACTION_NEXT) {
				StoutList.this.remove(currentPosition - 1);	//Remove the last accessed element.
				setup();	//Setup the iterator data again.
				lastAction = -1;	//Reset the last action.
				currentPosition--;	//Adjust the current position.
				if (currentPosition < 0) {
					currentPosition = 0;	//Ensure the position is not negative.
				}
			} else if (lastAction == LAST_ACTION_PREV) {
				StoutList.this.remove(currentPosition);	//Remove the last accessed element.
				setup();	//Setup the iterator data again.
				lastAction = -1;	 //Reset the last action.
			} else {
				throw new IllegalStateException();	//Throw an exception if the action is invalid.
			}
		}
		
		// Other methods you may want to add or override that could possibly facilitate 
		// other operations, for instance, addition, access to the previous element, etc.
		// 
		// ...
		
		/**
		 * Sets up the dataList array by initializing it with the appropriate size
		 * and populating it with elements from the StoutList nodes.
		 * 
		 * This method creates a new array and populates it by iterating through the StoutList nodes,
		 * collecting the elements in dataList for use by the iterator.
		 */
		private void setup() {
			dataList = (E[]) new Comparable[size];	//Initialize the data array.
			
			int index = 0;
			Node tempNode = head.next;
			while (tempNode != tail) {
				for (int i = 0; i < tempNode.count; i++) {
					dataList[index] = tempNode.data[i];	//Populate the data array.
					index++;
				}
				
				tempNode = tempNode.next;
			}
		}
		
		/**
		 * @return Iterator has previous available value or not.
		 */
		@Override
		public boolean hasPrevious() {
			if (currentPosition <= 0) {
				return false;	//No previous element.
			} else {
				return true;	//Previous element is available.
			}
		}
		
		/**
		 * @return Index of the next element.
		 */
		@Override
		public int nextIndex() {
			return currentPosition;	//Return the index of the next element.
		}
		
		/**
		 * @return Previous element.
		 */
		@Override
		public E previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();	//No previous element, throw exception.
			}
			
			lastAction = LAST_ACTION_PREV;	//Set last action to previous.
			currentPosition--;	//Move the cursor backwards.
			return dataList[currentPosition];	//Return the previous element.
		}
		
		/**
		 * @return Index of previous element.
		 */
		@Override
		public int previousIndex() {
			return currentPosition - 1;	//Return the index of the previous element.
		}
		
		/**
		 * @param Replacing element.
		 */
		@Override
		public void set(E rep) {
			if (lastAction == LAST_ACTION_NEXT) {
				NodeInfo nodeInfo = find(currentPosition - 1);
				nodeInfo.node.data[nodeInfo.offset] = rep;
				dataList[currentPosition - 1] = rep;
			} else if (lastAction == LAST_ACTION_PREV) {
				NodeInfo nodeInfo = find(currentPosition);
				nodeInfo.node.data[nodeInfo.offset] = rep;
				dataList[currentPosition] = rep;
			} else {
				throw new IllegalStateException();	//Invalid action, throw exception.
			}
		}
		
		/**
		 * @param Adding element.
		 */
		@Override
		public void add (E add) {
			if (add == null) {
				throw new NullPointerException();	//Null element, throw exception.
			}
			
			StoutList.this.add(currentPosition, add);	//Add the element at the current position.
			currentPosition++;	//Move the cursor forward.
			setup();	//Setup the iterator data again.
			lastAction = -1;	//Reset the last action.
		}
	  }
	  
	  /**
	   * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order. 
	   * @param arr   array storing elements from the list 
	   * @param comp  comparator used in sorting 
	   */
	  private void insertionSort(E[] arr, Comparator<? super E> comp)
	  {
		  for (int i = 1; i < arr.length; i++) {
			  E key = arr[i];
			  int j = i - 1;
			  
			  while (j >= 0 && comp.compare(arr[j], key) > 0) {
				  arr[j + 1] = arr[j];
				  j--;
			  }
			  
			  arr[j + 1] = key;
		  }
	  }
	  
	  /**
	   * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
	   * description of bubble sort please refer to Section 6.1 in the project description. 
	   * You must use the compareTo() method from an implementation of the Comparable 
	   * interface by the class E or ? super E. 
	   * @param arr  array holding elements from the list
	   */
	  private void bubbleSort(E[] arr)
	  {
		  int n = arr.length;
		  
		  for (int i = 0; i < n - 1; i++) {
			  for (int j = 0; j < n - i - 1; j++) {
				  if (arr[j].compareTo(arr[j+1]) < 0) {
					  E temp = arr[j];
					  arr[j] = arr[j + 1];
					  arr[j + 1] = temp;
				  }
			  }
		  }
	  }
	  
	  /**
	   * @author Erroll Barker
	   * 
	   * Helper class to store information about a node and offset.
	   */
	  private class NodeInfo {
		  public Node node;	//The node.
		  public int offset;	//The offset within the node.
		  
		  /**
		   * Constructor for NodeInfo.
		   * 
		   * @param node	the node
		   * @param offset	the offset within the node
		   */
		  public NodeInfo(Node node, int offset) {
			  this.node = node;
			  this.offset = offset;
		  }
	  }
	  
	  /**
	   * Finds the node and offset for a given position in the list.
	   * 
	   * @param pos	the position to find node and offset for
	   * @return NodeInfo containing the node and offset
	   */
	  private NodeInfo find(int pos) {
		  Node tempNode = head.next; // Start from the first node
		  int currentPos = 0; // Current position in the list

		  // Traverse the list to find the node and offset for the given position
		  while (tempNode != tail) {
		    if (currentPos + tempNode.count <= pos) {
		      // Move to the next node if the current node doesn't cover the position
		      currentPos += tempNode.count;
		      tempNode = tempNode.next;
		      continue;
		    }

		    // Found the node that covers the position, create and return NodeInfo
		    NodeInfo nodeInfo = new NodeInfo(tempNode, pos - currentPos);
		    return nodeInfo;
		  }

		  // Return null if the position is out of range
		  return null;
	  }
	  
	  /**
	   * Comparator for elements of type E. Compares elements using their natural ordering.
	   *
	   * @param <E> the type of elements to compare
	   */
	  class ElementComparator<E extends Comparable<E>> implements Comparator<E> {
	    @Override
	    public int compare(E arg0, E arg1) {
	      return arg0.compareTo(arg1); // Compare elements using their natural ordering
	    }
	  }
}