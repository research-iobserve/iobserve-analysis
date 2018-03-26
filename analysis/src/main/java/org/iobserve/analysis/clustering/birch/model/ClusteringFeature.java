package org.iobserve.analysis.clustering.birch.model;

import java.util.List;

import weka.core.Instance;

public class ClusteringFeature {

	public int Number;
	double[] linearSum;
	double[] squareSum;
	
	public ClusteringFeature(int n) {
		this.Number = 0;
		this.linearSum = new double[n];
		this.squareSum = new double[n];
	}
	
	public ClusteringFeature(Instance instance) {
		this.Number = 1;
		this.linearSum = new double[instance.numAttributes()];
		this.squareSum = new double[instance.numAttributes()];
		
	    	for(int i = 0; i < instance.numAttributes(); i++) {
	    		this.linearSum[i] = instance.value(i);
	    		this.squareSum[i] = this.linearSum[i] * this.linearSum[i];
	    		//BuildCFTree.LOGGER.debug(vector[i].toString());
	    	}
	}
	
	public ClusteringFeature(int number, double[] linearSum, double[] squareSum) {
		this.Number = number;
		this.linearSum = linearSum;
		this.squareSum = squareSum;
	}
	
	public int getDimension() {
		return this.linearSum.length;
	}
	
	public ClusteringFeature(Double[] linearSum) {
		this.Number = 1;
		
		final int  n = linearSum.length;
		
		this.linearSum = new double[n];
		this.squareSum = new double[n];
		
		for (int i = 0; i < n; i++) {
			this.linearSum[i] = linearSum[i];
			this.squareSum[i] = linearSum[i] * linearSum[i];			
		}
		
	}
	
		
	public ClusteringFeature(ClusteringFeature cf1, ClusteringFeature cf2) {
		this.Number = cf1.Number + cf2.Number;
		
		final int  n = cf1.linearSum.length;
		
		this.linearSum = new double[n];
		this.squareSum = new double[n];
		
		for (int i = 0; i < n; i++) {		
			this.linearSum[i] = cf1.linearSum[i] + cf2.linearSum[i];
			this.squareSum[i] = cf1.squareSum[i] + cf2.squareSum[i];
		}
	}
	
	
	public void add(ClusteringFeature cf) {
		this.Number += cf.Number;
		
		final int  n = linearSum.length;
		
		for (int i = 0; i < n; i++) {		
			this.linearSum[i] += cf.linearSum[i];
			this.squareSum[i] += cf.squareSum[i];
		}
	}
	
	public double compare(ClusteringFeature cf) {
		final int dimension = this.linearSum.length;
		double[] alpha = new double[dimension];
		double[] beta = new double[dimension];
		double[] gamma = new double[dimension];
		double res = 0;
		
		for(int i = 0; i < this.linearSum.length; i++) {
			alpha[i] = (this.linearSum[i] + cf.linearSum[i]) / (this.Number + cf.Number);
			beta[i] = this.linearSum[i]  / this.Number;
			gamma[i] = cf.linearSum[i]  / cf.Number;
		}
		
		for(int i = 0; i < this.linearSum.length; i++) {
			res += 2.0 * this.linearSum[i] * (beta[i] - alpha[i])
				+  2.0 * cf.linearSum[i] * (gamma[i] - alpha[i])
				+ (cf.Number + this.Number) * alpha[i] * alpha[i] 
						- this.Number * beta[i] * beta[i] - cf.Number * gamma[i] * gamma[i];
		}

		return Math.sqrt(res);
	}
	
	///based on D0 (euclidian distance)
	public double compareD0(ClusteringFeature cf) {
		double res = 0;
		for(int i = 0; i < this.linearSum.length; i++) {
			res += Math.pow((this.linearSum[i] / (1.0 * this.Number)) - (cf.linearSum[i] / (1.0 * cf.Number)), 2.0);
		}

		return Math.sqrt(res);
	}
	
	public double compareD2(ClusteringFeature cf) {
		double square = 0.0;
		double linear = 0.0;
		
		for(int i = 0; i < this.linearSum.length; i++) {
			square += this.squareSum[i] * cf.Number + cf.squareSum[i] * this.Number;
			linear += this.linearSum[i] * cf.linearSum[i]; 
		}
			return Math.sqrt((square - 2.0 * linear) 
				/ (this.Number * cf.Number));
	}
	
	public double compareD4(ClusteringFeature cf) {
		final int dimension = this.linearSum.length;
		double[] alpha = new double[dimension];
		double[] beta = new double[dimension];
		double[] gamma = new double[dimension];
		double res = 0;
		
		for(int i = 0; i < this.linearSum.length; i++) {
			alpha[i] = (this.linearSum[i] + cf.linearSum[i]) / (this.Number + cf.Number);
			beta[i] = this.linearSum[i]  / this.Number;
			gamma[i] = cf.linearSum[i]  / cf.Number;
		}
		
		for(int i = 0; i < this.linearSum.length; i++) {
			res += 2.0 * this.linearSum[i] * (beta[i] - alpha[i])
				+  2.0 * cf.linearSum[i] * (gamma[i] - alpha[i])
				+ (cf.Number + this.Number) * alpha[i] * alpha[i] 
						- this.Number * beta[i] * beta[i] - cf.Number * gamma[i] * gamma[i];
		}

		return Math.sqrt(res);
	}
	
	public boolean isBelowThreshold(double t) {
		//System.out.println("is " + this.getDiameter() + " < " + t);
		return (this.getDiameter() <= t);
	}
	
	public double getSquareSumError() {
		double sse = 0.0;
		for(int i = 0; i < this.linearSum.length; i++) {
			sse += this.squareSum[i] - 2.0 * this.linearSum[i] * this.linearSum[i] / this.Number 
					+ this.Number * Math.pow(this.linearSum[i] / this.Number, 2); 
		}
		return sse;
	}
	
	public double getRadius() {
		return Math.sqrt(this.getSquareSumError() / this.Number);
	}
	
	public double getDiameter() {
		if (this.Number <= 1)
			return 0.0;
		
		double square = 0.0;
		double linear = 0.0;
		
		for(int i = 0; i < this.linearSum.length; i++) {
			square += this.squareSum[i];
			linear += this.linearSum[i] * this.linearSum[i]; 
		}
			return Math.sqrt((this.Number * 2.0 * square - 2.0 * linear) 
				/ (this.Number * (this.Number - 1.0)));
	}
	
	public double[] getCentroid() {
		double[] centroid = new double[this.getDimension()];
		for(int i = 0; i < centroid.length; i++)
			centroid[i] = this.linearSum[i] / (1.0 * this.Number);
		return centroid;
	}
	
	public String toString() { 
		//return "N = " + this.Number + "|(" + this.linearSum[0] + "," + this.linearSum[1] + ")|(" + this.squareSum[0] + "," + this.squareSum[1] + ")";
		return "[" + this.Number + "@" + this.hashCode() + "]";
	}
	

}
