package org.iobserve.analysis.userbehavior.data;

import java.util.ArrayList;
import java.util.List;

public class LoopBranchElement implements BranchElement {

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

	@Override
	public int getAbsoluteCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAbsoluteCount(int absoluteCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getClassSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOperationSignature() {
		// TODO Auto-generated method stub
		return null;
	}
}
