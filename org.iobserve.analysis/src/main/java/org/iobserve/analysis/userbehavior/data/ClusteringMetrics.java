package org.iobserve.analysis.userbehavior.data;

import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;

public class ClusteringMetrics {

	// Distance between the centroids and the assignments of the cluster
	private double sumOfSquaredErrors = 0;
	
	private Instances centroids;
	private Instances instances; 
	private int[] assignments;
	
	public ClusteringMetrics (Instances centroids, Instances instances, int[] assignments) {
		this.centroids = centroids;
		this.instances = instances;
		this.assignments = assignments;
	}
	
	public void calculateSimilarityMetrics () {
		this.sumOfSquaredErrors = calculateSumOfSquaredErrors();
	}
	
	private double calculateSumOfSquaredErrors() {
		
		DistanceFunction euclideanDistance = new EuclideanDistance();
		euclideanDistance.setInstances(instances);

		double numberOfCentroids = (double) centroids.numInstances();
		double sumOfSquaredErrors = 0;

		for (int i = 0; i < numberOfCentroids; i++) {
			for (int j = 0; j < instances.numInstances(); j++) {
				if (assignments[j] == i) {
					sumOfSquaredErrors += Math.pow(euclideanDistance.distance(instances.instance(j), centroids.instance(i)), 2);
				}
			}
		}
		
		return sumOfSquaredErrors;
	}

	
	
	// Prints the calculated metrics
	public void printSimilarityMetrics() {
		if (this.sumOfSquaredErrors == 0) {
			System.out.println("Metrics have not been calculated");
		} else {
			System.out.println("Sum of squared errors: " + this.sumOfSquaredErrors);
		}
	}

	public double getSumOfSquaredErrors() {
		return sumOfSquaredErrors;
	}
	
	

}
