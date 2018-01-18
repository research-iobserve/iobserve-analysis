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

import kieker.common.configuration.Configuration;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.clustering.EAggregationType;
import org.iobserve.analysis.clustering.EOutputMode;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.analysis.source.TCPSourceCompositeStage;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.AllocationModelProvider;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.model.provider.neo4j.RepositoryModelProvider;
import org.iobserve.model.provider.neo4j.ResourceEnvironmentModelProvider;
import org.iobserve.model.provider.neo4j.SystemModelProvider;
import org.iobserve.model.provider.neo4j.UsageModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

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
     * @param inputPort
     *            the input port where the analysis is listening
     * @param correspondenceModel
     *            the correspondence model
     * @param usageModelProvider
     *            the usage model provider
     * @param repositoryModelProvider
     *            the repository model provider
     * @param resourceEnvironmentModelProvider
     *            the resource environment provider
     * @param resourceEnvironmentModelGraphProvider
     *            the resource environment graph provider
     * @param allocationModelProvider
     *            the allocation model provider
     * @param allocationModelGraphProvider
     *            the allocation model graph provider
     * @param systemModelProvider
     *            the system model provider
     * @param systemModelGraphProvider
     *            the system model graph provider
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
     * @param perOpteryxHeadless
     *            perOpterxyheadless URI
     * @param lqnsDir
     *            layered queuing networks directory
     * @param deployablesFolder
     *            folder containing deployables
     */
    public MultiInputObservationConfiguration(final int inputPort, final ICorrespondence correspondenceModel,
            final UsageModelProvider usageModelProvider, final RepositoryModelProvider repositoryModelProvider,
            final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final AllocationModelProvider allocationModelProvider,
            final ModelProvider<Allocation> allocationModelGraphProvider, final SystemModelProvider systemModelProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload,
            final String visualizationServiceURL, final EAggregationType aggregationType, final EOutputMode outputMode,
            final SnapshotBuilder snapshotBuilder, final URI perOpteryxHeadless, final URI lqnsDir,
            final URI deployablesFolder) {
        super(correspondenceModel, usageModelProvider, repositoryModelProvider, resourceEnvironmentModelProvider,
                resourceEnvironmentModelGraphProvider, allocationModelProvider, allocationModelGraphProvider,
                systemModelProvider, systemModelGraphProvider, varianceOfUserGroups, thinkTime, closedWorkload,
                visualizationServiceURL, aggregationType, outputMode, snapshotBuilder, perOpteryxHeadless, lqnsDir,
                null, deployablesFolder);

        final Configuration configuration = new Configuration();

        configuration.setProperty(TCPSourceCompositeStage.SOURCE_PORT, inputPort);

        final TCPSourceCompositeStage reader = new TCPSourceCompositeStage(configuration);

        this.connectPorts(reader.getOutputPort(), this.recordSwitch.getInputPort());
    }

}
