package org.iobserve.analysis.userbehavior.data;

import java.util.ArrayList;
import java.util.List;

public class LoopBranchElement implements SequenceElement {

	private List<Branch> loopBranches;
	
	public LoopBranchElement() {
		loopBranches = new ArrayList<Branch>();
	}
	
	public void addBranchToLoopBranch(Branch branch) {
		loopBranches.add(branch);
	}
	
	public List<Branch> getLoopBranches() {
		return loopBranches;
	}

	public void setLoopBranches(List<Branch> loopBranches) {
		this.loopBranches = loopBranches;
	}

	public int getAbsoluteCount() {
		return 0;
	}

	public void setAbsoluteCount(int absoluteCount) {	
	}

	public String getClassSignature() {
		return null;
	}

	public String getOperationSignature() {
		return null;
	}
}
