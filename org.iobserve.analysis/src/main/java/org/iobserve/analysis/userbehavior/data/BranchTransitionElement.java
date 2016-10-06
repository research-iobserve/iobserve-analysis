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
 * Represents the branch transitions of a branch element. Used to represent the branched behavior of
 * branches that were subsequently merged.
 *
 * @author David Peter, Robert Heinrich
 */
public class BranchTransitionElement {

    private double transitionLikelihood;
    private List<ISequenceElement> branchSequence;

    public BranchTransitionElement() {
        this.branchSequence = new ArrayList<ISequenceElement>();
    }

    public double getTransitionLikelihood() {
        return this.transitionLikelihood;
    }

    public void setTransitionLikelihood(final double transitionLikelihood) {
        this.transitionLikelihood = transitionLikelihood;
    }

    public List<ISequenceElement> getBranchSequence() {
        return this.branchSequence;
    }

    public void setBranchSequence(final List<ISequenceElement> branchSequence) {
        this.branchSequence = branchSequence;
    }

}
