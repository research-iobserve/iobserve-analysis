package org.iobserve.analysis.userbehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.userbehavior.data.Branch;
import org.iobserve.analysis.userbehavior.data.BranchElement;
import org.iobserve.analysis.userbehavior.data.CallBranchModel;
import org.iobserve.analysis.userbehavior.data.CallElement;
import org.iobserve.analysis.userbehavior.data.ExitElement;

/**
 * This class holds a set of methods to create a CallBranchModel from an EntryCallSequenceModel and to calculate 
 * the branch likelihoods of a CallBranchModel. 
 * 
 * @author David Peter, Robert Heinrich
 */

public class CallBranchModelCreator {
	
	/**
	 * It calculates for each branch of the passed CallBranchModel its likelihood
	 * 
	 * @param callBranchModel whose branch likelihoods should be calculated
	 */
	public void calculateLikelihoodsOfBranches(CallBranchModel callBranchModel) {
		// The likelihood of the root branch is always 1
		callBranchModel.getRootBranch().setBranchLikelihood(1);
		traverseBranch(callBranchModel.getRootBranch());
	}
	
	/**
	 * Recursive traversing through the branches
	 * 
	 * @param branch is the start branch that is recursively traversed 
	 */
	private void traverseBranch(Branch branch) {
		setChildBranchLikelihoods(branch);
		for(int i=0;i<branch.getChildBranches().size();i++) {
			traverseBranch(branch.getChildBranches().get(i));
		}
	}
	
	/**
	 * It calculates the likelihoods of the branch´s child branches
	 * 
	 * @param examinedBranch whose child branches´ likelihoods are calculated and set
	 */
	private void setChildBranchLikelihoods(Branch examinedBranch) {
		double countOfParentNode = 0;
		if(examinedBranch.getBranchSequence().size()>0) {
			countOfParentNode = examinedBranch.getBranchSequence().get(examinedBranch.getBranchSequence().size()-1).getAbsoluteCount();
		} else if(examinedBranch.getChildBranches().size()>0) {
			for(int i=0;i<examinedBranch.getChildBranches().size();i++) {
				countOfParentNode += examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getAbsoluteCount();
			}
		} else {
			countOfParentNode = 1;
		}	
		for(int i=0;i<examinedBranch.getChildBranches().size();i++) {
			double countOfChildNode;
			countOfChildNode = examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getAbsoluteCount();
			double likelihhod = countOfChildNode/countOfParentNode;
			examinedBranch.getChildBranches().get(i).setBranchLikelihood(likelihhod);
		}
	}
		
