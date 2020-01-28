/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.userbehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iobserve.analysis.userbehavior.data.Branch;
import org.iobserve.analysis.userbehavior.data.BranchElement;
import org.iobserve.analysis.userbehavior.data.BranchModel;
import org.iobserve.analysis.userbehavior.data.BranchTransitionElement;
import org.iobserve.analysis.userbehavior.data.ExitElement;
import org.iobserve.analysis.userbehavior.data.ISequenceElement;
import org.iobserve.analysis.userbehavior.data.LoopElement;

/**
 * This class contains all necessary methods to detect iterated behavior and to replace the iterated
 * segments by loop elements including the number of iterations as loop counts.
 *
 * @author David Peter
 * @author Robert Heinrich
 * @author Nicolas Boltz
 */
public class LoopBranchModelCreator {

    /**
     * Default constructor.
     */
    public LoopBranchModelCreator() {

    }

    /**
     * Triggers the loop extraction process.
     * 
     * O(b* (e_b^3 + e_b^3/e_l + 3*l*e_l + 2*l))
     *
     * @param branchModel
     *            whose branch sequences are examined for iterated behavior
     */
    public void detectLoopsInCallBranchModel(final BranchModel branchModel) {
        // Detects Loops within the branches, starting at the root branch

        this.detectLoopsWithinBranch(branchModel.getRootBranch());
    }

    /**
     * Iterates over the branches and starts the detection process for each branch.
     * O(b* (e_b^3 + e_b^3/e_l + 3*l*e_l + 2*l))
     * 
     * @param branch
     *            that is examined for loops
     */
    private void detectLoopsWithinBranch(final Branch branch) {
    	//O(e_b^3 + e_b^3/e_l + 3*l*e_l + 2*l)
        this.detectLoopsWithinBranchSequence(branch.getBranchSequence());

        //O(b* rest of method)
        for (int i = 0; i < branch.getChildBranches().size(); i++) {
            this.detectLoopsWithinBranch(branch.getChildBranches().get(i));
        }
    }

    /**
     * Detects loops within a branch sequence 1. Finds all iterated segments within the given
     * sequence 2. For each obtained segment the loop count is determined 3. The loops are examined
     * if they overlap If there is an overlap between two loops, the loop that replaces less
     * sequence elements is filtered out 4. The loop elements are inserted into the sequence
     * Iterates until no new loops are found.
     * 
     * O(e_b^3 + e_b^3/e_l + 3*l*e_l + 2*l)
     *
     * @param branchSequence
     *            that is examined for iterated behavior
     */
    private void detectLoopsWithinBranchSequence(final List<ISequenceElement> branchSequence) {
        final List<LoopElement> loopElements = new ArrayList<>();
        loopElements.clear();

        // The exit element of a sequence is not considered for the loop detection
        int branchSequenceSizeWithoutExitElement = branchSequence.size();
        if (branchSequence.get(branchSequence.size() - 1).getClass().equals(ExitElement.class)) {
            branchSequenceSizeWithoutExitElement = branchSequenceSizeWithoutExitElement - 1;
        }

        if ((branchSequence != null) && (branchSequenceSizeWithoutExitElement > 0)) {
        	// O(e_b^3 + e_b^3/e_l)
            for (int indexOfElementInElementList = 0; indexOfElementInElementList < branchSequenceSizeWithoutExitElement; indexOfElementInElementList++) {

                // If the sequence element is a branch element, it is checked for loops within
                // the sequences of the branches
                if (branchSequence.get(indexOfElementInElementList).getClass().equals(BranchElement.class)) {
                    final BranchElement subBranchElement = (BranchElement) branchSequence.get(indexOfElementInElementList);
                    
                    // big-O time complexity of contained branchElements is already regarded for
                    for (BranchTransitionElement transition : subBranchElement.getBranchTransitions()) {
                        this.detectLoopsWithinBranchSequence(transition.getBranchSequence());
                    }
                }

                // Checks subSequences of the sequence for equality
                // Starting with the max longest sub sequence
                // If a loop is detected it is memorized
                // O(e_b^2 + e_b^2/e_l)
                for (int i = 0; i < ((branchSequenceSizeWithoutExitElement - indexOfElementInElementList)
                        / 2); i++) {

                    final int elementsToCheck = ((branchSequenceSizeWithoutExitElement
                            - indexOfElementInElementList) / 2) - i;
                    boolean isALoop = true;
                    final LoopElement loopElement = new LoopElement();
                    
                    // O(e_b)
                    for (int k = 0; k < elementsToCheck; k++) {
                        final ISequenceElement element1 = branchSequence.get(indexOfElementInElementList + k);
                        final ISequenceElement element2 = branchSequence
                                .get(indexOfElementInElementList + elementsToCheck + k);
                        if(!element1.getClass().equals(ExitElement.class) 
                        		&& !element1.getClass().equals(LoopElement.class)) {
                        	if(!element1.equals(element2)) {
                        		isALoop = false;
                                break;
                        	}
                        } else {
                            isALoop = false;
                            break;
                        }
                        loopElement.addElementToLoopSequence(branchSequence.get(indexOfElementInElementList + k));
                    }

                    if (isALoop) {

                        loopElement.setStartIndexInBranchSequence(indexOfElementInElementList);
                        loopElement.setEndIndexInBranchSequence(
                                (indexOfElementInElementList + elementsToCheck + elementsToCheck) - 1);

                        // Determines the number of loops for each loop
                        // O(e_b/e_l)
                        this.determineLoopCount(loopElement, branchSequence);

                        loopElements.add(loopElement);
                    }
                }
            }

            // O(3*l*e_l + 2*l)
            if (loopElements.size() > 0) {
                // Filters the loops from overlapping loops
            	// O(l*e_l)
                this.filterLoops(loopElements);
                // Find Loops within the loops
                // O(l*e_l) -> further contained branchElements are already regarded for
                for (final LoopElement loopElement : loopElements) {
                    this.detectLoopsWithinBranchSequence(loopElement.getLoopSequence());
                }
                
                // Inserts the loops into the branch
                // O(2*l+l*e_l)
                this.insertLoopsIntoBranch(loopElements, branchSequence);
            }
        }
    }

