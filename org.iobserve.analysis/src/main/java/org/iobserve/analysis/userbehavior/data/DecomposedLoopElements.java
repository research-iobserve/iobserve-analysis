package org.iobserve.analysis.userbehavior.data;

import java.util.ArrayList;
import java.util.List;

public class DecomposedLoopElements {
	
	List<BranchElement> sharedPrefixElementsOfBranch;
	List<BranchElement> sharedPostfixElementsOfBranch;
	List<Branch> childBranches;
	
	public DecomposedLoopElements() {
		childBranches = new ArrayList<Branch>();
	}
	
	public void addBranch(Branch branch) {
		childBranches.add(branch);
	}

	public List<BranchElement> getSharedPrefixElementsOfBranch() {
		return sharedPrefixElementsOfBranch;
	}

	public void setSharedPrefixElementsOfBranch(List<BranchElement> sharedPrefixElementsOfBranch) {
		this.sharedPrefixElementsOfBranch = sharedPrefixElementsOfBranch;
	}

	public List<BranchElement> getSharedPostfixElementsOfBranch() {
		return sharedPostfixElementsOfBranch;
	}

	public void setSharedPostfixElementsOfBranch(List<BranchElement> sharedPostfixElementsOfBranch) {
		this.sharedPostfixElementsOfBranch = sharedPostfixElementsOfBranch;
	}

	public List<Branch> getChildBranches() {
		return childBranches;
	}

	public void setChildBranches(List<Branch> childBranches) {
		this.childBranches = childBranches;
	}

	
	

}
