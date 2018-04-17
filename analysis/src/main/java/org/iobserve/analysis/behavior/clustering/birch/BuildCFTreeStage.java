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

package org.iobserve.analysis.behavior.clustering.birch;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.clustering.birch.model.CFTree;
import org.iobserve.analysis.behavior.clustering.birch.model.ClusteringFeature;
import org.iobserve.analysis.behavior.clustering.birch.model.ICFComparisonStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.Instances;

/**
 * Transforms BehaviorModelTable into weka instances. Adds all behavior model elements to the
 * instances and pass them to the output port on termination.
 *
 * @author Melf Lorenzen
 *
 */

public class BuildCFTreeStage extends AbstractConsumerStage<Instances> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildCFTreeStage.class);
    private CFTree tree;
    private Instances instances;
    private final OutputPort<CFTree> outputPort = this.createOutputPort();
    private final double threshold;
    private final int maxLeafSize;
    private final int maxNodeSize;
    private final ICFComparisonStrategy clusterComparisonStrategy;

    /**
     * Constructor for the BuildCFTree stage of the birch algorithm.
     *
     * @param threshold
     *            the merge threshold for the underlying cf tree
     * @param maxLeafSize
     *            the maximum number of entries in a leaf
     * @param maxNodeSize
     *            the maximum number of entries in a node
     * @param clusterComparisonStrategy
     *            the cluster comparison strategy
     */
    public BuildCFTreeStage(final double threshold, final int maxLeafSize, final int maxNodeSize,
            final ICFComparisonStrategy clusterComparisonStrategy) {
        super();
        this.threshold = threshold;
        this.maxLeafSize = maxLeafSize;
        this.maxNodeSize = maxNodeSize;
        this.clusterComparisonStrategy = clusterComparisonStrategy;
    }

    @Override
    protected void execute(final Instances instancesToCluster) {
        this.instances = instancesToCluster;
        BuildCFTreeStage.LOGGER.debug("Received {} of dimension {}", instancesToCluster.numInstances(),
                instancesToCluster.numAttributes());
        if (this.tree == null) {
            this.tree = new CFTree(this.threshold, this.maxLeafSize, this.maxNodeSize,
                    instancesToCluster.numAttributes(), this.clusterComparisonStrategy);
        }

        for (int j = 0; j < instancesToCluster.numInstances(); j++) {
            // final Double[] vector = new Double[instancesToCluster.numAttributes()];
            // if (j == 5 || j == 25 || j == 50 || j == 75 || j == 100) {
            // for (int i = 0; i < instancesToCluster.numAttributes(); i++) {
            // vector[i] = instancesToCluster.instance(j).value(i);
            // BuildCFTree.LOGGER.debug(instances.attribute(j).toString());
            // }
            // }

            final ClusteringFeature cf = new ClusteringFeature(instancesToCluster.instance(j));
            this.tree.insert(cf);
        }
        this.tree.updateLeafChain();
    }

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractStage#onTerminating()
     */
    @Override
    public void onTerminating() {
        if (this.instances == null) {
            BuildCFTreeStage.LOGGER.error("No instances created!");
        } else {
            BuildCFTreeStage.LOGGER.debug(this.tree.toString());
            this.outputPort.send(this.tree);
        }
        super.onTerminating();
    }

    public OutputPort<CFTree> getOutputPort() {
        return this.outputPort;
    }

}
