package org.iobserve.analysis.userbehavior.data;

import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;

public class ClusteringMetrics {
	
	// Mean distance between the clusters
	private double meanInterClusterSimilarity = 0;
	// Mean distance within the clusters
	private double meanIntraClusterSimilarity = 0;
	
	private Instances centroids;
	private Instances instances; 
	private int[] assignments;
	
	public ClusteringMetrics (Instances centroids, Instances instances, int[] assignments) {
		this.centroids = centroids;
		this.instances = instances;
		this.assignments = assignments;
	}
	
	public void calculateSimilarityMetrics () {
		this.meanInterClusterSimilarity = calculateInterClusterSimilarity();
		this.meanIntraClusterSimilarity = calculateIntraClusterSimilarity();
	}
	
	// Calculate mean distance between the clusters
	private double calculateInterClusterSimilarity () {
		
		DistanceFunction euclideanDistance = new EuclideanDistance();
		euclideanDistance.setInstances(centroids);

		double numberOfCentroids = (double) centroids.numInstances();
		double sumDistance = 0;

		for (int i = 0; i < numberOfCentroids; i++) {
			for (int j = i + 1; j < numberOfCentroids; j++) {
				sumDistance += euclideanDistance.distance(
						centroids.instance(i), centroids.instance(j));
			}
		}

		return (1 / (numberOfCentroids * (numberOfCentroids - 1) / 2)) * sumDistance;
	}
	
	// Calculate mean distance within the clusters
	private double calculateIntraClusterSimilarity () {
		
		DistanceFunction euclideanDistance = new EuclideanDistance();
		euclideanDistance.setInstances(instances);

		double[] avgIntraClusterSimilarity = new double[centroids
				.numInstances()];
		double numberOfCentroids = (double) centroids.numInstances();
		double sumDistance = 0;
		double counter = 0;
		double sumDistanceAllClusters = 0;

		for (int i = 0; i < numberOfCentroids; i++) {
			for (int j = 0; j < instances.numInstances(); j++) {
				if (assignments[j] == i) {
					sumDistance += euclideanDistance.distance(
							instances.instance(j), centroids.instance(i));
					counter += 1;
				}
			}
			avgIntraClusterSimilarity[i] = (1 / counter) * sumDistance;
			sumDistance = 0;
			counter = 0;
		}

		for (double clusterDistance : avgIntraClusterSimilarity) {
			sumDistanceAllClusters += clusterDistance;
		}

		return (1 / numberOfCentroids) * sumDistanceAllClusters;	
	}
	
	// Prints the calculated metrics
	public void printSimilarityMetrics() {
		if (meanInterClusterSimilarity == 0 && meanIntraClusterSimilarity == 0) {
			System.out.println("Metrics have not been calculated");
		} else {
			System.out.println("Mean distance beetween the clusters: " + meanInterClusterSimilarity);
			System.out.println("Mean distance within the clusters: " + meanIntraClusterSimilarity);
		}
	}

	public double getInterClusterSimilarity() {
		return meanInterClusterSimilarity;
	}

	public double getIntraClusterSimilarity() {
		return meanIntraClusterSimilarity;
	}
	

}
