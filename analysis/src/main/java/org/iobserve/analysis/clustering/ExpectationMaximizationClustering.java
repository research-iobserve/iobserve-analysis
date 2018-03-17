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
package org.iobserve.analysis.clustering;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.net4j.util.collection.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.clusterers.EM;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Marc Adolf
 *
 */
public class ExpectationMaximizationClustering implements IDensitityClustering {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpectationMaximizationClustering.class);

    public ExpectationMaximizationClustering() { // NOCS empty constructor
        // empty constructor
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.iobserve.analysis.cdoruserbehavior.clustering.IClustering#clusterInstances(weka.core.
     * Instances)
     */
    @Override
    public Map<Integer, List<Pair<Instance, Double>>> clusterInstances(final Instances instances) {
        final EM emClustering = new EM();
        if (ExpectationMaximizationClustering.LOGGER.isInfoEnabled()) {
            ExpectationMaximizationClustering.LOGGER
                    .info("Computing the EM-Clustering with following options: " + emClustering.getOptions());
        }
        final Map<Integer, List<Pair<Instance, Double>>> resultMap = new HashMap<>(); // NOPMD
        try {
            emClustering.buildClusterer(instances);
            /**
             * iterate through all instances and bucket sort them with their probabilities to their
             * assigned cluster.
             */
            for (int i = 0; i < instances.numInstances(); i++) {
                final Instance currentInstance = instances.instance(i);
                final int cluster = emClustering.clusterInstance(currentInstance);
                final double probability = emClustering.distributionForInstance(currentInstance)[cluster];
                if (resultMap.get(cluster) == null) {
                    resultMap.put(cluster, new LinkedList<Pair<Instance, Double>>());
                }
                resultMap.get(cluster).add(new Pair<>(currentInstance, probability));

            }
        } catch (final Exception e) { // NOPMD NOCS api induced
            ExpectationMaximizationClustering.LOGGER.error("Clustering failed.", e);
        }
        return resultMap;
    }

}
