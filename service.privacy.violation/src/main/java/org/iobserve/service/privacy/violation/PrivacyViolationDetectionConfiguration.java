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
import java.util.List;

import org.iobserve.analysis.deployment.DeploymentModelUpdater;
import org.iobserve.analysis.deployment.UndeploymentModelUpdater;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.model.provider.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.privacy.TGeoLocation;
import org.iobserve.analysis.systems.jpetstore.JPetStoreCallTraceMatcher;
import org.iobserve.service.privacy.violation.filter.AlarmAnalysis;
import org.iobserve.service.privacy.violation.filter.AlarmSink;
import org.iobserve.service.privacy.violation.filter.DataFlowDetectionStage;
import org.iobserve.service.privacy.violation.filter.EntryEventMapperStage;
import org.iobserve.service.privacy.violation.filter.ModelProbeController;
import org.iobserve.service.privacy.violation.filter.PrivacyWarner;
import org.iobserve.service.privacy.violation.filter.ProbeController;
import org.iobserve.service.privacy.violation.filter.ProbeMapper;
import org.iobserve.service.privacy.violation.filter.WarnSink;
import org.iobserve.stages.general.RecordSwitch;
import org.iobserve.stages.general.TEntryCallStage;
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
     * @param resourceEnvironmentModelProvider
     * @param allocationModelGraphProvider
     * @param systemModelGraphProvider
     * @param resourceEnvironmentModelGraphProvider
     * @param warningFile
     * @param alarmFile
     */
    public PrivacyViolationDetectionConfiguration(final int inputPort, final List<Control> outputs,
            final ICorrespondence rac, final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
            final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<System> systemModelGraphProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider, final File warningFile,
            final File alarmFile) {

        /** instantiating filters. */
        final MultipleConnectionTcpReaderStage reader = new MultipleConnectionTcpReaderStage(inputPort,
                PrivacyViolationDetectionConfiguration.BUFFER_SIZE);
        final RecordSwitch recordSwitch = new RecordSwitch();

        final DeploymentModelUpdater deploymentModelUpdater = new DeploymentModelUpdater(rac,
                allocationModelGraphProvider, systemModelGraphProvider, resourceEnvironmentModelGraphProvider);
        final UndeploymentModelUpdater undeploymentModelUpdater = new UndeploymentModelUpdater(rac,
                allocationModelGraphProvider, systemModelGraphProvider, resourceEnvironmentModelGraphProvider);

        final TGeoLocation geoLocation = new TGeoLocation(resourceEnvironmentModelProvider);

        final PrivacyWarner privacyWarner = new PrivacyWarner(allocationModelGraphProvider, systemModelGraphProvider,
                resourceEnvironmentModelGraphProvider);
        final WarnSink warnSink = new WarnSink(warningFile);

        final ConcurrentHashMapWithDefault<Long, EventBasedTrace> traceBuffer = new ConcurrentHashMapWithDefault<>(
                EventBasedTraceFactory.INSTANCE);
        final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(traceBuffer);

        final TEntryCallStage entryCallStage = new TEntryCallStage(new JPetStoreCallTraceMatcher());
        final EntryEventMapperStage entryEventMapperStage = new EntryEventMapperStage(rac);
        final DataFlowDetectionStage dataFlowDetectionStage = new DataFlowDetectionStage(allocationModelGraphProvider,
                systemModelGraphProvider, resourceEnvironmentModelGraphProvider);
        final AlarmAnalysis alarmAnalysis = new AlarmAnalysis();
        final AlarmSink alarmSink = new AlarmSink(alarmFile);

        final ModelProbeController modelProbeController = new ModelProbeController(allocationModelGraphProvider,
                systemModelGraphProvider, resourceEnvironmentModelGraphProvider);
        final ProbeMapper probeMapper = new ProbeMapper(rac);
        final ProbeController probeController = new ProbeController(outputs);

        /** connect ports. */
        this.connectPorts(reader.getOutputPort(), recordSwitch.getInputPort());
        this.connectPorts(recordSwitch.getDeploymentOutputPort(), deploymentModelUpdater.getInputPort());
        this.connectPorts(deploymentModelUpdater.getOutputPortSnapshot(), privacyWarner.getInputPort());
        this.connectPorts(recordSwitch.getUndeploymentOutputPort(), undeploymentModelUpdater.getInputPort());
        this.connectPorts(deploymentModelUpdater.getOutputPortSnapshot(), privacyWarner.getInputPort());
        this.connectPorts(recordSwitch.getGeoLocationPort(), geoLocation.getInputPort());
        this.connectPorts(geoLocation.getOutputPortSnapshot(), privacyWarner.getInputPort());

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
    }

    /**
     * Check whether the setup is complete.
     *
     * @return true when the service is connected to its application.
     */
    public boolean isSetupComplete() {
        return true;
    }

}
