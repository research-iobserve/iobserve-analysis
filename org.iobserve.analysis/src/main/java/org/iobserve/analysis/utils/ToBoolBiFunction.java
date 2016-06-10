package org.iobserve.analysis.utils;

import java.util.function.BiFunction;

/**
 * Represents a function that accepts two arguments and produces an boolean-valued
 * result.  This is the {@code boolean}-producing primitive specialization for
 * {@link BiFunction}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #applyAsBool(Object, Object)}.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 *
 * @see BiFunction
 */
@FunctionalInterface
public interface ToBoolBiFunction<T, U> {
	
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
	boolean applyAsBool(final T t, U u);

}
