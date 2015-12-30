package de.dungeonrunner.util;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Decorater to provide functionality to reverse iterate thorugh 
 * a list. 
 * 
 * @author Robert Wolfinger
 *
 * @param <T>
 */
public class ReverseList<T> implements Iterable<T>{

	private final List<T> mOriginalList;
	
	public ReverseList(List<T> list) {
		mOriginalList = list;
	}
	
	 public Iterator<T> iterator() {
	        final ListIterator<T> i = mOriginalList.listIterator(mOriginalList.size());

	        return new Iterator<T>() {
	            public boolean hasNext() { return i.hasPrevious(); }
	            public T next() { return i.previous(); }
	            public void remove() { i.remove(); }
	        };
	    }

	    public static <T> ReverseList<T> reversed(List<T> original) {
	        return new ReverseList<T>(original);
	    }
}
