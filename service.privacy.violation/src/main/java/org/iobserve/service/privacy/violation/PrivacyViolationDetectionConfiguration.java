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

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;

import teetime.framework.Configuration;

import org.iobserve.analysis.ConfigurationKeys;
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
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.service.privacy.violation.filter.AlarmSink;
import org.iobserve.service.privacy.violation.filter.NonAdaptiveModelProbeController;
import org.iobserve.service.privacy.violation.filter.PrivacyWarner;
import org.iobserve.service.privacy.violation.filter.WarnSink;
import org.iobserve.service.privacy.violation.filter.WhitelistFilter;
import org.iobserve.service.source.ISourceCompositeStage;
import org.iobserve.stages.general.ConfigurationException;
import org.iobserve.stages.general.DynamicEventDispatcher;
import org.iobserve.stages.general.IEventMatcher;
import org.iobserve.stages.general.ImplementsEventMatcher;
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
public class PrivacyViolationDetectionConfiguration extends Configuration {

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
     * @throws IOException
     *             when files cannot be opened
     * @throws ConfigurationException
     *             on configuration errors
     */
    public PrivacyViolationDetectionConfiguration(final kieker.common.configuration.Configuration configuration,
            final ModelResource<CorrespondenceModel> correspondenceResource,
            final ModelResource<Repository> repositoryResource,
            final ModelResource<ResourceEnvironment> resourceEnvironmentResource,
            final ModelResource<System> systemModelResource, final ModelResource<Allocation> allocationResource,
            final ModelResource<PrivacyModel> privacyModelResource, final File warningFile, final File alarmFile)
            throws IOException, ConfigurationException {

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

            final PrivacyWarner privacyWarner = new PrivacyWarner(configuration, repositoryResource,
                    resourceEnvironmentResource, systemModelResource, allocationResource, privacyModelResource);
            privacyWarner.declareActive();

            /** controlling probes. */
            // no (de-)activation in this one, just update
            final NonAdaptiveModelProbeController modelProbeController = new NonAdaptiveModelProbeController(
                    allocationResource, systemModelResource, repositoryResource);

            // final ModelProbeController modelProbeController = new ModelProbeController();
            final WhitelistFilter whitelistFilter = new WhitelistFilter(allocationResource,
                    resourceEnvironmentResource);
            // final ProbeMapper probeMapper = new ProbeMapper(correspondenceResource,
            // repositoryResource,
            // resourceEnvironmentResource, systemModelResource, allocationResource);

            // final ProbeControlFilter probeController = new ProbeControlFilter();

            // TODO remove for performance measurements
            final EventDelayer<IMonitoringRecord> eventDelayer = new EventDelayer<>(100);

            try {
                final AlarmSink alarmSink = new AlarmSink(alarmFile);

                try {
                    final WarnSink warnSink = new WarnSink(warningFile);

                    /** connect ports. */
                    this.connectPorts(sourceCompositeStage.getOutputPort(), eventDelayer.getInputPort());
                    this.connectPorts(eventDelayer.getOutputPort(), eventDispatcher.getInputPort());

                    /** event dispatcher. */
                    this.connectPorts(deployedEventMatcher.getOutputPort(), deploymentStage.getDeployedInputPort());
                    this.connectPorts(undeployedEventMatcher.getOutputPort(),
                            undeploymentStage.getUndeployedInputPort());
                    this.connectPorts(allocationEventMatcher.getOutputPort(), allocationStage.getInputPort());
                    this.connectPorts(deallocationEventMatcher.getOutputPort(), deallocationStage.getInputPort());

                    this.connectPorts(deploymentStage.getDeployedOutputPort(), geoLocationStage.getInputPort());

                    this.connectPorts(geoLocationStage.getOutputPort(), privacyWarner.getDeployedInputPort());

                    this.connectPorts(undeploymentStage.getUndeployedOutputPort(),
                            privacyWarner.getUndeployedInputPort());

                    this.connectPorts(privacyWarner.getWarningsOutputPort(), warnSink.getInputPort());

                    this.connectPorts(privacyWarner.getProbesOutputPort(), modelProbeController.getInputPort());
                    this.connectPorts(modelProbeController.getOutputPort(), whitelistFilter.getInputPort());
                    // this.connectPorts(whitelistFilter.getOutputPort(),
                    // probeMapper.getInputPort());
                    // this.connectPorts(probeMapper.getOutputPort(),
                    // probeController.getInputPort());

                    /** Alarm event processing. */
                    // TODO Trace analysis has become obsolete and will be replaced by an alarm
                    // event receiving part
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

}
