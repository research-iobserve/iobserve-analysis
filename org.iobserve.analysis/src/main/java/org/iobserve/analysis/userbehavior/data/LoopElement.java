/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.userbehavior.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a loop element. It contains the iterated sequence and the number of iterations as loop
 * count. The start and end index states at which index the iterated segment that is represented by
 * the loop element starts and ends within the sequence
 *
 * @author David Peter, Robert Heinrich
 */
public class LoopElement implements ISequenceElement {

    private int loopId;
    private List<ISequenceElement> loopSequence = new ArrayList<>();
    private int loopCount;
    private int startIndexInBranchSequence;
    private int endIndexInBranchSequence;
    private int numberOfReplacedElements;
    private int currentIndexToCheck;
    private boolean doContainBranchElement;

    /**
     * Entity constructor.
     */
    public LoopElement() {
    }

    /**
     * Add a sequence to this loop element.
     *
     * @param element
     *            the element to be added
     */
    public void addElementToLoopSequence(final ISequenceElement element) {
        this.loopSequence.add(element);
    }

    /**
     * Add a call element to this loop element.
     *
     * @param index
     *            where the element must be inserted
     * @param element
     *            the element to be inserted
     */
    public void insertElementToLoopSequence(final int index, final ISequenceElement element) {
        this.loopSequence.add(index, element);
    }

    public List<ISequenceElement> getLoopSequence() {
        return this.loopSequence;
    }

    public void setLoopSequence(final List<ISequenceElement> loopSequence) {
        this.loopSequence = loopSequence;
    }

    public int getLoopCount() {
        return this.loopCount;
    }

    public void setLoopCount(final int loopCount) {
        this.loopCount = loopCount;
    }

    public int getStartIndexInBranchSequence() {
        return this.startIndexInBranchSequence;
    }

    public void setStartIndexInBranchSequence(final int startIndexInBranchSequence) {
        this.startIndexInBranchSequence = startIndexInBranchSequence;
    }

    public int getEndIndexInBranchSequence() {
        return this.endIndexInBranchSequence;
    }

    public void setEndIndexInBranchSequence(final int endIndexInBranchSequence) {
        this.endIndexInBranchSequence = endIndexInBranchSequence;
    }

    public int getNumberOfReplacedElements() {
        return this.numberOfReplacedElements;
    }

    public void setNumberOfReplacedElements(final int numberOfReplacedElements) {
        this.numberOfReplacedElements = numberOfReplacedElements;
    }

    @Override
    public int getAbsoluteCount() {
        return 0;
    }

    @Override
    public void setAbsoluteCount(final int absoluteCount) {
    }

    @Override
    public String getClassSignature() {
        return null;
    }

    @Override
    public String getOperationSignature() {
        return null;
    }

    public int getCurrentIndexToCheck() {
        return this.currentIndexToCheck;
    }

    public void setCurrentIndexToCheck(final int currentIndexToCheck) {
        this.currentIndexToCheck = currentIndexToCheck;
    }

    /**
     * Reinitialize loop element.
     *
     * @param loopElement
     *            the new loop element
     */
    public void copyLoopValues(final LoopElement loopElement) {
        this.loopSequence = loopElement.getLoopSequence();
        this.loopCount = loopElement.getLoopCount();
        this.startIndexInBranchSequence = loopElement.getStartIndexInBranchSequence();
        this.endIndexInBranchSequence = loopElement.getEndIndexInBranchSequence();
        this.numberOfReplacedElements = loopElement.getNumberOfReplacedElements();
        this.currentIndexToCheck = loopElement.getCurrentIndexToCheck();
        this.loopId = loopElement.getLoopId();
    }

    public int getLoopId() {
        return this.loopId;
    }

    public void setLoopId(final int loopId) {
        this.loopId = loopId;
    }

    public boolean isDoContainBranchElement() {
        return this.doContainBranchElement;
    }

    public void setDoContainBranchElement(final boolean doContainBranchElement) {
        this.doContainBranchElement = doContainBranchElement;
    }

}
