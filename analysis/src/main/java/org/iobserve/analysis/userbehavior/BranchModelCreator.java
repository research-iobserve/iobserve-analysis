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

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.userbehavior.data.Branch;
import org.iobserve.analysis.userbehavior.data.BranchModel;
import org.iobserve.analysis.userbehavior.data.CallElement;
import org.iobserve.analysis.userbehavior.data.ExitElement;
import org.iobserve.analysis.userbehavior.data.ISequenceElement;

/**
 * This class holds a set of methods to create a BranchModel from an EntryCallSequenceModel and to
 * calculate the branch likelihoods of a BranchModel.
 *
 * @author David Peter
 * @author Robert Heinrich
 * @author Nicolas Boltz
 */
public class BranchModelCreator {

    /**
     * Descending Sort user sessions by call sequence length. User session with longest call
     * sequence will be first element.
     */
    private static final Comparator<UserSession> SORT_USER_SESSION_BY_CALL_SEQUENCE_SIZE = new Comparator<UserSession>() {

        @Override
        public int compare(final UserSession o1, final UserSession o2) {
            final int sizeO1 = o1.getEvents().size();
            final int sizeO2 = o2.getEvents().size();
            if (sizeO1 < sizeO2) {
                return 1;
            } else if (sizeO1 > sizeO2) {
                return -1;
            }
            return 0;
        }
    };

    

    /**
     * empty default constructor.
     */
    public BranchModelCreator() {

    }



    /**
     * It calculates for each branch of the passed BranchModel its likelihood.
     *
     * @param branchModel
     *            whose branch likelihoods should be calculated
     */
    public void calculateLikelihoodsOfBranches(final BranchModel branchModel) {
        // The likelihood of the root branch is always 1 because it is the starting branch for all
        // user sessions
        branchModel.getRootBranch().setBranchLikelihood(1);
        this.traverseBranch(branchModel.getRootBranch());
    }

    /**
     * Recursive traversing through the branches.
     *
     * @param branch
     *            is the start branch that is recursively traversed
     */
    private void traverseBranch(final Branch branch) {
        this.setChildBranchLikelihoods(branch);
        for (int i = 0; i < branch.getChildBranches().size(); i++) {
            this.traverseBranch(branch.getChildBranches().get(i));
        }
    }

    /**
     * It calculates the likelihoods of the branch's child branches.
     *
     * @param examinedBranch
     *            whose child branches' likelihoods are calculated and set
     */
    private void setChildBranchLikelihoods(final Branch examinedBranch) {
        double countOfParentNode = 0;
        if (examinedBranch.getBranchSequence().size() > 0) {
            countOfParentNode = examinedBranch.getBranchSequence().get(examinedBranch.getBranchSequence().size() - 1)
                    .getAbsoluteCount();
        } else if (examinedBranch.getChildBranches().size() > 0) {
            for (int i = 0; i < examinedBranch.getChildBranches().size(); i++) {
                countOfParentNode += examinedBranch.getChildBranches().get(i).getBranchSequence().get(0)
                        .getAbsoluteCount();
            }
        } else {
            countOfParentNode = 1;
        }
        for (int i = 0; i < examinedBranch.getChildBranches().size(); i++) {
            final double countOfChildNode;
            countOfChildNode = examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getAbsoluteCount();
            final double likelihhod = countOfChildNode / countOfParentNode;
            examinedBranch.getChildBranches().get(i).setBranchLikelihood(likelihhod);
        }
    }

