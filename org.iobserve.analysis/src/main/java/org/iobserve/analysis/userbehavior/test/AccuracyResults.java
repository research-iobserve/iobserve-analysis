package org.iobserve.analysis.userbehavior.test;

/**
 * 
 * Contains the metrics of the modeling accuracy: Jaccard Coefficient and Spearman Rank Coreelation Coefficient
 * 
 * @author David
 *
 */
public class AccuracyResults {
	
	private double jc;
	private double srcc;
	
	public double getJc() {
		return jc;
	}
	public void setJc(double jc) {
		this.jc = jc;
	}
	public double getSrcc() {
		return srcc;
	}
	public void setSrcc(double srcc) {
		this.srcc = srcc;
	}
		
}
