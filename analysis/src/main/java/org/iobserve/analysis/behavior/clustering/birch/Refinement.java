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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.clustering.birch.model.ClusteringFeature;

import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Melf Lorenzen
 * Implements the 4. phase of the
 * birch algorithm. The original
 * instances are clustered around
 * the centroids found through
 * previous clustering.
 */
public class Refinement extends AbstractConsumerStage<Object> {
   
    private final OutputPort<Instances> outputPort = this.createOutputPort();
    
	private Instances instances;
	
    private List<ClusteringFeature> clustering;
    	
	@SuppressWarnings("unchecked")
	@Override
	protected void execute(final Object object) throws Exception {
        if (object instanceof Instances) {
            this.instances = (Instances) object;
        } else if (object instanceof List<?>) {
            this.clustering = (List<ClusteringFeature>) object;
        }
        
        if (this.clustering != null && this.instances != null) {
        	refineClustering();
        }
	}
	
	private void refineClustering() {
		final Map<ClusteringFeature, ClusteringFeature> finalClustering = new HashMap<>();
		for (ClusteringFeature cf : this.clustering) {
			finalClustering.put(cf, new ClusteringFeature(cf.getDimension()));
		}
		for (int i = 0; i < this.instances.numInstances(); i++) {
			final ClusteringFeature cf = new ClusteringFeature(this.instances.instance(i));
			/** find cfs NN in clustering */
			final NavigableMap<Double, ClusteringFeature> ranking = new TreeMap<>();
			for (ClusteringFeature centroid : this.clustering) {
					ranking.put(centroid.compare(cf), centroid);
			}
			final ClusteringFeature nearestNeighbor = ranking.firstEntry().getValue();
			/** add to correct cluster */
			finalClustering.get(nearestNeighbor).add(cf);
		}
    	instances.delete();
    	for (ClusteringFeature cf : finalClustering.values()) {
    		instances.add(new Instance(1.0, cf.getCentroid()));
    	}
    	this.outputPort.send(instances);
	}
    
	public OutputPort<Instances> getOutputPort() {
		return outputPort;
	}

}