    /**
     * It creates a BranchModel from an EntryCallSequenceModel. At that the single sequences are
     * aggregated to a tree-like structure: Equal sequences are summarized to one sequence,
     * alternative sequences are represented via branches.
     *
     * @param entryCallSequenceModel
     *            whose call sequences are aggregated to a coherent CallBranchModel
     * @return a BranchModel corresponding to the passed EntryCallSequenceModel
     */
    public BranchModel createCallBranchModel(final EntryCallSequenceModel entryCallSequenceModel) {

        // Sets the user group's specific workload intensity and likelihood
        final BranchModel branchModel = new BranchModel(entryCallSequenceModel.getWorkloadIntensity(),
                entryCallSequenceModel.getLikelihoodOfUserGroup());

        final List<UserSession> userSessions = entryCallSequenceModel.getUserSessions();

        // The initial branch that contains the root node
        // Every sequence passes this branch -> likelihood of reaching this branch is 1
        final Branch rootBranch = new Branch();
        rootBranch.setBranchLikelihood(1);
        rootBranch.setBranchId(1);
        rootBranch.setTreeLevel(0);

        // Descending sort by call sequence length
        Collections.sort(userSessions, BranchModelCreator.SORT_USER_SESSION_BY_CALL_SEQUENCE_SIZE);

        // Initializes the root sequence with the longest call sequence
        this.setBranchSequence(rootBranch, userSessions.get(0).getEvents(), 0);

        int numberOfBranches = 1;

        // loops over all userSession without the first user session that initialized the rootBranch
        for (int j = 1; j < userSessions.size(); j++) {

            final UserSession userSession = userSessions.get(j);
            // The branchGuide guides through the tree structure. It determines the recent regarded
            // branch
            final List<Integer> branchGuide = new ArrayList<>();
            // The position states the recent position within the branch sequence
            int positionInBranch = 0;

            for (int i = 0; i <= userSession.getEvents().size(); i++) {

                // Determines which branch is currently examined
                final Branch examinedBranch = this.getExaminedBranch(branchGuide, rootBranch);

                if (i < userSession.getEvents().size()) {

                    final EntryCallEvent callEvent = userSession.getEvents().get(i);

                    // Checks whether there is a match between the call event and the element in the
                    // currently examined branch
                    if (this.checkPositionMatchInBranch(callEvent, examinedBranch, positionInBranch)) {
                        this.incrementCountOfBranchElement(examinedBranch, positionInBranch);
                        positionInBranch++;
                        continue;
                    }

                    // Checks whether there is a match between the call event and a first element of
                    // a child branch
                    if (this.isPositionLastElementInBranchSequence(examinedBranch, positionInBranch)) {
                        final int indexOfMatchingChildBranch = this.getIndexOfMatchingChildBranch(callEvent,
                                examinedBranch);
                        if (indexOfMatchingChildBranch > -1) {
                            // Continue with the same call event but switching to the new branch
                            branchGuide.add(indexOfMatchingChildBranch);
                            i--; // NOCS
                            positionInBranch = 0;
                            continue;
                        }
                    }

                    // No match could be found --> Split branch into child branches
                    numberOfBranches = this.splitBranch(examinedBranch, positionInBranch, numberOfBranches, false,
                            userSession, i);
                    break;

                } else { // End of sequence -> looking for an exit element
                    if (this.checkIfBranchSequenceTerminates(examinedBranch, positionInBranch)) {
                        this.incrementCountOfBranchElement(examinedBranch, positionInBranch);
                        break;
                    }
                    // Checks if there is an exit branch
                    if (this.isPositionLastElementInBranchSequence(examinedBranch, positionInBranch)) {
                        final int indexOfMatchingChildBranch = this.getIndexOfMatchingExitBranch(examinedBranch);
                        if (indexOfMatchingChildBranch > -1) {
                            // Iterate the exit state adding but switching to the new branch
                            branchGuide.add(indexOfMatchingChildBranch);
                            i--; // NOCS
                            positionInBranch = 0;
                            continue;
                        }
                    }

                    // No matching exit element found --> Split branch into child branches
                    numberOfBranches = this.splitBranch(examinedBranch, positionInBranch, numberOfBranches, true, null,
                            0);
                    break;

                }

            }

        }

        branchModel.setRootBranch(rootBranch);
        branchModel.setNumberOfBranches(numberOfBranches);

        return branchModel;
    }

    /**
     * It creates a new branch and adds it to the current examined branch as a new child branch.
     *
     * TODO this function is largely broken as it modifies input parameter
     *
     * @param examinedBranch
     *            that is splitted
     * @param positionInBranch
     *            states the recent regarded position in branch sequence
     * @param numberOfBranches
     *            is the recent amount of branches
     * @param isExit
     *            states if it is the end of a sequence
     * @param userSession
     *            if it is not the end of the sequence, the user session holds the rest of sequence
     *            that will be added to the new branch
     * @param indexOfCallEvent
     *            if it is not the end of the sequence, it states at which position in the sequence
     *            it is added to the new branch
     */
    private int splitBranch(Branch examinedBranch, final int positionInBranch, int numberOfBranches, // NOCS
            final boolean isExit, // NOCS
            final UserSession userSession, final int indexOfCallEvent) { // NOCS
        // If there is already a split at that position add a new branch
        if (this.isPositionLastElementInBranchSequence(examinedBranch, positionInBranch)) {
            this.addNewBranch(examinedBranch, numberOfBranches);
            numberOfBranches++; // NOCS
        } else { // Else split the branch into two branches
            this.splitBranch(examinedBranch, positionInBranch, numberOfBranches);
            numberOfBranches = numberOfBranches + 2; // NOCS
        }
        final int indexOfNewAddedBranch = examinedBranch.getChildBranches().size() - 1;
        examinedBranch = examinedBranch.getChildBranches().get(indexOfNewAddedBranch); // NOCS

        // Adds an exit element to the new exit branch
        if (isExit) {
            this.setExitElement(examinedBranch);
            // Adds the branch sequence to the new branch
        } else {
            this.setBranchSequence(examinedBranch, userSession.getEvents(), indexOfCallEvent);
        }

        return numberOfBranches;

    }

