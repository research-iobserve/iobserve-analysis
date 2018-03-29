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

package org.iobserve.analysis.clustering.birch;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.clustering.birch.model.CFTree;
import org.iobserve.analysis.clustering.birch.model.ClusteringFeature;
import org.iobserve.analysis.clustering.birch.model.ICFComparisonStrategy;
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

public class BuildCFTree extends AbstractConsumerStage<Instances> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildCFTree.class);
    private CFTree tree;
    private Instances instances;
    private final OutputPort<CFTree> outputPort = this.createOutputPort();
	private double threshold;
    private int maxLeafSize;
    private int maxNodeSize;
    private ICFComparisonStrategy clusterComparisonStrategy;

    /**
     * constructor.
     */
    public BuildCFTree(double threshold, int maxLeafSize, int maxNodeSize,
    		ICFComparisonStrategy clusterComparisonStrategy) {
		super();
		this.threshold = threshold;
		this.maxLeafSize = maxLeafSize;
		this.maxNodeSize = maxNodeSize;
		this.clusterComparisonStrategy = clusterComparisonStrategy;
	}

    @Override
    protected void execute(final Instances instances) {
    	this.instances = instances;
    	
    	if(this.tree == null)
    		this.tree = new CFTree(this.threshold, this.maxLeafSize, this.maxNodeSize, 
    				instances.numAttributes(), this.clusterComparisonStrategy);
    	   	
    	//BuildCFTree.LOGGER.debug("Number of instances: " + instances.numAttributes());
    	//BuildCFTree.LOGGER.debug("First instance: ");
    	BuildCFTree.LOGGER.debug("Start building tree...");
    	for(int i = 0; i < instances.numAttributes(); i++) {
    		BuildCFTree.LOGGER.debug(instances.attribute(i).toString());
    	}
    	for(int j = 0; j < instances.numInstances(); j++) {
    		Double[] vector = new Double[instances.numAttributes()];
    		if(j == 5 || j == 25 || j == 50 || j == 75 || j == 100) {
    	    	BuildCFTree.LOGGER.debug("Random vector:");
    	    	for(int i = 0; i < instances.numAttributes(); i++) {
    	    		vector[i] = instances.instance(j).value(i);
    	    		BuildCFTree.LOGGER.debug(vector[i].toString());
    	    	}	
    		}

    		ClusteringFeature cf = new ClusteringFeature(instances.instance(j));
	    	this.tree.insert(cf);
    	}
    	this.tree.updateLeafChain();
    	BuildCFTree.LOGGER.debug("Done building tree.");
    }

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractStage#onTerminating()
     */
    @Override
    public void onTerminating() {
        if (this.instances == null) {
        	BuildCFTree.LOGGER.error("No instances created!");
        } else {
            this.outputPort.send(this.tree);
        }
        //BuildCFTree.LOGGER.debug(this.tree.toString());
        super.onTerminating();
    }

    public OutputPort<CFTree> getOutputPort() {
        return this.outputPort;
    }

}
