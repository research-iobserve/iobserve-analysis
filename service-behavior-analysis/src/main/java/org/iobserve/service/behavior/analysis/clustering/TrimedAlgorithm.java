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
package org.iobserve.service.behavior.analysis.clustering;

import mtree.DistanceFunction;

/**
 * An implementation of the trimed algorithm. The algorithm is defined in the paper "A Sub-Quadratic
 * Exact Medoid Algorithm"
 *
 * @author Lars Jürgensen
 *
 * @param <T>
 *            the type of the Clustered Elements
 */
public class TrimedAlgorithm<T> {
    private final double[] lowerBounds;

    private double lowestEnergy = Double.MAX_VALUE;
    private T bestCandidate = null;

    private final T[] models;

    private final DistanceFunction<T> dm;

    public TrimedAlgorithm(final T[] models, final DistanceFunction<T> dm) {
        this.lowerBounds = new double[models.length];
        this.models = models;
        this.dm = dm;
    }

    public T calculate() {
        for (int i = 0; i < this.models.length; i++) {
            if (this.lowerBounds[i] < this.lowestEnergy) {

                final double[] distances = new double[this.models.length];

                double distanceSum = 0;
                for (int j = 0; j < this.models.length; j++) {
                    distances[j] = this.dm.calculate(this.models[i], this.models[j]);
                    distanceSum += distances[j];
                }

                this.lowerBounds[i] = distanceSum / (this.models.length - 1);

                if (this.lowerBounds[i] < this.lowestEnergy) {
                    this.lowestEnergy = this.lowerBounds[i];
                    this.bestCandidate = this.models[i];
                }

                for (int j = 0; j < this.models.length; j++) {
                    Math.max(this.lowerBounds[j], Math.abs(this.lowerBounds[i] - distances[j]));
                }
            }
        }
        return this.bestCandidate;
    }
}
