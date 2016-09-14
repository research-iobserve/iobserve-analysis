package org.iobserve.analysis.userbehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iobserve.analysis.userbehavior.data.Branch;
import org.iobserve.analysis.userbehavior.data.BranchElement;
import org.iobserve.analysis.userbehavior.data.BranchTransitionElement;
import org.iobserve.analysis.userbehavior.data.SequenceElement;
import org.iobserve.analysis.userbehavior.data.CallBranchModel;
import org.iobserve.analysis.userbehavior.data.CallElement;
import org.iobserve.analysis.userbehavior.data.ExitElement;
import org.iobserve.analysis.userbehavior.data.LoopElement;


/**
 * This class contains all necessary methods to detect iterated behavior within branch sequences and to replace the 
 * iterated segments by loop elements including the number of iterations as loop counts 
 * 
 * @author David Peter, Robert Heinrich 
 */
public class CallLoopBranchModelCreator {
	
	boolean isTreeReconstructed = false;
	
	/**
	 * Triggers the loop detection process
	 * 
	 * @param callBranchModel whose branch sequences are examined for iterated behavior
	 */
	public void detectLoopsInCallBranchModel(CallBranchModel callBranchModel) {
		// Detects Loops within the branch sequences
		detectLoopsWithinBranch(callBranchModel.getRootBranch());
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
	private void detectLoopsWithinBranchSequence(List<SequenceElement> branchSequence) {
		
		if(!(branchSequence.size()>1))
			return;
		
		List<LoopElement> loopElements = new ArrayList<LoopElement>();
	

		loopElements.clear();
		
		// The exit element of a sequence is not considered for the loop detection 
		int branchSequenceSizeWithoutExitElement = branchSequence.size();
		if(branchSequence.get(branchSequence.size()-1).getClass().equals(ExitElement.class))
			branchSequenceSizeWithoutExitElement = branchSequenceSizeWithoutExitElement - 1;
		if(branchSequence==null||branchSequenceSizeWithoutExitElement<2) {
			return;
		}
			
		for(int indexOfElementInElementList=0;indexOfElementInElementList<branchSequenceSizeWithoutExitElement;indexOfElementInElementList++) {
			
			// If the sequence element is a branch element, it is checked for loops within the sequences of the branches
			if(branchSequence.get(indexOfElementInElementList).getClass().equals(BranchElement.class)) {
				BranchElement loopi = (BranchElement) branchSequence.get(indexOfElementInElementList);
				for(int i=0;i<loopi.getBranchTransitions().size();i++) {
					detectLoopsWithinBranchSequence(loopi.getBranchTransitions().get(i).getBranchSequence());
				}
			}
			
			
			// Checks subSequences of the sequence for equality
			// Starting with the max longest sub sequence 
			// If a loop is detected it is memorized
			for(int i=0;i<((branchSequenceSizeWithoutExitElement-indexOfElementInElementList)/2);i++) {
				
				int elementsToCheck = ((branchSequenceSizeWithoutExitElement-indexOfElementInElementList)/2) - i;
				
				boolean isALoop = true;
				LoopElement loopElement = new LoopElement();
				for(int k=0;k<elementsToCheck;k++) {
					SequenceElement element1 = branchSequence.get(indexOfElementInElementList+k);
					SequenceElement element2 = branchSequence.get(indexOfElementInElementList+elementsToCheck+k);
					if(element1.getClass().equals(CallElement.class)&&element2.getClass().equals(CallElement.class)) {
						if(!areSequenceElementsEqual(element1,element2)) {
							isALoop = false;
							break;
						}
					} else if(element1.getClass().equals(BranchElement.class)&&element2.getClass().equals(BranchElement.class)) {
						if(!areSequenceElementsEqual(element1,element2)) {
							isALoop = false;
							break;
						}
					} else {
						isALoop = false;
						break;
					}
					loopElement.addSequenceElementToLoopSequence(branchSequence.get(indexOfElementInElementList+k));

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
			// Find Loops within the loops
			for(LoopElement loopElement : loopElements) {
				detectLoopsWithinBranchSequence(loopElement.getLoopSequence());
			}
			// Inserts the loops into the branch
			insertLoopsIntoBranch(loopElements, branchSequence);
		}
				
	}
	
	private boolean areSequenceElementsEqual(SequenceElement sequenceElement1, SequenceElement sequenceElement2) {
		if(!sequenceElement1.getClass().equals(sequenceElement2.getClass()))
			return false;
		if(sequenceElement1.getClass().equals(CallElement.class)&&sequenceElement2.getClass().equals(CallElement.class)) {
			if((!sequenceElement1.getClassSignature().equals(sequenceElement2.getClassSignature()))||
					(!sequenceElement1.getOperationSignature().equals(sequenceElement2.getOperationSignature())))
				return false;
		}
		if(sequenceElement1.getClass().equals(BranchElement.class)&&sequenceElement2.getClass().equals(BranchElement.class)) {
			if(!doBranchElementsMatch((BranchElement)sequenceElement1,(BranchElement)sequenceElement2))
				return false;
		}	
		return true;
	}
	
	private boolean doBranchElementsMatch(BranchElement branchElement1, BranchElement branchElement2) {
		if(branchElement1.getBranchTransitions().size()!=branchElement2.getBranchTransitions().size())
			return false;
		for(int i=0;i<branchElement1.getBranchTransitions().size();i++) {
			boolean matchFound = false;
			BranchTransitionElement transition1 = branchElement1.getBranchTransitions().get(i);
			for(int j=0;j<branchElement2.getBranchTransitions().size();j++) {
				BranchTransitionElement transition2 = branchElement2.getBranchTransitions().get(j);
				if(transition1.getTransitionLikelihood()!=transition2.getTransitionLikelihood())
					continue;
				else if(!doSequencesMatch(transition1.getBranchSequence(),transition2.getBranchSequence()))
					continue;
				else {
					matchFound = true;
					break;
				}	
			}
			if(!matchFound)
				return false;
		}
		return true;
	}

	private boolean doSequencesMatch(List<SequenceElement> sequence1, List<SequenceElement> sequence2) {
		if(sequence1.size()<sequence2.size())
			return false;
		for(int i=0;i<sequence2.size();i++) {
			SequenceElement branchElementOfSequence1 = sequence1.get(i);
			SequenceElement branchElementOfSequence2 = sequence2.get(i);
			if(!branchElementOfSequence1.getClass().equals(branchElementOfSequence2.getClass()))
				return false;
			if(branchElementOfSequence1.getClass().equals(CallElement.class)&&branchElementOfSequence2.getClass().equals(CallElement.class)) {
				if (!(branchElementOfSequence1.getClassSignature().equals(branchElementOfSequence2.getClassSignature())) ||
				!(branchElementOfSequence1.getOperationSignature().equals(branchElementOfSequence2.getOperationSignature())))
					return false;
			}
			else if(branchElementOfSequence1.getClass().equals(LoopElement.class)&&branchElementOfSequence2.getClass().equals(LoopElement.class)) {
				LoopElement loopElement1 = (LoopElement)branchElementOfSequence1;
				LoopElement loopElement2 = (LoopElement)branchElementOfSequence2;
				if(!doSequencesMatch(loopElement1.getLoopSequence(),loopElement2.getLoopSequence()))
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Inserts the passed loops into the branch sequence and removes the call elements that are
	 * replaced by the loops
	 * 
	 * @param loopElements are the loops that are inserted into the branch sequence
	 * @param branch receives the loops within its branch sequence
	 */
	private void insertLoopsIntoBranch(List<LoopElement> loopElements, List<SequenceElement> branchSequence) {
		
		Collections.sort(loopElements, this.SortLoopElementsByStartIndex); 
		
		for(int i=0;i<loopElements.size();i++) {
			LoopElement loopElement = loopElements.get(i);
			loopElement.setStartIndexInBranchSequence(loopElement.getStartIndexInBranchSequence()+i);
			loopElement.setEndIndexInBranchSequence(loopElement.getStartIndexInBranchSequence()+1);
			branchSequence.add(loopElement.getStartIndexInBranchSequence(), loopElement);
		}
		
		int countOfRemovedElementsFromLoopsBefore = 0;
		for(LoopElement loopElement : loopElements) {
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
	private void filterLoops(List<LoopElement> loopElements) {
		
		for(int i=0;i<loopElements.size();i++) {
			
			for(int j=i+1;j<loopElements.size();j++) {
				LoopElement loopElement1 = loopElements.get(i);
				LoopElement loopElement2 = loopElements.get(j);
				
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
	private boolean removeTheWeakerLoop(List<LoopElement> loopElements, LoopElement loopElement1,
			LoopElement loopElement2, int indexOfLoop1, int indexOfLoop2) {
		
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
	private void determineLoopCount(LoopElement loopElement, List<SequenceElement> branchSequence) {		
		int loopCount = 2;
		boolean doContainBranchElement = false;
		for(int i=loopElement.getEndIndexInBranchSequence();i+loopElement.getLoopSequence().size()<branchSequence.size();i=i+loopElement.getLoopSequence().size()) {
			boolean isAdditionalLoop = true;
			for(int j=0;j<loopElement.getLoopSequence().size();j++){
				SequenceElement element1 = branchSequence.get(loopElement.getStartIndexInBranchSequence()+j);
				SequenceElement element2 = branchSequence.get(i+1+j);
				if(element1.getClass().equals(CallElement.class)&&element2.getClass().equals(CallElement.class)) {
					if(!areSequenceElementsEqual(element1,element2)) {
						isAdditionalLoop = false;
						break;
					}
				} else if(element1.getClass().equals(BranchElement.class)&&element2.getClass().equals(BranchElement.class)) {
					doContainBranchElement = true;
					if(!areSequenceElementsEqual(element1,element2)) {
						isAdditionalLoop = false;
						break;
					}
				} else {
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
		loopElement.setNumberOfReplacedElements(loopCount*loopElement.getLoopSequence().size());
		loopElement.setEndIndexInBranchSequence(loopElement.getStartIndexInBranchSequence()+loopElement.getNumberOfReplacedElements()-1);
	}
	

	/**
	 * Comporator to sort the loopElements according to their position in the sequence
	 */
	private final Comparator<LoopElement> SortLoopElementsByStartIndex = new Comparator<LoopElement>() {
		
		@Override
		public int compare(final LoopElement e1, final LoopElement e2) {
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

	
//	private void detectLoopsWithinBranchSequence2(List<SequenceElement> branchSequence) {
//		
//		if(branchSequence==null||branchSequence.size()<2)
//			return;
//		
//		// The exit element of a sequence is not considered for the loop detection 
//		int branchSequenceSizeWithoutExitElement = branchSequence.size();
//		if(branchSequence.get(branchSequence.size()-1).getClass().equals(ExitElement.class))
//			branchSequenceSizeWithoutExitElement = branchSequenceSizeWithoutExitElement - 1;
//		if(branchSequenceSizeWithoutExitElement<2) {
//			return;
//		}
//		
//		HashMap<String,List<LoopElement>>potentialLoops = new HashMap<String,List<LoopElement>>();
//		List<LoopElement>potentialLoops2 = new ArrayList<LoopElement>();
//		List<LoopElement>loopsToCheck = new ArrayList<LoopElement>();
//		List<LoopElement>detectedLoops = new ArrayList<LoopElement>();
//		int loopId = 1;	
//		
//		for(int branchSequenceIndex=0;branchSequenceIndex<branchSequenceSizeWithoutExitElement;branchSequenceIndex++) {
//			
//			// If the sequence element is a branch element, it is checked for loops within the sequences of the branches
//			if(branchSequence.get(branchSequenceIndex).getClass().equals(BranchElement.class)) {
//				BranchElement loopi = (BranchElement) branchSequence.get(branchSequenceIndex);
//				for(int i=0;i<loopi.getBranchTransitions().size();i++) {
//					detectLoopsWithinBranchSequence2(loopi.getBranchTransitions().get(i).getBranchSequence());
//				}
//			}
//			
//			SequenceElement element = branchSequence.get(branchSequenceIndex);
//			List<LoopElement>newPotentialLoopsToAdd = new ArrayList<LoopElement>();
//			
//			//update potential loops and loops to check
//			for(int i=0;i<potentialLoops2.size();i++) {
//				
//				LoopElement loopElement = potentialLoops2.get(i);
//				
//				LoopElement potentialLoop = new LoopElement();
//				List<SequenceElement> b = new ArrayList<SequenceElement>(loopElement.getLoopSequence());
//				potentialLoop.setLoopSequence(b);
//				potentialLoop.addSequenceElementToLoopSequence(element);
//				potentialLoop.setLoopId(loopId);
//				loopId++;
//				potentialLoop.setLoopCount(1);
//				potentialLoop.setCurrentIndexToCheck(0);
//				potentialLoop.setDoContainBranchElement(loopElement.isDoContainBranchElement());
//				potentialLoop.setStartIndexInBranchSequence(loopElement.getStartIndexInBranchSequence());
//				if(element.getClass().equals(BranchElement.class))
//					potentialLoop.setDoContainBranchElement(true);
//				if(potentialLoop.getLoopSequence().size()*2<=branchSequenceSizeWithoutExitElement) {
//					newPotentialLoopsToAdd.add(potentialLoop);
//				}
//					
//				if(areSequenceElementsEqual(element,loopElement.getLoopSequence().get(loopElement.getCurrentIndexToCheck()))) {
//					if(loopElement.getCurrentIndexToCheck()==loopElement.getLoopSequence().size()-1) {
//						loopElement.setLoopCount(loopElement.getLoopCount()+1);
//						loopElement.setCurrentIndexToCheck(0);
//						loopElement.setEndIndexInBranchSequence(branchSequenceIndex);
//						setNumberOfReplacedElements(loopElement);
//						if(branchSequenceIndex==branchSequenceSizeWithoutExitElement-1)
//							detectedLoops.add(loopElement);
//					} else {
//						loopElement.setCurrentIndexToCheck(loopElement.getCurrentIndexToCheck()+1);
//					}
//				} else {
//					if(loopElement.getLoopCount()>1)
//						detectedLoops.add(loopElement);
//					potentialLoops2.remove(i);
//					i--;
//				}
//			}
//			
//			potentialLoops2.addAll(newPotentialLoopsToAdd);
//			
//			// new potential loop
//			if(branchSequenceIndex!=branchSequenceSizeWithoutExitElement-1) {
//				LoopElement potentialLoop = new LoopElement();
//				potentialLoop.setLoopId(loopId);
//				loopId++;
//				potentialLoop.setLoopCount(1);
//				potentialLoop.setStartIndexInBranchSequence(branchSequenceIndex);
//				potentialLoop.addSequenceElementToLoopSequence(element);
//				if(element.getClass().equals(BranchElement.class))
//					potentialLoop.setDoContainBranchElement(true);
//				potentialLoops2.add(potentialLoop);
//			}
//				
//		}
//		
//		if(detectedLoops.size()>0) {
//			// Filters the loops by overlaps
//			filterLoops(detectedLoops);
//			// Find Loops within the loops
//			for(LoopElement loopElement : detectedLoops) {
//				detectLoopsWithinBranchSequence2(loopElement.getLoopSequence());
//			}
//			// Inserts the loops into the branch
//			insertLoopsIntoBranch(detectedLoops, branchSequence);
//		}
//		
//	}
	
//	private void setNumberOfReplacedElements(LoopElement loopElement) {	
//		loopElement.setNumberOfReplacedElements(loopElement.getLoopSequence().size()*loopElement.getLoopCount());
//		return;
//		if(!loopElement.isDoContainBranchElement()) {
//			loopElement.setNumberOfReplacedElements(loopElement.getLoopSequence().size()*loopElement.getLoopCount());
//		} else {	
//			int numberOfReplacedElements = 0;
//			for(SequenceElement sequenceElement : loopElement.getLoopSequence()) {
//				if(sequenceElement.getClass().equals(CallElement.class)) {
//					numberOfReplacedElements++;
//				} else if(sequenceElement.getClass().equals(BranchElement.class)) {
//					BranchElement branchElement = (BranchElement)sequenceElement;
//					for(BranchTransitionElement branchTransition : branchElement.getBranchTransitions()) {
//						numberOfReplacedElements += branchTransition.getBranchSequence().size();
//					}
//				}
//				loopElement.setNumberOfReplacedElements(loopElement.getLoopCount()*numberOfReplacedElements);
//			}
//		}
//	}
}
