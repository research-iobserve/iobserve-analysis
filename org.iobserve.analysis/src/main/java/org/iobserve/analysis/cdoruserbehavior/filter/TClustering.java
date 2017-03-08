/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ***************************************************************************/
package org.iobserve.analysis.cdoruserbehavior.filter;

import java.util.Random;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import weka.clusterers.XMeans;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.NormalizableDistance;

/**
 * @author Christoph Dornieden
 */

public class TClustering extends AbstractConsumerStage<Instances> {
    private final OutputPort<Instances> outputPort = this.createOutputPort();

    private final int minClusters;
    private final int maxClusters;

    /**
     * constructor
     *
     * @param userGroups
     *            number of user groups
     * @param variance
     *            variance
     */
    public TClustering(final int userGroups, final int variance) {
        this.minClusters = (userGroups - variance) < 2 ? 1 : userGroups - variance;
        this.maxClusters = (userGroups + variance) < 2 ? 2 : userGroups - variance;
    }

    @Override
    protected void execute(Instances instances) {

        final XMeans xMeansClusterer = new XMeans();
        xMeansClusterer.setSeed(new Random().nextInt(Integer.MAX_VALUE));

        final NormalizableDistance manhattenDistance = new ManhattanDistance();
        manhattenDistance.setDontNormalize(false);
        manhattenDistance.setInstances(instances);
        xMeansClusterer.setDistanceF(manhattenDistance);

        xMeansClusterer.setMinNumClusters(this.minClusters);
        xMeansClusterer.setMaxNumClusters(this.maxClusters);

        try {
            xMeansClusterer.buildClusterer(instances);

            final Instances centroids = xMeansClusterer.getClusterCenters();
            this.outputPort.send(centroids);

        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * getter
     *
     * @return output port
     */
    public OutputPort<Instances> getOutputPort() {
        return this.outputPort;
    }

}
