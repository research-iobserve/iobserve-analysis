package org.iobserve.analysis.clustering.birch.model;

import java.util.List;

public class ProximityMatrix {

	double[][] matrix;
	int n;
	int farthest1;
	int farthest2;
	int closest1;
	int closest2;

	public ProximityMatrix(List<ClusteringFeature> cfs) {
		this.n = cfs.size();
		//if(n <= 0);
			//System.out.println(cfs.size());
		this.matrix = new double[n][n];
		double max = 0.0;
		double min = Double.MAX_VALUE;
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<i; j++) {
				this.matrix[i][j] = cfs.get(i).compare(cfs.get(j));
				this.matrix[j][i] = this.matrix[i][j]; 
				if (max < this.matrix[i][j]) {
					max = this.matrix[i][j];
					this.farthest1 = i;
					this.farthest2 = j;
				}
				if (min > this.matrix[i][j]) {
					min = this.matrix[i][j];
					this.closest1 = i;
					this.closest2 = j;
				}
			}
		}
		//this.toConsole();
	}
	
	public boolean isClosestPair(int i, int j) {
		return (i == closest1 && j == closest2) | (j == closest1 && i == closest2);
	}
	
	private void toConsole() {
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				System.out.print(" " + String.format("%.2f", this.matrix[i][j]));
			}
			System.out.print("\n");
		}
		System.out.println("Farthest: " + this.farthest1 +  "," + this.farthest2);
		System.out.println("Closest: " + this.closest1 +  "," + this.closest2);
	}
}
