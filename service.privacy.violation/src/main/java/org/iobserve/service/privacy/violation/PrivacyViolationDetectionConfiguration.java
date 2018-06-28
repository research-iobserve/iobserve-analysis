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
import java.util.ArrayList;
import java.util.List;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;

import teetime.framework.Configuration;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.analysis.deployment.AllocationStage;
import org.iobserve.analysis.deployment.DeallocationStage;
import org.iobserve.analysis.deployment.DeploymentCompositeStage;
import org.iobserve.analysis.deployment.UndeploymentCompositeStage;
import org.iobserve.analysis.privacy.GeoLocationStage;
import org.iobserve.analysis.systems.jpetstore.JPetStoreCallTraceMatcher;
import org.iobserve.analysis.traces.traceReconstruction.TraceReconstructionFilter;
import org.iobserve.common.record.IAllocationEvent;
import org.iobserve.common.record.IDeallocationEvent;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.IUndeployedEvent;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.model.provider.neo4j.ModelGraph;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.service.privacy.violation.filter.AlarmAnalysis;
import org.iobserve.service.privacy.violation.filter.AlarmSink;
import org.iobserve.service.privacy.violation.filter.DataFlowDetectionStage;
import org.iobserve.service.privacy.violation.filter.EntryEventMapperStage;
import org.iobserve.service.privacy.violation.filter.ModelProbeController;
import org.iobserve.service.privacy.violation.filter.PrivacyWarner;
import org.iobserve.service.privacy.violation.filter.ProbeController;
import org.iobserve.service.privacy.violation.filter.ProbeMapper;
import org.iobserve.service.privacy.violation.filter.WarnSink;
import org.iobserve.service.source.ISourceCompositeStage;
import org.iobserve.stages.data.trace.ConcurrentHashMapWithCreate;
import org.iobserve.stages.data.trace.EventBasedTrace;
import org.iobserve.stages.data.trace.EventBasedTraceFactory;
import org.iobserve.stages.general.ConfigurationException;
import org.iobserve.stages.general.DynamicEventDispatcher;
import org.iobserve.stages.general.EntryCallStage;
import org.iobserve.stages.general.IEventMatcher;
import org.iobserve.stages.general.ImplementsEventMatcher;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * Configuration for the log replayer.
 *
 * @author Reiner Jung
 *
 */
public class PrivacyViolationDetectionConfiguration extends Configuration {

