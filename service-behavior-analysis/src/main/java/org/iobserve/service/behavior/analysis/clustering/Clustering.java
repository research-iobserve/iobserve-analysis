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

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Lars Jürgensen
 *
 * @param <T>
 *            The type of the data in the clusters
 */
public class Clustering<T> {

    private Set<T> noise = new HashSet<>();

    private Set<Set<T>> clusters = new HashSet<>();

    public Set<T> getNoise() {
        return this.noise;
    }

    public void setNoise(final Set<T> noise) {
        this.noise = noise;
    }

    public Set<Set<T>> getClusters() {
        return this.clusters;
    }

    public void setClusters(final Set<Set<T>> clusters) {
        this.clusters = clusters;
    }

    public void addCluster(final Set<T> cluster) {
        this.clusters.add(cluster);
    }
}
