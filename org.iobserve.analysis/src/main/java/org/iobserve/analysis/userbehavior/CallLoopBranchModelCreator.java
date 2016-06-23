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
import org.iobserve.analysis.userbehavior.data.DecomposedLoopElements;
import org.iobserve.analysis.userbehavior.data.ExitElement;
import org.iobserve.analysis.userbehavior.data.LoopBranchElement;
import org.iobserve.analysis.userbehavior.data.LoopSequenceElement;


/**
 * This class contains all necessary methods to detect iterated behavior within branch sequences and to replace the 
 * iterated segments by loop elements including the number of iterations as loop counts 
 * 
 * @author David Peter, Robert Heinrich 
 */
public class CallLoopBranchModelCreator {
	
	/**
	 * Triggers the loop detection process
	 * 
	 * @param callBranchModel whose branch sequences are examined for iterated behavior
	 */
	public void detectLoopsInCallBranchModel(CallBranchModel callBranchModel) {
		// Looped branches
		setBranchTreeLevels(callBranchModel.getRootBranch(), callBranchModel.getRootBranch().getTreeLevel());
		detectLoopedBranches(callBranchModel.getRootBranch());
		
		// Loops within sequences
		detectLoopsWithinBranch(callBranchModel.getRootBranch());
		
		// Set Branch ids
		int maxBranchId = setBranchIds(callBranchModel.getRootBranch(),1,1);
		callBranchModel.setNumberOfBranches(maxBranchId);
	}
	
	/**
	 * Sets for each branch its unique branch id and returns the amount of branches
	 * 
	 * @param branch whose branchIds are set
	 * @param branchId is the current branchId that is incremented for each branch
	 * @param maxBranchId represents the amount of branches via the max branchId
	 * @return the max branch id that represents the entire amount of branches
	 */
	private int setBranchIds(Branch branch, int branchId, int maxBranchId) {
		branch.setBranchId(branchId);
		if(maxBranchId<branchId)
			maxBranchId = branchId;
		for(int i=0;i<branch.getChildBranches().size();i++) {
			int newBranchId = branchId +i+1;
			setBranchIds(branch.getChildBranches().get(i),newBranchId,maxBranchId);
		}
		return maxBranchId;
	}
	
	/**
	 * Iterates through the branches and sets the branches´ treeLevels. The treeLevel determines the tree depth of the branch.
	 * For example the treeLevel of the rootBranch is 0. Its childBranches´ treeLevel is 1 and so on.
	 * 
	 * @param branch whose branches´ treeLevel is set
	 * @param treeLevel is the current treeLevel
	 */
	private void setBranchTreeLevels(Branch branch, int treeLevel) {
		branch.setTreeLevel(treeLevel);
		for(int i=0;i<branch.getChildBranches().size();i++) {
			setBranchTreeLevels(branch.getChildBranches().get(i), treeLevel+1);
		}
	}
	
	/**
	 * Iterates over the branches and starts the detection process for each branch
	 * 
	 * @param branch that is examined for loops
	 */
	private void detectLoopsWithinBranch(Branch branch) {		
		detectLoopsWithinBranchSequence(branch.getBranchSequence());
		for(int i=0;i<branch.getChildBranches().size();i++) {
			detectLoopsWithinBranch(branch.getChildBranches().get(i));
		}
	}
	
