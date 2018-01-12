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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BooleanConverter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import teetime.framework.Configuration;
import teetime.framework.Execution;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.FileObservationConfiguration;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.clustering.EAggregationType;
import org.iobserve.analysis.clustering.EOutputMode;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.model.provider.neo4j.AllocationModelProvider;
import org.iobserve.analysis.model.provider.neo4j.Graph;
import org.iobserve.analysis.model.provider.neo4j.GraphLoader;
import org.iobserve.analysis.model.provider.neo4j.ModelProvider;
import org.iobserve.analysis.model.provider.neo4j.RepositoryModelProvider;
import org.iobserve.analysis.model.provider.neo4j.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.provider.neo4j.SystemModelProvider;
import org.iobserve.analysis.model.provider.neo4j.UsageModelProvider;
import org.iobserve.analysis.service.InitializeDeploymentVisualization;
import org.iobserve.analysis.service.ServiceConfiguration;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Main class for starting the iObserve application.
 *
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class AnalysisMain {
    private static final Log LOG = LogFactory.getLog(AnalysisMain.class);

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
        final AnalysisMain main = new AnalysisMain();
        final JCommander commander = new JCommander(main);
        try {
            commander.parse(args);
            if (main.service) {
                main.executeService(commander);
            } else {
                main.execute(commander);
            }
        } catch (final ParameterException e) {
            AnalysisMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        } catch (final IOException e) {
            AnalysisMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        } catch (final InitializationException e) {
            AnalysisMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander) throws ParameterException, IOException, InitializationException {
        if (this.help) {
            commander.usage();
            System.exit(1);
            // required parameters.
        } else if (this.snapshotPath == null || this.perOpteryxUriPath == null || this.lqnsUriPath == null
                || this.deployablesFolderPath == null || this.interactiveMode == false) {
            throw new ParameterException("Missing required parameter for cli.");
        } else {
            this.checkDirectory(this.monitoringDataDirectory, "Kieker log", commander);
            this.checkDirectory(this.pcmModelsDirectory, "Palladio Model", commander);
            /** process parameter. */

            EAggregationType aggregationType;
            if ("em".equals(this.aggregationTypeName)) {
                aggregationType = EAggregationType.EM_CLUSTERING;
            } else if ("xmeans".equals(this.aggregationTypeName)) {
                aggregationType = EAggregationType.X_MEANS_CLUSTERING;
            } else {
                commander.usage();
                return;
            }

            /** this is an ugly hack. For now lets keep it. */
            EOutputMode outputMode;

            if (this.outputPathPrefix != null) {
                this.visualizationServiceURL = this.outputPathPrefix;
                outputMode = EOutputMode.FILE_OUTPUT;
            } else {
                outputMode = EOutputMode.UBM_VISUALIZATION;
            }

            /** create and run application */
            final Collection<File> monitoringDataDirectories = new ArrayList<>();
            AnalysisMain.findDirectories(this.monitoringDataDirectory.listFiles(), monitoringDataDirectories);

            final InitializeModelProviders modelProviderPlatform = new InitializeModelProviders(
                    this.pcmModelsDirectory);

            final ICorrespondence correspondenceModel = modelProviderPlatform.getCorrespondenceModel();
            final RepositoryModelProvider repositoryModelProvider = modelProviderPlatform.getRepositoryModelProvider();
            final UsageModelProvider usageModelProvider = modelProviderPlatform.getUsageModelProvider();
            final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider = modelProviderPlatform
                    .getResourceEnvironmentModelProvider();
            final AllocationModelProvider allocationModelProvider = modelProviderPlatform.getAllocationModelProvider();
            final SystemModelProvider systemModelProvider = modelProviderPlatform.getSystemModelProvider();

            // initialize neo4j graphs
            final GraphLoader graphLoader = new GraphLoader(this.pcmModelsNeo4jDirectory);
            Graph resourceEnvironmentModelGraph = graphLoader
                    .initializeResourceEnvironmentModelGraph(resourceEnvironmentModelProvider.getModel());
            Graph allocationModelGraph = graphLoader.initializeAllocationModelGraph(allocationModelProvider.getModel());
            Graph systemModelGraph = graphLoader.initializeSystemModelGraph(systemModelProvider.getModel());

            // load neo4j graphs
            resourceEnvironmentModelGraph = graphLoader.getResourceEnvironmentModelGraph();
            allocationModelGraph = graphLoader.getAllocationModelGraph();
            systemModelGraph = graphLoader.getSystemModelGraph();

            // new graphModelProvider
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider = new ModelProvider<>(
                    resourceEnvironmentModelGraph);
            final ModelProvider<Allocation> allocationModelGraphProvider = new ModelProvider<>(allocationModelGraph);
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider = new ModelProvider<>(
                    systemModelGraph);

            SnapshotBuilder.setBaseSnapshotURI(URI.createFileURI(this.snapshotPath));
            final SnapshotBuilder snapshotBuilder = new SnapshotBuilder("Runtime", modelProviderPlatform);
            final URI perOpteryxUri = URI.createFileURI(this.perOpteryxUriPath);
            final URI lqnsUri = URI.createFileURI(this.lqnsUriPath);
            final URI deployablesFolder = URI.createFileURI(this.deployablesFolderPath);
            final CLIEventListener eventListener = new CLIEventListener(this.interactiveMode);

            final Configuration configuration = new FileObservationConfiguration(monitoringDataDirectories,
                    correspondenceModel, usageModelProvider, repositoryModelProvider, resourceEnvironmentModelProvider,
                    resourceEnvironmentModelGraphProvider, allocationModelProvider, allocationModelGraphProvider,
                    systemModelProvider, systemModelGraphProvider, this.varianceOfUserGroups, this.thinkTime,
                    this.closedWorkload, this.visualizationServiceURL, aggregationType, outputMode, snapshotBuilder,
                    perOpteryxUri, lqnsUri, eventListener, deployablesFolder);

            AnalysisMain.LOG.info("Analysis configuration");
            final Execution<Configuration> analysis = new Execution<>(configuration);
            AnalysisMain.LOG.info("Analysis start");
            analysis.executeBlocking();
            AnalysisMain.LOG.info("Anaylsis complete");
        }
    }

    // execute function of org.iobserve.analysis.service.AnalysisMain.java
    private void executeService(final JCommander commander) throws IOException, InitializationException {
        if (this.help) {
            commander.usage();
            System.exit(1);
            // required parameters
        } else if (this.visualizationServiceURL == null || this.output == null || this.listenPort == 0) {
            throw new ParameterException("Missing required parameter.");
        } else {
            this.checkDirectory(this.pcmModelsDirectory, "Palladio Model", commander);
            this.checkDirectory(this.pcmModelsNeo4jDirectory, "Palladio Model Neo4j", commander);
            /** process parameter. */

            final String[] outputs = this.output.split(":");
            if (outputs.length == 2) {
                final String outputHostname = outputs[0];
                final String outputPort = outputs[1];

                /** process parameter. */
                // old model providers without neo4j
                final InitializeModelProviders modelProvider = new InitializeModelProviders(this.pcmModelsDirectory);

                final ICorrespondence correspondenceModel = modelProvider.getCorrespondenceModel();
                final UsageModelProvider usageModelProvider = modelProvider.getUsageModelProvider();
                final RepositoryModelProvider repositoryModelProvider = modelProvider.getRepositoryModelProvider();
                final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider = modelProvider
                        .getResourceEnvironmentModelProvider();
                final AllocationModelProvider allocationModelProvider = modelProvider.getAllocationModelProvider();
                final SystemModelProvider systemModelProvider = modelProvider.getSystemModelProvider();
                // initialize neo4j graphs
                final GraphLoader graphLoader = new GraphLoader(this.pcmModelsNeo4jDirectory);
                Graph resourceEnvironmentModelGraph = graphLoader
                        .initializeResourceEnvironmentModelGraph(resourceEnvironmentModelProvider.getModel());
                Graph allocationModelGraph = graphLoader
                        .initializeAllocationModelGraph(allocationModelProvider.getModel());
                Graph systemModelGraph = graphLoader.initializeSystemModelGraph(systemModelProvider.getModel());
                Graph usageModelGraph = graphLoader.initializeUsageModelGraph(usageModelProvider.getModel());

                // load neo4j graphs
                resourceEnvironmentModelGraph = graphLoader.getResourceEnvironmentModelGraph();
                allocationModelGraph = graphLoader.getAllocationModelGraph();
                systemModelGraph = graphLoader.getSystemModelGraph();
                usageModelGraph = graphLoader.getUsageModelGraph();

                // new graphModelProvider
                final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider = new ModelProvider<>(
                        resourceEnvironmentModelGraph);
                final ModelProvider<ResourceContainer> resourceContainerModelGraphProvider = new ModelProvider<>(
                        resourceEnvironmentModelGraph);
                final ModelProvider<Allocation> allocationModelGraphProvider = new ModelProvider<>(
                        allocationModelGraph);
                final ModelProvider<AssemblyContext> assemblyContextModelGraphProvider = new ModelProvider<>(
                        allocationModelGraph);
                final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider = new ModelProvider<>(
                        systemModelGraph);
                final ModelProvider<AssemblyContext> assCtxSystemModelGraphProvider = new ModelProvider<>(
                        systemModelGraph);
                final ModelProvider<UsageModel> usageModelGraphProvider = new ModelProvider<>(usageModelGraph);

                // get systemId
                final org.palladiosimulator.pcm.system.System systemModel = systemModelGraphProvider
                        .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);
                final String systemId = systemModel.getId();
                // URLs for sending updates to the deployment visualization
                final URL systemUrl = new URL("http://" + outputHostname + ":" + outputPort + "/v1/systems/");
                final URL changelogUrl = new URL(systemUrl + systemId + "/changelogs");

                final InitializeDeploymentVisualization deploymentVisualization = new InitializeDeploymentVisualization(
                        systemUrl, changelogUrl, allocationModelGraphProvider, systemModelGraphProvider,
                        resourceEnvironmentModelGraphProvider, usageModelGraphProvider);
                try {
                    deploymentVisualization.initialize();
                } catch (final Exception e) {
                    AnalysisMain.LOG.debug("deploymentVisualization.initialize() went wrong!", e);
                }

                SnapshotBuilder snapshotBuilder = null;
                URI perOpteryxUri = null;
                URI lqnsUri = null;
                URI deployablesFolder = null;
                if (!this.snapshotPath.isEmpty() && !this.perOpteryxUriPath.isEmpty() && !this.lqnsUriPath.isEmpty()
                        && !this.deployablesFolderPath.isEmpty()) {
                    SnapshotBuilder.setBaseSnapshotURI(URI.createFileURI(this.snapshotPath));
                    snapshotBuilder = new SnapshotBuilder("Runtime", modelProvider);
                    perOpteryxUri = URI.createFileURI(this.perOpteryxUriPath);
                    lqnsUri = URI.createFileURI(this.lqnsUriPath);
                    deployablesFolder = URI.createFileURI(this.deployablesFolderPath);
                }

                final Configuration configuration = new ServiceConfiguration(this.listenPort, outputHostname,
                        outputPort, "", this.varianceOfUserGroups, this.thinkTime, this.closedWorkload,
                        correspondenceModel, usageModelProvider, repositoryModelProvider,
                        resourceEnvironmentModelProvider, allocationModelProvider, systemModelProvider,
                        resourceEnvironmentModelGraphProvider, resourceContainerModelGraphProvider,
                        allocationModelGraphProvider, assemblyContextModelGraphProvider, systemModelGraphProvider,
                        assCtxSystemModelGraphProvider, this.visualizationServiceURL, snapshotBuilder, perOpteryxUri,
                        lqnsUri, deployablesFolder);

                AnalysisMain.LOG.info("Analysis configuration");
                final Execution<Configuration> analysis = new Execution<>(configuration);
                AnalysisMain.LOG.info("Analysis start");
                analysis.executeBlocking();
                AnalysisMain.LOG.info("Anaylsis complete");
            }
        }
    }

    private void checkDirectory(final File location, final String locationLabel, final JCommander commander)
            throws IOException {
        if (!location.exists()) {
            AnalysisMain.LOG.error(locationLabel + " path " + location.getCanonicalPath() + " does not exist.");
            commander.usage();
            System.exit(1);
        }
        if (!location.isDirectory()) {
            AnalysisMain.LOG.error(locationLabel + " path " + location.getCanonicalPath() + " is not a directory.");
            commander.usage();
            System.exit(1);
        }

    }

    private static void findDirectories(final File[] listFiles, final Collection<File> monitoringDataDirectories) {
        for (final File file : listFiles) {
            if (file.isDirectory()) {
                monitoringDataDirectories.add(file);
                AnalysisMain.findDirectories(file.listFiles(), monitoringDataDirectories);
            }
        }
    }
}
