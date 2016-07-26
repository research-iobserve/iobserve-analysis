package org.iobserve.analysis.userbehavior.data;

import java.util.ArrayList;
import java.util.List;

public class BranchElement implements SequenceElement {
	
	List<BranchTransitionElement> branchTransitions;
	
	public BranchElement() {
		branchTransitions = new ArrayList<BranchTransitionElement>();
	}
	
	public void addBranchTransition(BranchTransitionElement transition) {
		this.branchTransitions.add(transition);
	}

	public List<BranchTransitionElement> getBranchTransitions() {
		return branchTransitions;
	}

	public void setBranchTransitions(List<BranchTransitionElement> branchTransitions) {
		this.branchTransitions = branchTransitions;
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
