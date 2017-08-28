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
import java.net.URL;

import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.Graph;
import org.iobserve.analysis.modelneo4j.GraphLoader;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.utils.ExecutionTimeLogger;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;

import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * Main class for starting the iObserve application.
 *
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class AnalysisMain {

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
            main.execute(commander);
        } catch (final ParameterException e) {
            System.err.println(e.getLocalizedMessage());
            commander.usage();
        } catch (final IOException e) {
            System.err.println(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander) throws IOException {
        if (this.help) {
            commander.usage();
            System.exit(1);
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
                Graph repositoryModelGraph = graphLoader
                        .initializeRepositoryModelGraph(repositoryModelProvider.getModel());
                // load neo4j graphs
                resourceEnvironmentModelGraph = graphLoader.getResourceEnvironmentModelGraph();
                allocationModelGraph = graphLoader.getAllocationModelGraph();
                systemModelGraph = graphLoader.getSystemModelGraph();
                usageModelGraph = graphLoader.getUsageModelGraph();
                repositoryModelGraph = graphLoader.getRepositoryModelGraph();
                // new graphModelProvider
                final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider = new ModelProvider<>(
                        resourceEnvironmentModelGraph);
                final ModelProvider<ResourceContainer> resourceContainerModelGraphProvider = new ModelProvider<>(
                        resourceEnvironmentModelGraph);
                final ModelProvider<ResourceContainer> allocationResourceContainerModelGraphProvider = new ModelProvider<>(
                        allocationModelGraph);
                final ModelProvider<Allocation> allocationModelGraphProvider = new ModelProvider<>(
                        allocationModelGraph);
                final ModelProvider<AssemblyContext> assemblyContextModelGraphProvider = new ModelProvider<>(
                        allocationModelGraph);
                final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider = new ModelProvider<>(
                        systemModelGraph);
                final ModelProvider<AssemblyContext> assCtxSystemModelGraphProvider = new ModelProvider<>(
                        systemModelGraph);
                final ModelProvider<UsageModel> usageScenarioModelGraphProvider = new ModelProvider<>(usageModelGraph);
                final ModelProvider<Repository> repositoryModelGraphProvider = new ModelProvider<>(
                        repositoryModelGraph);
                // get systemId
                final org.palladiosimulator.pcm.system.System systemModel = systemModelGraphProvider
                        .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);
                final String systemId = systemModel.getId();
                // URLs for sending updates to the deployment visualization
                final URL systemUrl = new URL("http://" + outputHostname + ":" + outputPort + "/v1/systems/");
                final URL changelogUrl = new URL(systemUrl + systemId + "/changelogs");

                final InitializeDeploymentVisualization deploymentVisualization = new InitializeDeploymentVisualization(
                        systemUrl, changelogUrl, allocationModelGraphProvider,
                        allocationResourceContainerModelGraphProvider, systemModelGraphProvider,
                        resourceEnvironmentModelGraphProvider, usageScenarioModelGraphProvider,
                        repositoryModelGraphProvider);
                try {
                    deploymentVisualization.initialize();
                } catch (final Exception e) {
                    System.out.println("deploymentVisualization.initialize() went wrong!");
                    e.printStackTrace();
                }

                final Configuration configuration = new ServiceConfiguration(this.listenPort, outputHostname,
                        outputPort, systemId, this.varianceOfUserGroups, this.thinkTime, this.closedWorkload,
                        correspondenceModel, usageModelProvider, repositoryModelProvider,
                        resourceEnvironmentModelProvider, resourceEnvironmentModelGraphProvider,
                        resourceContainerModelGraphProvider, allocationModelProvider, allocationModelGraphProvider,
                        assemblyContextModelGraphProvider, systemModelProvider, systemModelGraphProvider,
                        assCtxSystemModelGraphProvider);

                System.out.println("Analysis configuration");
                final Execution<Configuration> analysis = new Execution<>(configuration);
                System.out.println("Analysis start");
                analysis.executeBlocking();
                System.out.println("Anaylsis complete");
                ExecutionTimeLogger.getInstance().exportAsCsv();
            }
        }
    }

    private void checkDirectory(final File location, final String locationLabel, final JCommander commander)
            throws IOException {
        if (!location.exists()) {
            System.err.println(locationLabel + " path " + location.getCanonicalPath() + " does not exist.");
            commander.usage();
            System.exit(1);
        }
        if (!location.isDirectory()) {
            System.err.println(locationLabel + " path " + location.getCanonicalPath() + " is not a directory.");
            commander.usage();
            System.exit(1);
        }

    }

}
