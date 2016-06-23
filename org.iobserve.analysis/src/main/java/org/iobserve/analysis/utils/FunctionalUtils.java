package org.iobserve.analysis.utils;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class FunctionalUtils {

	private FunctionalUtils() {
		// no implementation
	}

	/**
	 * Convert the given two parameter function into a curry equivalent one.
	 * @param function function to convert
	 * @return curried function
	 */
	public static <X, Y, Z> Function<X, Function<Y, Z>> curry(final BiFunction<X, Y, Z> function) {
		return (final X x) -> (final Y y) -> function.apply(x, y);
	}

}
