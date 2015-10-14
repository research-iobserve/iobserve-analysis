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
package org.iobserve.analysis.usage.utils;

/**
 * An IDProvider is just a component which is able to provide an appropriated ID
 * for the given element.<br>
 * The right place to use this utility component is, where ever another
 * component has to store such elements and needs a kind of unique ID for each
 * of those elements. Using a {@link java.util.Map} could be one example.
 *
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 * @version 1.0, 23.10.2014
 *
 * @param <E>
 */
public interface IIdProvider<E> {

	/**
	 * Get the ID of the given element
	 *
	 * @param element
	 * @return a unique id which can be used in maps, lists, etc..
	 */
	public abstract String getId(E element);

}
