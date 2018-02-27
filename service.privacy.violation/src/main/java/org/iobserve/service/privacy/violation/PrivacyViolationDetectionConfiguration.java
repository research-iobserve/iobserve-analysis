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
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.privacy.violation.filter.AlarmAnalysis;
import org.iobserve.service.privacy.violation.filter.AlarmSink;
import org.iobserve.service.privacy.violation.filter.DataFlowDetectionStage;
import org.iobserve.service.privacy.violation.filter.EntryEventMapperStage;
import org.iobserve.service.privacy.violation.filter.ModelProbeController;
import org.iobserve.service.privacy.violation.filter.PrivacyWarner;
import org.iobserve.service.privacy.violation.filter.ProbeController;
import org.iobserve.service.privacy.violation.filter.ProbeMapper;
import org.iobserve.service.privacy.violation.filter.WarnSink;
import org.iobserve.stages.general.EntryCallStage;
import org.iobserve.stages.general.RecordSwitch;
import org.iobserve.stages.source.MultipleConnectionTcpReaderStage;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import teetime.framework.Configuration;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;
import teetime.stage.trace.traceReconstruction.EventBasedTraceFactory;
import teetime.stage.trace.traceReconstruction.TraceReconstructionFilter;
import teetime.util.ConcurrentHashMapWithDefault;

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
     * @param rac
     *            correspondence model
     * @param resourceEnvironmentModelProvider
     *            resource environment model provider
     * @param allocationModelProvider
     *            allocation model provider
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
            final ICorrespondence rac, final ModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final ModelProvider<Allocation> allocationModelProvider, final ModelProvider<System> systemModelProvider,
            final File warningFile, final File alarmFile) throws IOException {

        final kieker.common.configuration.Configuration configuration = new kieker.common.configuration.Configuration();

        /** instantiating filters. */
        final MultipleConnectionTcpReaderStage reader = new MultipleConnectionTcpReaderStage(inputPort,
                PrivacyViolationDetectionConfiguration.BUFFER_SIZE);
        final RecordSwitch recordSwitch = new RecordSwitch();

        /** allocation. */
        final AllocationStage allocationStage = new AllocationStage(resourceEnvironmentModelProvider);

        /** deployment. */
        final DeploymentCompositeStage deploymentStage = new DeploymentCompositeStage(resourceEnvironmentModelProvider,
                allocationModelProvider, systemModelProvider, rac);
        final UndeploymentCompositeStage undeploymentStage = new UndeploymentCompositeStage(
                resourceEnvironmentModelProvider, allocationModelProvider, systemModelProvider, rac);

        /** geolocation. */
        final GeoLocation geoLocation = new GeoLocation(resourceEnvironmentModelProvider);

        final PrivacyWarner privacyWarner = new PrivacyWarner(allocationModelProvider, systemModelProvider,
                resourceEnvironmentModelProvider);

        final ConcurrentHashMapWithDefault<Long, EventBasedTrace> traceBuffer = new ConcurrentHashMapWithDefault<>(
                EventBasedTraceFactory.INSTANCE);
        final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(traceBuffer);

        final EntryCallStage entryCallStage = new EntryCallStage(new JPetStoreCallTraceMatcher());
        final EntryEventMapperStage entryEventMapperStage = new EntryEventMapperStage(rac);
        final DataFlowDetectionStage dataFlowDetectionStage = new DataFlowDetectionStage(allocationModelProvider,
                systemModelProvider, resourceEnvironmentModelProvider);
        final AlarmAnalysis alarmAnalysis = new AlarmAnalysis();

        final ModelProbeController modelProbeController = new ModelProbeController(allocationModelProvider,
                systemModelProvider, resourceEnvironmentModelProvider);
        final ProbeMapper probeMapper = new ProbeMapper(rac);
        final ProbeController probeController = new ProbeController(outputs);

        try {
            final AlarmSink alarmSink = new AlarmSink(alarmFile);

            try {
                final WarnSink warnSink = new WarnSink(warningFile);

                /** connect ports. */
                this.connectPorts(reader.getOutputPort(), recordSwitch.getInputPort());
                this.connectPorts(recordSwitch.getDeployedOutputPort(), deploymentStage.getDeployedInputPort());
                this.connectPorts(recordSwitch.getUndeployedOutputPort(), undeploymentStage.getUndeployedInputPort());
                this.connectPorts(recordSwitch.getAllocationOutputPort(), allocationStage.getInputPort());

                this.connectPorts(deploymentStage.getDeployedOutputPort(), geoLocation.getInputPort());
                this.connectPorts(geoLocation.getOutputPort(), privacyWarner.getDeployedInputPort());
                this.connectPorts(undeploymentStage.getUndeployedOutputPort(), privacyWarner.getUndeployedInputPort());

                this.connectPorts(privacyWarner.getProbesOutputPort(), modelProbeController.getInputPort());
                this.connectPorts(modelProbeController.getOutputPort(), probeMapper.getInputPort());
                this.connectPorts(probeMapper.getOutputPort(), probeController.getInputPort());
                this.connectPorts(privacyWarner.getWarningsOutputPort(), warnSink.getInputPort());

                this.connectPorts(recordSwitch.getFlowOutputPort(), traceReconstructionFilter.getInputPort());
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
