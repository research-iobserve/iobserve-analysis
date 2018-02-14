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

import org.iobserve.analysis.ConfigurationException;
import org.iobserve.analysis.IBehaviorCompositeStage;
import org.iobserve.analysis.IContainerManagementCompositeStage;
import org.iobserve.analysis.IContainerManagementSinksStage;
import org.iobserve.analysis.IDataFlowCompositeStage;
import org.iobserve.analysis.IGeoLocationCompositeStage;
import org.iobserve.analysis.ISourceCompositeStage;
import org.iobserve.analysis.ITraceCompositeStage;
import org.iobserve.analysis.InstantiationFactory;
import org.iobserve.analysis.deployment.AllocationStage;
import org.iobserve.analysis.deployment.DeploymentCompositeStage;
import org.iobserve.analysis.deployment.UndeploymentCompositeStage;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.analysis.traces.TraceReconstructionCompositeStage;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.stages.general.RecordSwitch;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
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

    /** Set the used behavior aggregation and clustering stage class name. STRING */
    public static final String BEHAVIOR_CLUSTERING = IBehaviorCompositeStage.class.getCanonicalName();

    /** Set whether a behavior visualization sink shall be created. STRING ARRAY. */
    public static final String BEHAVIOR_CLUSTERING_SINK = AnalysisConfiguration.BEHAVIOR_CLUSTERING + ".sink.visual";

    /** Set whether container management shall be activated. BOOLEAN */
    public static final String CONTAINER_MANAGEMENT = IContainerManagementCompositeStage.class.getCanonicalName();

    /** Set whether trace reconstruction should be activated. BOOLEAN */
    public static final String TRACES = ITraceCompositeStage.class.getCanonicalName();

    /** Set whether data flow analysis should be activated. BOOLEAN */
    public static final String DATA_FLOW = IDataFlowCompositeStage.class.getCanonicalName();

    /** Set whether separate geo location events should be processed. BOOLEAN */
    public static final String GEO_LOCATION = IGeoLocationCompositeStage.class.getCanonicalName();

    /** Set the preferred source. STRING */
    public static final String SOURCE = ISourceCompositeStage.class.getCanonicalName();

    /** Set whether container management visualization sinks shall be created. STRING ARRAY */
    public static final String CONTAINER_MANAGEMENT_SINK = IContainerManagementCompositeStage.class.getCanonicalName()
            + ".sink";

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
     * @param resourceEnvironmentModelProvider
     *            provider for an environment model
     * @param allocationModelProvider
     *            provider for the allocation model
     * @param systemModelProvider
     *            provider for the system model
     * @param correspondenceModelProvider
     *            provider for the correspondence model
     * @throws ConfigurationException
     *             if the configuration faisl
     */
    public AnalysisConfiguration(final kieker.common.configuration.Configuration configuration,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final ModelProvider<Allocation> allocationModelProvider, final ModelProvider<System> systemModelProvider,
            final ICorrespondence correspondenceModelProvider) throws ConfigurationException {
        this.recordSwitch = new RecordSwitch();

        /** Source stage. */
        final String sourceClassName = configuration.getStringProperty(AnalysisConfiguration.SOURCE);
        if (!sourceClassName.isEmpty()) {
            final ISourceCompositeStage sourceCompositeStage = InstantiationFactory
                    .createAndInitialize(ISourceCompositeStage.class, sourceClassName, configuration);

            this.connectPorts(sourceCompositeStage.getOutputPort(), this.recordSwitch.getInputPort());

            this.containerManagement(configuration, resourceEnvironmentModelProvider, allocationModelProvider,
                    systemModelProvider, correspondenceModelProvider);
            this.trace(configuration);
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
     */
    private void containerManagement(final kieker.common.configuration.Configuration configuration,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final ModelProvider<Allocation> allocationModelProvider, final ModelProvider<System> systemModelProvider,
            final ICorrespondence correspondenceModelProvider) {
        if (configuration.getBooleanProperty(AnalysisConfiguration.CONTAINER_MANAGEMENT, false)) {
            /** allocation. */
            this.allocationStage = new AllocationStage(resourceEnvironmentModelProvider);

            /** deployment. */
            this.deploymentStage = new DeploymentCompositeStage(configuration, resourceEnvironmentModelProvider,
                    allocationModelProvider, systemModelProvider, correspondenceModelProvider);

            /** undeployment. */
            this.undeploymentStage = new UndeploymentCompositeStage(resourceEnvironmentModelProvider,
                    allocationModelProvider, systemModelProvider, correspondenceModelProvider);

            /** deallocation. */
            // this.deallocationStage = new DeallocationStage(resourceEnvironmentModelProvider);
            // TODO missing

            /** connect ports. */
            this.connectPorts(this.recordSwitch.getAllocationOutputPort(), this.allocationStage.getInputPort());
            // this.connectPorts(this.recordSwitch.getDeallocationOutputPort(),
            // this.deallocationStage.getInputPort(targetPort));
            this.connectPorts(this.recordSwitch.getDeployedOutputPort(), this.deploymentStage.getDeployedInputPort());
            this.connectPorts(this.recordSwitch.getUndeployedOutputPort(),
                    this.undeploymentStage.getUndeployedInputPort());

            /** dependent features. */
            this.createContainerManagementSink(configuration);
        }

    }

    /**
     * Create sinks for container management visualization.
     *
     * @param configuration
     *            configuration object
     */
    private void createContainerManagementSink(final kieker.common.configuration.Configuration configuration) {
        final String[] containerManagementSinks = configuration.getStringArrayProperty(
                AnalysisConfiguration.CONTAINER_MANAGEMENT_SINK, AnalysisConfiguration.DELIMETER);
        if (containerManagementSinks.length == 1) {
            final IContainerManagementSinksStage containerManagementSinksStage = InstantiationFactory
                    .createAndInitialize(IContainerManagementSinksStage.class, containerManagementSinks[0],
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
                        .createAndInitialize(IContainerManagementSinksStage.class, containerManagementSink,
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
     */
    private void trace(final kieker.common.configuration.Configuration configuration) {
        if (configuration.getBooleanProperty(AnalysisConfiguration.TRACES, false)) {
            final TraceReconstructionCompositeStage traceReconstructionStage = new TraceReconstructionCompositeStage(
                    configuration);

            OutputPort<EventBasedTrace> behaviorClusteringEventBasedTracePort = traceReconstructionStage
                    .getTraceValidOutputPort();
            OutputPort<EventBasedTrace> dataFlowEventBasedTracePort = traceReconstructionStage
                    .getTraceValidOutputPort();

            /** Connect ports. */
            this.connectPorts(this.recordSwitch.getFlowOutputPort(), traceReconstructionStage.getInputPort());

            /** Include distributor to support tow simultaneous sinks. */
            if (configuration.getBooleanProperty(AnalysisConfiguration.DATA_FLOW, false)
                    && !configuration.getStringProperty(AnalysisConfiguration.BEHAVIOR_CLUSTERING).isEmpty()) {
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

    private void dataflow(final kieker.common.configuration.Configuration configuration,
            final OutputPort<EventBasedTrace> eventBasedTraceOutputPort) {
        if (configuration.getBooleanProperty(AnalysisConfiguration.DATA_FLOW, false)) {
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
     */
    private void behaviorClustering(final kieker.common.configuration.Configuration configuration,
            final OutputPort<EventBasedTrace> eventBasedTraceOutputPort) {
        final String behaviorClustringClassName = configuration
                .getStringProperty(AnalysisConfiguration.BEHAVIOR_CLUSTERING);
        if (!behaviorClustringClassName.isEmpty()) {
            final IBehaviorCompositeStage behavior = InstantiationFactory
                    .createAndInitialize(IBehaviorCompositeStage.class, behaviorClustringClassName, configuration);
            this.connectPorts(eventBasedTraceOutputPort, behavior.getEventBasedTracePort());
            this.connectPorts(this.recordSwitch.getSessionEventOutputPort(), behavior.getSessionEventInputPort());

            if (configuration.getBooleanProperty(AnalysisConfiguration.BEHAVIOR_CLUSTERING_SINK)) {
                // TODO needs visualization trigger
                AnalysisConfiguration.LOGGER.warn("Configuration for behavior sink missing.");
            }
        }
    }

    private void geoLocation(final kieker.common.configuration.Configuration configuration) {
        if (configuration.getBooleanProperty(AnalysisConfiguration.GEO_LOCATION, false)) {
            AnalysisConfiguration.LOGGER.warn("Configuration for geolocation.");
        }
    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }

}