    /**
     * Inserts the passed loops into the branch sequence and removes the call elements that are.
     * replaced by the loops.
     * 
     * O(2*l+l*e_l)
     *
     * @param loopElements
     *            are the loops that are inserted into the branch sequence
     * @param branch
     *            receives the loops within its branch sequence
     */
    private void insertLoopsIntoBranch(final List<LoopElement> loopElements,
            final List<ISequenceElement> branchSequence) {

    	// O(l)
        Collections.sort(loopElements, new Comparator<LoopElement>() {
            @Override
            public int compare(final LoopElement e1, final LoopElement e2) {
                final int index1 = e1.getStartIndexInBranchSequence();
                final int index2 = e2.getStartIndexInBranchSequence();
                if (index1 > index2) {
                    return 1;
                } else if (index1 < index2) {
                    return -1;
                }
                return 0;
            }
        });

        // O(l)
        for (int i = 0; i < loopElements.size(); i++) {
            final LoopElement loopElement = loopElements.get(i);
            loopElement.setStartIndexInBranchSequence(loopElement.getStartIndexInBranchSequence() + i);
            loopElement.setEndIndexInBranchSequence(loopElement.getStartIndexInBranchSequence() + 1);
            branchSequence.add(loopElement.getStartIndexInBranchSequence(), loopElement);
        }

        // O(l*e_l)
        int countOfRemovedElementsFromLoopsBefore = 0;
        for (final LoopElement loopElement : loopElements) {
            for (int i = 0; i < loopElement.getNumberOfReplacedElements(); i++) {
                final int indexToRemove = (loopElement.getStartIndexInBranchSequence() + 1)
                        - countOfRemovedElementsFromLoopsBefore;
                branchSequence.remove(indexToRemove);
            }
            countOfRemovedElementsFromLoopsBefore += loopElement.getNumberOfReplacedElements();
        }
    }

