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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.userbehavior.UserBehaviorTransformation;
import org.iobserve.analysis.userbehavior.test.builder.SimpleSequenceReference;

/**
 * Test of the TEntryEventSequence filter.
 *
 * @author Reiner Jung
 *
 */
public final class TEntryEventSequenceTest {

    private static final int THINK_TIME = 1;
    private static final int NUMBER_OF_USER_GROUPS = 1;
    private static final boolean CLOSED_WORKLOAD = true;

    private static final String USAGE_MODEL_FOLDER = "output/usageModels/";

    private static final String OUTPUT_USAGE_MODEL = TEntryEventSequenceTest.USAGE_MODEL_FOLDER
            + "OutputModel.usagemodel";

    private static final String REFERENCE_USAGE_MODEL = TEntryEventSequenceTest.USAGE_MODEL_FOLDER
            + "ReferenceModel.usagemodel";

    /**
     * Test class.
     */
    private TEntryEventSequenceTest() {

    }

    /**
     * Test branch within a loop.
     *
     * @throws IOException
     *             when reading and writing files.
     */
    @Test
    public void testBranchWithinLoop() throws IOException {

        final ICorrespondence correspondenceModel = null; // TODO load that model
        final RepositoryModelProvider repositoryModelProvider = null; // TODO load that model

        final int numberOfIterations = 500;
        final int stepSize = 1;
        final List<AccuracyResults> results = new ArrayList<>();

        for (int i = 1; i <= numberOfIterations; i += stepSize) {
            final int numberOfUserGroups = TEntryEventSequenceTest.NUMBER_OF_USER_GROUPS;
            final int varianceOfUserGroups = 0;
            final int thinkTime = TEntryEventSequenceTest.THINK_TIME;
            final boolean isClosedWorkload = TEntryEventSequenceTest.CLOSED_WORKLOAD;

            final ReferenceElements referenceElements = SimpleSequenceReference.getModel(
                    TEntryEventSequenceTest.REFERENCE_USAGE_MODEL, repositoryModelProvider, correspondenceModel,
                    thinkTime, isClosedWorkload);

            final UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), numberOfUserGroups, varianceOfUserGroups,
                    isClosedWorkload, thinkTime, repositoryModelProvider, correspondenceModel);

            behaviorModeling.modelUserBehavior();

            final AccuracyResults accuracyResults = UserBehaviorEvaluation
                    .matchUsageModels(behaviorModeling.getPcmUsageModel(), referenceElements.getUsageModel());
            results.add(accuracyResults);

            final double relativeMeasurementError = WorkloadEvaluation.calculateRME(behaviorModeling.getPcmUsageModel(),
                    referenceElements);

            System.out.println("RME " + relativeMeasurementError);

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(), TEntryEventSequenceTest.OUTPUT_USAGE_MODEL);

            System.out.println("Iteration :" + i + "/" + numberOfIterations);
        }

        TestHelper.writeAccuracyResults(results);
    }
}
