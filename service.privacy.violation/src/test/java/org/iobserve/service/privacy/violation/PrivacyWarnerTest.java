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
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.Is;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.model.PCMModelHandler;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.privacy.violation.filter.PrivacyWarner;
import org.iobserve.stages.data.Warnings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import teetime.framework.test.StageTester;

/**
 * @author Clemens Brackmann
 *
 */
public class PrivacyWarnerTest {
    // private static final String PCM_DIRECTORY_PATH =
    // "/home/reiner/Projects/iObserve/experiments/distributed-jpetstore-experiment/pcm/JPetStore";
    // private static final String MODEL_DATABASE_DIRECTORY_PATH =
    // "/home/reiner/Projects/iObserve/experiments/jss-privacy-experiment/db";

    private static final String PCM_DIRECTORY_PATH = "D:/Experiment/distributed-jpetstore-experiment/pcm/JPetStore";
    private static final String MODEL_DATABASE_DIRECTORY_PATH = "D:/Experiment/distributed-jpetstore-experiment/db";

    private final File pcmDirectory = new File(PrivacyWarnerTest.PCM_DIRECTORY_PATH);
    private final File modelDatabaseDirectory = new File(PrivacyWarnerTest.MODEL_DATABASE_DIRECTORY_PATH);

    private PrivacyWarner pw;

    /**
     * Initialize database.
     */
    @Before
    public void initializePW() {
        final PCMModelHandler modelHandler = new PCMModelHandler(this.pcmDirectory);
        final GraphLoader graphLoader = new GraphLoader(this.modelDatabaseDirectory);

        /** graphs. */
        graphLoader.initializeModelGraph(Repository.class, modelHandler.getRepositoryModel(),
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        graphLoader.initializeModelGraph(System.class, modelHandler.getSystemModel(), ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        graphLoader.initializeModelGraph(ResourceEnvironment.class, modelHandler.getResourceEnvironmentModel(),
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        graphLoader.initializeModelGraph(Allocation.class, modelHandler.getAllocationModel(),
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        graphLoader.initializeModelGraph(PrivacyModel.class, modelHandler.getPrivacyModel(),
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        /** load neo4j graphs. */
        final Graph<Repository> repositoryGraph = graphLoader.createModelGraph(Repository.class);
        final Graph<System> systemGraph = graphLoader.createModelGraph(System.class);
        final Graph<ResourceEnvironment> resourceEnvironmentGraph = graphLoader
                .createModelGraph(ResourceEnvironment.class);
        final Graph<Allocation> allocationModelGraph = graphLoader.createModelGraph(Allocation.class);
        final Graph<PrivacyModel> privacyModelGraph = graphLoader.createModelGraph(PrivacyModel.class);

        /** model provider. */
        final ModelProvider<Repository, Repository> repositoryModelProvider = new ModelProvider<>(repositoryGraph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final ModelProvider<System, System> systemModelProvider = new ModelProvider<>(systemGraph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final ModelProvider<Allocation, Allocation> allocationModelProvider = new ModelProvider<>(allocationModelGraph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> resourceEnvironmentModelProvider = new ModelProvider<>(
                resourceEnvironmentGraph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final ModelProvider<PrivacyModel, PrivacyModel> privacyModelProvider = new ModelProvider<>(privacyModelGraph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        this.pw = new PrivacyWarner(allocationModelProvider, systemModelProvider, resourceEnvironmentModelProvider,
                repositoryModelProvider, privacyModelProvider);

    }

    /**
     * Test run component.
     */
    @Test
    public void testPW() {
        final AssemblyContext assemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        assemblyContext.setEntityName("EntityName");
        final PCMDeployedEvent pcmdpe = new PCMDeployedEvent("TestService", assemblyContext, "http://Test.test",
                ISOCountryCode.EVIL_EMPIRE);

        final List<Warnings> results = new ArrayList<>();
        StageTester.test(this.pw).and().send(pcmdpe).to(this.pw.getDeployedInputPort()).and().receive(results)
                .from(this.pw.getWarningsOutputPort()).and().start();
        Assert.assertThat("No warning generated", false, Is.is(results.isEmpty()));
    }

}