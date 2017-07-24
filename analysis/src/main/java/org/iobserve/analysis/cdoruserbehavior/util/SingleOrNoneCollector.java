/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.cdoruserbehavior.util;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Collector for one or none element streams. Throws a exeption if the stream has to many elements
 *
 * @author Christoph Dornieden
 *
 * @param <T> 
 */
public class SingleOrNoneCollector<T> implements Collector<T, SingleOrNoneAccumulator<T>, Optional<T>> {

    @Override
    public BiConsumer<SingleOrNoneAccumulator<T>, T> accumulator() {
        return (a, t) -> a.add(t);
    }

    @Override
    public Set<java.util.stream.Collector.Characteristics> characteristics() {
        // TODO Auto-generated method stub
        return EnumSet.of(Characteristics.UNORDERED);

    }

    @Override
    public BinaryOperator<SingleOrNoneAccumulator<T>> combiner() {
        return (a, b) -> {
            throw new IllegalStateException("Expected single element but found multiple elements: " + a + ", " + b);
        };
    }

    @Override
    public Function<SingleOrNoneAccumulator<T>, Optional<T>> finisher() {
        return a -> a.get();
    }

    @Override
    public Supplier<SingleOrNoneAccumulator<T>> supplier() {
        return () -> new SingleOrNoneAccumulator<>();
    }
}
