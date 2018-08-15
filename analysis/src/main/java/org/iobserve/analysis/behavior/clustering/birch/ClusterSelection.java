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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.clustering.birch.model.ClusteringFeature;

/**
 * @author Melf Lorenzen Selects a clustering out of all the clustering arriving at this stage.
 *         Calculates correct cluster size based on l-method.
 */
public class ClusterSelection extends AbstractConsumerStage<List<ClusteringFeature>> {
    private final OutputPort<List<ClusteringFeature>> outputPort = this.createOutputPort();
    private int expectedNumberOfClusters = 1;
    private final List<List<ClusteringFeature>> list = new ArrayList<>();
    private final SortedMap<Double, Double> evaluationGraph = new TreeMap<>();
    private final boolean useClusterNumberMetric;
    private final ILMethodEvalStrategy evalStrategy;

    /**
     * Constructor for the ClusterSelection Stage.
     *
     * @param expectedNumberOfClusters
     *            the expected number of clusters
     * @param useClusterNumberMetric
     *            flag whether to use the expectedNumberOfClusters or use the internal algorithm to
     *            select a clustering
     * @param evalStrategy
     *            the strategy for the l-method evaluation graph
     */
    public ClusterSelection(final int expectedNumberOfClusters, final boolean useClusterNumberMetric,
            final ILMethodEvalStrategy evalStrategy) {
        this.useClusterNumberMetric = useClusterNumberMetric;
        this.expectedNumberOfClusters = expectedNumberOfClusters;
        this.evalStrategy = evalStrategy;
    }

    @Override
    protected void execute(final List<ClusteringFeature> element) throws Exception {
        this.list.add(element);
        this.evaluationGraph.put(element.size() * 1.0, this.evalStrategy.calculateClusterMetric(element));
        if (element.size() == 2) {
            this.sendResult();
        }
    }

    private void sendResult() {
        if (this.useClusterNumberMetric) {
            this.expectedNumberOfClusters = this.calculateClusterNumber();
        }
        final Optional<List<ClusteringFeature>> result = this.list.stream()
                .filter(clustering -> clustering.size() >= this.expectedNumberOfClusters).findFirst();
        if (result.isPresent()) {
            this.outputPort.send(result.get());
        }
    }

    private int lmethod(final double cutOff) {
        final SortedMap<Double, Double> partialGraph = this.evaluationGraph.subMap(2.0, cutOff);
        final NavigableMap<Double, Integer> ranking = new TreeMap<>();
        final double b = partialGraph.lastKey();
        for (int i = 3; i < partialGraph.size(); i++) {
            final double rmse = (i - 1.0) / (b - 1.0) * this.calculateRMSE(partialGraph.subMap(2.0, i + 1.0).entrySet())
                    + (b - i) / (b - 1.0) * this.calculateRMSE(partialGraph.subMap(i + 1.0, b + 1.0).entrySet());
            ranking.put(rmse, i);
        }
        return ranking.firstEntry().getValue();
    }

    private int calculateClusterNumber() {
        if (this.evaluationGraph.size() < 4) {
            return (this.evaluationGraph.size() + 1) / 2;
        } else {
            double cutOff = this.evaluationGraph.lastKey() + 1.0;
            int lastKnee = 0;
            int currentKnee = this.evaluationGraph.size();
            do {
                lastKnee = currentKnee;
                currentKnee = this.lmethod(cutOff);
                cutOff = currentKnee * 2.0;
            } while (currentKnee >= lastKnee);
            return currentKnee;
        }
    }

    private double calculateRMSE(final Set<Entry<Double, Double>> graph) {
        double avgMetric = 0;
        double avgSize = 0;
        double rmse = 0;

        for (final Entry<Double, Double> entry : graph) {
            avgSize += entry.getKey();
            avgMetric += entry.getValue();
        }

        if (!graph.isEmpty()) {
            avgMetric /= this.evaluationGraph.size();
            avgSize /= this.evaluationGraph.size();
        }

        double a = 0.0;
        double b = 0.0;
        for (final Entry<Double, Double> pair : graph) {
            b += (pair.getKey() - avgSize) * (pair.getValue() - avgMetric);
            a += Math.pow(pair.getKey() - avgSize, 2);
        }

        b = a > 0 ? b / a : b;
        a = avgMetric - b * avgSize;

        for (final Entry<Double, Double> pair : graph) {
            rmse += Math.pow(pair.getValue() - (a + b * pair.getKey()), 2);
        }
        rmse = graph.isEmpty() ? 0.0 : rmse / graph.size();
        rmse = Math.sqrt(rmse);

        return rmse;
    }

    public OutputPort<List<ClusteringFeature>> getOutputPort() {
        return this.outputPort;
    }
}