	/**
	 * Iterates through the branches and checks the child branches for iteration
	 * 
	 * @param branch whose child branches are checked for iteration
	 */
	private void detectLoopedBranches(Branch branch) {
		if(checkPlausibilityForIteratedChildBranches(branch)) {
			DecomposedLoopElements decomposedElements = getDecomposedElements(branch);
			detectAndSetBranchLoops(branch, decomposedElements);
		}
		for(int i=0;i<branch.getChildBranches().size();i++) {
			detectLoopedBranches(branch.getChildBranches().get(i));
		}
	}
	
	
	/**
	 * It checks the plausibility of iterated child branches 
	 * If the child branches´ child branches are from the same length an iteration is possible  
	 * 
	 * @param branch whose child branches are checked for iterations
	 */
	private boolean checkPlausibilityForIteratedChildBranches(Branch branch) {
			
		if(branch.getChildBranches().size()==0)
			return false;
		for(int i=0;i<branch.getChildBranches().size();i++) {
			if(branch.getChildBranches().get(i).getChildBranches().size()==0||branch.getChildBranches().get(i).getChildBranches().size()!=branch.getChildBranches().size())
				return false;
		}
		
		return true;	
	}
	
	
	private DecomposedLoopElements getDecomposedElements(Branch branch) {
		
		List<BranchElement>prefixElementsOfLoopedBranches = new ArrayList<BranchElement>();
		List<BranchElement>postfixElementsOfLoopedBranches = new ArrayList<BranchElement>();
		boolean isPrefixReachable = true;
		int indexCount = 0;
		for(int k=branch.getChildBranches().get(0).getBranchSequence().size()-1;k>=1;k--){
			BranchElement branchElement1 = branch.getChildBranches().get(0).getBranchSequence().get(k);
			boolean elementsAreEqual = true;
			for(int i=1;i<branch.getChildBranches().size();i++) {
				int consideredIndex = branch.getChildBranches().get(i).getBranchSequence().size()-1-indexCount;
				if(consideredIndex<1) {
					elementsAreEqual = false;
					break;
				}
				BranchElement branchElement2 = branch.getChildBranches().get(i).getBranchSequence().get(consideredIndex);
				if(!areBranchElementsEqual(branchElement1, branchElement2)){
					elementsAreEqual = false;
					break;
				}
			}
			if(elementsAreEqual) {
				if(isPrefixReachable && branch.getBranchSequence().size()>0 && areBranchElementsEqual(branchElement1, branch.getBranchSequence().get(branch.getBranchSequence().size()-1-indexCount))) {
					prefixElementsOfLoopedBranches.add(0,branchElement1);
				} else {
					isPrefixReachable = false;
					postfixElementsOfLoopedBranches.add(0,branchElement1);
				}
			} else {
				break;
			}
			indexCount++;
		}
		
		
		DecomposedLoopElements loopedBranchElements = new DecomposedLoopElements();
		loopedBranchElements.setSharedPrefixElementsOfBranch(prefixElementsOfLoopedBranches);
		loopedBranchElements.setSharedPostfixElementsOfBranch(postfixElementsOfLoopedBranches);
		for(int i=0;i<branch.getChildBranches().size();i++) {
			List<BranchElement> branchSequenceToCheck = new ArrayList<BranchElement>();
			for(int j=0;j<=branch.getChildBranches().get(i).getBranchSequence().size()-1-indexCount;j++) {
				branchSequenceToCheck.add(0,branch.getChildBranches().get(i).getBranchSequence().get(j));
			}
			Branch branchToCheck = new Branch();
			branchToCheck.setBranchSequence(branchSequenceToCheck);
			branchToCheck.setBranchLikelihood(branch.getChildBranches().get(i).getBranchLikelihood());
			loopedBranchElements.addBranch(branchToCheck);
		}
		return loopedBranchElements;
	}
	
