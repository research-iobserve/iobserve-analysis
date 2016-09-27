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
package org.iobserve.analysis.userbehavior.test;

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

    public boolean equals(final ModelElement modelElement) {
        if (this.isStartElement != modelElement.isStartElement) {
            return false;
        }
        if (this.isStopElement != modelElement.isStopElement) {
            return false;
        }
        if (this.isCallElement != modelElement.isCallElement) {
            return false;
        }
        if (this.isBranchTransitionElement != modelElement.isBranchTransitionElement) {
            return false;
        }
        if (this.isLoopElement != modelElement.isLoopElement) {
            return false;
        }
        if (!this.entiteyName.equals(modelElement.entiteyName)) {
            return false;
        }
        if (!this.loopIteration.equals(modelElement.loopIteration)) {
            return false;
        }
        if (this.branchProbability != modelElement.branchProbability) {
            return false;
        }

        return true;
    }

}
