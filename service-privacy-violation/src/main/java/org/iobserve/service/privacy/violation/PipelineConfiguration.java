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

import kieker.analysis.source.ISourceCompositeStage;
import kieker.common.exception.ConfigurationException;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.util.classpath.InstantiationFactory;
import kieker.tools.common.CommonConfigurationKeys;

import teetime.framework.Configuration;

import org.iobserve.analysis.deployment.AllocationStage;
import org.iobserve.analysis.deployment.DeallocationStage;
import org.iobserve.analysis.deployment.DeploymentCompositeStage;
import org.iobserve.analysis.deployment.UndeploymentCompositeStage;
import org.iobserve.analysis.privacy.GeoLocationStage;
import org.iobserve.common.record.IAllocationEvent;
import org.iobserve.common.record.IDeallocationEvent;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.IUndeployedEvent;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.persistence.IModelResource;
import org.iobserve.model.privacy.DataProtectionModel;
import org.iobserve.service.privacy.violation.filter.AlarmSink;
import org.iobserve.service.privacy.violation.filter.DataProtectionWarner;
import org.iobserve.service.privacy.violation.filter.ModelSnapshotWriter;
import org.iobserve.service.privacy.violation.filter.NonAdaptiveModelProbeController;
import org.iobserve.service.privacy.violation.filter.ProbeMapper;
import org.iobserve.service.privacy.violation.filter.WarnSink;
import org.iobserve.service.privacy.violation.filter.WhitelistFilter;
import org.iobserve.stages.general.DynamicEventDispatcher;
import org.iobserve.stages.general.IEventMatcher;
import org.iobserve.stages.general.ImplementsEventMatcher;
import org.iobserve.stages.tcp.ProbeControlFilter;
import org.iobserve.utility.tcp.DummyProbeController;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * Configuration for the log replayer.
 *
 * @author Reiner Jung
 *
 */
public class PipelineConfiguration extends Configuration {