	/**
	 * It detects iterated child branches of the passed branch. For that it traverses as deep through the child branches as it finds
	 * the iterated child branches. Equality is determined by the child branches´ sequences.  
	 * If it finds iterations it calls the reconstruction method to replace the iterated branches and adjust the succeeding branches.
	 * 
	 * @param branch whose child branches are checked for iteration
	 */
	private void detectAndSetBranchLoops(Branch branch, DecomposedLoopElements decomposedElements) {
		
		List<Branch> branchesAfterTheIteratedBranches = new ArrayList<Branch>();
//		List<Branch> branchesToCheck = branch.getChildBranches();
		int treeLevel = branch.getTreeLevel();
		int countOfLoopedBranches = 1;
		boolean treeLevelIsEqual = true;
		boolean sequenceIsPrefix = true;
		
		while(treeLevelIsEqual) {
		
			treeLevel++;
			List<Branch> branchesOfTreeLevel = new ArrayList<Branch>();
			branchesOfTreeLevel = getBranchesOfTreeLevel(branch, treeLevel, branchesOfTreeLevel);
			if(branchesOfTreeLevel==null||branchesOfTreeLevel.size()==0) {
				treeLevelIsEqual = false;
				sequenceIsPrefix = false;
			}
			
			for(int j=0;j<branchesOfTreeLevel.size();j++) {
				if(branchesOfTreeLevel.get(j).getChildBranches().size()!=branch.getChildBranches().size()) {
					treeLevelIsEqual = false;
					sequenceIsPrefix = false;
					break;
				}
				boolean matchingSequenceFound = false;
				for(int s=0;s<branchesOfTreeLevel.get(j).getChildBranches().size();s++) {
					matchingSequenceFound = false;
//					for(int k=0;k<decomposedElements.getUniqueChildBranchSequences().size();k++) {
//						if(doBranchesMatch(branchesOfTreeLevel.get(j).getChildBranches().get(s), branchesToCheck.get(k))) {
//							if(branchesOfTreeLevel.get(j).getChildBranches().get(s).getBranchSequence().size()!=branchesToCheck.get(k).getBranchSequence().size()) {
//								treeLevelIsEqual = false;
//							}
						if(isBranchAnIteration(branchesOfTreeLevel.get(j).getChildBranches().get(s), decomposedElements)) {
							matchingSequenceFound = true;
							treeLevelIsEqual = true;
							break;
						} else if (isBranchEndOfTheIteration(branchesOfTreeLevel.get(j).getChildBranches().get(s), decomposedElements)) {
							matchingSequenceFound = true;
							treeLevelIsEqual = false;
							break;
						} else {
							sequenceIsPrefix = false;
							treeLevelIsEqual = false;
							break;
						}
//					}
//					if(!matchingSequenceFound) {
//						sequenceIsPrefix = false;
//						treeLevelIsEqual = false;
//						break;
//					} 
				}
				if(!matchingSequenceFound) {
					sequenceIsPrefix = false;
					treeLevelIsEqual = false;
					break;
				}
			}
			
			if(sequenceIsPrefix) 
				countOfLoopedBranches++;
			
			if(sequenceIsPrefix&&!treeLevelIsEqual&&countOfLoopedBranches>1) {
				branchesAfterTheIteratedBranches = getBranchesOfTreeLevel(branch, treeLevel+1, branchesAfterTheIteratedBranches);
				reconstructBranch(branch, countOfLoopedBranches, branchesAfterTheIteratedBranches, decomposedElements);
				return;
			} else if(!sequenceIsPrefix&&countOfLoopedBranches>1) {
				List<Branch> branchesOfFatherTreeLevel = new ArrayList<Branch>();
				branchesOfFatherTreeLevel = getBranchesOfTreeLevel(branch, treeLevel-1, branchesOfFatherTreeLevel);
				if(doChildBranchesMatch(branchesOfFatherTreeLevel)) {
					branchesAfterTheIteratedBranches = getBranchesOfTreeLevel(branch, treeLevel+1, branchesAfterTheIteratedBranches);
					reconstructBranch(branch, countOfLoopedBranches, branchesAfterTheIteratedBranches, decomposedElements);
					return;
				}
			}
				
		}
		return;
	}
	
	private boolean isBranchAnIteration(Branch branch, DecomposedLoopElements decomposedElements) {
		
		for(int k=0;k<decomposedElements.getChildBranches().size();k++) {
			
			if(branch.getBranchLikelihood()!=decomposedElements.getChildBranches().get(k).getBranchLikelihood())
				continue;
			
			List<BranchElement> sequenceToCheck = new ArrayList<BranchElement>();
			sequenceToCheck.addAll(decomposedElements.getChildBranches().get(k).getBranchSequence());
			sequenceToCheck.addAll(decomposedElements.getSharedPostfixElementsOfBranch());
			sequenceToCheck.addAll(decomposedElements.getSharedPrefixElementsOfBranch());
			
			if(doSequencesMatch(sequenceToCheck, branch.getBranchSequence()))
				return true;
		}
		
		return false;
	}
	
	private boolean isBranchEndOfTheIteration(Branch branch, DecomposedLoopElements decomposedElements) {
		
		for(int k=0;k<decomposedElements.getChildBranches().size();k++) {
			
			if(branch.getBranchLikelihood()!=decomposedElements.getChildBranches().get(k).getBranchLikelihood())
				continue;
			
			List<BranchElement> sequenceToCheck = new ArrayList<BranchElement>();
			sequenceToCheck.addAll(decomposedElements.getChildBranches().get(k).getBranchSequence());
			sequenceToCheck.addAll(decomposedElements.getSharedPostfixElementsOfBranch());
			
			if(isPrefixOfSequence(sequenceToCheck, branch.getBranchSequence()))
				return true;
		}
		
		return false;
	}

