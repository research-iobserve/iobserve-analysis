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
package org.iobserve.analysis.clustering.filter;

import java.util.List;
import java.util.Map;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.eclipse.net4j.util.collection.Pair;
import org.iobserve.analysis.clustering.XMeansClustering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Picks one {@link Instance} of every cluster. Similar to the centroids of the
 * {@link XMeansClustering}.
 *
 * @author Marc Adolf
 *
 */
public class ClusterMerger extends AbstractConsumerStage<Map<Integer, List<Pair<Instance, Double>>>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterMerger.class);

    private final OutputPort<Instances> outputPort = this.createOutputPort();

    public ClusterMerger() { // NOCS empty constructor
        // empty constructor
    }

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractConsumerStage#execute(java.lang.Object)
     */
    @Override
    protected void execute(final Map<Integer, List<Pair<Instance, Double>>> clustering) throws Exception {
        /**
         * simply pick the first instance of every cluster lookup attributes to build a new
         * instances Object
         */
        Instance instance = clustering.entrySet().iterator().next().getValue().get(0).getElement1();
        final FastVector attributes = new FastVector();
        for (int j = 0; j < instance.numAttributes(); j++) {
            attributes.addElement(instance.attribute(j));
        }

        final Instances result = new Instances("Clustering Result", attributes, clustering.size());

        for (final List<Pair<Instance, Double>> entry : clustering.values()) {
            if (!entry.isEmpty()) {
                instance = entry.get(0).getElement1();
                result.add(instance);
            }
        }
        //this.printInstances(result);
        this.outputPort.send(result);
    }

    public OutputPort<Instances> getOutputPort() {
        return this.outputPort;
    }

    private void printInstances(final Instances instances) {
        for (int i = 0; i < instances.numInstances(); i++) {
            String logString = "";
            logString += "***************************";
            logString += "Cluster " + i;
            logString += "***************************";
            final Instance instance = instances.instance(i);
            for (int a = 0; a < instance.numAttributes(); a++) {
                logString += instances.attribute(a).name() + " : " + instance.value(a);
            }
            ClusterMerger.LOGGER.info(logString);
        }
    }
}
