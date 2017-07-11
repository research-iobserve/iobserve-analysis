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
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

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
public final class AnalysisMainNeo4j {

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

    @Parameter(names = { "-s", "--system" }, required = true, description = "system id.")
    private String systemId;

    @Parameter(names = { "-p",
            "--pcm" }, required = true, description = "Directory containing PCM model data.", converter = FileConverter.class)
    private File pcmModelsDirectory;

    @Parameter(names = { "-pn4j",
            "--pcmneo4j" }, required = true, description = "Directory containing Neo4j database with PCM model data.", converter = FileConverter.class)
    private File pcmModelsNeo4jDirectory;

    /**
     * Default constructor.
     */
    private AnalysisMainNeo4j() {
        // do nothing here
    }

    /**
     * Main function.
     *
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {
        final AnalysisMainNeo4j main = new AnalysisMainNeo4j();
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
                final InitializeModelProviders modelProvider = new InitializeModelProviders(this.pcmModelsDirectory);

                final ICorrespondence correspondenceModel = modelProvider.getCorrespondenceModel();
                final UsageModelProvider usageModelProvider = modelProvider.getUsageModelProvider();
                final RepositoryModelProvider repositoryModelProvider = modelProvider.getRepositoryModelProvider();
                final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider = modelProvider
                        .getResourceEnvironmentModelProvider();
                final AllocationModelProvider allocationModelProvider = modelProvider.getAllocationModelProvider();
                final SystemModelProvider systemModelProvider = modelProvider.getSystemModelProvider();

                /** Neo4j database ************************************************************/
                // Create a graph loader to receive the different neo4j graphs for each model
                final GraphLoader graphLoader = new GraphLoader(this.pcmModelsNeo4jDirectory);

                // If you use the database for the very first time the graphs will be empty. You can
                // initialize the graph with a model from an old provider. Note: The initialization
                // overwrites possibly existing models, so you want to do this just on the very
                // first setup or if you want to return to clean test data in the graph.
                Graph allocationModelGraph = graphLoader
                        .initializeAllocationModelGraph(allocationModelProvider.getModel());
                System.out.println("Initialized allocation model graph");
                Graph repositoryModelGraph = graphLoader
                        .initializeRepositoryModelGraph(repositoryModelProvider.getModel());
                System.out.println("Initialized repository model graph");
                Graph resourceEnvironmentModelGraph = graphLoader
                        .initializeResourceEnvironmentModelGraph(resourceEnvironmentModelProvider.getModel());
                System.out.println("Initialized resource environment model graph");
                Graph systemModelGraph = graphLoader.initializeSystemModelGraph(systemModelProvider.getModel());
                System.out.println("Initialized system model graph");
                Graph usageModelGraph = graphLoader.initializeUsageModelGraph(usageModelProvider.getModel());
                System.out.println("Initialized usage model graph");

                // Alternatively, if there are already graphs in the database, you can simply get
                // them
                allocationModelGraph = graphLoader.getAllocationModelGraph();
                System.out.println("Loaded allocation model graph");
                repositoryModelGraph = graphLoader.getRepositoryModelGraph();
                System.out.println("Loaded repository model graph");
                resourceEnvironmentModelGraph = graphLoader.getResourceEnvironmentModelGraph();
                System.out.println("Loaded resource environment model graph");
                systemModelGraph = graphLoader.getSystemModelGraph();
                System.out.println("Loaded system model graph");
                usageModelGraph = graphLoader.getUsageModelGraph();
                System.out.println("Loaded usage model graph");

                // You can access it with a model provider, for example
                final String idOfInterfaceIWant = repositoryModelProvider.getModel().getInterfaces__Repository().get(0)
                        .getId();
                final OperationInterface opInter = new ModelProvider<OperationInterface>(repositoryModelGraph)
                        .readComponentById(OperationInterface.class, idOfInterfaceIWant);
                System.out.println(opInter);

                // Or you can clone the current graph to a new version
                new ModelProvider<ResourceEnvironment>(resourceEnvironmentModelGraph)
                        .cloneNewGraphVersion(ResourceEnvironment.class);

                // new graphModelProvider
                final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider = new ModelProvider<>(
                        resourceEnvironmentModelGraph);
                final ModelProvider<ResourceContainer> resourceContainerModelGraphProvider = new ModelProvider<>(
                        resourceEnvironmentModelGraph);
                final ModelProvider<Allocation> allocationModelGraphProvider = new ModelProvider<>(
                        allocationModelGraph);
                final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider = new ModelProvider<>(
                        systemModelGraph);
                /******************************************************************************/

                final Configuration configuration = new ServiceConfiguration(this.listenPort, outputHostname,
                        outputPort, this.systemId, this.varianceOfUserGroups, this.thinkTime, this.closedWorkload,
                        correspondenceModel, usageModelProvider, repositoryModelProvider,
                        resourceEnvironmentModelProvider, resourceEnvironmentModelGraphProvider,
                        resourceContainerModelGraphProvider, allocationModelProvider, allocationModelGraphProvider,
                        systemModelProvider, systemModelGraphProvider);

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
