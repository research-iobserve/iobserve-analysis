/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
import org.iobserve.analysis.clustering.EAggregationType;
import org.iobserve.analysis.clustering.EOutputMode;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.analysis.source.FileSourceCompositeStage;
import org.iobserve.analysis.toggle.FeatureToggle;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;

/**
 * Configuration prepared to handle multiple TCP input streams.
 *
 * @author Reiner Jung
 *
 * @deprecated since 0.0.2 use AnalysisConfiguration
 */
@Deprecated
public class MultiInputObservationConfiguration extends AbstractObservationConfiguration {

    /**
     * Construct an analysis for multiple TCP inputs.
     *
     * @param configuration
     *            configuration object handling configuration file parameters
     * @param correspondenceModel
     *            the correspondence model
     * @param usageModelProvider
     *            the usage model provider
     * @param repositoryModelProvider
     *            the repository model provider
     * @param resourceEnvironmentModelProvider
     *            the resource environment provider
     * @param allocationModelProvider
     *            the allocation model provider
     * @param systemModelProvider
     *            the system model provider
     * @param varianceOfUserGroups
     *            variance of user groups, configuration for entry event filter
     * @param thinkTime
     *            think time, configuration for entry event filter
     * @param closedWorkload
     *            kind of workload, configuration for entry event filter
     * @param visualizationServiceURL
     *            url to the visualization service
     * @param aggregationType
     *            aggregation type
     * @param outputMode
     *            output mode
     * @param snapshotBuilder
     *            snapshotbuilder
     * @param featureToggle
     *            feature toggle
     */
    public MultiInputObservationConfiguration(final Configuration configuration,
            final ICorrespondence correspondenceModel, final IModelProvider<UsageModel> usageModelProvider,
            final IModelProvider<Repository> repositoryModelProvider,
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final IModelProvider<Allocation> allocationModelProvider,
            final IModelProvider<org.palladiosimulator.pcm.system.System> systemModelProvider,
            final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload,
            final String visualizationServiceURL, final EAggregationType aggregationType, final EOutputMode outputMode,
            final SnapshotBuilder snapshotBuilder, final FeatureToggle featureToggle) {
        super(correspondenceModel, usageModelProvider, repositoryModelProvider, resourceEnvironmentModelProvider,
                allocationModelProvider, systemModelProvider, varianceOfUserGroups, thinkTime, closedWorkload,
                visualizationServiceURL, aggregationType, outputMode, snapshotBuilder, featureToggle);

        // final TCPSourceCompositeStage reader = new
        // TCPSourceCompositeStage(configuration);
        try {
            final FileSourceCompositeStage reader = new FileSourceCompositeStage(configuration);
            this.connectPorts(reader.getOutputPort(), this.recordSwitch.getInputPort());
        } catch (final ConfigurationException ex) {
            LoggerFactory.getLogger(MultiInputObservationConfiguration.class).error(ex.getLocalizedMessage());
        }

    }

}