    /**
     * Sets the passed events as branch sequence of the passed branch. The sequenceStartIndex
     * defines at which position of the passed events the branch sequence starts
     *
     * @param examinedBranch
     *            for that the branchSequence will be set
     * @param events
     *            represent the sequence to set
     * @param sequenceStartIndex
     *            states at which position of the passed events the branch sequence starts
     */
    private void setBranchSequence(final Branch examinedBranch, final List<EntryCallEvent> events,
            final int sequenceStartIndex) {
        final List<ISequenceElement> branchSequence = new ArrayList<>();
        for (int j = sequenceStartIndex; j < events.size(); j++) {
            final EntryCallEvent callEvent = events.get(j);
            final CallElement callElement = new CallElement(callEvent.getClassSignature(),
                    callEvent.getOperationSignature());
            callElement.setAbsoluteCount(1);
            branchSequence.add(callElement);
        }
        final ExitElement exitElement = new ExitElement();
        exitElement.setAbsoluteCount(1);
        branchSequence.add(exitElement);
        examinedBranch.setBranchSequence(branchSequence);
    }

    /**
     * Adds an exit element to the end of the branch sequence of the passed branch.
     *
     * @param examinedBranch
     *            receives an exit element at the end of its branch sequence
     */
    private void setExitElement(final Branch examinedBranch) {
        final ExitElement exitElement = new ExitElement();
        exitElement.setAbsoluteCount(1);
        examinedBranch.getBranchSequence().add(exitElement);
    }

    /**
     * Adds a new child branch to the passed branch. The branch id of the new branch is the current
     * number of branches.
     *
     * @param examinedBranch
     *            receives a new child branch
     * @param numberOfBranches
     *            is the current overall number of branches. Defines the id of the new added branch
     */
    private void addNewBranch(final Branch examinedBranch, final int numberOfBranches) {
        final Branch childBranch = new Branch();
        childBranch.setBranchId(numberOfBranches + 1);
        examinedBranch.addBranch(childBranch);
    }

    /**
     * It splits the passed branch at the passed position of its branch sequence into two child
     * branches. The branch sequence of the passed branch is shorted to the position the split is
     * performed. The first child branch receives the remaining branch sequence, starting at the
     * position the split is performed. The second child branch´s sequence stays empty and is filled
     * later
     *
     * @param examinedBranch
     *            is split into two branches
     * @param positionInBranch
     *            is the position in the branch sequence where the split is performed
     * @param numberOfBranches
     *            is the current overall number of branches. Defines the new branch ids
     */
    private void splitBranch(final Branch examinedBranch, final int positionInBranch, final int numberOfBranches) {
        final Branch childBranch1 = new Branch();
        final Branch childBranch2 = new Branch();

        final List<ISequenceElement> branchSequence = new ArrayList<>(
                examinedBranch.getBranchSequence().subList(0, positionInBranch));
        final List<ISequenceElement> branchSequence1 = new ArrayList<>(examinedBranch.getBranchSequence()
                .subList(positionInBranch, examinedBranch.getBranchSequence().size()));
        final List<ISequenceElement> branchSequence2 = new ArrayList<>();

        examinedBranch.setBranchSequence(branchSequence);
        childBranch1.setBranchSequence(branchSequence1);
        childBranch2.setBranchSequence(branchSequence2);

        childBranch1.setBranchId(numberOfBranches + 1);
        childBranch2.setBranchId(numberOfBranches + 2);

        for (final Branch childBranch : examinedBranch.getChildBranches()) {
            childBranch1.addBranch(childBranch);
        }
        examinedBranch.getChildBranches().clear();

        examinedBranch.addBranch(childBranch1);
        examinedBranch.addBranch(childBranch2);
    }

