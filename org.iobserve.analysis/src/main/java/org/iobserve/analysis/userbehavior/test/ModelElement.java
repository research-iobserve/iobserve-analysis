package org.iobserve.analysis.userbehavior.test;

public class ModelElement {
	
	private boolean isStartElement;
	private boolean isStopElement;
	private boolean isCallElement;
	private boolean isBranchTransitionElement;
	private boolean isLoopElement;
	private String entiteyName;
	private String loopIteration;
	private double branchProbability;
	

	public ModelElement(boolean isStartElement, boolean isStopElement, boolean isCallElement,
			boolean isBranchTransitionElement, boolean isLoopElement, String entiteyName, String loopIteration,
			double branchProbability) {
		super();
		this.isStartElement = isStartElement;
		this.isStopElement = isStopElement;
		this.isCallElement = isCallElement;
		this.isBranchTransitionElement = isBranchTransitionElement;
		this.isLoopElement = isLoopElement;
		this.entiteyName = entiteyName;
		this.loopIteration = loopIteration;
		this.branchProbability = branchProbability;
	}
	
	public boolean equals(ModelElement modelElement) {
		if(this.isStartElement!=modelElement.isStartElement)
			return false;
		if(this.isStopElement!=modelElement.isStopElement)
			return false;
		if(this.isCallElement!=modelElement.isCallElement)
			return false;
		if(this.isBranchTransitionElement!=modelElement.isBranchTransitionElement)
			return false;
		if(this.isLoopElement!=modelElement.isLoopElement)
			return false;
		if(!this.entiteyName.equals(modelElement.entiteyName))
			return false;
		if(!this.loopIteration.equals(modelElement.loopIteration))
			return false;
		if(this.branchProbability!=modelElement.branchProbability)
			return false;
		
		return true;
	}
	

}