	/**
	 * It replaces the iterated branches by a loop element that contains the iterated segments/branches. And also reconstruct the 
	 * succeeding part of the branch after the iterated child branches
	 * 
	 * @param branch whose iterated child branches are replaced by an loop element and whose succeeding branches are adapted
	 * @param countOfLoopedBranches is the number the child branches are iterated in a row
	 * @param branchesAfterTheIteratedBranches are the branches that succeed to the iterated branches, i.e. the first branches that are not iterated
	 * @param branchesToCheck are the branch´s child branches. These branches are iterated
	 */
	private void reconstructBranch(Branch branch, int countOfLoopedBranches, List<Branch> branchesAfterTheIteratedBranches, DecomposedLoopElements decomposedElements) {
		
		/*
		 * The loop sequence receives iterated single call events and the iterated branches in form of a LoopBranchElement
		 * The LoopBranchElement is added to the looped sequence
		 */
		LoopBranchElement loopBranch = new LoopBranchElement();
		LoopSequenceElement loopSequence = new LoopSequenceElement();
		loopSequence.setLoopCount(countOfLoopedBranches);
		
		/*
		 * branchesAfterTheIteratedBranches: The branches that succeed the iterated branches
		 * branchesToCheck: The branches that are iterated
		 * This for-loop removes the iterated segment from the succeeding branches because these segments are already contained in 
		 * the branch loop
		 */
		for(int i=0;i<branchesAfterTheIteratedBranches.size();i++) {
			for(int j=0;j<decomposedElements.getChildBranches().size();j++) {
				if(areBranchElementsEqual(decomposedElements.getChildBranches().get(j).getBranchSequence().get(0),branchesAfterTheIteratedBranches.get(i).getBranchSequence().get(0))) {
					int countOfElementsToRemove = decomposedElements.getChildBranches().get(j).getBranchSequence().size() + decomposedElements.getSharedPostfixElementsOfBranch().size();
					for(int k=0;k<countOfElementsToRemove;k++) {
						branchesAfterTheIteratedBranches.get(i).getBranchSequence().remove(0);
					}
					break;
				}
			}
		}
		
		/*
		 * This for-loop checks if the different iterated branches contain equal segments that are contained in every iterated branch
		 * (and additionally in the branch sequence before the child branches are reached). 
		 * Equal segments are removed from the iterated branches (and from the end of the branch sequence before the child branches are reached) 
		 * and added to the loop sequence
		 */
//		List<BranchElement>prefixElementsOfLoopedBranches = new ArrayList<BranchElement>();
//		List<BranchElement>postfixElementsOfLoopedBranches = new ArrayList<BranchElement>();
//		boolean isPrefixReachable = true;
//		int indexCount = 0;
//		for(int k=branch.getChildBranches().get(0).getBranchSequence().size()-1;k>=1;k--){
//			BranchElement branchElement1 = branch.getChildBranches().get(0).getBranchSequence().get(k);
//			boolean elementsAreEqual = true;
//			for(int i=1;i<branch.getChildBranches().size();i++) {
//				int consideredIndex = branch.getChildBranches().get(i).getBranchSequence().size()-1-indexCount;
//				if(consideredIndex<0) {
//					elementsAreEqual = false;
//					break;
//				}
//				BranchElement branchElement2 = branch.getChildBranches().get(i).getBranchSequence().get(consideredIndex);
//				if(!areBranchElementsEqual(branchElement1, branchElement2)){
//					elementsAreEqual = false;
//					break;
//				}
//			}
//			if(elementsAreEqual) {
//				if(isPrefixReachable && branch.getBranchSequence().size()>0 && areBranchElementsEqual(branchElement1, branch.getBranchSequence().get(branch.getBranchSequence().size()-1))) {
//					prefixElementsOfLoopedBranches.add(0,branchElement1);
//					branch.getBranchSequence().remove(branch.getBranchSequence().size()-1);
//				} else {
//					isPrefixReachable = false;
//					postfixElementsOfLoopedBranches.add(0,branchElement1);
//				}
//			} else {
//				break;
//			}
//			indexCount++;
//		}
//		for(int k=0;k<indexCount;k++) {
//			for(int i=0;i<branch.getChildBranches().size();i++) {
//				branch.getChildBranches().get(i).getBranchSequence().remove(branch.getChildBranches().get(i).getBranchSequence().size()-1);
//			}
//		}
		
		/*
		 * Removes the prefix Sequence from the root branch because it is contained it the iteration
		 */
		for(int i=0;i<decomposedElements.getSharedPrefixElementsOfBranch().size();i++) {
			branch.getBranchSequence().remove(branch.getBranchSequence().size()-1);
		}
		
		/*
		 * The looped sequence is created
		 * The looped Branch element is created and added to the looped sequence
		 */
		for(int i=0;i<decomposedElements.getSharedPrefixElementsOfBranch().size();i++) {
			loopSequence.addCallElementToLoopSequence(decomposedElements.getSharedPrefixElementsOfBranch().get(i));
		}
		for(int i=0;i<decomposedElements.getChildBranches().size();i++) {
			loopBranch.addBranchToLoopBranch(decomposedElements.getChildBranches().get(i));
		}
		loopSequence.addCallElementToLoopSequence(loopBranch);
		for(int i=0;i<decomposedElements.getSharedPostfixElementsOfBranch().size();i++) {
			loopSequence.addCallElementToLoopSequence(decomposedElements.getSharedPostfixElementsOfBranch().get(i));
		}
		branch.getBranchSequence().add(loopSequence);
		
		/*
		 * Finally the branches and call elements that succeed to the iterated branch is reconstructed by a back transformation of
		 * the succeeding branches to an entryCallSequenceModel that subsequent is transformed to a callBranchModel.
		 * The CallBranchModel represents the succeeding branches and is added to the passed branch
		 */
		branch.getChildBranches().clear();
		EntryCallSequenceModel entryCallSequenceModel = getEntryCallSequenceModelFromBranches(branchesAfterTheIteratedBranches);
		CallBranchModelCreator callBranchModelCreator = new CallBranchModelCreator();
		CallBranchModel callBranchModel = callBranchModelCreator.createCallBranchModel(entryCallSequenceModel);
		callBranchModelCreator.calculateLikelihoodsOfBranches(callBranchModel);
		Branch branchToAdd = callBranchModel.getRootBranch();
		for(int i=0;i<branchToAdd.getBranchSequence().size();i++) {
			branch.getBranchSequence().add(branchToAdd.getBranchSequence().get(i));
		}
		for(int i=0;i<branchToAdd.getChildBranches().size();i++) {
			branch.getChildBranches().add(branchToAdd.getChildBranches().get(i));
		}
	}
	
