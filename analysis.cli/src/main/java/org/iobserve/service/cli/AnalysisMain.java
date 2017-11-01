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

import org.iobserve.analysis.FileObservationConfiguration;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.clustering.EAggregationType;
import org.iobserve.analysis.clustering.EOutputMode;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.Graph;
import org.iobserve.analysis.modelneo4j.GraphLoader;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

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

    @Parameter(names = { "-u",
            "--ubm-visualization" }, required = false, description = "User behavior model visualitation service URL.")
    private String visualizationServiceURL;

    @Parameter(names = { "-o", "--ubm-output" }, required = false, description = "File output of user behavior.")
    private String outputPathPrefix;

    @Parameter(names = { "-m", "--aggregation-type" }, required = true, description = "Aggregation type.")
    private String aggregationTypeName;

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
            AnalysisMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        } catch (final IOException e) {
            AnalysisMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander) throws IOException {

        if (this.help) {
            commander.usage();
            System.exit(1);
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

            final Configuration configuration = new FileObservationConfiguration(monitoringDataDirectories,
                    correspondenceModel, usageModelProvider, repositoryModelProvider, resourceEnvironmentModelProvider,
                    resourceEnvironmentModelGraphProvider, allocationModelProvider, allocationModelGraphProvider,
                    systemModelProvider, systemModelGraphProvider, this.varianceOfUserGroups, this.thinkTime,
                    this.closedWorkload, this.visualizationServiceURL, aggregationType, outputMode);

            AnalysisMain.LOG.info("Analysis configuration");
            final Execution<Configuration> analysis = new Execution<>(configuration);
            AnalysisMain.LOG.info("Analysis start");
            analysis.executeBlocking();
            AnalysisMain.LOG.info("Anaylsis complete");
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
