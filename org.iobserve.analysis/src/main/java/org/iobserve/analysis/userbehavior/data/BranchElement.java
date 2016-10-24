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
package org.iobserve.analysis.userbehavior.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a branch by branch transitions that is contained within a sequence. Used for branches
 * that are merged. It can be added to a sequence
 *
 * @author David Peter, Robert Heinrich
 */
public class BranchElement implements ISequenceElement {

    private List<BranchTransitionElement> branchTransitions;

    /**
     * Default constructor initializing branchTransition with a default array.
     */
    public BranchElement() {
        this.branchTransitions = new ArrayList<>();
    }

    /**
     * Add a transition.
     *
     * @param transition
     *            the transition to add
     */
    public void addBranchTransition(final BranchTransitionElement transition) {
        this.branchTransitions.add(transition);
    }

    public List<BranchTransitionElement> getBranchTransitions() {
        return this.branchTransitions;
    }

    public void setBranchTransitions(final List<BranchTransitionElement> branchTransitions) {
        this.branchTransitions = branchTransitions;
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

}