	private EntryCallSequenceModel getEntryCallSequenceModelFromBranches(List<Branch> branchesAfterTheIteratedBranches) {
		List<UserSession> userSessions = new ArrayList<UserSession>();
		for(Branch branch : branchesAfterTheIteratedBranches) {
			List<UserSession> userSessionsOfBranch = new ArrayList<UserSession>();
			List<EntryCallEvent> events = new ArrayList<EntryCallEvent>();
			userSessionsOfBranch = getUserSessionsFromBranch(branch, events, userSessionsOfBranch);
			userSessions.addAll(userSessionsOfBranch);
		}
		
		return new EntryCallSequenceModel(userSessions);
	}
	
	private List<UserSession> getUserSessionsFromBranch(Branch branch,List<EntryCallEvent> events, List<UserSession> userSessions) {

		for(int i=0;i<branch.getBranchSequence().size();i++) {
			if(branch.getBranchSequence().get(i).getClass().equals(CallElement.class)) {
				EntryCallEvent entryCallEvent = new EntryCallEvent(0,0,branch.getBranchSequence().get(i).getOperationSignature(),branch.getBranchSequence().get(i).getClassSignature(),"","");
				events.add(entryCallEvent); 
			}
		}
		for(int i=0;i<branch.getChildBranches().size();i++) {
			userSessions = getUserSessionsFromBranch(branch.getChildBranches().get(i),events,userSessions);
		}
		if(branch.getChildBranches().size()==0) {
			UserSession userSession = new UserSession("","");
			for(EntryCallEvent event : events) {
				userSession.add(event);
			}
			userSessions.add(userSession);
		}
		
		return userSessions;
	}

	private boolean doChildBranchesMatch(List<Branch> branches) {
		List<Branch> branchesToMatch = new ArrayList<Branch>();
		for(int i=0;i<branches.get(0).getChildBranches().size();i++) {
			branchesToMatch.add(branches.get(0).getChildBranches().get(i));
		}
		for(int i=0;i<branches.size();i++) {
			if(branches.get(i).getChildBranches().size()!=branchesToMatch.size())
				return false;
			for(int k=0;k<branchesToMatch.size();k++) {
				boolean matchingElementFound = false;
				for(int j=0;j<branches.get(i).getChildBranches().size();j++) {
					if(areBranchElementsEqual(branchesToMatch.get(k).getBranchSequence().get(0),branches.get(i).getChildBranches().get(j).getBranchSequence().get(0))
							&&branchesToMatch.get(k).getBranchLikelihood()==branches.get(i).getChildBranches().get(j).getBranchLikelihood()) {
						matchingElementFound = true;
						break;
					}
				}
				if(!matchingElementFound) {
					return false;
				}
			}	
		}

		return true;
	}

