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
package org.iobserve.analysis.configurations;

import org.iobserve.analysis.deployment.AllocationStage;
import org.iobserve.analysis.deployment.DeploymentCompositeStage;
import org.iobserve.analysis.deployment.UndeploymentCompositeStage;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.analysis.feature.IBehaviorCompositeStage;
import org.iobserve.analysis.feature.IContainerManagementSinksStage;
import org.iobserve.analysis.traces.TraceReconstructionCompositeStage;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.service.source.ISourceCompositeStage;
import org.iobserve.stages.general.ConfigurationException;
import org.iobserve.stages.general.RecordSwitch;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.framework.Configuration;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

/**
 * This is a generic configuration for all analyses.
 *
 * Note: We already added lines to deallocation. However, deallocation is currently missing.
 *
 * @author Reiner Jung
 *
 */
public class AnalysisConfiguration extends Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisConfiguration.class);

    private static final String DELIMETER = ",";

    private final RecordSwitch recordSwitch;

    private AllocationStage allocationStage;

    private DeploymentCompositeStage deploymentStage;

    private UndeploymentCompositeStage undeploymentStage;

    /**
     * Create an analysis configuration, configured by a configuration object.
     *
     * @param configuration
     *            the configuration parameter
     * @param repositoryModelProvider
     * @param resourceEnvironmentModelProvider
     *            provider for an environment model
     * @param allocationModelProvider
     *            provider for the allocation model
     * @param systemModelProvider
     *            provider for the system model
     * @param usageModelProvider
     * @param correspondenceModelProvider
     *            provider for the correspondence model
     * @throws ConfigurationException
     *             if the configuration faisl
     */
    public AnalysisConfiguration(final kieker.common.configuration.Configuration configuration,
            final IModelProvider<Repository> repositoryModelProvider,
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final IModelProvider<Allocation> allocationModelProvider, final IModelProvider<System> systemModelProvider,
            final IModelProvider<UsageModel> usageModelProvider, final ICorrespondence correspondenceModelProvider)
            throws ConfigurationException {
        this.recordSwitch = new RecordSwitch();

        /** Source stage. */
        final String sourceClassName = configuration.getStringProperty(ConfigurationKeys.SOURCE);
        if (!sourceClassName.isEmpty()) {
            final ISourceCompositeStage sourceCompositeStage = InstantiationFactory
                    .createWithConfiguration(ISourceCompositeStage.class, sourceClassName, configuration);

            this.connectPorts(sourceCompositeStage.getOutputPort(), this.recordSwitch.getInputPort());

            this.containerManagement(configuration, resourceEnvironmentModelProvider, allocationModelProvider,
                    systemModelProvider, correspondenceModelProvider);
            this.traceProcessing(configuration);
            this.geoLocation(configuration);
        } else {
            AnalysisConfiguration.LOGGER.error("Initialization incomplete: No source stage specified.");
            throw new ConfigurationException("Initialization incomplete: No source stage specified.");
        }
    }

    /**
     * Create all stages necessary for the container management.
     *
     * @param configuration
     *            potential configuration parameter for filters
     * @param resourceEnvironmentModelProvider
     * @param allocationModelProvider
     * @param systemModelProvider
     * @param correspondenceModelProvider
     * @throws ConfigurationException
     *             when configuration fails
     */
    private void containerManagement(final kieker.common.configuration.Configuration configuration,
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final IModelProvider<Allocation> allocationModelProvider, final IModelProvider<System> systemModelProvider,
            final ICorrespondence correspondenceModelProvider) throws ConfigurationException {
        if (configuration.getBooleanProperty(ConfigurationKeys.CONTAINER_MANAGEMENT, false)) {

            // Initiate stages and connect their ports according to toggle settings.
            /** allocation. */
            this.allocationStage = new AllocationStage(resourceEnvironmentModelProvider);
            /** connect ports. */
            this.connectPorts(this.recordSwitch.getAllocationOutputPort(), this.allocationStage.getInputPort());

            /** deployment. */
            this.deploymentStage = new DeploymentCompositeStage(resourceEnvironmentModelProvider,
                    allocationModelProvider, systemModelProvider, correspondenceModelProvider);
            /** connect ports. */
            this.connectPorts(this.recordSwitch.getDeployedOutputPort(), this.deploymentStage.getDeployedInputPort());

            /** undeployment. */
            this.undeploymentStage = new UndeploymentCompositeStage(resourceEnvironmentModelProvider,
                    allocationModelProvider, systemModelProvider, correspondenceModelProvider);
            /** connect ports. */
            this.connectPorts(this.recordSwitch.getUndeployedOutputPort(),
                    this.undeploymentStage.getUndeployedInputPort());

            /** deallocation. */
            // this.deallocationStage = new DeallocationStage(resourceEnvironmentModelProvider);
            // TODO missing
            /** connect ports. */
            // this.connectPorts(this.recordSwitch.getDeallocationOutputPort(),
            // this.deallocationStage.getInputPort(targetPort));

            /** dependent features. */
            this.createContainerManagementSink(configuration);
        }

    }

    /**
     * Create sinks for container management visualization.
     *
     * @param configuration
     *            configuration object
     * @throws ConfigurationException
     *             when configuration fails
     */
    private void createContainerManagementSink(final kieker.common.configuration.Configuration configuration)
            throws ConfigurationException {
        final String[] containerManagementSinks = configuration
                .getStringArrayProperty(ConfigurationKeys.CONTAINER_MANAGEMENT_SINK, AnalysisConfiguration.DELIMETER);
        if (containerManagementSinks.length == 1) {
            final IContainerManagementSinksStage containerManagementSinksStage = InstantiationFactory
                    .createWithConfiguration(IContainerManagementSinksStage.class, containerManagementSinks[0],
                            configuration);
            /** connect ports. */
            this.connectPorts(this.allocationStage.getAllocationNotifyOutputPort(),
                    containerManagementSinksStage.getAllocationInputPort());
            // this.connectPorts(this.deallocation.getAllocationNotifyOutputPort(),
            // containerManagementSinksStage.getDeallocationInputPort());
            this.connectPorts(this.deploymentStage.getDeployedOutputPort(),
                    containerManagementSinksStage.getDeployedInputPort());
            this.connectPorts(this.undeploymentStage.getUndeployedOutputPort(),
                    containerManagementSinksStage.getUndeployedInputPort());
        } else if (containerManagementSinks.length > 1) {
            /** In case of multiple outputs, we require distributors. */
            final Distributor<ResourceContainer> allocationDistributor = new Distributor<>(
                    new CopyByReferenceStrategy());
            // final Distributor<ResourceContainer> deallocationDistributor = new Distributor<>(
            // new CopyByReferenceStrategy());
            final Distributor<PCMDeployedEvent> deploymentDistributor = new Distributor<>(
                    new CopyByReferenceStrategy());
            final Distributor<PCMUndeployedEvent> undeploymentDistributor = new Distributor<>(
                    new CopyByReferenceStrategy());

            /** link distributor to container management. */
            this.connectPorts(this.allocationStage.getAllocationNotifyOutputPort(),
                    allocationDistributor.getInputPort());
            // this.connectPorts(this.deallocation.getAllocationNotifyOutputPort(),
            // allocationDistributor.getInputPort());
            this.connectPorts(this.deploymentStage.getDeployedOutputPort(), deploymentDistributor.getInputPort());
            this.connectPorts(this.undeploymentStage.getUndeployedOutputPort(), undeploymentDistributor.getInputPort());

            /** Create and connect sinks. */
            for (final String containerManagementSink : containerManagementSinks) {
                final IContainerManagementSinksStage containerManagementSinksStage = InstantiationFactory
                        .createWithConfiguration(IContainerManagementSinksStage.class, containerManagementSink,
                                configuration);
                /** connect ports. */
                this.connectPorts(allocationDistributor.getNewOutputPort(),
                        containerManagementSinksStage.getAllocationInputPort());
                // this.connectPorts(deallocationDistributor.getNewOutputPort(),
                // containerManagementSinksStage.getDeallocationInputPort());
                this.connectPorts(deploymentDistributor.getNewOutputPort(),
                        containerManagementSinksStage.getDeployedInputPort());
                this.connectPorts(undeploymentDistributor.getNewOutputPort(),
                        containerManagementSinksStage.getUndeployedInputPort());
            }
        } else {
            AnalysisConfiguration.LOGGER.warn(
                    "No container management sinks specified. Therefore, deployment and allocation changes will not be communicated.");
        }
    }

    /**
     * Create trace related filter chains.
     *
     * @param configuration
     *            filter configurations
     * @throws ConfigurationException
     *             when configuration fails
     */
    private void traceProcessing(final kieker.common.configuration.Configuration configuration)
            throws ConfigurationException {
        if (configuration.getBooleanProperty(ConfigurationKeys.TRACES, false)) {
            final TraceReconstructionCompositeStage traceReconstructionStage = new TraceReconstructionCompositeStage(
                    configuration);

            OutputPort<EventBasedTrace> behaviorClusteringEventBasedTracePort = traceReconstructionStage
                    .getTraceValidOutputPort();
            OutputPort<EventBasedTrace> dataFlowEventBasedTracePort = traceReconstructionStage
                    .getTraceValidOutputPort();

            /** Connect ports. */
            this.connectPorts(this.recordSwitch.getFlowOutputPort(), traceReconstructionStage.getInputPort());

            /** Include distributor to support tow simultaneous sinks. */
            if (configuration.getBooleanProperty(ConfigurationKeys.DATA_FLOW, false)
                    && !configuration.getStringProperty(ConfigurationKeys.BEHAVIOR_CLUSTERING).isEmpty()) {
                final IDistributorStrategy strategy = new CopyByReferenceStrategy();
                final Distributor<EventBasedTrace> distributor = new Distributor<>(strategy);
                this.connectPorts(traceReconstructionStage.getTraceValidOutputPort(), distributor.getInputPort());

                behaviorClusteringEventBasedTracePort = distributor.getNewOutputPort();
                dataFlowEventBasedTracePort = distributor.getNewOutputPort();
            }

            /** Initialize depending features. */
            this.behaviorClustering(configuration, behaviorClusteringEventBasedTracePort);
            this.dataflow(configuration, dataFlowEventBasedTracePort);
        }
    }

    /**
     * Create data flow processing setup.
     *
     * @param configuration
     * @param eventBasedTraceOutputPort
     */
    private void dataflow(final kieker.common.configuration.Configuration configuration,
            final OutputPort<EventBasedTrace> eventBasedTraceOutputPort) {
        if (configuration.getBooleanProperty(ConfigurationKeys.DATA_FLOW, false)) {
            /** connect ports. */
            // this.connectPorts(eventBasedTraceOutputPort, targetPort);
            AnalysisConfiguration.LOGGER.warn("Configuration for dataflow analysis missing.");
        }
    }

    /**
     * Create the behavioral clustering.
     *
     * @param configuration
     *            analysis configuration
     * @throws ConfigurationException
     *             when filter configuration fails
     */
    private void behaviorClustering(final kieker.common.configuration.Configuration configuration,
            final OutputPort<EventBasedTrace> eventBasedTraceOutputPort) throws ConfigurationException {
        final String behaviorClustringClassName = configuration
                .getStringProperty(ConfigurationKeys.BEHAVIOR_CLUSTERING);
        if (!behaviorClustringClassName.isEmpty()) {
            final IBehaviorCompositeStage behavior = InstantiationFactory
                    .createWithConfiguration(IBehaviorCompositeStage.class, behaviorClustringClassName, configuration);
            this.connectPorts(eventBasedTraceOutputPort, behavior.getEventBasedTracePort());
            this.connectPorts(this.recordSwitch.getSessionEventOutputPort(), behavior.getSessionEventInputPort());

            if (configuration.getBooleanProperty(ConfigurationKeys.BEHAVIOR_CLUSTERING_SINK)) {
                // TODO needs visualization trigger
                AnalysisConfiguration.LOGGER.warn("Configuration for behavior sink missing.");
            }
        }
    }

    private void geoLocation(final kieker.common.configuration.Configuration configuration) {
        if (configuration.getBooleanProperty(ConfigurationKeys.GEO_LOCATION, false)) {
            AnalysisConfiguration.LOGGER.warn("Configuration for geolocation.");
        }
    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }
}
