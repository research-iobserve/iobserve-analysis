/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Utility class for functional style programming helper methods.
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class FunctionalUtils { 

	/**
	 * Private constructor, since this class is an utility class.
	 */
	private FunctionalUtils() {
		// no implementation
	}

	/**
	 * Convert the given two parameter function into a curry equivalent one.
	 * @param function function to convert
	 * @param <X> input first function
	 * @param <Y> input second function
	 * @param <Z> output second function
	 * @return curried function
	 */
	public static <X, Y, Z> Function<X, Function<Y, Z>> curry(
			final BiFunction<X, Y, Z> function) {
		return (final X x) -> (final Y y) -> function.apply(x, y);
	}

}
