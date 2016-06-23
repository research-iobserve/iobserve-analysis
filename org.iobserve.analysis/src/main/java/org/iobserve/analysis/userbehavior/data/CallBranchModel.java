package org.iobserve.analysis.userbehavior.data;

import java.util.List;

public class CallBranchModel {
	
	private Branch rootBranch;
	private final WorkloadIntensity workloadIntensity;
	private final double likelihoodOfUserGroup;
	private int numberOfBranches;
	
	public CallBranchModel(WorkloadIntensity workloadIntensity, double likelihoodOfUserGroup) {
		this.workloadIntensity = workloadIntensity;
		this.likelihoodOfUserGroup = likelihoodOfUserGroup;
	}
	
	public Branch getExaminedBranch(List<Integer> branchGuide) {
		if(this.rootBranch==null)
			return null;
		Branch examinedBranch = this.rootBranch;
		for(int i=0;i<branchGuide.size();i++) {
			examinedBranch = examinedBranch.getChildBranches().get(branchGuide.get(i));
		}
		return examinedBranch;
	}

	public Branch getRootBranch() {
		return rootBranch;
	}

	public void setRootBranch(Branch rootBranch) {
		this.rootBranch = rootBranch;
	}

	public WorkloadIntensity getWorkloadIntensity() {
		return workloadIntensity;
	}

	public double getLikelihoodOfUserGroup() {
		return likelihoodOfUserGroup;
	}

	public int getNumberOfBranches() {
		return numberOfBranches;
	}

	public void setNumberOfBranches(int numberOfBranches) {
		this.numberOfBranches = numberOfBranches;
	}
	
	
		

}
