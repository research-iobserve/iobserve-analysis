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

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.clustering.birch.model.CFTree;
import org.iobserve.analysis.clustering.birch.model.ClusteringFeature;

/**
 * @author Melf Lorenzen
 * This class realizes the third phase of the birch
 * algorithm. It creates a series of clusterings on
 * the cf tree.
 * The clustering is based on a nearest neighbor
 * algorithm.
 */
public class ClusterOnTree extends AbstractConsumerStage<CFTree> { 
	private List<ClusteringFeature> clusterList;
    private final OutputPort<List<ClusteringFeature>> outputPort = this.createOutputPort();	

	@Override
	protected void execute(final CFTree tree) throws Exception {
		this.clusterList = tree.getLeafEntries();
		List<ClusteringFeature> neighborChain = new ArrayList<>();

		int i = 1;

		neighborChain.add(new ClusteringFeature(0));
		neighborChain.add(clusterList.get(0));
		
		while (clusterList.size() > 1) {
			this.outputPort.send(new ArrayList<ClusteringFeature>(this.clusterList));
			do {
				neighborChain.add(getNearestNeighbor(neighborChain.get(i)));
				i++;
			} while(!neighborChain.get(i).equals(neighborChain.get(i - 2)));
		
		/** Agglomerate clusters i, i-1 */
		this.clusterList.remove(neighborChain.get(i));
		this.clusterList.remove(neighborChain.get(i - 1));
		this.clusterList.add(new ClusteringFeature(neighborChain.get(i), neighborChain.get(i - 1)));
		
		if (i > 3) {
			i -= 3;
		} else {
			i = 1;	
		}

		neighborChain = neighborChain.subList(0, i + 1);
		}
	}


	private ClusteringFeature getNearestNeighbor(final ClusteringFeature clusteringFeature) {
		final   NavigableMap<Double, ClusteringFeature> ranking = new TreeMap<>();
		
		for (ClusteringFeature cf : this.clusterList) {
			if (!cf.equals(clusteringFeature)) {
				ranking.put(clusteringFeature.compare(cf), cf);
			}
		}
		return ranking.firstEntry().getValue();
	}
		
	public OutputPort<List<ClusteringFeature>> getOutputPort() {
		return outputPort;
	}

}
