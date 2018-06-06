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
import java.util.List;

import org.iobserve.analysis.deployment.AllocationStage;
import org.iobserve.analysis.deployment.DeploymentCompositeStage;
import org.iobserve.analysis.deployment.UndeploymentCompositeStage;
import org.iobserve.analysis.privacy.GeoLocation;
import org.iobserve.analysis.systems.jpetstore.JPetStoreCallTraceMatcher;
import org.iobserve.analysis.traces.traceReconstruction.TraceReconstructionFilter;
import org.iobserve.common.record.IAllocationEvent;
import org.iobserve.common.record.IDeallocationEvent;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.IUndeployedEvent;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.service.privacy.violation.filter.AlarmAnalysis;
import org.iobserve.service.privacy.violation.filter.AlarmSink;
import org.iobserve.service.privacy.violation.filter.DataFlowDetectionStage;
import org.iobserve.service.privacy.violation.filter.EntryEventMapperStage;
import org.iobserve.service.privacy.violation.filter.ModelProbeController;
import org.iobserve.service.privacy.violation.filter.PrivacyWarner;
import org.iobserve.service.privacy.violation.filter.ProbeController;
import org.iobserve.service.privacy.violation.filter.ProbeMapper;
import org.iobserve.service.privacy.violation.filter.WarnSink;
import org.iobserve.stages.data.trace.ConcurrentHashMapWithCreate;
import org.iobserve.stages.data.trace.EventBasedTrace;
import org.iobserve.stages.data.trace.EventBasedTraceFactory;
import org.iobserve.stages.general.DynamicEventDispatcher;
import org.iobserve.stages.general.EntryCallStage;
import org.iobserve.stages.general.IEventMatcher;
import org.iobserve.stages.general.ImplementsEventMatcher;
import org.iobserve.stages.source.MultipleConnectionTcpReaderStage;
import org.iobserve.stages.source.NoneTraceMetadataRewriter;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import kieker.common.record.flow.IFlowRecord;
import teetime.framework.Configuration;

/**
 * Configuration for the log replayer.
 *
 * @author Reiner Jung
 *
 */
public class PrivacyViolationDetectionConfiguration extends Configuration {

    private static final int BUFFER_SIZE = 4096;

    /**
     * Configuration for the JSS privacy violation detection service.
     *
     * @param inputPort
     *            port to listen for Kieker records
     * @param outputs
     *            host and port for the Kieker adaptive monitoring
     * @param correspondenceProvider
     *            correspondence model
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
     * @param warningFile
     *            warnings
     * @param alarmFile
     *            alarms
     * @throws IOException
     *             when files cannot be opened
     */
    public PrivacyViolationDetectionConfiguration(final int inputPort, final List<ConnectionData> outputs,
            final IModelProvider<AssemblyEntry> correspondenceProvider,
            final IModelProvider<Repository> repositoryModelProvider,
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final IModelProvider<Allocation> allocationModelProvider,
            final IModelProvider<AllocationContext> allocationContextModelProvider,
            final IModelProvider<System> systemModelProvider, final IModelProvider<PrivacyModel> privacyModelProvider,
            final File warningFile, final File alarmFile) throws IOException {

        final kieker.common.configuration.Configuration configuration = new kieker.common.configuration.Configuration();

        /** instantiating filters. */
        final MultipleConnectionTcpReaderStage reader = new MultipleConnectionTcpReaderStage(inputPort,
                PrivacyViolationDetectionConfiguration.BUFFER_SIZE, new NoneTraceMetadataRewriter());

        final IEventMatcher<IDeployedEvent> deployedEventMatcher = new ImplementsEventMatcher<>(IDeployedEvent.class,
                null);
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

        /** deployment. */
        final DeploymentCompositeStage deploymentStage = new DeploymentCompositeStage(resourceEnvironmentModelProvider,
                allocationModelProvider, allocationContextModelProvider, correspondenceProvider);
        final UndeploymentCompositeStage undeploymentStage = new UndeploymentCompositeStage(
                allocationContextModelProvider, correspondenceProvider);

        /** geolocation. */
        final GeoLocation geoLocation = new GeoLocation(resourceEnvironmentModelProvider);

        final PrivacyWarner privacyWarner = new PrivacyWarner(allocationModelProvider, systemModelProvider,
                resourceEnvironmentModelProvider, repositoryModelProvider, privacyModelProvider);

        final ConcurrentHashMapWithCreate<Long, EventBasedTrace> traceBuffer = new ConcurrentHashMapWithCreate<>(
                EventBasedTraceFactory.INSTANCE);
        final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(traceBuffer);

        final EntryCallStage entryCallStage = new EntryCallStage(new JPetStoreCallTraceMatcher());
        final EntryEventMapperStage entryEventMapperStage = new EntryEventMapperStage(correspondenceProvider);
        final DataFlowDetectionStage dataFlowDetectionStage = new DataFlowDetectionStage(allocationModelProvider,
                systemModelProvider, resourceEnvironmentModelProvider);
        final AlarmAnalysis alarmAnalysis = new AlarmAnalysis();

        final ModelProbeController modelProbeController = new ModelProbeController(allocationModelProvider,
                systemModelProvider, resourceEnvironmentModelProvider);
        final ProbeMapper probeMapper = new ProbeMapper(correspondenceProvider);
        final ProbeController probeController = new ProbeController(outputs);

        try {
            final AlarmSink alarmSink = new AlarmSink(alarmFile);

            try {
                final WarnSink warnSink = new WarnSink(warningFile);

                /** connect ports. */
                this.connectPorts(reader.getOutputPort(), eventDispatcher.getInputPort());
                this.connectPorts(deployedEventMatcher.getOutputPort(), deploymentStage.getDeployedInputPort());
                this.connectPorts(undeployedEventMatcher.getOutputPort(), undeploymentStage.getUndeployedInputPort());
                this.connectPorts(allocationEventMatcher.getOutputPort(), allocationStage.getInputPort());

                this.connectPorts(deploymentStage.getDeployedOutputPort(), geoLocation.getInputPort());
                this.connectPorts(geoLocation.getOutputPort(), privacyWarner.getDeployedInputPort());
                this.connectPorts(undeploymentStage.getUndeployedOutputPort(), privacyWarner.getUndeployedInputPort());

                this.connectPorts(privacyWarner.getProbesOutputPort(), modelProbeController.getInputPort());
                this.connectPorts(modelProbeController.getOutputPort(), probeMapper.getInputPort());
                this.connectPorts(probeMapper.getOutputPort(), probeController.getInputPort());
                this.connectPorts(privacyWarner.getWarningsOutputPort(), warnSink.getInputPort());

                this.connectPorts(flowMatcher.getOutputPort(), traceReconstructionFilter.getInputPort());
                this.connectPorts(traceReconstructionFilter.getTraceValidOutputPort(), entryCallStage.getInputPort());
                this.connectPorts(entryCallStage.getOutputPort(), entryEventMapperStage.getInputPort());
                this.connectPorts(entryEventMapperStage.getOutputPort(), dataFlowDetectionStage.getInputPort());
                this.connectPorts(dataFlowDetectionStage.getOutputPort(), alarmAnalysis.getInputPort());
                this.connectPorts(alarmAnalysis.getOutputPort(), alarmSink.getInputPort());
            } catch (final IOException eWarning) {
                throw new IOException("Cannot create warning file.", eWarning);
            }
        } catch (final IOException eAlarm) {
            throw new IOException("Cannot create warning file.", eAlarm);
        }

    }

}
