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
import java.util.List;
import java.util.Set;

import teetime.stage.basic.AbstractTransformation;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The algorithm extracts clusters, which are equivalent to DBScan clusters from the OPTICS plot.
 * The algorithm was proposed in the optics paper.
 * 
 * @author Lars Jürgensen
 *
 */
public class ExtractDBScanClusters extends AbstractTransformation<List<OpticsData>, Clustering<BehaviorModelGED>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtractDBScanClusters.class);

    private final double clusteringDistance;

    public ExtractDBScanClusters(final double clusteringDistance) {
        this.clusteringDistance = clusteringDistance;
    }

    @Override
    protected void execute(final List<OpticsData> opticsResults) throws Exception {

        ExtractDBScanClusters.LOGGER.info("received optics result");
        for (final OpticsData model : opticsResults) {
            ExtractDBScanClusters.LOGGER.debug(Double.toString(model.getReachabilityDistance()) + " and core: "
                    + Double.toString(model.getCoreDistance()));
        }
        final Clustering<BehaviorModelGED> clustering = new Clustering<>();

        Set<BehaviorModelGED> currentCluster = clustering.getNoise();

        for (final OpticsData model : opticsResults) {
            if ((model.getReachabilityDistance() == OpticsData.UNDEFINED)
                    || (model.getReachabilityDistance() > this.clusteringDistance)) {
                if ((model.getCoreDistance() <= this.clusteringDistance)
                        && (model.getCoreDistance() != OpticsData.UNDEFINED)) {
                    final Set<BehaviorModelGED> newCluster = new HashSet<>();
                    clustering.addCluster(newCluster);
                    newCluster.add(model.getData());
                    currentCluster = newCluster;
                } else {
                    clustering.getNoise().add(model.getData());
                }
            } else {
                currentCluster.add(model.getData());
            }
        }
        ExtractDBScanClusters.LOGGER.info("generated " + clustering.getClusters().size() + " clusters");

        this.getOutputPort().send(clustering);
    }
}
