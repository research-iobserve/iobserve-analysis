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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import teetime.framework.test.StageTester;

import org.hamcrest.core.Is;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.model.ModelImporter;
import org.iobserve.model.privacy.PrivacyFactory;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.privacy.violation.filter.PrivacyWarner;
import org.iobserve.stages.data.Warnings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemFactory;

/**
 * @author Clemens Brackmann
 *
 */
public class PrivacyWarnerTest {
    private static final String PCM_DIRECTORY_PATH = "/home/reiner/Projects/iObserve/jpetstore-6/pcm/5.2";
    private static final String MODEL_DATABASE_DIRECTORY_PATH = "/home/reiner/Projects/iObserve/experiments/jss-privacy-experiment/db";

    // private static final String PCM_DIRECTORY_PATH =
    // "D:/Experiment/distributed-jpetstore-experiment/pcm/JPetStore";
    // private static final String MODEL_DATABASE_DIRECTORY_PATH =
    // "D:/Experiment/distributed-jpetstore-experiment/db";

    private final File pcmDirectory = new File(PrivacyWarnerTest.PCM_DIRECTORY_PATH);
    private final File modelDatabaseDirectory = new File(PrivacyWarnerTest.MODEL_DATABASE_DIRECTORY_PATH);

    private PrivacyWarner pw;

    /**
     * Initialize database.
     */
    @Before
    public void initializePW() {
        this.clearDirectory(this.modelDatabaseDirectory);
        this.modelDatabaseDirectory.mkdirs();

        try {
            final ModelImporter modelHandler = new ModelImporter(this.pcmDirectory);
            final GraphLoader graphLoader = new GraphLoader(this.modelDatabaseDirectory);

            /** graphs. */
            graphLoader.initializeModelGraph(RepositoryFactory.eINSTANCE, modelHandler.getRepositoryModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(SystemFactory.eINSTANCE, modelHandler.getSystemModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(ResourceenvironmentFactory.eINSTANCE,
                    modelHandler.getResourceEnvironmentModel(), ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(AllocationFactory.eINSTANCE, modelHandler.getAllocationModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(PrivacyFactory.eINSTANCE, modelHandler.getPrivacyModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

            /** load neo4j graphs. */
            final Graph repositoryGraph = graphLoader.createModelGraph(RepositoryFactory.eINSTANCE);
            final Graph systemGraph = graphLoader.createModelGraph(SystemFactory.eINSTANCE);
            final Graph resourceEnvironmentGraph = graphLoader.createModelGraph(ResourceenvironmentFactory.eINSTANCE);
            final Graph allocationModelGraph = graphLoader.createModelGraph(AllocationFactory.eINSTANCE);
            final Graph privacyModelGraph = graphLoader.createModelGraph(PrivacyFactory.eINSTANCE);

            /** model provider. */
            final IModelProvider<Repository> repositoryModelProvider = new ModelProvider<>(repositoryGraph,
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            final IModelProvider<System> systemModelProvider = new ModelProvider<>(systemGraph,
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            final IModelProvider<Allocation> allocationModelProvider = new ModelProvider<>(allocationModelGraph,
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider = new ModelProvider<>(
                    resourceEnvironmentGraph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            final IModelProvider<PrivacyModel> privacyModelProvider = new ModelProvider<>(privacyModelGraph,
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

            final PrivacyModel model = privacyModelProvider.readRootNode(PrivacyModel.class);

            java.lang.System.err.println(model.toString());

            this.pw = new PrivacyWarner(allocationModelProvider, systemModelProvider, resourceEnvironmentModelProvider,
                    repositoryModelProvider, privacyModelProvider);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void clearDirectory(final File directory) {
        if (directory.isDirectory()) {
            for (final File content : directory.listFiles()) {
                if (content.isFile()) {
                    content.delete();
                } else if (content.isDirectory()) {
                    this.clearDirectory(content);
                }
            }
            directory.delete();
        }
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