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
package org.iobserve.analysis;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.clustering.EAggregationType;
import org.iobserve.analysis.clustering.EOutputMode;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.model.provider.AllocationModelProvider;
import org.iobserve.analysis.model.provider.RepositoryModelProvider;
import org.iobserve.analysis.model.provider.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.provider.SystemModelProvider;
import org.iobserve.analysis.model.provider.UsageModelProvider;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.stages.source.MultipleConnectionTcpReaderStage;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * Configuration prepared to handle multiple TCP input streams.
 *
 * @author Reiner Jung
 *
 */
public class MultiInputObservationConfiguration extends AbstractObservationConfiguration {

    private static final int CAPACITY = (1024 * 1024);

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

        final MultipleConnectionTcpReaderStage reader = new MultipleConnectionTcpReaderStage(inputPort,
                MultiInputObservationConfiguration.CAPACITY);
        this.connectPorts(reader.getOutputPort(), this.recordSwitch.getInputPort());
    }

}
