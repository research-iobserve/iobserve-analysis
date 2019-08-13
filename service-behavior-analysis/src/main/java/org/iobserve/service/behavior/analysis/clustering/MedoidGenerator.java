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
public class MedoidGenerator extends AbstractTransformation<Clustering<BehaviorModelGED>, BehaviorModelGED> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MedoidGenerator.class);

    private final DistanceFunction<BehaviorModelGED> dm;

    public MedoidGenerator(final DistanceFunction<BehaviorModelGED> dm) {
        this.dm = dm;
    }

    @Override
    protected void execute(final Clustering<BehaviorModelGED> clustering) throws Exception {

        for (final Set<BehaviorModelGED> clusterSet : clustering.getClusters()) {

            final BehaviorModelGED[] cluster = clusterSet.toArray(new BehaviorModelGED[clusterSet.size()]);
            if (cluster.length == 0) {
                MedoidGenerator.LOGGER.warn("Empty cluster received");
                return;
            }

            final TrimedAlgorithm<BehaviorModelGED> trimed = new TrimedAlgorithm<>(cluster, this.dm);

            this.outputPort.send(trimed.calculate());
        }
        MedoidGenerator.LOGGER.info("gernerated all mediods of a clustering");

    }

}
