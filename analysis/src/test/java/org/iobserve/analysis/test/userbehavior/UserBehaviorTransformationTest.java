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
package org.iobserve.analysis.test.userbehavior;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.behavior.karlsruhe.UserBehaviorTransformation;
import org.iobserve.analysis.test.userbehavior.builder.SimpleSequenceReference;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.provider.deprecated.RepositoryLookupModelProvider;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBehaviorTransformationTest.class);

    /**
     * Test class.
     */
    public UserBehaviorTransformationTest() {
        // empty constructor
    }

    /**
     * TODO this test is dysfunctional as models are missing Test branch within a loop.
     *
     * @throws IOException
     *             when reading and writing files.
     */
    // @Test
    public void testBranchWithinLoop() throws IOException {

        final ModelResource<Repository> repositoryModelProvider = new ModelResource<>(RepositoryPackage.eINSTANCE,
                new File("x"));
        final RepositoryLookupModelProvider repositoryLookupModel = new RepositoryLookupModelProvider(
                repositoryModelProvider.getAndLockModelRootNode(Repository.class,
                        RepositoryPackage.Literals.REPOSITORY));

        final int numberOfIterations = 500;
        final int stepSize = 1;
        final List<AccuracyResults> results = new ArrayList<>();

        for (int i = 1; i <= numberOfIterations; i += stepSize) {
            final int numberOfUserGroups = UserBehaviorTransformationTest.NUMBER_OF_USER_GROUPS;
            final int varianceOfUserGroups = 0;
            final int thinkTime = UserBehaviorTransformationTest.THINK_TIME;
            final boolean isClosedWorkload = UserBehaviorTransformationTest.CLOSED_WORKLOAD;

            final ReferenceElements referenceElements = SimpleSequenceReference.getModel(
                    UserBehaviorTransformationTest.REFERENCE_USAGE_MODEL, repositoryLookupModel, null, thinkTime,
                    isClosedWorkload);

            final UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), numberOfUserGroups, varianceOfUserGroups,
                    isClosedWorkload, thinkTime, repositoryLookupModel, null);

            behaviorModeling.modelUserBehavior();

            final AccuracyResults accuracyResults = UserBehaviorEvaluation
                    .matchUsageModels(behaviorModeling.getPcmUsageModel(), referenceElements.getUsageModel());
            results.add(accuracyResults);

            final double relativeMeasurementError = WorkloadEvaluation.calculateRME(behaviorModeling.getPcmUsageModel(),
                    referenceElements);

            if (UserBehaviorTransformationTest.LOGGER.isDebugEnabled()) {
                UserBehaviorTransformationTest.LOGGER.debug("RME " + relativeMeasurementError);
            }

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(),
                    UserBehaviorTransformationTest.OUTPUT_USAGE_MODEL);

            if (UserBehaviorTransformationTest.LOGGER.isDebugEnabled()) {
                UserBehaviorTransformationTest.LOGGER.debug("Iteration :" + i + "/" + numberOfIterations);
            }
        }

        TestHelper.writeAccuracyResults(results);
    }
}
