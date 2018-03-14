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

/**
 * Contains the information of an PCM usage model element. Thereby, a model element can be any model
 * element of a PCM usage model. Is used to match the elements of two usage models against each
 * other. Used by the UserBehaviorEvaluation.
 *
 * @author David Peter, Robert Heinrich
 */
public class ModelElement {

    private final boolean isStartElement;
    private final boolean isStopElement;
    private final boolean isCallElement;
    private final boolean isBranchTransitionElement;
    private final boolean isLoopElement;
    private final String entiteyName;
    private final String loopIteration;
    private final double branchProbability;

    /**
     * Construct a model element.
     *
     * @param isStartElement
     *            indicate start element
     * @param isStopElement
     *            indicate stop element
     * @param isCallElement
     *            indicate call element
     * @param isBranchTransitionElement
     *            indicate branch transition element
     * @param isLoopElement
     *            indicate loop element
     * @param entiteyName
     *            the name of the element
     * @param loopIteration
     *            number of loop iterations for loop element
     * @param branchProbability
     *            probability of a branch
     */
    public ModelElement(final boolean isStartElement, final boolean isStopElement, final boolean isCallElement,
            final boolean isBranchTransitionElement, final boolean isLoopElement, final String entiteyName,
            final String loopIteration, final double branchProbability) {
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

    /**
     * Special equals method.
     *
     * @param modelElement
     * @return true if element is the equivalence of this element
     */
    public boolean equals(final ModelElement modelElement) { // NOCS
        return this.isStartElement == modelElement.isStartElement && this.isStopElement == modelElement.isStopElement
                && this.isCallElement == modelElement.isCallElement
                && this.isBranchTransitionElement == modelElement.isBranchTransitionElement
                && this.isLoopElement == modelElement.isLoopElement && this.entiteyName.equals(modelElement.entiteyName)
                && this.loopIteration.equals(modelElement.loopIteration)
                && this.branchProbability == modelElement.branchProbability;
    }

}
