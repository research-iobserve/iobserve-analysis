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
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.clustering.birch.model.ClusteringFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ClusterSelection extends AbstractConsumerStage<List<ClusteringFeature>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterSelection.class);
	private final OutputPort<List<ClusteringFeature>> outputPort = this.createOutputPort();
	private int expectedNumberOfClusters = 1;
	private final List<List<ClusteringFeature>> list = new ArrayList<>();
    private final SortedMap<Double, Double> evaluationGraph = new TreeMap<>();
    private final boolean useClusterNumberMetric;
   
	/**Constructor for the ClusterSelection Stage.
	 * @param expectedNumberOfClusters the expected number of clusters
	 * @param useClusterNumberMetric flag whether to use the expectedNumberOfClusters or use the internal 
	 * algorithm to select a clustering
	 */
	public ClusterSelection(final int expectedNumberOfClusters, final boolean useClusterNumberMetric) {
		this.useClusterNumberMetric = useClusterNumberMetric;
		this.expectedNumberOfClusters = expectedNumberOfClusters;
	}
    
    //Abschicken wenn cluster mit size 2 kommt
    
	@Override
	protected void execute(List<ClusteringFeature> element) throws Exception {
		ClusterSelection.LOGGER.debug("Received clustering of size " + element.size());
		this.list.add(element);
		this.evaluationGraph.put(element.size() * 1.0, calculateClusterMetricRadius(element));
		if(element.size() == 2) 
			this.sendResult();
	}
	
	private void sendResult() {

		ClusterSelection.LOGGER.debug("Average weighted sqare Radius/Diameter:");
		int calcNumber = this.calculateClusterNumber();
		
		ClusterSelection.LOGGER.debug("SSEs:");
		for(List<ClusteringFeature> ls : this.list) {
			double sse = 0;
			for(ClusteringFeature cf : ls)
				sse += cf.getSquareSumError();
			if(ls.size() == this.expectedNumberOfClusters || ls.size() == calcNumber)
			ClusterSelection.LOGGER.debug("(" + ls.size() + ", " + sse + ")");
		}
		ClusterSelection.LOGGER.debug("WACRS:");
		for(List<ClusteringFeature> ls : this.list) {
			double wacrs = 0;
			if(ls.size() == this.expectedNumberOfClusters || ls.size() == calcNumber)
			ClusterSelection.LOGGER.debug("(" + ls.size() + ", " 
			+ calculateClusterMetricRadius(ls) + ")");
		}
		ClusterSelection.LOGGER.debug("WACDS:");
		for(List<ClusteringFeature> ls : this.list) {
			double wacrs = 0;
			if(ls.size() == this.expectedNumberOfClusters || ls.size() == calcNumber)
			ClusterSelection.LOGGER.debug("(" + ls.size() + ", " 
			+ calculateClusterMetricDiameter(ls) + ")");
		}
		if(this.useClusterNumberMetric) {
			this.expectedNumberOfClusters = this.calculateClusterNumber();
		}
		ClusterSelection.LOGGER.debug("Optimal number of clusters: " + this.calculateClusterNumber());
		Optional<List<ClusteringFeature>> result= this.list.stream().filter(
				clustering -> clustering.size() == this.expectedNumberOfClusters).findFirst();
		if(result.isPresent())
			this.outputPort.send(result.get());
	}
	
	private double calculateClusterMetricRadius(List<ClusteringFeature> cluster) {
		double sum = 0.0;
		double cnt = 0.0;
		
		for(ClusteringFeature cf : cluster) {
			sum += cf.getNumber() * Math.pow(cf.getRadius(), 2);
			cnt += cf.getNumber();
		}
			
		return cnt != 0.0 ? sum/cnt : 0;
	}
	
	private double calculateClusterMetricDiameter(List<ClusteringFeature> cluster) {
		double sum = 0.0;
		double cnt = 0.0;
		
		for(ClusteringFeature cf : cluster) {
			sum += (cf.getNumber() * (cf.getNumber() - 1)) * Math.pow(cf.getDiameter(), 2);
			cnt += (cf.getNumber() * (cf.getNumber() - 1));
		}
			
		return cnt != 0.0 ? sum/cnt : 0;
	}
	
	private int LMethod(double cutOff) {
		SortedMap<Double, Double> partialGraph = this.evaluationGraph.subMap(2.0, cutOff);
		TreeMap<Double, Integer> ranking = new TreeMap<>();
		double b = partialGraph.lastKey();
		for(int i = 3; i < partialGraph.size(); i++) {
			double rmse = (i - 1.0) / (b - 1.0) 
					* calculateRMSE(partialGraph.subMap(2.0, i + 1.0).entrySet())
					+ (b - i) / (b - 1.0)
					* calculateRMSE(partialGraph.subMap(i + 1.0, b + 1.0).entrySet());
			ranking.put(rmse, i);
		}
		return ranking.firstEntry().getValue();
	}
	
	private int calculateClusterNumber() {
		int i = 0;
		if(this.evaluationGraph.size() < 4) {
			return (this.evaluationGraph.size() + 1) / 2;
		} else {
				double cutOff = this.evaluationGraph.lastKey() + 1.0;
				int lastKnee = 0;
				int currentKnee = this.evaluationGraph.size();
			do {
				i++;
				lastKnee = currentKnee;
				currentKnee = LMethod(cutOff);
				cutOff = currentKnee * 2.0;
			} while(currentKnee >= lastKnee);
			ClusterSelection.LOGGER.debug("LMETHOD: Number of iterations: " + i) ;
			ClusterSelection.LOGGER.debug("LMETHOD: CutOff " + ((int) cutOff)) ;
			return currentKnee;
		}
	}
	
	
	
	private double calculateRMSE(final Set<Entry<Double, Double>> graph) {
		double avgMetric = 0;
		double avgSize = 0;
		double rmse = 0;
		
		for(Entry<Double, Double> entry : graph) {
			avgSize += entry.getKey();
			avgMetric += entry.getValue();
		}
		
		if(graph.size() > 0) {
			avgMetric /= this.evaluationGraph.size();
			avgSize /= this.evaluationGraph.size();
		}

		double a = 0.0;
		double b = 0.0;
		for(Entry<Double, Double> pair : graph) {
			b += (pair.getKey() - avgSize) * (pair.getValue() - avgMetric);
			a += Math.pow((pair.getKey() - avgSize), 2);
		}
		
		b = a > 0 ? b / a : b;
		a = avgMetric - b * avgSize;
		
		for(Entry<Double, Double> pair : graph) {
			rmse += Math.pow(pair.getValue() - (a + b*pair.getKey()), 2);
		}
		rmse = graph.size() > 0 ? rmse / graph.size() : 0.0; 
		rmse = Math.sqrt(rmse);
		
		return rmse; 
	}
	
    public OutputPort<List<ClusteringFeature>> getOutputPort() {
        return this.outputPort;
    }
}