    /**
     * Checks if there are overlaps between the passed loops, i.e. if more than one loop uses the
     * same call element. If it detects an overlap it filters the loop out that replaces less call
     * elements.
     * 
     * O(l*e_l)
     *
     * @param loopElements
     */
    private void filterLoops(final List<LoopElement> loopElements) {

        for (int i = 0; i < loopElements.size(); i++) {

            for (int j = i + 1; j < loopElements.size(); j++) {
                final LoopElement loopElement1 = loopElements.get(i);
                final LoopElement loopElement2 = loopElements.get(j);

                // Checks if there is an overlap between two loops
                if (((loopElement1.getStartIndexInBranchSequence() >= loopElement2.getStartIndexInBranchSequence())
                        && (loopElement1.getStartIndexInBranchSequence() <= loopElement2.getEndIndexInBranchSequence()))
                        || ((loopElement1.getEndIndexInBranchSequence() >= loopElement2.getStartIndexInBranchSequence())
                                && (loopElement1.getEndIndexInBranchSequence() <= loopElement2
                                        .getEndIndexInBranchSequence()))
                        || ((loopElement2.getStartIndexInBranchSequence() >= loopElement1
                                .getStartIndexInBranchSequence())
                                && (loopElement2.getStartIndexInBranchSequence() <= loopElement1
                                        .getEndIndexInBranchSequence()))
                        || ((loopElement2.getEndIndexInBranchSequence() >= loopElement1.getStartIndexInBranchSequence())
                                && (loopElement2.getEndIndexInBranchSequence() <= loopElement1
                                        .getEndIndexInBranchSequence()))) {

                    // Returns the removed loop
                    final boolean loop1Weaker = this.removeTheWeakerLoop(loopElements, loopElement1, loopElement2, i,
                            j);

                    // Iterate loop considering the removing
                    if (loop1Weaker) {
                        i--; // NOCS
                        break;
                    } else {
                        j--; // NOCS
                    }
                }
            }
        }
    }

    /**
     * Checks which loop of the two passed loops replaces less elements and removes it from the list
     * of loops.
     *
     * @param loopElements
     *            contains all detected loops of one sequence
     * @param loopElement1
     *            is checked against the loopElement2 who replaces less elements
     * @param loopElement2
     *            is checked against the loopElement1 who replaces less elements
     * @param indexOfLoop1
     *            within the loopElements list
     * @param indexOfLoop2
     *            within the loopElements list
     * @return
     */
    private boolean removeTheWeakerLoop(final List<LoopElement> loopElements, final LoopElement loopElement1,
            final LoopElement loopElement2, final int indexOfLoop1, final int indexOfLoop2) {

        if (loopElement1.getNumberOfReplacedElements() > loopElement2.getNumberOfReplacedElements()) {
            loopElements.remove(indexOfLoop2);
            return false;
        } else if (loopElement2.getNumberOfReplacedElements() > loopElement1.getNumberOfReplacedElements()) {
            loopElements.remove(indexOfLoop1);
            return true;
        } else if (loopElement1.getLoopSequence().size() <= loopElement2.getLoopSequence().size()) {
            loopElements.remove(indexOfLoop2);
            return false;
        } else {
            loopElements.remove(indexOfLoop1);
            return true;
        }
    }

    /**
     * Determines the loop count by checking how often the loop is iterated in a row.
     * 
     * O(e_b/e_l)
     *
     * @param loopElement
     *            whose loopCount is determined
     * @param branchSequence
     *            within the loopCount is determined
     */
    private void determineLoopCount(final LoopElement loopElement, final List<ISequenceElement> branchSequence) {
        int loopCount = 2;
        // O(e_b/e_l)
        for (int i = loopElement.getEndIndexInBranchSequence(); 
        		(i + loopElement.getLoopSequence().size()) < branchSequence.size(); 
        		i = i + loopElement.getLoopSequence().size()) {
            boolean isAdditionalLoop = true;
            // O(e_l)
            for (int j = 0; j < loopElement.getLoopSequence().size(); j++) {
                final ISequenceElement element1 = branchSequence.get(loopElement.getStartIndexInBranchSequence() + j);
                final ISequenceElement element2 = branchSequence.get(i + 1 + j);
                if(!element1.getClass().equals(ExitElement.class) 
                		&& !element1.getClass().equals(LoopElement.class)) {
                	if(!element1.equals(element2)) {
                		isAdditionalLoop = false;
                        break;
                	}
                } else {
                    isAdditionalLoop = false;
                    break;
                }
            }
            if (isAdditionalLoop) {
                loopCount++;
            } else {
                break;
            }
        }

        loopElement.setLoopCount(loopCount);
        loopElement.setNumberOfReplacedElements(loopCount * loopElement.getLoopSequence().size());
        loopElement.setEndIndexInBranchSequence(
                (loopElement.getStartIndexInBranchSequence() + loopElement.getNumberOfReplacedElements()) - 1);
    }
}
