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

import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;
import teetime.stage.io.filesystem.Dir2RecordsFilter;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.snapshot.SnapshotBuilder;

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
     */
    public FileObservationConfiguration(final Collection<File> directories, final ICorrespondence correspondenceModel,
            final UsageModelProvider usageModelProvider, final RepositoryModelProvider repositoryModelProvider,
            final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
            final AllocationModelProvider allocationModelProvider, final SystemModelProvider systemModelProvider,
            final SnapshotBuilder snapshotBuilder, final URI perOpteryxHeadless,
            final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload) {
        super(correspondenceModel, usageModelProvider, repositoryModelProvider, resourceEnvironmentModelProvider,
                allocationModelProvider, systemModelProvider, snapshotBuilder, perOpteryxHeadless, varianceOfUserGroups, thinkTime, closedWorkload);

        this.files = new InitialElementProducer<>(directories);
        this.reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

        /** connecting filters */
        this.connectPorts(this.files.getOutputPort(), this.reader.getInputPort());
        this.connectPorts(this.reader.getOutputPort(), this.recordSwitch.getInputPort());
    }

}
