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
package org.iobserve.analysis.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.ConfigurationException;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.AllocationModelProvider;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.model.provider.neo4j.RepositoryModelProvider;
import org.iobserve.model.provider.neo4j.ResourceEnvironmentModelProvider;
import org.iobserve.model.provider.neo4j.SystemModelProvider;
import org.iobserve.model.provider.neo4j.UsageModelProvider;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
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
public final class AnalysisMain extends AbstractServiceMain<ServiceConfiguration> {
    @Parameter(names = "--help", help = true)
    private boolean help;

    @Parameter(names = { "-v",
            "--variance-of-user-groups" }, required = true, description = "Variance of user groups.", converter = IntegerConverter.class)
    private int varianceOfUserGroups;

    @Parameter(names = { "-t",
            "--think-time" }, required = true, description = "Think time.", converter = IntegerConverter.class)
    private int thinkTime;

    @Parameter(names = { "-c",
            "--closed-workload" }, required = false, description = "Closed workload.", converter = IntegerConverter.class)
    private boolean closedWorkload;

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "port number to listen for new connections of Kieker writers.", converter = IntegerConverter.class)
    private int listenPort;

    @Parameter(names = { "-o",
            "--output" }, required = true, description = "hostname and port of the iobserve visualization, e.g., visualization:80.")
    private String output;

    @Parameter(names = { "-p",
            "--pcm" }, required = true, description = "Directory containing PCM model data.", converter = FileConverter.class)
    private File pcmModelsDirectory;

    @Parameter(names = { "-pn4j",
            "--pcmneo4j" }, required = true, description = "Directory containing Neo4j database with PCM model data.", converter = FileConverter.class)
    private File pcmModelsNeo4jDirectory;

    @Parameter(names = { "-u",
            "--ubm-visualization" }, required = true, description = "User behavior model visualitation service URL.")
    private String visualizationServiceURL;

    @Parameter(names = { "-sl", "--snapshot-location" }, required = false, description = "snapshot save location")
    private final String snapshotPath = "";

    @Parameter(names = { "-po",
            "--perOpteryx-headless-location" }, required = false, description = "the location of the PerOpteryx headless plugin", converter = FileConverter.class)
    private final String perOpteryxUriPath = "";

    @Parameter(names = { "-l",
            "--lqns-location" }, required = false, description = "the location of the LQN Solver for optimization", converter = FileConverter.class)
    private final String lqnsUriPath = "";

    @Parameter(names = { "-d",
            "--deployables-folder" }, required = false, description = "the location of the deployable/executable scripts for adaptation execution", converter = FileConverter.class)
    private final String deployablesFolderPath = "";

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
        new AnalysisMain().run("Analysis Service", "analysis", args);
    }

    @Override
    protected ServiceConfiguration createConfiguration() throws ConfigurationException {
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
            Graph allocationModelGraph = graphLoader.initializeAllocationModelGraph(allocationModelProvider.getModel());
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
            final ModelProvider<Allocation> allocationModelGraphProvider = new ModelProvider<>(allocationModelGraph);
            final ModelProvider<AssemblyContext> assemblyContextModelGraphProvider = new ModelProvider<>(
                    allocationModelGraph);
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider = new ModelProvider<>(
                    systemModelGraph);
            final ModelProvider<AssemblyContext> assCtxSystemModelGraphProvider = new ModelProvider<>(systemModelGraph);
            final ModelProvider<UsageModel> usageModelGraphProvider = new ModelProvider<>(usageModelGraph);

            // get systemId
            final org.palladiosimulator.pcm.system.System systemModel = systemModelGraphProvider
                    .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);
            final String systemId = systemModel.getId();

            try {
                // URLs for sending updates to the deployment visualization
                final URL systemUrl = new URL("http://" + outputHostname + ":" + outputPort + "/v1/systems/");
                final URL changelogUrl = new URL(systemUrl + systemId + "/changelogs");

                final InitializeDeploymentVisualization deploymentVisualization = new InitializeDeploymentVisualization(
                        systemUrl, changelogUrl, allocationModelGraphProvider, systemModelGraphProvider,
                        resourceEnvironmentModelGraphProvider, usageModelGraphProvider);

                deploymentVisualization.initialize();
            } catch (final MalformedURLException e) {
                AbstractServiceMain.LOG.debug("URL construction for deployment visualization failed.", e);
            } catch (final IOException e) {
                AbstractServiceMain.LOG.debug("Deployment visualization could not connect to visualization service.",
                        e);
            }

            SnapshotBuilder snapshotBuilder = null;
            URI perOpteryxUri = null;
            URI lqnsUri = null;
            URI deployablesFolder = null;
            if (!this.snapshotPath.isEmpty() && !this.perOpteryxUriPath.isEmpty() && !this.lqnsUriPath.isEmpty()
                    && !this.deployablesFolderPath.isEmpty()) {
                SnapshotBuilder.setBaseSnapshotURI(URI.createFileURI(this.snapshotPath));
                try {
                    snapshotBuilder = new SnapshotBuilder("Runtime", modelProvider);
                    perOpteryxUri = URI.createFileURI(this.perOpteryxUriPath);
                    lqnsUri = URI.createFileURI(this.lqnsUriPath);
                    deployablesFolder = URI.createFileURI(this.deployablesFolderPath);
                } catch (final InitializationException e) {
                    throw new ConfigurationException(e);
                }

            }

            try {
                return new ServiceConfiguration(this.listenPort, outputHostname, outputPort, "",
                        this.varianceOfUserGroups, this.thinkTime, this.closedWorkload, correspondenceModel,
                        usageModelProvider, repositoryModelProvider, resourceEnvironmentModelProvider,
                        allocationModelProvider, systemModelProvider, resourceEnvironmentModelGraphProvider,
                        resourceContainerModelGraphProvider, allocationModelGraphProvider,
                        assemblyContextModelGraphProvider, systemModelGraphProvider, assCtxSystemModelGraphProvider,
                        this.visualizationServiceURL, snapshotBuilder, perOpteryxUri, lqnsUri, deployablesFolder);
            } catch (final MalformedURLException e) {
                throw new ConfigurationException(e);
            }
        } else {
            return null;
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            return CommandLineParameterEvaluation.checkDirectory(this.pcmModelsDirectory, "Palladio Model", commander)
                    && CommandLineParameterEvaluation.checkDirectory(this.pcmModelsNeo4jDirectory,
                            "Palladio Model Neo4j", commander);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

}
