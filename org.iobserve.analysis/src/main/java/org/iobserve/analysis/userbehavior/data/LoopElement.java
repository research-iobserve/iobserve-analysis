package org.iobserve.analysis.userbehavior.data;

import java.util.ArrayList;
import java.util.List;

public class LoopElement implements SequenceElement {
	
	private int loopId;
	private List<SequenceElement> loopSequence;
	private int loopCount;
	private int startIndexInBranchSequence;
	private int endIndexInBranchSequence;
	private int numberOfReplacedElements;
	private int currentIndexToCheck;
	private boolean doContainBranchElement;
	
	public LoopElement() {
		loopSequence = new ArrayList<SequenceElement>();
	}
	
	public void addSequenceElementToLoopSequence(SequenceElement sequenceElement) {
		loopSequence.add(sequenceElement);
	}
	
	public void addCallElementToLoopSequence(int index, SequenceElement branchElement) {
		loopSequence.add(index, branchElement);
	}

	public List<SequenceElement> getLoopSequence() {
		return loopSequence;
	}

	public void setLoopSequence(List<SequenceElement> loopSequence) {
		this.loopSequence = loopSequence;
	}

	public int getLoopCount() {
		return loopCount;
	}

	public void setLoopCount(int loopCount) {
		this.loopCount = loopCount;
	}

	public int getStartIndexInBranchSequence() {
		return startIndexInBranchSequence;
	}

	public void setStartIndexInBranchSequence(int startIndexInBranchSequence) {
		this.startIndexInBranchSequence = startIndexInBranchSequence;
	}

	public int getEndIndexInBranchSequence() {
		return endIndexInBranchSequence;
	}

	public void setEndIndexInBranchSequence(int endIndexInBranchSequence) {
		this.endIndexInBranchSequence = endIndexInBranchSequence;
	}

	public int getNumberOfReplacedElements() {
		return numberOfReplacedElements;
	}

	public void setNumberOfReplacedElements(int numberOfReplacedElements) {
		this.numberOfReplacedElements = numberOfReplacedElements;
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

	public int getCurrentIndexToCheck() {
		return currentIndexToCheck;
	}

	public void setCurrentIndexToCheck(int currentIndexToCheck) {
		this.currentIndexToCheck = currentIndexToCheck;
	}

	public void copyLoopValues(LoopElement loopElement) {
		this.loopSequence = loopElement.getLoopSequence();
		this.loopCount = loopElement.getLoopCount();
		this.startIndexInBranchSequence = loopElement.getStartIndexInBranchSequence();
		this.endIndexInBranchSequence = loopElement.getEndIndexInBranchSequence();
		this.numberOfReplacedElements = loopElement.getNumberOfReplacedElements();
		this.currentIndexToCheck = loopElement.getCurrentIndexToCheck();
		this.loopId = loopElement.getLoopId();
	}

	public int getLoopId() {
		return loopId;
	}

	public void setLoopId(int loopId) {
		this.loopId = loopId;
	}

	public boolean isDoContainBranchElement() {
		return doContainBranchElement;
	}

	public void setDoContainBranchElement(boolean doContainBranchElement) {
		this.doContainBranchElement = doContainBranchElement;
	}

	
	
	
	
	

}
