/***************************************************************************
 * Copyright 2015 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.utils;

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
 * @author Robert Heinrich, Alessandro Giusa, alessandrogiusa@gmail.com
 * @version 1.0, 23.10.2014
 *
 */
public final class CollectionUtility {

	private CollectionUtility() {
		// No implementation needed. This class is intended to be static at all.
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
	public static final <T> T[] toArray(final Collection<T> collection, final Class<?> classOfElements) {
		@SuppressWarnings("unchecked")
		final T[] newGenericArray = (T[]) Array.newInstance(classOfElements, collection.size());
		int i = -1;
		for (final T nextElement : collection) {
			newGenericArray[++i] = nextElement;
		}
		return Arrays.copyOf(newGenericArray, newGenericArray.length);
	}

}
