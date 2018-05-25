/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.CommaParameterSplitter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;

import kieker.common.configuration.Configuration;

import org.iobserve.model.PCMModelHandler;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.stages.general.ConfigurationException;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class PrivacyViolationDetectionServiceMain
        extends AbstractServiceMain<PrivacyViolationDetectionConfiguration> {

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "Input port.", converter = IntegerConverter.class)
    private int inputPort;

    @Parameter(names = { "-c",
            "--control" }, required = true, description = "Control hosts and ports.", splitter = CommaParameterSplitter.class, converter = HostPortConverter.class)
    private List<ConnectionData> outputs;

    @Parameter(names = { "-p",
            "--pcm" }, required = true, description = "PCM model directory.", converter = FileConverter.class)
    private File pcmDirectory;

    @Parameter(names = { "-w",
            "--warnings" }, required = true, description = "Warnings file.", converter = FileConverter.class)
    private File warningFile;

    @Parameter(names = { "-a",
            "--alarms" }, required = true, description = "Alarms file.", converter = FileConverter.class)
    private File alarmsFile;

    @Parameter(names = { "-d",
            "--db-directory" }, required = true, description = "Model database directory.", converter = FileConverter.class)
    private File modelDatabaseDirectory;

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private PrivacyViolationDetectionServiceMain() {

    }

    /**
     * Configure and execute the TCP Kieker data collector.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        new PrivacyViolationDetectionServiceMain().run("Privacy Violation Detection Service", "service", args);
    }

    @Override
    protected PrivacyViolationDetectionConfiguration createConfiguration(final Configuration configuration)
            throws ConfigurationException {

        /** load models. */
        final PCMModelHandler modelHandler = new PCMModelHandler(this.pcmDirectory);
        final GraphLoader graphLoader = new GraphLoader(this.modelDatabaseDirectory);

        /** initialize database. */
        graphLoader.initializeModelGraph(Repository.class, modelHandler.getRepositoryModel(),
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        graphLoader.initializeModelGraph(ResourceEnvironment.class, modelHandler.getResourceEnvironmentModel(),
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        graphLoader.initializeModelGraph(Allocation.class, modelHandler.getAllocationModel(),
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        graphLoader.initializeModelGraph(System.class, modelHandler.getSystemModel(), ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        graphLoader.initializeModelGraph(CorrespondenceModel.class, modelHandler.getCorrespondenceModel(), null,
                ModelProvider.PCM_ID);
        graphLoader.initializeModelGraph(PrivacyModel.class, modelHandler.getPrivacyModel(), null,
                ModelProvider.PCM_ID);

        /** load neo4j graphs. */
        final Graph<Repository> repositoryGraph = graphLoader.createModelGraph(Repository.class);
        final Graph<ResourceEnvironment> resourceEnvironmentGraph = graphLoader
                .createModelGraph(ResourceEnvironment.class);
        final Graph<Allocation> allocationModelGraph = graphLoader.createModelGraph(Allocation.class);
        final Graph<System> systemModelGraph = graphLoader.createModelGraph(System.class);
        final Graph<CorrespondenceModel> correspondenceModelGraph = graphLoader
                .createModelGraph(CorrespondenceModel.class);
        final Graph<PrivacyModel> privacyModelGraph = graphLoader.createModelGraph(PrivacyModel.class);

        /** model provider. */
        final IModelProvider<Repository> repositoryModelProvider = new ModelProvider<>(repositoryGraph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        final ModelProvider<Allocation, Allocation> allocationModelProvider = new ModelProvider<>(allocationModelGraph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final IModelProvider<AllocationContext> allocationContextModelProvider = new ModelProvider<>(
                allocationModelGraph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        final ModelProvider<ResourceEnvironment, ResourceEnvironment> resourceEnvironmentModelProvider = new ModelProvider<>(
                resourceEnvironmentGraph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        final ModelProvider<System, System> systemModelProvider = new ModelProvider<>(systemModelGraph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        final IModelProvider<AssemblyEntry> assemblyEntryCorrespondenceModelProvider = new ModelProvider<>(
                correspondenceModelGraph, ModelProvider.IMPLEMENTATION_ID, null);

        final ModelProvider<PrivacyModel, PrivacyModel> privacyModelProvider = new ModelProvider<>(privacyModelGraph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        try {
            return new PrivacyViolationDetectionConfiguration(this.inputPort, this.outputs,
                    assemblyEntryCorrespondenceModelProvider, repositoryModelProvider, resourceEnvironmentModelProvider,
                    allocationModelProvider, allocationContextModelProvider, systemModelProvider, privacyModelProvider,
                    this.warningFile, this.alarmsFile);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            return CommandLineParameterEvaluation.checkDirectory(this.pcmDirectory, "PCM model directory", commander);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected void shutdownService() {
        // empty, no special shutdown required
    }

    @Override
    protected File getConfigurationFile() {
        return null;
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        try {
            return CommandLineParameterEvaluation.checkDirectory(this.modelDatabaseDirectory, "model database",
                    commander)
                    && CommandLineParameterEvaluation.checkDirectory(this.alarmsFile.getParentFile(), "alarm location",
                            commander)
                    && CommandLineParameterEvaluation.checkDirectory(this.warningFile.getParentFile(),
                            "warnings location", commander);
        } catch (final IOException e) {
            AbstractServiceMain.LOGGER.error("Evaluating command line parameter failed.", e);
            return false;
        }
    }

}
