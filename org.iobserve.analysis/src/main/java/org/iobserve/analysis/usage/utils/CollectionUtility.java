package org.iobserve.analysis.usage.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

/**
 * This utility class is helping in issues, where a data structure has to be
 * converted into another especially when the converting code is more
 * complicated. <br>
 * <br>
 * For example a collection into an array when the collection is using a generic
 * type. Use the {@link #toArray(Collection, Class)} function accomplish that
 * job.
 * 
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 * @version 1.0, 23.10.2014
 * 
 */
public final class CollectionUtility {

	private CollectionUtility() {
		//  No implementation needed. This class is intended to be static at all.
	}

	/**
	 * Convert the given collection into an array of the type T. <br>
	 * <br>
	 * This function is needed, because it is not possible through the
	 * JDK-Collection-API to convert a Collection with a generic type, into an
	 * array of generic type without additional lines of code.
	 * 
	 * @param collection
	 * @param classOfElements
	 * @return array of type T with the exact same data and ordering
	 */
	public final static <T> T[] toArray(Collection<T> collection, Class<?> classOfElements) {
		@SuppressWarnings("unchecked")
		final T[] newGenericArray = (T[]) Array.newInstance(classOfElements, collection.size());
		int i = -1;
		for (final T nextElement : collection) {
			newGenericArray[++i] = nextElement;
		}
		return Arrays.copyOf(newGenericArray, newGenericArray.length);
	}

}
