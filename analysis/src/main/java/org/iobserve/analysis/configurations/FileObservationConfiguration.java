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

import java.io.File;
import java.util.Collection;

import org.iobserve.analysis.ConfigurationException;
import org.iobserve.analysis.clustering.EAggregationType;
import org.iobserve.analysis.clustering.EOutputMode;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.analysis.source.FileSourceCompositeStage;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import kieker.common.configuration.Configuration;

/**
 *
 * @author Reiner Jung
 *
 * @deprecated since 0.0.2 use AnalysisConfiguration
 */
@Deprecated
public class FileObservationConfiguration extends AbstractObservationConfiguration {

    /**
     * Analysis configuration constructor.
     *
     * @param directories
     *            a collection of directories containing kieker logs
     * @param correspondenceModel
     *            the correspondence model
     * @param usageModelProvider
     *            the usage model provider
     * @param repositoryModelProvider
     *            the repository model provider
     * @param resourceEnvironmentModelProvider
     *            the resource environment graph provider
     * @param allocationModelProvider
     *            the allocation model graph provider
     * @param systemModelProvider
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
     * @param eventListener
     *            eventlistener of some kind
     * @param deployablesFolder
     *            folder containing deployables
     * @throws ConfigurationException
     *             on configuration error
     */
    public FileObservationConfiguration(final Collection<File> directories, final ICorrespondence correspondenceModel,
            final ModelProvider<UsageModel> usageModelProvider, final ModelProvider<Repository> repositoryModelProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final ModelProvider<Allocation> allocationModelProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelProvider,
            final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload,
            final String visualizationServiceURL, final EAggregationType aggregationType, final EOutputMode outputMode,
            final SnapshotBuilder snapshotBuilder) throws ConfigurationException {
        super(correspondenceModel, usageModelProvider, repositoryModelProvider, resourceEnvironmentModelProvider,
                allocationModelProvider, systemModelProvider, varianceOfUserGroups, thinkTime, closedWorkload,
                visualizationServiceURL, aggregationType, outputMode, snapshotBuilder, null);

        final Configuration configuration = new Configuration();

        configuration.setStringArrayProperty(FileSourceCompositeStage.SOURCE_DIRECTORIES, this.getNames(directories));

        final FileSourceCompositeStage input = new FileSourceCompositeStage(configuration);

        /** connecting filters */
        this.connectPorts(input.getOutputPort(), this.recordSwitch.getInputPort());
    }

    private String[] getNames(final Collection<File> directories) {
        final String[] names = new String[directories.size()];
        int i = 0;
        for (final File directory : directories) {
            names[i++] = directory.getAbsolutePath();
        }
        return names;
    }
}
