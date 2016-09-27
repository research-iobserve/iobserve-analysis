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

package org.iobserve.analysis.userbehavior;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.userbehavior.data.BranchModel;

/**
 * Entry Point of the aggregation of the single entryCall sequences to a coherent model. At that the
 * single sequences are aggregated to a tree-like structure: Equal sequences are summarized to one
 * sequence, alternative sequences are represented via branches. The likelihood of each branch
 * depends on the occurrence frequency of its sequence. The result is a BranchModel for each
 * EntryCallSequenceModel(user group).
 *
 * @author David Peter, Robert Heinrich
 */
public class BranchExtraction {

    private final List<EntryCallSequenceModel> entryCallSequenceModels;
    private List<BranchModel> branchModels = null;

    /**
     * @param entryCallSequenceModels
     *            are the entryCallSequenceModels of the extracted user groups(For each user group
     *            one entryCallSequenceModel)
     */
    public BranchExtraction(final List<EntryCallSequenceModel> entryCallSequenceModels) {
        this.entryCallSequenceModels = entryCallSequenceModels;
    }

    public void createCallBranchModels() {

        final BranchModelCreator modelCreator = new BranchModelCreator();
        this.branchModels = new ArrayList<BranchModel>();

        for (final EntryCallSequenceModel entryCallSequenceModel : this.entryCallSequenceModels) {
            /**
             * 1. Aggregates the single EntryCall sequences to a BranchModel
             */
            final BranchModel branchModel = modelCreator.createCallBranchModel(entryCallSequenceModel);

            /**
             * 2. Calculates the likelihoods of the branches of the obtained BranchModel
             */
            modelCreator.calculateLikelihoodsOfBranches(branchModel);

            /**
             * 3. Tries to fuse branches to obtain a more compact model
             */
            modelCreator.compactBranchModel(branchModel);

            this.branchModels.add(branchModel);
        }

    }

    /**
     *
     * @return the created BranchModels. For each user group one BranchModel
     */
    public List<BranchModel> getBranchOperationModels() {
        return this.branchModels;
    }

}
