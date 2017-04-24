/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.CorrespondenceModelImpl;
import org.iobserve.analysis.model.correspondence.ICorrespondence;

/**
 *
 * will load all model and {@link ICorrespondence} model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class InitializeModelProviders {

    private RepositoryModelProvider repositoryModelProvider;
    private UsageModelProvider usageModelProvider;
    private AllocationModelProvider allocationModelProvider;
    private ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;
    private SystemModelProvider systemModelProvider;
    private ICorrespondence correspondenceModel;

    /**
     * Create model provider.
     *
     * @param dirPcm
     *            directory of pcm models.
     */
    public InitializeModelProviders(final File dirPcm) {
        final File[] files = dirPcm.listFiles();
        for (final File nextFile : files) {
            final String extension = this.getFileExtension(nextFile.getName());
            if ("repository".equalsIgnoreCase(extension)) {
                final URI uri = this.getUri(nextFile);
                this.repositoryModelProvider = new RepositoryModelProvider(uri);
            } else if ("allocation".equalsIgnoreCase(extension)) {
                final URI uri = this.getUri(nextFile);
                this.allocationModelProvider = new AllocationModelProvider(uri);

            } else if ("resourceenvironment".equalsIgnoreCase(extension)) {
                final URI uri = this.getUri(nextFile);
                this.resourceEnvironmentModelProvider = new ResourceEnvironmentModelProvider(uri);

            } else if ("system".equalsIgnoreCase(extension)) {
                final URI uri = this.getUri(nextFile);
                this.systemModelProvider = new SystemModelProvider(uri);

            } else if ("usagemodel".equalsIgnoreCase(extension)) {
                final URI uri = this.getUri(nextFile);
                this.usageModelProvider = new UsageModelProvider(uri);

            } else if ("rac".equalsIgnoreCase(extension)) {
                final String pathMappingFile = nextFile.getAbsolutePath();
                this.correspondenceModel = new CorrespondenceModelImpl(URI.createFileURI(pathMappingFile));
            }
        }
    }

    /**
     * @return allocation model provider
     */
    public AllocationModelProvider getAllocationModelProvider() {
        return this.allocationModelProvider;
    }

    /**
     * @return resource environment model provider
     */
    public ResourceEnvironmentModelProvider getResourceEnvironmentModelProvider() {
        return this.resourceEnvironmentModelProvider;
    }

    /**
     * @return system model provider
     */
    public SystemModelProvider getSystemModelProvider() {
        return this.systemModelProvider;
    }

    /**
     * @return usage model provider
     */
    public UsageModelProvider getUsageModelProvider() {
        return this.usageModelProvider;
    }

    /**
     * @return correspondence model
     */
    public ICorrespondence getCorrespondenceModel() {
        return this.correspondenceModel;
    }

    /**
     * @return repository model provider
     */
    public RepositoryModelProvider getRepositoryModelProvider() {
        return this.repositoryModelProvider;
    }

    /**
     * Get file extension of the given path.
     *
     * @param path
     *            path
     * @return file extension
     */
    private String getFileExtension(final String path) {
        return path.substring(path.lastIndexOf(".") + 1, path.length());
    }

    /**
     * Get uri from the given file.
     *
     * @param file
     *            file
     * @return uri
     */
    private URI getUri(final File file) {
        return URI.createFileURI(file.getAbsolutePath());
    }

}
