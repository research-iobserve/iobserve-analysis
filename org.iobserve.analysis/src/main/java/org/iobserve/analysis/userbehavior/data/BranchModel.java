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

import java.util.List;

/**
 * Represents the BranchModel of a user group. It contains the user group's behavior in form of
 * branches. It also contains the user group's specific workload and occurrence likelihood of the
 * user group
 *
 * @author David Peter, Robert Heinrich
 */
public class BranchModel {

    private Branch rootBranch;
    private final WorkloadIntensity workloadIntensity;
    private final double likelihoodOfUserGroup;
    private int numberOfBranches;

    /**
     * Constructor.
     *
     * @param workloadIntensity
     *            the workload intensity for the branch
     * @param likelihoodOfUserGroup
     *            possibility of this branch
     */
    public BranchModel(final WorkloadIntensity workloadIntensity, final double likelihoodOfUserGroup) {
        this.workloadIntensity = workloadIntensity;
        this.likelihoodOfUserGroup = likelihoodOfUserGroup;
    }

    /**
     * get the requested branch. TODO what is the purpose of this?
     *
     * @param branchGuide
     *            selector for a branch
     * @return the selected branch
     */
    public Branch getExaminedBranch(final List<Integer> branchGuide) {
        if (this.rootBranch == null) {
            return null;
        }
        Branch examinedBranch = this.rootBranch;
        for (int i = 0; i < branchGuide.size(); i++) {
            examinedBranch = examinedBranch.getChildBranches().get(branchGuide.get(i));
        }
        return examinedBranch;
    }

    public Branch getRootBranch() {
        return this.rootBranch;
    }

    public void setRootBranch(final Branch rootBranch) {
        this.rootBranch = rootBranch;
    }

    public WorkloadIntensity getWorkloadIntensity() {
        return this.workloadIntensity;
    }

    public double getLikelihoodOfUserGroup() {
        return this.likelihoodOfUserGroup;
    }

    public int getNumberOfBranches() {
        return this.numberOfBranches;
    }

    public void setNumberOfBranches(final int numberOfBranches) {
        this.numberOfBranches = numberOfBranches;
    }

}
