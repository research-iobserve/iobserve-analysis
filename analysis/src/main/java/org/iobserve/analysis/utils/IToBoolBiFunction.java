/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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

import java.util.function.BiFunction; // NOCS

/**
 * Represents a function that accepts two arguments and produces an boolean-valued result. This is
 * the {@code boolean}-producing primitive specialization for {@link BiFunction}.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose functional method is
 * {@link #applyAsBool(Object, Object)}.
 *
 * @param <T>
 *            the type of the first argument to the function
 * @param <U>
 *            the type of the second argument to the function
 *
 * @see BiFunction
 *
 * @author Alessandro Giusa
 */
@FunctionalInterface
public interface IToBoolBiFunction<T, U> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t
     *            the first function argument
     * @param u
     *            the second function argument
     * @return the function result
     */
    boolean applyAsBool(final T t, U u);

}