	/**
	 * It creates a CallBranchModel from an EntryCallSequenceModel. At that the single sequences are 
	 * aggregated to a tree-like structure: Equal sequences are summarized to one sequence, alternative sequences are 
	 * represented via branches.
	 * 
	 * @param entryCallSequenceModel whose call sequences are aggregated to a coherent CallBranchModel
	 * @return a CallBranchModel corresponding to the passed EntryCallSequenceModel
	 */
	public CallBranchModel createCallBranchModel(EntryCallSequenceModel entryCallSequenceModel) {
		
		// Set the user group´s specific workload intensity and likelihood
		CallBranchModel callBranchModel = new CallBranchModel(entryCallSequenceModel.getWorkloadIntensity(), entryCallSequenceModel.getLikelihoodOfUserGroup());
		List<UserSession> userSessions = entryCallSequenceModel.getUserSessions();
		
		// The initial branch that contains the root node
		// Every sequence passes this branch -> likelihood of reaching this branch is 100%
		Branch rootBranch = new Branch();
		rootBranch.setBranchLikelihood(1);
		rootBranch.setBranchId(1);
		rootBranch.setTreeLevel(0);
		
		// Descending sort by call sequence length
		Collections.sort(userSessions, this.SortUserSessionByCallSequenceSize); 
		
		// Initialzes the root sequence with the longest call sequence
		setBranchSequence(rootBranch, userSessions.get(0).getEvents(), 0);
		
		int numberOfBranches = 1;
		
		// loops over all userSession without the first user session that initialized the rootBranch
		for(int j=1;j<userSessions.size();j++) {
			
			UserSession userSession = userSessions.get(j);	
			// The branchGuide guides through the tree structure. It determines the recent regarded branch
			List<Integer> branchGuide = new ArrayList<Integer>();
			// The position states the recent position within the branch sequence
			int positionInBranch = 0;
			
			for(int i=0;i<=userSession.getEvents().size();i++) {
				
				// Determines which branch is currently examined
				Branch examinedBranch = getExaminedBranch(branchGuide, rootBranch);
				
				if(i<userSession.getEvents().size()){
					
					EntryCallEvent callEvent = userSession.getEvents().get(i);
					
					// Checks if there is a match between the call event and the element in the currently examined branch
					if(checkPositionMatchInBranch(callEvent, examinedBranch, positionInBranch)) {
						incrementCountOfBranchElement(examinedBranch, positionInBranch);
						positionInBranch++;
						continue;
					}	
					
					// Checks if there is a match between the call event and a first element of a child branch 
					if(isPositionLastElementInBranchSequence(examinedBranch, positionInBranch)) {
						int indexOfMatchingChildBranch = getIndexOfMatchingChildBranch(callEvent, examinedBranch);
						if(indexOfMatchingChildBranch>-1) {
							// Continue with the same call event but switching to the new branch
							branchGuide.add(indexOfMatchingChildBranch);
							i--;
							positionInBranch = 0;
							continue;
						}
					}
					
					// No match could be found --> Split branch into child branches
					numberOfBranches = splitBranch(examinedBranch, positionInBranch, numberOfBranches, false, userSession, i);
					break;
					
				} 
				// End of sequence -> looking for an exit element
				else {
					
					if(checkIfBranchSequenceTerminates(examinedBranch, positionInBranch)) {
						incrementCountOfBranchElement(examinedBranch, positionInBranch);
						break;
					}
					//Checks if there is an exit branch
					if(isPositionLastElementInBranchSequence(examinedBranch, positionInBranch)) {
						int indexOfMatchingChildBranch = getIndexOfMatchingExitBranch(examinedBranch);
						if(indexOfMatchingChildBranch>-1) {
							// Iterate the exit state adding but switching to the new branch
							branchGuide.add(indexOfMatchingChildBranch);
							i--;
							positionInBranch = 0;
							continue;
						}
					}
					
					// No matching exit element found --> Split branch into child branches
					numberOfBranches = splitBranch(examinedBranch, positionInBranch, numberOfBranches, true, null, 0);
					break;
					
				}
				
			}
			
		}
		
		callBranchModel.setRootBranch(rootBranch);
		callBranchModel.setNumberOfBranches(numberOfBranches);
		
		return callBranchModel;
	}
	
	/**
	 * It creates a new branch and adds it to the current examined branch as a new child branch. 
	 * 
	 * @param examinedBranch that is splitted
	 * @param positionInBranch states the recent regarded position in branch sequence
	 * @param numberOfBranches is the recent amount of branches
	 * @param isExit states if it is the end of a sequence
	 * @param userSession if it is not the end of the sequence, the user session holds the rest of sequence that will be added to the new branch
	 * @param indexOfCallEvent if it is not the end of the sequence, it states at which position in the sequence it is added to the new branch 
	 */
	private int splitBranch(Branch examinedBranch, int positionInBranch, int numberOfBranches, boolean isExit, UserSession userSession, int indexOfCallEvent) {
		// If there is already a split at that position add a new branch
		if(isPositionLastElementInBranchSequence(examinedBranch, positionInBranch)) {
			addNewBranch(examinedBranch, numberOfBranches);
			numberOfBranches++;
		} 
		// Else split the branch into two branches
		else {
			splitBranch(examinedBranch, positionInBranch, numberOfBranches);
			numberOfBranches = numberOfBranches + 2;
		}	
		int indexOfNewAddedBranch = examinedBranch.getChildBranches().size()-1;
		examinedBranch = examinedBranch.getChildBranches().get(indexOfNewAddedBranch);
		positionInBranch = 0;
		//Add exit element to the new exit branch
		if(isExit)
			setExitElement(examinedBranch);
		//Add branch sequence to the new branch
		else 
			setBranchSequence(examinedBranch, userSession.getEvents(), indexOfCallEvent);
		
		return numberOfBranches;
		
	}

	/**
	 * Sets the passed events as branch sequence of the passed branch. The sequenceStartIndex defines at which position of
	 * the passed events the branch sequence starts
	 * 
	 * @param examinedBranch for that the branchSequence will be set
	 * @param events represent the sequence to set
	 * @param sequenceStartIndex states at which position of the passed events the branch sequence starts
	 */
	private void setBranchSequence(Branch examinedBranch, List<EntryCallEvent> events, int sequenceStartIndex) {
		List<BranchElement> branchSequence = new ArrayList<BranchElement>();
		for(int j=sequenceStartIndex;j<events.size();j++) {
			EntryCallEvent callEvent = events.get(j);
			CallElement callElement = new CallElement(callEvent.getClassSignature(), callEvent.getOperationSignature());
			callElement.setAbsoluteCount(1);
			branchSequence.add(callElement);
		}
		ExitElement exitElement = new ExitElement();
		exitElement.setAbsoluteCount(1);
		branchSequence.add(exitElement);
		examinedBranch.setBranchSequence(branchSequence);
	}

