package org.iobserve.analysis.userbehavior;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.userbehavior.data.Branch;
import org.iobserve.analysis.userbehavior.data.BranchElement;
import org.iobserve.analysis.userbehavior.data.BranchModel;
import org.iobserve.analysis.userbehavior.data.BranchTransitionElement;
import org.iobserve.analysis.userbehavior.data.CallElement;
import org.iobserve.analysis.userbehavior.data.ExitElement;
import org.iobserve.analysis.userbehavior.data.ISequenceElement;

/**
 * This class contains all functionality to compact an existing BranchModel, by performing an depth search approach.
 * The branch tree is iterated and compacted bottom up, merging equal (parts of) child branches.
 * Branch Elements are inserted in the branches sequence and the child branches are removed,
 * resulting in the complete branch sequence in the root branch of the branch model.
 * 
 * @author Nicolas Boltz
 * @author Robert Heinrich
 *
 */
public class BranchModelCompactor {
	
    /**
     * Examines a BranchModel whether child branches that have the same parent branch can be merged.
     * Child branches whose sequences contain equal calls at their endings can be merged. Therefore,
     * it is looped over its branches
     *
     * @param branchModel
     *            that is examined whether branches of the BranchModel can be merged
     */
    public void compactBranchModel(final BranchModel branchModel) {
    	this.compactBranch(branchModel.getRootBranch());
    }
	
    /**
     * Examines for each branch its child branches to check whether they can be merged.
     *
     * @param branch
     *            that is examined whether its child branches can be merged
     *            
     * O((S(b) * b)^2 * b * b) 
     * b = number branches, S(b) = sequence elements of branch
     */
    private void compactBranch(final Branch branch) {
    	if (branch.getChildBranches().size() > 0) {
    		for (Branch childBranch : branch.getChildBranches()) {
        		compactBranch(childBranch);
        	}
    		//O((S(b) * b)^2 * b)
        	// 
    		mergeChildBranches(branch);
    	}
    }
    
    private int getIndexOfEqualLastElements(List<Branch> branches) {
    	int indexOfEqualElements = 0;
        boolean isElementEqual = false;
        // Counts the amount of equal calls all the child branches sequences contain at the ending of their sequences.
        Branch initialBranch = branches.get(0);
        int lastElementIndex = initialBranch.getBranchSequence().size() - 1;
        for (int i = lastElementIndex; i >= 0; i--) {
            final ISequenceElement sequenceElementToCompareTo = initialBranch.getBranchSequence().get(i);
            isElementEqual = true;
            for(int k = 1; k < branches.size(); k++) {
            	Branch compareBranch = branches.get(k);
            	// Branch is to short
            	if (compareBranch.getBranchSequence().size() - 1 < indexOfEqualElements) {
                    isElementEqual = false;
                    break; 
                }
            	
            	int currentCompareIndex = compareBranch.getBranchSequence().size() - 1 - indexOfEqualElements;
            	// Branch element not equal?
            	if (!sequenceElementToCompareTo.equals(compareBranch.getBranchSequence().get(currentCompareIndex))) {
            		isElementEqual = false;
                    break; 
                }
            }
            if (!isElementEqual) {
                break;
            }
            indexOfEqualElements++;
        }
        
        return indexOfEqualElements;
    }
    
    /**
     * Merges child branches.
     *
     * @param branch
     *            whose child branches are merged
     */
    private void mergeChildBranches(final Branch branch) {
    	// Potential todo: do not require all child branch sequences to be equal to be merged, 
    	// but cluster child branch sequences and merge the clusters, for a more compacted and correct
    	// result, if necessary.
    	// O((S(b) * b)^2)
    	// b = number branches, S(b) = sequence elements of branch
    	final List<ISequenceElement> mergeableBranchSequence = getAndRemoveMergableBranchSequence(branch);
                
        // Create a new BranchElement that replaces the remainders of the child branches that are merged
    	// O(b)
        final BranchElement branchElement = new BranchElement();
        for (final Branch childBranch : branch.getChildBranches()) {
            final BranchTransitionElement branchTransition = new BranchTransitionElement();
            branchTransition.setBranchSequence(childBranch.getBranchSequence());
            branchTransition.setTransitionLikelihood(childBranch.getBranchLikelihood());
            branchElement.addBranchTransition(branchTransition);
        }

        // First add the branch, then add the merged sequence
        branch.getBranchSequence().add(branchElement);
        branch.getBranchSequence().addAll(mergeableBranchSequence);
        branch.getChildBranches().clear();
    }
    
