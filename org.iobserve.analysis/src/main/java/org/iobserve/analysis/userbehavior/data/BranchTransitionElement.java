package org.iobserve.analysis.userbehavior.data;

import java.util.ArrayList;
import java.util.List;

public class BranchTransitionElement {
	
	private double transitionLikelihood;
	private List<SequenceElement> branchSequence;
	
	public BranchTransitionElement() {
		branchSequence = new ArrayList<SequenceElement>();
	}
	
	public double getTransitionLikelihood() {
		return transitionLikelihood;
	}
	public void setTransitionLikelihood(double transitionLikelihood) {
		this.transitionLikelihood = transitionLikelihood;
	}
	public List<SequenceElement> getBranchSequence() {
		return branchSequence;
	}
	public void setBranchSequence(List<SequenceElement> branchSequence) {
		this.branchSequence = branchSequence;
	}
	
	

}