	/**
	 * Adds an exit element to the end of the branch sequence of the passed branch
	 * 
	 * @param examinedBranch receives an exit element at the end of its branch sequence
	 */
	private void setExitElement(Branch examinedBranch) {
		ExitElement exitElement = new ExitElement();
		exitElement.setAbsoluteCount(1);
		examinedBranch.getBranchSequence().add(exitElement);
	}

	/**
	 * Adds a new child branch to the passed branch. The branch id of the new branch is the current number of branches.
	 * 
	 * @param examinedBranch receives a new child branch
	 * @param numberOfBranches is the current overall number of branches. Defines the id of the new added branch
	 */
	private void addNewBranch(Branch examinedBranch, int numberOfBranches) {
		Branch childBranch = new Branch();
		numberOfBranches++;
		childBranch.setBranchId(numberOfBranches);
		examinedBranch.addBranch(childBranch);
	}

	/**
	 * It splits the passed branch at the passed position of its branch sequence into two child branches. The branch sequence of
	 * the passed branch is shorted to the position the split is performed. The first child branch 
	 * receives the remaining branch sequence, starting at the position the split is performed. The second child branch´s sequence 
	 * stays empty and is filled later
	 * 
	 * @param examinedBranch is split into two branches
	 * @param positionInBranch is the position in the branch sequence where the split is performed
	 * @param numberOfBranches is the current overall number of branches. Defines the new branch ids
	 */
	private void splitBranch(Branch examinedBranch, int positionInBranch, int numberOfBranches) {
		Branch childBranch1 = new Branch();
		Branch childBranch2 = new Branch();

		List<BranchElement> branchSequence = new ArrayList<BranchElement>(examinedBranch.getBranchSequence().subList(0, positionInBranch));
		List<BranchElement> branchSequence1 = new ArrayList<BranchElement>(examinedBranch.getBranchSequence().subList(positionInBranch, examinedBranch.getBranchSequence().size()));
		List<BranchElement> branchSequence2 = new ArrayList<BranchElement>();
		
		examinedBranch.setBranchSequence(branchSequence);
		childBranch1.setBranchSequence(branchSequence1);
		childBranch2.setBranchSequence(branchSequence2);
		
		numberOfBranches++;
		childBranch1.setBranchId(numberOfBranches);
		numberOfBranches++;
		childBranch2.setBranchId(numberOfBranches);
		
		for(Branch childBranch : examinedBranch.getChildBranches()) {
			childBranch1.addBranch(childBranch);
		}
		examinedBranch.getChildBranches().clear();
		
		examinedBranch.addBranch(childBranch1);
		examinedBranch.addBranch(childBranch2);
	}
	
