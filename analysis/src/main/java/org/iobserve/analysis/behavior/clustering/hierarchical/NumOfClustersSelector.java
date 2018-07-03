/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.behavior.clustering.hierarchical;

import java.util.List;
import java.util.Map;

import org.eclipse.net4j.util.collection.Pair;

import weka.core.Instance;
import weka.core.SelectedTag;
import weka.core.Tag;

/**
 * @author Stephan Lenga
 *
 */
public class NumOfClustersSelector {

    private static final int ELBOW = 0;
    private static final int GAP_STATISTIC = 1;
    public static final Tag[] STRATEGY_TYPE = { new Tag(NumOfClustersSelector.ELBOW, "ELBOW"),
            new Tag(NumOfClustersSelector.GAP_STATISTIC, "GAP_STATISTIC"), };

    private final Map<Integer, List<Pair<Instance, Double>>> initialCluster;
    private int strategy;

    /**
     *
     * @param initialCluster
     *            single cluster which contains all the data
     * @param strategy
     *            strategy which will be used to find the optimal number of clusters for the
     *            initialCluster
     */
    public NumOfClustersSelector(final Map<Integer, List<Pair<Instance, Double>>> initialCluster, final int strategy) {
        this.initialCluster = initialCluster;
        this.strategy = strategy;
    }

    public void setLinkType(final SelectedTag newLinkType) {
        if (newLinkType.getTags() == NumOfClustersSelector.STRATEGY_TYPE) {
            this.strategy = newLinkType.getSelectedTag().getID();
        }
    }

}
