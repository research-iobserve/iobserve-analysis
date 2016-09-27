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
 * Represents branched behavior and the probability of reaching the behavior
 *
 * @author David Peter, Robert Heinrich
 */
public class Branch {

    private int branchId;
    private int treeLevel;
    private List<SequenceElement> branchSequence;
    private double branchLikelihood;
    private List<Branch> childBranches;

    public Branch() {
        this.branchSequence = new ArrayList<SequenceElement>();
        this.childBranches = new ArrayList<Branch>();
        this.branchLikelihood = 0;
    }

    /**
     * Adds a branch as a child branch to the branch
     *
     * @param childBranch
     *            that is added as child branch
     */
    public void addBranch(final Branch childBranch) {
        this.childBranches.add(childBranch);
    }

    public List<SequenceElement> getBranchSequence() {
        return this.branchSequence;
    }

    public void setBranchSequence(final List<SequenceElement> branchSequence) {
        this.branchSequence = branchSequence;
    }

    public double getBranchLikelihood() {
        return this.branchLikelihood;
    }

    public void setBranchLikelihood(final double branchLikelihood) {
        this.branchLikelihood = branchLikelihood;
    }

    public List<Branch> getChildBranches() {
        return this.childBranches;
    }

    public void setChildBranches(final List<Branch> childBranches) {
        this.childBranches = childBranches;
    }

    public int getBranchId() {
        return this.branchId;
    }

    public void setBranchId(final int branchId) {
        this.branchId = branchId;
    }

    public int getTreeLevel() {
        return this.treeLevel;
    }

    public void setTreeLevel(final int treeLevel) {
        this.treeLevel = treeLevel;
    }

}