	/**
	 * Returns the index of a child branch whose first branch sequence element matches the passed EntryCallEvent
	 * If there is no matching child branch -1 is returned.
	 * 
	 * @param callEvent that is checked for a match
	 * @param examinedBranch whose child branches´ first sequence element is checked for a match with the passed EntryCallEvent
	 * @return the index of a matching childBranch. Else -1
	 */
	private int getIndexOfMatchingChildBranch(EntryCallEvent callEvent, Branch examinedBranch) {
		if(examinedBranch.getChildBranches().size()==0){
			return -1;
		}
		for(int i=0;i<examinedBranch.getChildBranches().size();i++) {
			if(examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getClass().equals(CallElement.class)) {
				CallElement callElement = (CallElement) examinedBranch.getChildBranches().get(i).getBranchSequence().get(0);
				if(isCallEventCallElementMatch(callEvent, callElement))
					return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of a child branch whose first branch sequence element is an ExitElement, i.e. whose sequence ends
	 * without any further callElement.
	 * If there is no matching child branch -1 is returned. 
	 * 
	 * @param examinedBranch whose child branches´ first sequence element is checked for an exit element
	 * @return the index of a matching childBranch. Else -1
	 */
	private int getIndexOfMatchingExitBranch(Branch examinedBranch) {
		if(examinedBranch.getChildBranches().size()==0){
			return -1;
		}
		for(int i=0;i<examinedBranch.getChildBranches().size();i++) {
			if(examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getClass().equals(ExitElement.class)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks if there is an ExitElement at the passed position in the branch sequence of the passed branch
	 * 
	 * @param examinedBranch holds the branch sequence that is examined for an ExitElement
	 * @param positionInBranch is the position where it is looked for an ExitElement in the branch sequence of the passed branch
	 * @return
	 */
	private boolean checkIfBranchSequenceTerminates(Branch examinedBranch, int positionInBranch) {
		if(positionInBranch>=examinedBranch.getBranchSequence().size())
			return false;
		if(examinedBranch.getBranchSequence().get(positionInBranch).getClass().equals(ExitElement.class))
			return true;
		return false;
	}

	/**
	 * Increments the count of an branch element
	 * 
	 * @param examinedBranch holds the branch sequence´s element that is incremented
	 * @param positionInBranch specifies the branch sequence´s element whose count is incremented
	 */
	private void incrementCountOfBranchElement(Branch examinedBranch, int positionInBranch) {
		int absoluteCount = examinedBranch.getBranchSequence().get(positionInBranch).getAbsoluteCount() + 1;
		examinedBranch.getBranchSequence().get(positionInBranch).setAbsoluteCount(absoluteCount);		
	}
	
	/**
	 * Returns the branch that is described in the branch Guide. The branch guide holds the child branches´ indices.
	 * It traverses through the root branch until the desired child branch is reached and returns it.
	 * 
	 * @param branchGuide holds the child branches´ indices 
	 * @param rootBranch is traversed until the current branch is reached
	 * @return the child branch that is described in the branchGuide
	 */
	private Branch getExaminedBranch(List<Integer> branchGuide, Branch rootBranch) {
		Branch examinedBranch = rootBranch;
		for(int i=0;i<branchGuide.size();i++) {
			examinedBranch = examinedBranch.getChildBranches().get(branchGuide.get(i));
		}
		return examinedBranch;
	}

	/**
	 * Checks if a callEvent and a callElement match
	 * 
	 * @param callEvent that will be matched with the callElement
	 * @param callElement that will be matched with the callEvent
	 * @return if the passed callEvent and callElement match
	 */
	private boolean isCallEventCallElementMatch(EntryCallEvent callEvent, CallElement callElement) {
		if(callEvent.getOperationSignature().equals(callElement.getOperationSignature())&&callEvent.getClassSignature().equals(callElement.getClassSignature()))
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if the passed callEvent matches the callElement at the passed position in the branch sequence of the passed
	 * branch
	 * 
	 * @param callEvent that will be matched
	 * @param examinedBranch holds the branch sequence whose branch element will be matched at the passed position
	 * @param positionInBranch is the position of the branch element within the branch sequence that will be matched against the passed call event
	 * @return if the callEvent matches the branch element at the passed position of the passed branch
	 */
	private boolean checkPositionMatchInBranch(EntryCallEvent callEvent, Branch examinedBranch, int positionInBranch) {
		if(positionInBranch>=examinedBranch.getBranchSequence().size())
			return false;
		if(!examinedBranch.getBranchSequence().get(positionInBranch).getClass().equals(CallElement.class))
			return false;
		CallElement callElement = (CallElement) examinedBranch.getBranchSequence().get(positionInBranch);
		if(isCallEventCallElementMatch(callEvent, callElement))
			return true;
		return false;
	}
	
	/**
	 * Checks if the passed positionInBranch is equal to the length of the branch sequence of the passed branch
	 * 
	 * @param examinedBranch whose branch sequence length is checked
	 * @param positionInBranch is the value that will be matched with the length of the branch sequence of the passed branch
	 * @return if the passed positionInBranch matches the length of the branch sequence
	 */
	private boolean isPositionLastElementInBranchSequence(Branch examinedBranch, int positionInBranch) {
		if(examinedBranch.getBranchSequence().size()==positionInBranch)
			return true;
		else
			return false;
	}
	

	
	// Descending Sort user sessions by call sequence length
	// User session with longest call sequence will be first element
	private final Comparator<UserSession> SortUserSessionByCallSequenceSize = new Comparator<UserSession>() {
		
		@Override
		public int compare(final UserSession o1, final UserSession o2) {
			int sizeO1 = o1.getEvents().size();
			int sizeO2 = o2.getEvents().size();
			if(sizeO1 < sizeO2) {
				return 1;
			} else if(sizeO1 > sizeO2) {
				return -1;
			}
			return 0;
		}
	};

}
