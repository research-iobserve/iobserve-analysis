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

import java.util.Set;

import teetime.stage.basic.AbstractTransformation;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mtree.DistanceFunction;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class NaiveMediodGenerator extends AbstractTransformation<Clustering<BehaviorModelGED>, BehaviorModelGED> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NaiveMediodGenerator.class);

    private final DistanceFunction<BehaviorModelGED> dm;

    public NaiveMediodGenerator(final DistanceFunction<BehaviorModelGED> dm) {
        this.dm = dm;
    }

    @Override
    protected void execute(final Clustering<BehaviorModelGED> clustering) throws Exception {

        for (final Set<BehaviorModelGED> clusterSet : clustering.getClusters()) {

            final BehaviorModelGED[] cluster = clusterSet.toArray(new BehaviorModelGED[clusterSet.size()]);
            if (cluster.length == 0) {
                NaiveMediodGenerator.LOGGER.warn("Empty cluster received");
                return;
            }

            BehaviorModelGED medoid = cluster[0];
            double minDistanceSum = Double.MAX_VALUE;

            for (int i = 0; i < cluster.length; i++) {
                double distanceSum = 0;

                for (int j = 0; j < cluster.length; j++) {

                    if (i != j) {
                        distanceSum += this.dm.calculate(cluster[i], cluster[j]);
                    }

                }

                if (distanceSum < minDistanceSum) {
                    minDistanceSum = distanceSum;
                    medoid = cluster[i];
                }

            }

            this.outputPort.send(medoid);
        }

        NaiveMediodGenerator.LOGGER.info("mediod generated");
    }

}