	private List<Branch> getBranchesOfTreeLevel(Branch branch, int treeLevel, List<Branch> childBranches) {
		
		if(branch.getTreeLevel()==treeLevel) 
			childBranches.add(branch);	
		for(int i=0;i<branch.getChildBranches().size();i++) {
			if(branch.getChildBranches().get(i).getTreeLevel()>treeLevel)
				break;
			childBranches = getBranchesOfTreeLevel(branch.getChildBranches().get(i), treeLevel, childBranches);
		}
		
		return childBranches;
	}
		
	
	private boolean areBranchElementsEqual(BranchElement branchElement1, BranchElement branchElement2) {
		if(!branchElement1.getClass().equals(branchElement2.getClass()))
			return false;
		if(branchElement1.getClass().equals(CallElement.class)&&branchElement2.getClass().equals(CallElement.class)) {
			if((!branchElement1.getClassSignature().equals(branchElement2.getClassSignature()))||
					(!branchElement1.getOperationSignature().equals(branchElement2.getOperationSignature())))
				return false;
		}
		return true;
	}
		
	
	private boolean doSequencesMatch(List<BranchElement> sequence1, List<BranchElement> sequence2) {
		if(sequence1.size()<sequence2.size())
			return false;
		for(int i=0;i<sequence2.size();i++) {
			BranchElement branchElementOfSequence1 = sequence1.get(i);
			BranchElement branchElementOfSequence2 = sequence2.get(i);
			if(!branchElementOfSequence1.getClass().equals(branchElementOfSequence2.getClass()))
				return false;
			if(branchElementOfSequence1.getClass().equals(CallElement.class)&&branchElementOfSequence2.getClass().equals(CallElement.class)) {
				if (!(branchElementOfSequence1.getClassSignature().equals(branchElementOfSequence2.getClassSignature())) ||
				!(branchElementOfSequence1.getOperationSignature().equals(branchElementOfSequence2.getOperationSignature())))
					return false;
			}			
		}
		return true;
	}
	
	private boolean isPrefixOfSequence(List<BranchElement> prefix, List<BranchElement> sequence) {
		if(prefix.size()>sequence.size())
			return false;
		for(int i=0;i<prefix.size();i++) {
			BranchElement branchElementOfPrefix = prefix.get(i);
			BranchElement branchElementOfSequence = sequence.get(i);
			if(!branchElementOfPrefix.getClass().equals(branchElementOfSequence.getClass()))
				return false;
			if(branchElementOfPrefix.getClass().equals(CallElement.class)&&branchElementOfSequence.getClass().equals(CallElement.class)) {
				if (!(branchElementOfPrefix.getClassSignature().equals(branchElementOfSequence.getClassSignature())) ||
				!(branchElementOfPrefix.getOperationSignature().equals(branchElementOfSequence.getOperationSignature())))
					return false;
			}			
		}
		return true;
	}
	
