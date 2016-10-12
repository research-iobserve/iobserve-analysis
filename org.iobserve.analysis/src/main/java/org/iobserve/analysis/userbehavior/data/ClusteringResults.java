/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.userbehavior.data;

/**
 * It contains the results of the clustering: The assignments of the clustered data to the clusters
 * and the clustering metrics.
 *
 * @author David Peter, Robert Heinrich
 */
public class ClusteringResults {

    private final String clusteringMethod;
    private final int numberOfClusters;
    private final int[] assignments;
    private final ClusteringMetrics clusteringMetrics;

    /**
     * Create a result node.
     *
     * @param clusteringMethod
     *            kind of clustering
     * @param numberOfClusters
     *            number of clusters
     * @param assignments
     * @param clusteringMetrics
     */
    public ClusteringResults(final String clusteringMethod, final int numberOfClusters, final int[] assignments,
            final ClusteringMetrics clusteringMetrics) {
        this.clusteringMethod = clusteringMethod;
        this.numberOfClusters = numberOfClusters;
        this.assignments = assignments;
        this.clusteringMetrics = clusteringMetrics;
    }

    /**
     * Prints the clustering results.
     */
    public void printClusteringResults() {
        System.out.println("#######################################");
        System.out.println("Clustering method: " + this.clusteringMethod);
        System.out.println("Number of clusters: " + this.numberOfClusters);
        this.clusteringMetrics.printSimilarityMetrics();
    }

    public int getNumberOfClusters() {
        return this.numberOfClusters;
    }

    public int[] getAssignments() {
        return this.assignments;
    }

    public ClusteringMetrics getClusteringMetrics() {
        return this.clusteringMetrics;
    }

    public String getClusteringMethod() {
        return this.clusteringMethod;
    }

}
