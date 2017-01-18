/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Enhancement of {@link Optional} for the sake of better readability.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 * @param <T>
 *            type
 */
public final class Opt<T> {

	/** immutable optional. */
	private Optional<T> optional = null;

	/**
	 * Actual logic to do the if-else dispatch in case the value is available.
	 */
	private final BiFunction<Consumer<T>, Runnable, Void> ifPresent = (present, notPresent) -> {
		if (this.optional.isPresent()) {
			present.accept(this.optional.get());
		} else {
			notPresent.run();
		}
		return null;
	};

	/**
	 * Actual logic to do the if-else dispatch in case the value is NOT
	 * available.
	 */
	private final BiFunction<Runnable, Consumer<T>, Void> ifNotPresent = (notPresent, present) -> {
		if (!this.optional.isPresent()) {
			notPresent.run();
		} else {
			present.accept(this.optional.get());
		}
		return null;
	};

	/**
	 * Private constructor. Use the static methods to create Opt instances.
	 *
	 * @param theOptional
	 *            optional instance
	 */
	private Opt(final Optional<T> theOptional) {
		this.optional = theOptional;
	}

	/**
	 * Just a empty method which is doing nothing.
	 *
	 * @param t
	 *            value, not relevant can be null
	 */
	public static void doNothing(final Object t) {
		// nothing here
	}

	/**
	 * Like {@link Optional#of(Object)}.
	 *
	 * @param value
	 *            value
	 * @param <T>
	 *            type
	 * @return Opt instance
	 */
	public static <T> Opt<T> of(final T value) {
		return new Opt<>(Optional.of(value));
	}

	/**
	 * Use the value of the optional.
	 *
	 * @param optional
	 *            optional
	 * @param <T>
	 *            type
	 * @return Opt instance
	 */
	public static <T> Opt<T> of(final Optional<T> optional) {
		return new Opt<>(optional);
	}

	/**
	 * Like {@link Optional#ofNullable(Object)}.
	 *
	 * @param value
	 *            value
	 * @param <T>
	 *            type
	 * @return Opt instance
	 */
	public static <T> Opt<T> ofNullable(final T value) {
		return new Opt<>(Optional.ofNullable(value));
	}

	/**
	 * Like {@link Optional#empty()}.
	 *
	 * @param <T>
	 *            type
	 * @return empty Opt instance
	 */
	public static <T> Opt<T> empty() {
		return new Opt<>(Optional.empty());
	}

	/**
	 * Get the optional instance used internally.
	 *
	 * @return optional instance.
	 */
	public Optional<T> optional() {
		return this.optional;
	}

	/**
	 * @return function like {@link Function} but with a "cosmatically"
	 *         decorated apply function.
	 */
	public IFkt<Consumer<T>, IFkt<Runnable, Void>> ifPresent() {
		return Opt.curry(this.ifPresent);
	}

	/**
	 * @return function like {@link Function} but with a "cosmatically"
	 *         decorated apply function.
	 */
	public IFkt<Runnable, IFkt<Consumer<T>, Void>> ifNotPresent() {
		return Opt.curry(this.ifNotPresent);
	}

	/**
	 * Function to transform a {@link BiFunction} using curring in two function
	 * one parameter each.
	 *
	 * @param function
	 *            bi-function
	 * @param <X>
	 *            1. input type
	 * @param <Y>
	 *            2. input type
	 * @param <Z>
	 *            output type
	 * @return curried bi-function
	 */
	private static <X, Y, Z> IFkt<X, IFkt<Y, Z>> curry(final BiFunction<X, Y, Z> function) {
		return (final X x) -> (final Y y) -> function.apply(x, y);
	}

	/**
	 * Little "cosmetically" enhanced {@link Function} interface.
	 *
	 * @author Robert Heinrich
	 * @author Alessandro Giusa
	 *
	 * @param <T>
	 *            input
	 * @param <R>
	 *            return
	 */
	@FunctionalInterface
	public interface IFkt<T, R> extends Function<T, R> {

		/**
		 * Just the same as {@link #apply(Object)}. Butter readability when used
		 * with {@link Opt#ifPresent()}. <br>
		 * <br>
		 *
		 * <b>Terminal action:</b> If this or {@link #apply(Object)} is not
		 * called, nothing will happen.
		 *
		 * @param t
		 *            input
		 * @return return value. Probably null!
		 */
		default R elseApply(final T t) {
			return this.apply(t);
		}
	}

}