    /**
     * Returns the index of a child branch whose first branch sequence element matches the passed
     * EntryCallEvent If there is no matching child branch -1 is returned.
     *
     * @param callEvent
     *            that is checked for a match
     * @param examinedBranch
     *            whose child branches´ first sequence element is checked for a match with the
     *            passed EntryCallEvent
     * @return the index of a matching childBranch. Else -1
     */
    private int getIndexOfMatchingChildBranch(final EntryCallEvent callEvent, final Branch examinedBranch) {
        if (examinedBranch.getChildBranches().size() == 0) {
            return -1;
        }
        for (int i = 0; i < examinedBranch.getChildBranches().size(); i++) {
            if (examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getClass()
                    .equals(CallElement.class)) {
                final CallElement callElement = (CallElement) examinedBranch.getChildBranches().get(i)
                        .getBranchSequence().get(0);
                if (this.isCallEventCallElementMatch(callEvent, callElement)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Returns the index of a child branch whose first branch sequence element is an ExitElement,
     * i.e. whose sequence ends without any further callElement. If there is no matching child
     * branch -1 is returned.
     *
     * @param examinedBranch
     *            whose child branches´ first sequence element is checked for an exit element
     * @return the index of a matching childBranch. Else -1
     */
    private int getIndexOfMatchingExitBranch(final Branch examinedBranch) {
        if (examinedBranch.getChildBranches().size() == 0) {
            return -1;
        }
        for (int i = 0; i < examinedBranch.getChildBranches().size(); i++) {
            if (examinedBranch.getChildBranches().get(i).getBranchSequence().get(0).getClass()
                    .equals(ExitElement.class)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if there is an ExitElement at the passed position in the branch sequence of the passed
     * branch.
     *
     * @param examinedBranch
     *            holds the branch sequence that is examined for an ExitElement
     * @param positionInBranch
     *            is the position where it is looked for an ExitElement in the branch sequence of
     *            the passed branch
     * @return
     */
    private boolean checkIfBranchSequenceTerminates(final Branch examinedBranch, final int positionInBranch) {
        if (positionInBranch >= examinedBranch.getBranchSequence().size()) {
            return false;
        }
        if (examinedBranch.getBranchSequence().get(positionInBranch).getClass().equals(ExitElement.class)) {
            return true;
        }
        return false;
    }

    /**
     * Increments the count of an branch element.
     *
     * @param examinedBranch
     *            holds the branch sequence´s element that is incremented
     * @param positionInBranch
     *            specifies the branch sequence´s element whose count is incremented
     */
    private void incrementCountOfBranchElement(final Branch examinedBranch, final int positionInBranch) {
        final int absoluteCount = examinedBranch.getBranchSequence().get(positionInBranch).getAbsoluteCount() + 1;
        examinedBranch.getBranchSequence().get(positionInBranch).setAbsoluteCount(absoluteCount);
    }

    /**
     * Returns the branch that is described in the branch Guide. The branch guide holds the child
     * branches´ indices. It traverses through the root branch until the desired child branch is
     * reached and returns it.
     *
     * @param branchGuide
     *            holds the child branches´ indices
     * @param rootBranch
     *            is traversed until the current branch is reached
     * @return the child branch that is described in the branchGuide
     */
    private Branch getExaminedBranch(final List<Integer> branchGuide, final Branch rootBranch) {
        Branch examinedBranch = rootBranch;
        for (int i = 0; i < branchGuide.size(); i++) {
            examinedBranch = examinedBranch.getChildBranches().get(branchGuide.get(i));
        }
        return examinedBranch;
    }

    /**
     * Checks if a callEvent and a callElement match.
     *
     * @param callEvent
     *            that will be matched with the callElement
     * @param callElement
     *            that will be matched with the callEvent
     * @return if the passed callEvent and callElement match
     */
    private boolean isCallEventCallElementMatch(final EntryCallEvent callEvent, final CallElement callElement) {
        return callEvent.getOperationSignature().equals(callElement.getOperationSignature())
                && callEvent.getClassSignature().equals(callElement.getClassSignature());
    }

    /**
     * Checks if the passed callEvent matches the callElement at the passed position in the branch
     * sequence of the passed branch.
     *
     * @param callEvent
     *            that will be matched
     * @param examinedBranch
     *            holds the branch sequence whose branch element will be matched at the passed
     *            position
     * @param positionInBranch
     *            is the position of the branch element within the branch sequence that will be
     *            matched against the passed call event
     * @return if the callEvent matches the branch element at the passed position of the passed
     *         branch
     */
    private boolean checkPositionMatchInBranch(final EntryCallEvent callEvent, final Branch examinedBranch,
            final int positionInBranch) {
        if (positionInBranch < examinedBranch.getBranchSequence().size()) {
            if (examinedBranch.getBranchSequence().get(positionInBranch).getClass().equals(CallElement.class)) {
                final CallElement callElement = (CallElement) examinedBranch.getBranchSequence().get(positionInBranch);
                return this.isCallEventCallElementMatch(callEvent, callElement);
            }
        }

        return false;
    }

    /**
     * Checks if the passed positionInBranch is equal to the length of the branch sequence of the
     * passed branch.
     *
     * @param examinedBranch
     *            whose branch sequence length is checked
     * @param positionInBranch
     *            is the value that will be matched with the length of the branch sequence of the
     *            passed branch
     * @return if the passed positionInBranch matches the length of the branch sequence
     */
    private boolean isPositionLastElementInBranchSequence(final Branch examinedBranch, final int positionInBranch) {
        return examinedBranch.getBranchSequence().size() == positionInBranch;
    }
}
