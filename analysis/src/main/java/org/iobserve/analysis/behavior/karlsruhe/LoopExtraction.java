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
package org.iobserve.analysis.behavior.karlsruhe;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.behavior.karlsruhe.data.BranchModel;

/**
 * Entry point of the LoopExtraction process that detects iterated behavior within the passed
 * BranchModels. At that, single callElements, sequences of callElements and branched sequences that
 * are iterated in a row are detected and summarized to loops containing the number of loops as the
 * count of each loop. If several loops overlap (More than one loop uses the same callElement) the
 * loop is used that replaces the most callElements. The result is one LoopBranchModel for each user
 * group that additionally contains loops for iterated entryCalls.
 *
 * @author David Peter, Robert Heinrich
 */

public class LoopExtraction {

    private final List<BranchModel> branchModels;
    private List<BranchModel> loopBranchModels;

    /**
     *
     * @param branchModels
     *            are the BranchModels that are created by the BranchExtraction process and that are
     *            checked for iterated behavior
     */
    public LoopExtraction(final List<BranchModel> branchModels) {
        this.branchModels = branchModels;
    }

    /**
     * Executes the extraction of iterated behavior process.
     */
    public void createCallLoopBranchModels() {

        final LoopBranchModelCreator modelCreator = new LoopBranchModelCreator();
        this.loopBranchModels = new ArrayList<>();

        for (final BranchModel branchModel : this.branchModels) {
            // Each BranchModel is checked for iterated behavior
            modelCreator.detectLoopsInCallBranchModel(branchModel);
            this.loopBranchModels.add(branchModel);
        }

    }

    /**
     *
     * @return the created LoopBranchModels that contain iterated behavior in form of loops
     */
    public List<BranchModel> getloopBranchModels() {
        return this.loopBranchModels;
    }

}
