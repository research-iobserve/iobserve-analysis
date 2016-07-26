package org.iobserve.analysis.userbehavior.data;

import java.util.ArrayList;
import java.util.List;

public class Branch {
	
	private int branchId;
	private int treeLevel;
	private List<SequenceElement> branchSequence;
	private double branchLikelihood;
	private List<Branch> childBranches;
	
	public Branch() {
		branchSequence = new ArrayList<SequenceElement>();
		childBranches = new ArrayList<Branch>();
		branchLikelihood = 0;
	}
		
	public void addBranch(Branch childBranch) {
		childBranches.add(childBranch);
	}
	
	public List<SequenceElement> getBranchSequence() {
		return branchSequence;
	}

	public void setBranchSequence(List<SequenceElement> branchSequence) {
		this.branchSequence = branchSequence;
	}

	public double getBranchLikelihood() {
		return branchLikelihood;
	}

	public void setBranchLikelihood(double branchLikelihood) {
		this.branchLikelihood = branchLikelihood;
	}

	public List<Branch> getChildBranches() {
		return childBranches;
	}

	public void setChildBranches(List<Branch> childBranches) {
		this.childBranches = childBranches;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getTreeLevel() {
		return treeLevel;
	}

	public void setTreeLevel(int treeLevel) {
		this.treeLevel = treeLevel;
	}
	
	
	
	
	
}
