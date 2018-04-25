/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.privacy.violation;

import java.io.File;

import teetime.framework.test.StageTester;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.model.PCMModelHandler;
import org.iobserve.model.correspondence.CorrespondentFactory;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.privacy.violation.filter.PrivacyWarner;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * @author Clemens Brackmann
 *
 */
public class PrivacyWarnerTest {
    private final File pcmDirectory = new File(
            "/home/reiner/Projects/iObserve/experiments/distributed-jpetstore/pcm/JPetStore");

    private final File modelDatabaseDirectory = new File(
            "/home/reiner/Projects/iObserve/experiments/jss-privacy-experiment/db");

    private PrivacyWarner pw;

    @Before
    public void initializePW() {
        final PCMModelHandler modelHandler = new PCMModelHandler(this.pcmDirectory);
        final GraphLoader graphLoader = new GraphLoader(this.modelDatabaseDirectory);

        final Graph<Allocation> allocationModelGraph = graphLoader
                .initializeAllocationModelGraph(modelHandler.getAllocationModel());

        final Graph<ResourceEnvironment> resourceEnvironmentGraph = graphLoader
                .initializeResourceEnvironmentModelGraph(modelHandler.getResourceEnvironmentModel());
        final Graph<System> systemGraph = graphLoader.initializeSystemModelGraph(modelHandler.getSystemModel());

        final ModelProvider<Allocation, Allocation> allocationModelProvider = new ModelProvider<>(allocationModelGraph);
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> resourceEnvironmentModelProvider = new ModelProvider<>(
                resourceEnvironmentGraph);
        final ModelProvider<System, System> systemModelProvider = new ModelProvider<>(systemGraph);

        this.pw = new PrivacyWarner(allocationModelProvider, systemModelProvider, resourceEnvironmentModelProvider);

    }

    @Test
    public void testPW() {
        final PCMDeployedEvent pcmdpe = new PCMDeployedEvent("TestService",
                CorrespondentFactory.newInstance("Testname", "TestID", "Testmethode", "TestmethodenID"),
                "http://Test.test", (short) 5);
        StageTester.test(this.pw).and().send(pcmdpe).to(this.pw.getDeployedInputPort()).and().start();
    }

}