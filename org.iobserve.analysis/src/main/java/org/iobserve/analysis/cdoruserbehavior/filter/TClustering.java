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

import java.util.Optional;

import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.IClustering;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import weka.core.Instances;

/**
 * aggregate the given user behavior
 *
 * @author Christoph Dornieden
 */

public class TClustering extends AbstractConsumerStage<Instances> {
    private final OutputPort<Instances> outputPort = this.createOutputPort();
    private final IClustering clustering;

    /**
     * constructor
     *
     * @param clustering
     *            clustering used
     */
    public TClustering(IClustering clustering) {
        this.clustering = clustering;
    }

    @Override
    protected void execute(Instances instances) {
        final Optional<Instances> centroids = this.clustering.getClusterCenters(instances);

        if (centroids.isPresent()) {
            this.outputPort.send(centroids.get());
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