	/**
	 * Detects loops within a branch sequence
	 * 1. 	Finds all iterated segments within the given sequence 
	 * 2.	For each obtained segment the loop count is determined
	 * 3. 	The loops are examined if they overlap 
	 * 		If there is an overlap between two loops, the loop that replaces less sequence elements is filtered out
	 * 4.	The loop elements are inserted into the sequence
	 * Iterates until no new loops are found
	 * 
	 * @param branchSequence that is examined for iterated behavior
	 */
	private void detectLoopsWithinBranchSequence(List<BranchElement> branchSequence) {
		
		if(!(branchSequence.size()>0))
			return;
		
		List<LoopSequenceElement> loopElements = new ArrayList<LoopSequenceElement>();
		
		do{
			loopElements.clear();
			
			// The exit element of a sequence is not considered for the loop detection 
			int branchSequenceSizeWithoutExitElement = branchSequence.size();
			if(branchSequence.get(branchSequence.size()-1).getClass().equals(ExitElement.class))
				branchSequenceSizeWithoutExitElement = branchSequenceSizeWithoutExitElement - 1;
			if(branchSequence==null||branchSequenceSizeWithoutExitElement<2) {
				return;
			}
				
			for(int indexOfElementInElementList=0;indexOfElementInElementList<branchSequenceSizeWithoutExitElement;indexOfElementInElementList++) {
				
				// If the current examined branch element is a loop element, it is checked for loops within the loop
				// by a recursive call of this method
				if(branchSequence.get(indexOfElementInElementList).getClass().equals(LoopSequenceElement.class)) {
					LoopSequenceElement loopi = (LoopSequenceElement) branchSequence.get(indexOfElementInElementList);
					detectLoopsWithinBranchSequence(loopi.getLoopSequence());
					continue;
				}
				// If the branch element is a loop branch element, it is checked for loops within the sequences of the branches
				if(branchSequence.get(indexOfElementInElementList).getClass().equals(LoopBranchElement.class)) {
					LoopBranchElement loopi = (LoopBranchElement) branchSequence.get(indexOfElementInElementList);
					for(int i=0;i<loopi.getLoopBranches().size();i++) {
						detectLoopsWithinBranchSequence(loopi.getLoopBranches().get(i).getBranchSequence());
					}
					continue;
				}
				
				// Checks subSequences of the sequence for equality
				// Starting with the max longest sub sequence(Half of the total sequence) and always trying smaller
				// sub sequences until all sub sequences are compared with each other
				// If a loop is detected it is memorized
				for(int i=0;i<((branchSequenceSizeWithoutExitElement-indexOfElementInElementList)/2);i++) {
					
					int elementsToCheck = ((branchSequenceSizeWithoutExitElement-indexOfElementInElementList)/2) - i;
					
					boolean isALoop = true;
					LoopSequenceElement loopElement = new LoopSequenceElement();
					for(int k=0;k<elementsToCheck;k++) {
						if(!branchSequence.get(indexOfElementInElementList+k).getClass().equals(CallElement.class)||!branchSequence.get(indexOfElementInElementList+elementsToCheck+k).getClass().equals(CallElement.class)) {
							isALoop = false;
							break;
						}
						if(!branchSequence.get(indexOfElementInElementList+k).getOperationSignature().equals(branchSequence.get(indexOfElementInElementList+elementsToCheck+k).getOperationSignature())) {
							isALoop = false;
							break;
						} 
						if(branchSequence.get(indexOfElementInElementList+k).getClass().equals(CallElement.class)){
							loopElement.addCallElementToLoopSequence((CallElement) branchSequence.get(indexOfElementInElementList+k));
						}
					}
					
					if(isALoop) {
						
						loopElement.setStartIndexInBranchSequence(indexOfElementInElementList);
						loopElement.setEndIndexInBranchSequence(indexOfElementInElementList+elementsToCheck+elementsToCheck-1);

						// Determines the number of loops for each loop
						determineLoopCount(loopElement, branchSequence);
						
						loopElements.add(loopElement);
						
					}
	
				}
				
			}
			
			if(loopElements.size()>0) {
				// Filters the loops by overlaps
				filterLoops(loopElements);
				// Inserts the loops into the branch
				insertLoopsIntoBranch(loopElements, branchSequence);
			}
			
		}while(loopElements.size()!=0);
	
	}
	
	/**
	 * Inserts the passed loops into the branch sequence and removes the call elements that are
	 * replaced by the loops
	 * 
	 * @param loopElements are the loops that are inserted into the branch sequence
	 * @param branch receives the loops within its branch sequence
	 */
	private void insertLoopsIntoBranch(List<LoopSequenceElement> loopElements, List<BranchElement> branchSequence) {
		
		Collections.sort(loopElements, this.SortLoopElementsByStartIndex); 
		
		for(int i=0;i<loopElements.size();i++) {
			LoopSequenceElement loopElement = loopElements.get(i);
			loopElement.setStartIndexInBranchSequence(loopElement.getStartIndexInBranchSequence()+i);
			loopElement.setEndIndexInBranchSequence(loopElement.getStartIndexInBranchSequence()+1);
			branchSequence.add(loopElement.getStartIndexInBranchSequence(), loopElement);
		}
		
		int countOfRemovedElementsFromLoopsBefore = 0;
		for(LoopSequenceElement loopElement : loopElements) {
			for(int i=0;i<loopElement.getNumberOfReplacedElements();i++) {
				int indexToRemove = loopElement.getStartIndexInBranchSequence()+1-countOfRemovedElementsFromLoopsBefore;
				branchSequence.remove(indexToRemove);
			}
			countOfRemovedElementsFromLoopsBefore += loopElement.getNumberOfReplacedElements();
		}
		
	}

