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
import org.iobserve.analysis.ISourceCompositeStage;
import org.iobserve.analysis.ITraceCompositeStage;
import org.iobserve.analysis.InstantiationFactory;
import org.iobserve.analysis.traces.TraceReconstructionCompositeStage;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.stages.general.RecordSwitch;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.framework.Configuration;
import teetime.framework.OutputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

public class MJConfiguration extends Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MJConfiguration.class);

    /** Set the used behavior aggregation and clustering stage class name. STRING */
    public static final String BEHAVIOR_CLUSTERING = IBehaviorCompositeStage.class.getCanonicalName();

    /** Set whether a behavior visualization sink shall be created. STRING ARRAY. */
    public static final String BEHAVIOR_CLUSTERING_SINK = MJConfiguration.BEHAVIOR_CLUSTERING + ".sink.visual";

    /** Set whether trace reconstruction should be activated. BOOLEAN */
    public static final String TRACES = ITraceCompositeStage.class.getCanonicalName();

    /** Set the preferred source. STRING */
    public static final String SOURCE = ISourceCompositeStage.class.getCanonicalName();

    private final RecordSwitch recordSwitch;

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
    public MJConfiguration(final kieker.common.configuration.Configuration configuration,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final ModelProvider<Allocation> allocationModelProvider, final ModelProvider<System> systemModelProvider,
            final ICorrespondence correspondenceModelProvider) throws ConfigurationException {
        this.recordSwitch = new RecordSwitch();

        /** Source stage. */
        final String sourceClassName = configuration.getStringProperty(MJConfiguration.SOURCE);
        if (!sourceClassName.isEmpty()) {
            final ISourceCompositeStage sourceCompositeStage = InstantiationFactory
                    .createAndInitialize(ISourceCompositeStage.class, sourceClassName, configuration);

            this.connectPorts(sourceCompositeStage.getOutputPort(), this.recordSwitch.getInputPort());

            this.trace(configuration);
        } else {
            MJConfiguration.LOGGER.error("Initialization incomplete: No source stage specified.");
            throw new ConfigurationException("Initialization incomplete: No source stage specified.");
        }
    }

    /**
     * Create trace related filter chains.
     *
     * @param configuration
     *            filter configurations
     */
    private void trace(final kieker.common.configuration.Configuration configuration) {
        if (configuration.getBooleanProperty(MJConfiguration.TRACES, false)) {
            final TraceReconstructionCompositeStage traceReconstructionStage = new TraceReconstructionCompositeStage(
                    configuration);

            final OutputPort<EventBasedTrace> behaviorClusteringEventBasedTracePort = traceReconstructionStage
                    .getTraceValidOutputPort();

            /** Connect ports. */
            this.connectPorts(this.recordSwitch.getFlowOutputPort(), traceReconstructionStage.getInputPort());

            /** Initialize depending features. */
            this.behaviorClustering(configuration, behaviorClusteringEventBasedTracePort);
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
        final String behaviorClustringClassName = configuration.getStringProperty(MJConfiguration.BEHAVIOR_CLUSTERING);
        if (!behaviorClustringClassName.isEmpty()) {
            final IBehaviorCompositeStage behavior = InstantiationFactory
                    .createAndInitialize(IBehaviorCompositeStage.class, behaviorClustringClassName, configuration);
            this.connectPorts(eventBasedTraceOutputPort, behavior.getEventBasedTracePort());
            this.connectPorts(this.recordSwitch.getSessionEventOutputPort(), behavior.getSessionEventInputPort());

            if (configuration.getBooleanProperty(MJConfiguration.BEHAVIOR_CLUSTERING_SINK)) {
                // TODO needs visualization trigger
                MJConfiguration.LOGGER.warn("Configuration for behavior sink missing.");
            }
        }
    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }

}