    /**
     * Remove equal elements from the end of child branch sequences and save in list
     * 
     * @param branch
     * @return The removed sequence elements
     */
    private List<ISequenceElement> getAndRemoveMergableBranchSequence(Branch branch) {
    	// O(S(b) * b)
    	// S(b) = sequence elements of branch
    	int indexOfMergableElements = getIndexOfEqualLastElements(branch.getChildBranches());
        
        // Have equal elements been found?
        if(indexOfMergableElements > 0) {
        	Branch initialChildBranch = branch.getChildBranches().get(0);
        	
    		// Remove equal elements from child branches and save in list
        	// O(S(b) * b)
    		final List<ISequenceElement> mergeableBranchSequence = new ArrayList<>();
            for (int i = 0; i < indexOfMergableElements; i++) {
                mergeableBranchSequence.add(0, initialChildBranch.getBranchSequence()
                        .get(initialChildBranch.getBranchSequence().size() - 1));
                for (final Branch childBranch : branch.getChildBranches()) {
                    childBranch.getBranchSequence().remove(childBranch.getBranchSequence().size() - 1);
                }
            }
            // Remove unnecessary last ExitElement.
            // Makes the loop detection easier, since an attached ExitElement could falsely brake a loop.
            int lastIndex = mergeableBranchSequence.size() - 1;
            if (mergeableBranchSequence.get(lastIndex).getClass().equals(ExitElement.class)) {
            	mergeableBranchSequence.remove(lastIndex);
            }
            
            return mergeableBranchSequence;
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unused")
	private void printBranch(Branch branch, String pre) {
    	System.out.print(pre);
    	if(branch.getBranchSequence().isEmpty()) {
    		System.out.print("EMPTY");
    	}
    	
    	for(ISequenceElement element : branch.getBranchSequence()) {
    		if(element instanceof CallElement) {
    			System.out.print(((CallElement)element).getOperationSignature());
    		} else {
    			System.out.print("EMPTY");
    		}
    		System.out.print(" ");
    	}
    	
    	System.out.println("");
    	
    	for(Branch child : branch.getChildBranches()) {
    		printBranch(child, pre + "--");
    	}
    }
    
    // Different approach, incomplete, parts may be usable later on.
//  private List<ISequenceElement> compactBranches(List<Branch> branches, int lastElementsIndex) {
//	List<ISequenceElement> compactedSequence = new ArrayList<ISequenceElement>();
//	if(lastElementsIndex > 0) {
//		Branch initialBranch = branches.get(0);
//        int lastElementIndex = initialBranch.getBranchSequence().size() - 1;
//    	// If the only found equal elements are exit elements, don't merge
//    	if(lastElementsIndex == 1 && initialBranch.getBranchSequence()
//                .get(initialBranch.getBranchSequence().size() - 1).getClass().equals(ExitElement.class)) {
//    		return null;
//    	} else {
//    		// Merge equal elements
//    		// Creates a new BranchElement that replaces the child branches that are merged
//            final BranchElement branchElement = new BranchElement();
//            final List<ISequenceElement> sequenceToAddToBranch = new ArrayList<>();
//            
//            for (int i = 0; i < lastElementsIndex; i++) {
//                sequenceToAddToBranch.add(0, initialBranch.getBranchSequence()
//                        .get(initialBranch.getBranchSequence().size() - 1));
//                for (final Branch branch : branches) {
//                	int lastBranchIndex = branch.getBranchSequence().size() - 1;
//                	branch.getBranchSequence().remove(lastBranchIndex);
//                }
//            }
//            
//            for (final Branch branch : branches) {
//                final BranchTransitionElement branchTransition = new BranchTransitionElement();
//                branchTransition.setBranchSequence(branch.getBranchSequence());
//                branchTransition.setTransitionLikelihood(branch.getBranchLikelihood());
//                branchElement.addBranchTransition(branchTransition);
//            }
//            compactedSequence.add(branchElement);
//            compactedSequence.addAll(sequenceToAddToBranch);
//    	}
//	}
//	
//	return compactedSequence;
//	
//	
//}
//
//private List<Branch> doSequencesOfChildBranchesMatch2(final List<Branch> branches, Branch parent) {
//	HashMap<Integer, List<Branch>> childBranchCountSortMap = new HashMap<Integer, List<Branch>>(); 
//	for(Branch parentBranch : branches) {
//		int size = parentBranch.getChildBranches().size();
//		if(!childBranchCountSortMap.containsKey(size)) {
//			List<Branch> sizeList = new ArrayList<>();
//			childBranchCountSortMap.put(size, sizeList);
//		}
//		
//		childBranchCountSortMap.get(size).add(parentBranch);
//	}
//	
//	
//	if(childBranchCountSortMap.containsKey(0)) {
//		List<Branch> endBranches = childBranchCountSortMap.get(0);
//		int equalLastElementsIndex = getIndexOfEqualLastElements(endBranches);
//		
//		compactBranches(endBranches, equalLastElementsIndex);
//		
//		// check for equal sequence pieces bottom up and return result of compacting
//		
//		
//	} else {
//    	for(List<Branch> equalChildBranchCountBranches : childBranchCountSortMap.values()) {
//    		
//    		for(Branch branch : equalChildBranchCountBranches) {
//    			doSequencesOfChildBranchesMatch2(branch.getChildBranches(), branch);
//    		}
//    		
//    		
//    		// check for equal sequence piece and childbranches bottom up and return result of compacting
//    	}
//	}
//
//	List<Branch> endBranches = childBranchCountSortMap.get(0);
//	// keine Childbranches und eine Iteration in dieser Funktion sagt aus, dass die Branches die selbe Sequenz haben -> zusammenfügen und als einzelner Branch zurückgeben
//	// Der zurückgegebene Branch muss dann an die Parentbranches gefügt werden. Nicht als Child, sondern als Child deren Parent.
//	// Deshalb muss nach return des rekursiven Aufrufs die Sequenzen der EqualChildBranchCoundBranches verglichen werden.
//	// Gleiche werden dann wieder als Branch zusammengefügt. und die Sammlung an Branches zurückgegeben. usw.
//	
//	for(List<Branch> equalChildBranchCountBranches : childBranchCountSortMap.values()) {
//		while(!equalChildBranchCountBranches.isEmpty()) {
//			Branch matchBranch = equalChildBranchCountBranches.remove(0);
//			List<Branch> branchWithMatchingChildBranches = getBranchesWithMatchingChildBranchSequences(matchBranch, equalChildBranchCountBranches);
//			equalChildBranchCountBranches.removeAll(branchWithMatchingChildBranches);
//			branchWithMatchingChildBranches.add(matchBranch);
//			
//			final List<Branch> branchesToCheck = new ArrayList<>();
//	        for (final Branch branch : branchWithMatchingChildBranches) {
//	            branchesToCheck.addAll(branch.getChildBranches());
//	        }
//	        //List<Branch> compactedChildBranches = doSequencesOfChildBranchesMatch2(branchesToCheck, branch);
//	        
//	        for (final Branch branch : branchWithMatchingChildBranches) {
//	            //branch.setChildBranches(compactedChildBranches);
//	        }
//		}
//	}
//	
//	return null;
//}
//
//private List<Branch> getBranchesWithMatchingChildBranchSequences(Branch parentBranchToMatch, List<Branch> parentBranches) {
//    List<Branch> matchingBranches = new ArrayList<Branch>();
//	
//	for (int i = 0; i < parentBranches.size(); i++) {
//    	Branch parentBranch = parentBranches.get(i);
//    	
//    	boolean parentBranchesMatch = true;
//    	for(Branch branchToMatch : parentBranchToMatch.getChildBranches()) {
//            boolean matchingBranchFound = false;
//            
//            for(Branch testBranch : parentBranch.getChildBranches()) {
//                if (ISequenceElement.doSequencesMatch(branchToMatch.getBranchSequence(), testBranch.getBranchSequence())
//                        && (branchToMatch.getBranchLikelihood() == testBranch.getBranchLikelihood())) {
//                	matchingBranchFound = true;
//                    break;
//                }
//            }
//            
//            if (!matchingBranchFound) {
//            	parentBranchesMatch = false;
//                break;
//            }
//        }
//    	if(parentBranchesMatch) {
//    		matchingBranches.add(parentBranch);
//    	}
//    }
//	
//	return matchingBranches;
//}
//
//  // Only used if combined top down
//  private void combineEqualBranchTransitions(BranchElement branchElement) {
//  	for (int i = 0; i < branchElement.getBranchTransitions().size(); i++) {
//  		BranchTransitionElement transition1 = branchElement.getBranchTransitions().get(i);
//  		for (int k = i + 1; k < branchElement.getBranchTransitions().size(); k++) {
//  			BranchTransitionElement transition2 = branchElement.getBranchTransitions().get(k);
//  			if(ISequenceElement.doSequencesMatch(transition1.getBranchSequence(), transition2.getBranchSequence())) {
//  				double combinedLikelihood = transition1.getTransitionLikelihood() + transition2.getTransitionLikelihood();
//  				transition1.setTransitionLikelihood(combinedLikelihood);
//  			}
//  			branchElement.getBranchTransitions().remove(transition2);
//  		}
//  	}
//  }
}