    /**
     * Configuration for the JSS privacy violation detection service.
     *
     * @param configuration
     *            configuration object
     * @param correspondenceGraph
     *            correspondence model graph
     * @param repositoryModelProvider
     *            repository model provider
     * @param resourceEnvironmentModelProvider
     *            resource environment model provider
     * @param allocationModelProvider
     *            allocation model provider
     * @param allocationContextModelProvider
     *            allocation context model provider (view)
     * @param systemModelProvider
     *            system model provider
     * @param privacyModelGraph
     *            graph for the privacy model
     * @param warningFile
     *            warnings
     * @param alarmFile
     *            alarms
     * @throws IOException
     *             when files cannot be opened
     * @throws ConfigurationException
     *             on configuration errors
     */
    public PrivacyViolationDetectionConfiguration(final kieker.common.configuration.Configuration configuration,
            final ModelGraph correspondenceGraph, final IModelProvider<Repository> repositoryModelProvider,
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final IModelProvider<Allocation> allocationModelProvider,
            final IModelProvider<AllocationContext> allocationContextModelProvider,
            final IModelProvider<System> systemModelProvider, final ModelGraph privacyModelGraph,
            final File warningFile, final File alarmFile) throws IOException, ConfigurationException {

        final ModelProvider<AssemblyContext> assemblyContextModelProvider = new ModelProvider<>(
                systemModelProvider.getGraph(), ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final IModelProvider<ResourceContainer> resourceContainerModelProvider = new ModelProvider<>(
                resourceEnvironmentModelProvider.getGraph(), ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        /** instantiating filters. */
        final String sourceClassName = configuration.getStringProperty(ConfigurationKeys.SOURCE);
        if (!sourceClassName.isEmpty()) {
            final ISourceCompositeStage sourceCompositeStage = InstantiationFactory
                    .createWithConfiguration(ISourceCompositeStage.class, sourceClassName, configuration);

            final IEventMatcher<IDeployedEvent> deployedEventMatcher = new ImplementsEventMatcher<>(
                    IDeployedEvent.class, null);
            final IEventMatcher<IUndeployedEvent> undeployedEventMatcher = new ImplementsEventMatcher<>(
                    IUndeployedEvent.class, deployedEventMatcher);

            final IEventMatcher<IAllocationEvent> allocationEventMatcher = new ImplementsEventMatcher<>(
                    IAllocationEvent.class, undeployedEventMatcher);
            final IEventMatcher<IDeallocationEvent> deallocationEventMatcher = new ImplementsEventMatcher<>(
                    IDeallocationEvent.class, allocationEventMatcher);

            final IEventMatcher<IFlowRecord> flowMatcher = new ImplementsEventMatcher<>(IFlowRecord.class,
                    deallocationEventMatcher);

            final DynamicEventDispatcher eventDispatcher = new DynamicEventDispatcher(flowMatcher, true, true, false);

            /** allocation. */
            final AllocationStage allocationStage = new AllocationStage(resourceEnvironmentModelProvider);
            final DeallocationStage deallocationStage = new DeallocationStage(resourceEnvironmentModelProvider);

            /** deployment. */
            final DeploymentCompositeStage deploymentStage = new DeploymentCompositeStage(
                    resourceEnvironmentModelProvider, allocationModelProvider, allocationContextModelProvider,
                    assemblyContextModelProvider, correspondenceGraph);
            final UndeploymentCompositeStage undeploymentStage = new UndeploymentCompositeStage(
                    allocationContextModelProvider, assemblyContextModelProvider, resourceContainerModelProvider,
                    correspondenceGraph);

            /** geo location. */
            final GeoLocationStage geoLocationStage = new GeoLocationStage(privacyModelGraph);

            final PrivacyWarner privacyWarner = new PrivacyWarner(configuration, allocationModelProvider,
                    systemModelProvider, resourceEnvironmentModelProvider, repositoryModelProvider,
                    new ModelProvider<PrivacyModel>(privacyModelGraph, ModelProvider.PCM_ENTITY_NAME,
                            ModelProvider.PCM_ID));
            privacyWarner.declareActive();

            final ConcurrentHashMapWithCreate<Long, EventBasedTrace> traceBuffer = new ConcurrentHashMapWithCreate<>(
                    EventBasedTraceFactory.INSTANCE);
            final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(traceBuffer);

            final EntryCallStage entryCallStage = new EntryCallStage(new JPetStoreCallTraceMatcher());
            final EntryEventMapperStage entryEventMapperStage = new EntryEventMapperStage(correspondenceGraph,
                    repositoryModelProvider.getGraph(), systemModelProvider.getGraph(),
                    allocationModelProvider.getGraph());
            final DataFlowDetectionStage dataFlowDetectionStage = new DataFlowDetectionStage(allocationModelProvider,
                    systemModelProvider, resourceEnvironmentModelProvider);
            final AlarmAnalysis alarmAnalysis = new AlarmAnalysis();

            final ModelProbeController modelProbeController = new ModelProbeController(allocationModelProvider,
                    systemModelProvider, resourceEnvironmentModelProvider);
            final ProbeMapper probeMapper = new ProbeMapper(correspondenceGraph);

            final ProbeController probeController = new ProbeController(this.createProbeConnections(
                    configuration.getStringArrayProperty(PrivacyConfigurationsKeys.PROBE_CONNECTIONS_OUTPUTS)));

            final EventDelayer<IMonitoringRecord> eventDelayer = new EventDelayer<>(200);

            try {
                final AlarmSink alarmSink = new AlarmSink(alarmFile);

                try {
                    final WarnSink warnSink = new WarnSink(warningFile);

                    /** connect ports. */
                    this.connectPorts(sourceCompositeStage.getOutputPort(), eventDelayer.getInputPort());
                    this.connectPorts(eventDelayer.getOutputPort(), eventDispatcher.getInputPort());
                    this.connectPorts(deployedEventMatcher.getOutputPort(), deploymentStage.getDeployedInputPort());
                    this.connectPorts(undeployedEventMatcher.getOutputPort(),
                            undeploymentStage.getUndeployedInputPort());
                    this.connectPorts(allocationEventMatcher.getOutputPort(), allocationStage.getInputPort());
                    this.connectPorts(deallocationEventMatcher.getOutputPort(), deallocationStage.getInputPort());

                    this.connectPorts(deploymentStage.getDeployedOutputPort(), geoLocationStage.getInputPort());
                    this.connectPorts(geoLocationStage.getOutputPort(), privacyWarner.getDeployedInputPort());
                    this.connectPorts(undeploymentStage.getUndeployedOutputPort(),
                            privacyWarner.getUndeployedInputPort());

                    this.connectPorts(privacyWarner.getProbesOutputPort(), modelProbeController.getInputPort());
                    this.connectPorts(modelProbeController.getOutputPort(), probeMapper.getInputPort());
                    this.connectPorts(probeMapper.getOutputPort(), probeController.getInputPort());
                    this.connectPorts(privacyWarner.getWarningsOutputPort(), warnSink.getInputPort());

                    this.connectPorts(flowMatcher.getOutputPort(), traceReconstructionFilter.getInputPort());
                    this.connectPorts(traceReconstructionFilter.getTraceValidOutputPort(),
                            entryCallStage.getInputPort());
                    this.connectPorts(entryCallStage.getOutputPort(), entryEventMapperStage.getInputPort());
                    this.connectPorts(entryEventMapperStage.getOutputPort(), dataFlowDetectionStage.getInputPort());
                    this.connectPorts(dataFlowDetectionStage.getOutputPort(), alarmAnalysis.getInputPort());
                    this.connectPorts(alarmAnalysis.getOutputPort(), alarmSink.getInputPort());
                } catch (final IOException eWarning) { // NOPMD cannot be avoided to be used as flow
                                                       // control
                    throw new IOException("Cannot create warning file.", eWarning);
                }
            } catch (final IOException eAlarm) { // NOPMD cannot be avoided to be used as flow
                                                 // control
                throw new IOException("Cannot create alarm file.", eAlarm);
            }
        }
    }

    private List<ConnectionData> createProbeConnections(final String[] connectionConfigurations) {
        final List<ConnectionData> probeConnections = new ArrayList<>();

        for (final String connectionConfig : connectionConfigurations) {
            final String[] parameter = connectionConfig.split(":");
            if (parameter.length == 2) {
                probeConnections.add(new ConnectionData(parameter[0], Integer.parseInt(parameter[1])));
            }
        }

        return probeConnections;
    }

}
