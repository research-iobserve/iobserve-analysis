/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.ConfigurationException;
import org.iobserve.analysis.clustering.EAggregationType;
import org.iobserve.analysis.clustering.EOutputMode;
import org.iobserve.analysis.configurations.FileObservationConfiguration;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.model.PCMModelHandler;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.BooleanConverter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;

import kieker.common.configuration.Configuration;

/**
 * Main class for starting the iObserve application.
 *
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class AnalysisMain extends AbstractServiceMain<FileObservationConfiguration> {

    @Parameter(names = "--help", help = true)
    private boolean help;

    @Parameter(names = { "-v",
            "--variance-of-user-groups" }, required = true, description = "Variance of user groups.", converter = IntegerConverter.class)
    private int varianceOfUserGroups;

    @Parameter(names = { "-t",
            "--think-time" }, required = true, description = "Think time.", converter = IntegerConverter.class)
    private int thinkTime;

    @Parameter(names = { "-c",
            "--closed-workload" }, required = false, description = "Closed workload.", converter = BooleanConverter.class)
    private boolean closedWorkload;

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "Kieker monitoring data directory.", converter = FileConverter.class)
    private File monitoringDataDirectory;

    @Parameter(names = { "-p",
            "--pcm" }, required = true, description = "Directory containing PCM model data.", converter = FileConverter.class)
    private File pcmModelsDirectory;

    @Parameter(names = { "-pn4j",
            "--pcmneo4j" }, required = true, description = "Directory containing Neo4j database with PCM model data.", converter = FileConverter.class)
    private File pcmModelsNeo4jDirectory;

    @Parameter(names = { "-ser",
            "--service" }, required = false, description = "Use service version.", converter = BooleanConverter.class)
    private boolean service;

    @Parameter(names = { "-u",
            "--ubm-visualization" }, required = false, description = "Required for service only. User behavior model visualitation service URL.")
    private String visualizationServiceURL;

    @Parameter(names = { "-sl",
            "--snapshot-location" }, required = false, description = "Required for cli only. snapshot save location")
    private String snapshotPath;

    @Parameter(names = { "-po",
            "--perOpteryx-headless-location" }, required = false, description = "Required for cli only. the location of the PerOpteryx headless plugin", converter = FileConverter.class)
    private String perOpteryxUriPath;

    @Parameter(names = { "-l",
            "--lqns-location" }, required = false, description = "Required for cli only. the location of the LQN Solver for optimization", converter = FileConverter.class)
    private String lqnsUriPath;

    @Parameter(names = { "-d",
            "--deployables-folder" }, required = false, description = "Required for cli only. the location of the deployable/executable scripts for adaptation execution", converter = FileConverter.class)
    private String deployablesFolderPath;

    @Parameter(names = { "-in",
            "--interactive-adaptation" }, required = false, description = "Required for cli only. interact with operator during adaptation", converter = FileConverter.class)
    private boolean interactiveMode;

    @Parameter(names = { "-o", "--ubm-output" }, required = false, description = "File output of user behavior.")
    private String outputPathPrefix;

    @Parameter(names = { "-m", "--aggregation-type" }, required = false, description = "Aggregation type.")
    private String aggregationTypeName;

    @Parameter(names = { "-o",
            "--output" }, required = false, description = "Required for service only. hostname and port of the iobserve visualization, e.g., visualization:80.")
    private String output;

    @Parameter(names = { "-i",
            "--input" }, required = false, description = "Required for service only. port number to listen for new connections of Kieker writers.", converter = IntegerConverter.class)
    private int listenPort;

    private EAggregationType aggregationType;

    private EOutputMode outputMode;

    /**
     * Default constructor.
     */
    private AnalysisMain() {
        // do nothing here
    }

    /**
     * Main function.
     *
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {
        new AnalysisMain().run("Analysis File", "analysis", args);
    }

    private static void findDirectories(final File[] listFiles, final Collection<File> monitoringDataDirectories) {
        for (final File file : listFiles) {
            if (file.isDirectory()) {
                monitoringDataDirectories.add(file);
                AnalysisMain.findDirectories(file.listFiles(), monitoringDataDirectories);
            }
        }
    }

    @Override
    protected FileObservationConfiguration createConfiguration(final Configuration configuration)
            throws ConfigurationException {

        /** create and run application */
        final Collection<File> monitoringDataDirectories = new ArrayList<>();
        AnalysisMain.findDirectories(this.monitoringDataDirectory.listFiles(), monitoringDataDirectories);

        final PCMModelHandler modelHandler = new PCMModelHandler(this.pcmModelsDirectory);

        /** load models from files. */
        final ICorrespondence correspondenceModel = modelHandler.getCorrespondenceModel();
        final Repository repositoryModel = modelHandler.getRepositoryModel();
        final ResourceEnvironment resourceEnvironmentModel = modelHandler.getResourceEnvironmentModel();
        final Allocation allocationModel = modelHandler.getAllocationModel();
        final System systemModel = modelHandler.getSystemModel();
        final UsageModel usageModel = modelHandler.getUsageModel();

        /** initialize neo4j graphs. */
        final GraphLoader graphLoader = new GraphLoader(this.pcmModelsNeo4jDirectory);
        Graph repositoryModelGraph = graphLoader.initializeResourceEnvironmentModelGraph(resourceEnvironmentModel);
        Graph resourceEnvironmentModelGraph = graphLoader
                .initializeResourceEnvironmentModelGraph(resourceEnvironmentModel);
        Graph allocationModelGraph = graphLoader.initializeAllocationModelGraph(allocationModel);
        Graph systemModelGraph = graphLoader.initializeSystemModelGraph(systemModel);
        Graph usageModelGraph = graphLoader.initializeUsageModelGraph(usageModel);

        // TODO is this reloading really necessary?
        /** load neo4j graphs. */
        repositoryModelGraph = graphLoader.createRepositoryModelGraph();
        resourceEnvironmentModelGraph = graphLoader.createResourceEnvironmentModelGraph();
        allocationModelGraph = graphLoader.createAllocationModelGraph();
        systemModelGraph = graphLoader.createSystemModelGraph();
        usageModelGraph = graphLoader.createUsageModelGraph();

        /** new graphModelProvider. */
        final ModelProvider<Repository> repositoryModelGraphProvider = new ModelProvider<>(repositoryModelGraph);
        final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider = new ModelProvider<>(
                resourceEnvironmentModelGraph);
        final ModelProvider<Allocation> allocationModelGraphProvider = new ModelProvider<>(allocationModelGraph);
        final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider = new ModelProvider<>(
                systemModelGraph);
        final ModelProvider<UsageModel> usageModelGraphProvider = new ModelProvider<>(usageModelGraph);

        SnapshotBuilder.setBaseSnapshotURI(URI.createFileURI(this.snapshotPath));
        try {
            final SnapshotBuilder snapshotBuilder = new SnapshotBuilder("Runtime", modelHandler);

            return new FileObservationConfiguration(monitoringDataDirectories, correspondenceModel,
                    usageModelGraphProvider, repositoryModelGraphProvider, resourceEnvironmentModelGraphProvider,
                    allocationModelGraphProvider, systemModelGraphProvider, this.varianceOfUserGroups, this.thinkTime,
                    this.closedWorkload, this.visualizationServiceURL, this.aggregationType, this.outputMode,
                    snapshotBuilder);
        } catch (final InitializationException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {

        if ((this.snapshotPath == null) || (this.perOpteryxUriPath == null) || (this.lqnsUriPath == null)
                || (this.deployablesFolderPath == null) || (this.interactiveMode == false)) {
            AbstractServiceMain.LOGGER.error("Missing required parameter for cli.");
            commander.usage();
            return false;
        }

        if ("em".equals(this.aggregationTypeName)) {
            this.aggregationType = EAggregationType.EM_CLUSTERING;
        } else if ("xmeans".equals(this.aggregationTypeName)) {
            this.aggregationType = EAggregationType.X_MEANS_CLUSTERING;
        } else {
            commander.usage();
            return false;
        }

        /** this is an ugly hack. For now lets keep it. */
        if (this.outputPathPrefix != null) {
            this.visualizationServiceURL = this.outputPathPrefix;
            this.outputMode = EOutputMode.FILE_OUTPUT;
        } else {
            this.outputMode = EOutputMode.UBM_VISUALIZATION;
        }

        try {
            return CommandLineParameterEvaluation.checkDirectory(this.monitoringDataDirectory, "Kieker log", commander)
                    && CommandLineParameterEvaluation.checkDirectory(this.pcmModelsDirectory, "Palladio Model",
                            commander);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected void shutdownService() {
        // TODO serialize PCM models.
    }

    @Override
    protected File getConfigurationFile() {
        return null;
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        return true;
    }
}
