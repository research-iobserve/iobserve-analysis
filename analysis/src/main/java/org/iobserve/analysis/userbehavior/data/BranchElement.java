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
import java.util.Objects;

/**
 * Represents a branch by branch transitions that is contained within a sequence. Used for branches
 * that are merged. It can be added to a sequence
 *
 * @author David Peter
 * @author Robert Heinrich
 * @author Nicolas Boltz
 */
public class BranchElement extends ISequenceElement {

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
    public boolean equals(Object o) {
    	if(o instanceof BranchElement) {
    		BranchElement branchElement2 = (BranchElement)o;
        	return doBranchElementsMatch(this, branchElement2);
        }
    	
    	return false;
    }
    
    private static boolean doBranchElementsMatch(BranchElement branchElement1, BranchElement branchElement2) {
    	if(branchElement1.getBranchTransitions().size() != branchElement2.getBranchTransitions().size()) {
    		return false;
    	}
		for(BranchTransitionElement branchTransitionElement1 : branchElement1.getBranchTransitions()) {
			boolean transitionsMatch = false;
			for(BranchTransitionElement branchTransitionElement2 : branchElement2.getBranchTransitions())  {
				// Better performance, comparing likelihood is enough to rule out most branch transitions
				if(branchTransitionElement1.getTransitionLikelihood() == branchTransitionElement2.getTransitionLikelihood()) {
					if(doSequencesMatch(branchTransitionElement1.getBranchSequence(), branchTransitionElement2.getBranchSequence())) {
						transitionsMatch = true;
						break;
					}
				}
				
				
			}
			if(transitionsMatch == false) {
				return false;
			}
    	}
		return true;
    }

    @Override
    public int hashCode() {
    	return Objects.hash(this.getClass(), this.branchTransitions.size());
    }
}
