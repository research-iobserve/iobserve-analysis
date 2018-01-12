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
package org.iobserve.analysis.userbehavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.model.provider.neo4j.RepositoryModelProvider;
import org.iobserve.analysis.userbehavior.builder.SimpleSequenceReference;

/**
 * Test of the UserBehaviorTransformation.
 *
 * TODO this test is broken. Relevant models are note provided. Therefore, the test crashes with a
 * null pointer exception.
 *
 * @author unknown
 *
 */
public final class UserBehaviorTransformationTest {

    private static final int THINK_TIME = 1;
    private static final int NUMBER_OF_USER_GROUPS = 1;
    private static final boolean CLOSED_WORKLOAD = true;

    private static final String USAGE_MODEL_FOLDER = "output/usageModels/";

    private static final String OUTPUT_USAGE_MODEL = UserBehaviorTransformationTest.USAGE_MODEL_FOLDER
            + "OutputModel.usagemodel";

    private static final String REFERENCE_USAGE_MODEL = UserBehaviorTransformationTest.USAGE_MODEL_FOLDER
            + "ReferenceModel.usagemodel";

    private static final Log LOG = LogFactory.getLog(UserBehaviorTransformationTest.class);

    /**
     * Test class.
     */
    public UserBehaviorTransformationTest() {

    }

    /**
     * Test branch within a loop.
     *
     * @throws IOException
     *             when reading and writing files.
     */
    // @Test
    public void testBranchWithinLoop() throws IOException {

        final ICorrespondence correspondenceModel = null; // TODO load that model
        final RepositoryModelProvider repositoryModelProvider = null; // TODO load that model

        final int numberOfIterations = 500;
        final int stepSize = 1;
        final List<AccuracyResults> results = new ArrayList<>();

        for (int i = 1; i <= numberOfIterations; i += stepSize) {
            final int numberOfUserGroups = UserBehaviorTransformationTest.NUMBER_OF_USER_GROUPS;
            final int varianceOfUserGroups = 0;
            final int thinkTime = UserBehaviorTransformationTest.THINK_TIME;
            final boolean isClosedWorkload = UserBehaviorTransformationTest.CLOSED_WORKLOAD;

            final ReferenceElements referenceElements = SimpleSequenceReference.getModel(
                    UserBehaviorTransformationTest.REFERENCE_USAGE_MODEL, repositoryModelProvider, correspondenceModel,
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

            UserBehaviorTransformationTest.LOG.debug("RME " + relativeMeasurementError);

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(),
                    UserBehaviorTransformationTest.OUTPUT_USAGE_MODEL);

            UserBehaviorTransformationTest.LOG.debug("Iteration :" + i + "/" + numberOfIterations);
        }

        TestHelper.writeAccuracyResults(results);
    }
}
