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