	/**
	 * Checks if there are overlaps between the passed loops, i.e. if more than one loop uses the same call element. 
	 * If it detects an overlap it filters the loop out that replaces less call elements
	 * 
	 * @param loopElements
	 */
	private void filterLoops(List<LoopSequenceElement> loopElements) {
		
		for(int i=0;i<loopElements.size();i++) {
			
			for(int j=i+1;j<loopElements.size();j++) {
				LoopSequenceElement loopElement1 = loopElements.get(i);
				LoopSequenceElement loopElement2 = loopElements.get(j);
				
				// Checks if there is an overlap between two loops
				if((loopElement1.getStartIndexInBranchSequence()>=loopElement2.getStartIndexInBranchSequence()&&loopElement1.getStartIndexInBranchSequence()<=loopElement2.getEndIndexInBranchSequence())
						||(loopElement1.getEndIndexInBranchSequence()>=loopElement2.getStartIndexInBranchSequence()&&loopElement1.getEndIndexInBranchSequence()<=loopElement2.getEndIndexInBranchSequence())
						||(loopElement2.getStartIndexInBranchSequence()>=loopElement1.getStartIndexInBranchSequence()&&loopElement2.getStartIndexInBranchSequence()<=loopElement1.getEndIndexInBranchSequence())
						||(loopElement2.getEndIndexInBranchSequence()>=loopElement1.getStartIndexInBranchSequence()&&loopElement2.getEndIndexInBranchSequence()<=loopElement1.getEndIndexInBranchSequence())){
					
					// Returns the removed loop 
					boolean loop1Weaker = removeTheWeakerLoop(loopElements, loopElement1, loopElement2, i, j);
					
					// Iterate loop considering the removing
					if(loop1Weaker) {
						i--;
						break;	
					} else {
						j--;
					}
				}
			}
		}
	}

	/**
	 * Checks which loop of the two passed loops replaces less elements and removes it from the list of loops.
	 * 
	 * @param loopElements contains all detected loops of one sequence
	 * @param loopElement1 is checked against the loopElement2 who replaces less elements
	 * @param loopElement2 is checked against the loopElement1 who replaces less elements
	 * @param indexOfLoop1 within the loopElements list 
	 * @param indexOfLoop2 within the loopElements list
	 * @return
	 */
	private boolean removeTheWeakerLoop(List<LoopSequenceElement> loopElements, LoopSequenceElement loopElement1,
			LoopSequenceElement loopElement2, int indexOfLoop1, int indexOfLoop2) {
		
		if(loopElement1.getNumberOfReplacedElements()>loopElement2.getNumberOfReplacedElements()) {
			loopElements.remove(indexOfLoop2);
			return false;
		} else if(loopElement2.getNumberOfReplacedElements()>loopElement1.getNumberOfReplacedElements()) {
			loopElements.remove(indexOfLoop1);
			return true;
		} else if(loopElement1.getLoopSequence().size()<=loopElement2.getLoopSequence().size()) {
			loopElements.remove(indexOfLoop2);
			return false;
		} else {
			loopElements.remove(indexOfLoop1);
			return true;
		}
		
	}

	/**
	 * Determines the loop count by checking how often the loop is iterated in a row
	 *
	 * @param loopElement whose loopCount is determined 
	 * @param branchSequence within the loopCount is determined
	 */
	private void determineLoopCount(LoopSequenceElement loopElement, List<BranchElement> branchSequence) {
				
		int loopCount = 2;
		for(int i=loopElement.getEndIndexInBranchSequence();i+loopElement.getLoopSequence().size()<branchSequence.size();i=i+loopElement.getLoopSequence().size()) {
			boolean isAdditionalLoop = true;
			for(int j=0;j<loopElement.getLoopSequence().size();j++){
				if(!branchSequence.get(loopElement.getStartIndexInBranchSequence()+j).getOperationSignature().equals(branchSequence.get(i+1+j).getOperationSignature())) {
					isAdditionalLoop = false;
					break;
				}
			}
			if(isAdditionalLoop)
				loopCount++;
			else
				break;
		}
		loopElement.setLoopCount(loopCount);
		loopElement.setNumberOfReplacedElements(loopElement.getLoopCount()*loopElement.getLoopSequence().size());
		loopElement.setEndIndexInBranchSequence(loopElement.getStartIndexInBranchSequence()+loopElement.getNumberOfReplacedElements()-1);
	}
	

	/**
	 * Comporator to sort the loopElements according to their position in the sequence
	 */
	private final Comparator<LoopSequenceElement> SortLoopElementsByStartIndex = new Comparator<LoopSequenceElement>() {
		
		@Override
		public int compare(final LoopSequenceElement e1, final LoopSequenceElement e2) {
			int index1 = e1.getStartIndexInBranchSequence();
			int index2 = e2.getStartIndexInBranchSequence();
			if(index1 > index2) {
				return 1;
			} else if(index1 < index2) {
				return -1;
			}
			return 0;
		}
	};

}
