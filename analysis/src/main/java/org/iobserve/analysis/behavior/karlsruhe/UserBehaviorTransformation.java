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

import java.io.IOException;
import java.util.List;

import org.iobserve.analysis.behavior.karlsruhe.data.BranchModel;
import org.iobserve.analysis.data.UserSessionCollectionModel;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.provider.deprecated.RepositoryLookupModelProvider;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Entry Point of the user behavior modeling. This class subsequently calls the user behavior
 * modeling processes and stores the resulting pcm usage model that can be retrieved via a getter
 * method. The modelUserBehavior method triggers the user behavior modeling. The constructor takes
 * the input entryCallSequenceModel that contains the user sessions that are used to analyze and
 * build the user behavior. The numberOfUserGroupsFromInputUsageModel states the number of user
 * groups in the latest created usage model. It serves as input for the number of clusters within
 * the user group detection. It also measures the response times of the individual process steps and
 * an overall response time
 *
 * @author David Peter, Robert Heinrich
 */
public class UserBehaviorTransformation {

    private final UserSessionCollectionModel inputEntryCallSequenceModel;
    private final int numberOfUserGroupsFromInputUsageModel;
    private final int varianceOfUserGroups;
    private final boolean isClosedWorkload;
    private final double thinkTime;
    private final CorrespondenceModel correspondenceModel;

    private UsageModel pcmUsageModel;
    private final RepositoryLookupModelProvider repositoryLookupModel;

    /**
     *
     * @param inputEntryCallSequenceModel
     *            contains the user sessions that are used to analyze and build the user behavior
     * @param numberOfUserGroupsFromInputUsageModel
     *            states the number of user groups in the latest created usage model. It serves as
     *            input for the number of clusters
     * @param varianceOfUserGroups
     *            enables the creation of a minimum and maximum number of clusters within the
     *            detection of user groups process
     * @param isClosedWorkload
     *            states whether a closed or open workload specification is requested by the user
     * @param thinkTime
     *            states the think time of a closed workload specification
     * @param repositoryLookupModel
     *            repository model provider
     * @param correspondenceModel
     *            necessary for the creation of a PCM usage model
     */
    public UserBehaviorTransformation(final UserSessionCollectionModel inputEntryCallSequenceModel,
            final int numberOfUserGroupsFromInputUsageModel, final int varianceOfUserGroups,
            final boolean isClosedWorkload, final double thinkTime,
            final RepositoryLookupModelProvider repositoryLookupModel, final CorrespondenceModel correspondenceModel) {
        this.inputEntryCallSequenceModel = inputEntryCallSequenceModel;
        this.numberOfUserGroupsFromInputUsageModel = numberOfUserGroupsFromInputUsageModel;
        this.varianceOfUserGroups = varianceOfUserGroups;
        this.isClosedWorkload = isClosedWorkload;
        this.thinkTime = thinkTime;
        this.correspondenceModel = correspondenceModel;
        this.repositoryLookupModel = repositoryLookupModel;
    }

    /**
     * It triggers the user behavior modeling. At that, it serves as a controller method that
     * subsequently invokes the single process steps that comprise 1. The detection of user groups
     * 2. The detection of branched behavior in form of branches 3. The detection of iterated
     * behavior in form of loops 4. The creation of a corresponding PCM usage model
     *
     * After performing this method the resulting user behavior model that contains for each user
     * group its specific behavior model can be retrieved via the getter method.
     *
     * @throws IOException
     *             on various errors
     */
    public void modelUserBehavior() throws IOException {

        if (this.inputEntryCallSequenceModel.getUserSessions().size() < 1) {
            return;
        }

        /**
         * 1. The extraction of user groups. It clusters the entry call sequence model to detect
         * different user groups within the user sessions. The result is a separate entry call
         * sequence model for each detected user group. Each entry call sequence model contains: -
         * the user sessions that are assigned to the user group - the likelihood of its user group
         * - the parameters for the workload intensity of its user group
         */
        final UserGroupExtraction extractionOfUserGroups = new UserGroupExtraction(this.inputEntryCallSequenceModel,
                this.numberOfUserGroupsFromInputUsageModel, this.varianceOfUserGroups, this.isClosedWorkload);
        extractionOfUserGroups.extractUserGroups();
        // The result of the user group extraction process is a separate EntryCallSequenceModel for
        // each user group
        final List<UserSessionCollectionModel> entryCallSequenceModels = extractionOfUserGroups
                .getEntryCallSequenceModelsOfUserGroups();

        /**
         * 2. The aggregation of the call sequences. It aggregates each call sequence model that is
         * created during the user group extraction into a branch model that consists of a tree like
         * structure: The single sequences are aggregated to one coherent model that contains
         * alternative sequences in form of branches. It detects branches and the branch
         * likelihoods. The result is one BranchModel for each user group.
         */
        final BranchExtraction branchExtraction = new BranchExtraction(entryCallSequenceModels);
        branchExtraction.createCallBranchModels();
        final List<BranchModel> branchModels = branchExtraction.getBranchOperationModels();

        /**
         * 3. The detection of iterated behavior. It detects iterated behavior within the created
         * BranchModels in form of loops. Single entryCalls or sequences of entryCalls that are
         * iterated in a row are detected and summarized to loops containing the number of loops as
         * the count of each loop. The result is one LoopBranchModel for each user group that
         * additionally contains loops for iterated entryCalls.
         */
        final LoopExtraction loopDetection = new LoopExtraction(branchModels);
        loopDetection.createCallLoopBranchModels();
        final List<BranchModel> loopBranchModels = loopDetection.getloopBranchModels();

        /**
         * 4. Modeling of the usage behavior. It creates a PCM usage model corresponding to the
         * LoopBranchModels. Each detected user group is represented as one usage scenario within
         * the usage model. It contains loops and branches corresponding to the LoopBranchModels.
         * The resulting PCM usage model can be retrieved via the getter method.
         */
        final PcmUsageModelBuilder pcmUsageModelBuilder = new PcmUsageModelBuilder(loopBranchModels,
                this.isClosedWorkload, this.thinkTime, this.repositoryLookupModel, this.correspondenceModel);
        this.pcmUsageModel = pcmUsageModelBuilder.createUsageModel();

    }

    /**
     * Returns the generated PCM Usage Model. The modelUserBehavior method that performs the
     * transformations from the passed entryCallSequenceModel and generates the PCM usage model has
     * to be invoked before.
     *
     * @return the generated PCM Usage Model
     */
    public UsageModel getPcmUsageModel() {
        return this.pcmUsageModel;
    }

}
