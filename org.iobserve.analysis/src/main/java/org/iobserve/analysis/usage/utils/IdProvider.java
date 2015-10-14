package org.iobserve.analysis.usage.utils;

import java.util.Map;

/**
 * An IDProvider is just a component which is able to provide an appropriated ID
 * for the given element.<br>
 * The right place to use this utility component is, where ever another
 * component has to store such elements and needs a kind of unique ID for each
 * of those elements. Using a {@link Map} could be one example.
 * 
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 * @version 1.0, 23.10.2014
 * 
 * @param <E>
 */
public interface IdProvider<E> {
	
	/**
	 * Get the ID of the given element
	 * @param element
	 * @return a unique id which can be used in maps, lists, etc..
	 */
	public abstract String getId(E element);

}
