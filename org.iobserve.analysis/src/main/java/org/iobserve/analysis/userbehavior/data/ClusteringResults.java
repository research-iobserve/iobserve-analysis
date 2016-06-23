package org.iobserve.analysis.userbehavior.data;

public class ClusteringResults {

	private String clusteringMethod;
	private int numberOfClusters;
	private int[] assignments;
	private ClusteringMetrics clusteringMetrics;
	
	public ClusteringResults(String clusteringMethod, int numberOfClusters, int[] assignments, ClusteringMetrics clusteringMetrics) {
		this.clusteringMethod = clusteringMethod;
		this.numberOfClusters = numberOfClusters;
		this.assignments = assignments;
		this.clusteringMetrics = clusteringMetrics;
	}
	
	public void printClusteringResults() {
		System.out.println("#######################################");
		System.out.println("Clustering method: "+clusteringMethod);
		System.out.println("Number of clusters: "+numberOfClusters);
		clusteringMetrics.printSimilarityMetrics();
	}

	public int getNumberOfClusters() {
		return numberOfClusters;
	}

	public int[] getAssignments() {
		return assignments;
	}

	public ClusteringMetrics getClusteringMetrics() {
		return clusteringMetrics;
	}

	public String getClusteringMethod() {
		return clusteringMethod;
	}	
	
}
