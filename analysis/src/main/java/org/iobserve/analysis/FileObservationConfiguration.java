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

import java.io.File;
import java.util.Collection;

import org.iobserve.analysis.filter.reader.Dir2RecordsFilter;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.neo4j.graphdb.GraphDatabaseService;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;

/**
 *
 * @author Reiner Jung
 *
 */
public class FileObservationConfiguration extends AbstractObservationConfiguration {

    private final InitialElementProducer<File> files;
    private final Dir2RecordsFilter reader;

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
     */
    public FileObservationConfiguration(final Collection<File> directories, final ICorrespondence correspondenceModel,
            final UsageModelProvider usageModelProvider, final RepositoryModelProvider repositoryModelProvider,
            final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final GraphDatabaseService resourceEnvironmentModelGraph,
            final AllocationModelProvider allocationModelProvider,
            final ModelProvider<Allocation> allocationModelGraphProvider, final SystemModelProvider systemModelProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload) {
        super(correspondenceModel, usageModelProvider, repositoryModelProvider, resourceEnvironmentModelProvider,
                resourceEnvironmentModelGraphProvider, allocationModelProvider, allocationModelGraphProvider,
                systemModelProvider, systemModelGraphProvider, varianceOfUserGroups, thinkTime, closedWorkload);

        this.files = new InitialElementProducer<>(directories);
        this.reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

        /** connecting filters */
        this.connectPorts(this.files.getOutputPort(), this.reader.getInputPort());
        this.connectPorts(this.reader.getOutputPort(), this.recordSwitch.getInputPort());
    }
}
