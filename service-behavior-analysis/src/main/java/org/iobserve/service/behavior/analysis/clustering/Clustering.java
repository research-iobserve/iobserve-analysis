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

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class Clustering {

    private Set<BehaviorModelGED> noise = new HashSet<>();

    private Set<Set<BehaviorModelGED>> clusters = new HashSet<>();

    public Set<BehaviorModelGED> getNoise() {
        return this.noise;
    }

    public void setNoise(final Set<BehaviorModelGED> noise) {
        this.noise = noise;
    }

    public Set<Set<BehaviorModelGED>> getClusters() {
        return this.clusters;
    }

    public void setClusters(final Set<Set<BehaviorModelGED>> clusters) {
        this.clusters = clusters;
    }

    public void addCluster(final Set<BehaviorModelGED> cluster) {
        this.clusters.add(cluster);
    }
}