    /**
     * Configuration for the JSS privacy violation detection service.
     *
     * @param configuration
     *            configuration object
     * @param correspondenceResource
     *            correspondence model graph
     * @param repositoryResource
     *            repository model provider
     * @param resourceEnvironmentResource
     *            resource environment model provider
     * @param allocationResource
     *            allocation model provider
     * @param systemModelResource
     *            system model provider
     * @param privacyModelResource
     *            graph for the privacy model
     * @param warningFile
     *            warnings
     * @param alarmFile
     *            alarms
     * @param modelDumpDirectory
     *            where model revision shall be dumped
     * @throws IOException
     *             when files cannot be opened
     * @throws ConfigurationException
     *             on configuration errors
     */
    public PipelineConfiguration(final kieker.common.configuration.Configuration configuration,
            final IModelResource<CorrespondenceModel> correspondenceResource,
            final IModelResource<Repository> repositoryResource,
            final IModelResource<ResourceEnvironment> resourceEnvironmentResource,
            final IModelResource<System> systemModelResource, final IModelResource<Allocation> allocationResource,
            final IModelResource<DataProtectionModel> privacyModelResource, final File warningFile,
            final File alarmFile, final File modelDumpDirectory) throws IOException, ConfigurationException {

        /** instantiating filters. */
        final String sourceClassName = configuration.getStringProperty(CommonConfigurationKeys.SOURCE_STAGE);
        if (!sourceClassName.isEmpty()) {
            final ISourceCompositeStage sourceCompositeStage = InstantiationFactory
                    .createWithConfiguration(ISourceCompositeStage.class, sourceClassName, configuration);

            final IEventMatcher<IFlowRecord> flowMatcher = new ImplementsEventMatcher<>(IFlowRecord.class, null);
            final IEventMatcher<IDeployedEvent> deployedEventMatcher = new ImplementsEventMatcher<>(
                    IDeployedEvent.class, flowMatcher);
            final IEventMatcher<IUndeployedEvent> undeployedEventMatcher = new ImplementsEventMatcher<>(
                    IUndeployedEvent.class, deployedEventMatcher);

            final IEventMatcher<IAllocationEvent> allocationEventMatcher = new ImplementsEventMatcher<>(
                    IAllocationEvent.class, undeployedEventMatcher);
            final IEventMatcher<IDeallocationEvent> deallocationEventMatcher = new ImplementsEventMatcher<>(
                    IDeallocationEvent.class, allocationEventMatcher);

            final DynamicEventDispatcher eventDispatcher = new DynamicEventDispatcher(deallocationEventMatcher, true,
                    true, false);

            /** allocation. */
            final AllocationStage allocationStage = new AllocationStage(resourceEnvironmentResource);
            final DeallocationStage deallocationStage = new DeallocationStage(resourceEnvironmentResource);

            /** deployment. */
            final DeploymentCompositeStage deploymentStage = new DeploymentCompositeStage(resourceEnvironmentResource,
                    systemModelResource, allocationResource, correspondenceResource);
            final UndeploymentCompositeStage undeploymentStage = new UndeploymentCompositeStage(
                    resourceEnvironmentResource, systemModelResource, allocationResource, correspondenceResource);

            /** geo location. */
            final GeoLocationStage geoLocationStage = new GeoLocationStage(resourceEnvironmentResource,
                    privacyModelResource);

            final DataProtectionWarner privacyWarner = new DataProtectionWarner(configuration, repositoryResource,
                    resourceEnvironmentResource, systemModelResource, allocationResource, privacyModelResource);
            privacyWarner.declareActive();

            /** controlling probes. */
            // no (de-)activation in this one, just update
            final NonAdaptiveModelProbeController modelProbeController = new NonAdaptiveModelProbeController(
                    allocationResource, systemModelResource, repositoryResource);

            // final ModelProbeController modelProbeController = new ModelProbeController();
            final WhitelistFilter whitelistFilter = new WhitelistFilter(allocationResource,
                    resourceEnvironmentResource);

            final ProbeMapper probeMapper = new ProbeMapper(correspondenceResource, repositoryResource,
                    resourceEnvironmentResource, systemModelResource, allocationResource);

            final ProbeControlFilter probeController = new ProbeControlFilter(new DummyProbeController());

            // Model dumper
            ModelSnapshotWriter modelDumper = null;
            if (modelDumpDirectory != null) {
                modelDumper = new ModelSnapshotWriter(modelDumpDirectory, correspondenceResource, repositoryResource,
                        resourceEnvironmentResource, systemModelResource, allocationResource, privacyModelResource);
            }
            // Remove for performance measurements
            // final EventDelayer<IMonitoringRecord> eventDelayer = new EventDelayer<>(100);

            try {
                final AlarmSink alarmSink = new AlarmSink(alarmFile);
            } catch (final IOException eAlarm) { // NOPMD cannot be avoided to be used as flow
                // control
                throw new IOException("Cannot create alarm file.", eAlarm);
            }
            try {
                final WarnSink warnSink = new WarnSink(warningFile);

                /** connect ports. */
                this.connectPorts(sourceCompositeStage.getOutputPort(), // eventDelayer.getInputPort());
                        // this.connectPorts(eventDelayer.getOutputPort(),
                        eventDispatcher.getInputPort());

                /** event dispatcher. */
                this.connectPorts(deployedEventMatcher.getOutputPort(), deploymentStage.getDeployedInputPort());
                this.connectPorts(undeployedEventMatcher.getOutputPort(), undeploymentStage.getUndeployedInputPort());
                this.connectPorts(allocationEventMatcher.getOutputPort(), allocationStage.getInputPort());
                this.connectPorts(deallocationEventMatcher.getOutputPort(), deallocationStage.getInputPort());

                /** deployment. */
                this.connectPorts(deploymentStage.getDeployedOutputPort(), geoLocationStage.getInputPort());

                if (modelDumper != null) {
                    this.connectPorts(geoLocationStage.getOutputPort(), modelDumper.getInputPort());
                    this.connectPorts(modelDumper.getOutputPort(), privacyWarner.getDeployedInputPort());
                } else {
                    this.connectPorts(geoLocationStage.getOutputPort(), privacyWarner.getDeployedInputPort());
                }

                /** undeployment. */
                this.connectPorts(undeploymentStage.getUndeployedOutputPort(), privacyWarner.getUndeployedInputPort());

                /** privacy. */
                this.connectPorts(privacyWarner.getWarningsOutputPort(), warnSink.getInputPort());

                /** execution. */
                this.connectPorts(privacyWarner.getProbesOutputPort(), modelProbeController.getInputPort());
                this.connectPorts(modelProbeController.getOutputPort(), whitelistFilter.getInputPort());
                this.connectPorts(whitelistFilter.getOutputPort(), probeMapper.getInputPort());
                this.connectPorts(probeMapper.getOutputPort(), probeController.getInputPort());
            } catch (final IOException eWarning) { // NOPMD cannot be avoided to be used as flow
                                                   // control
                throw new IOException("Cannot create warning file.", eWarning);
            }

        }
    }

}
