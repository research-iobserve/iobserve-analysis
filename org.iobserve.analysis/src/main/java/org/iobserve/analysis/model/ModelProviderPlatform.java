/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.model;

import java.io.File;

import org.eclipse.emf.common.util.URI;

import org.iobserve.analysis.correspondence.CorrespondeceModelFactory;
import org.iobserve.analysis.correspondence.ICorrespondence;

<<<<<<< HEAD
=======
/**
 *
 * The model platform will load all model and {@link ICorrespondence} model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
>>>>>>> master
public final class ModelProviderPlatform {

    private RepositoryModelProvider repositoryModelProvider;
    private UsageModelProvider usageModelProvider;
    private AllocationModelProvider allocationModelProvider;
    private ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;
    private SystemModelProvider systemModelProvider;
    private ICorrespondence correspondenceModel;

<<<<<<< HEAD
    // *****************************************************************
    // CONFIGURATION
    // *****************************************************************

=======
    /**
     * Create model provider.
     *
     * @param pathPcm
     *            directory of pcm models.
     */
>>>>>>> master
    public ModelProviderPlatform(final String pathPcm) {
        final File dirPcm = new File(pathPcm);
        if (!dirPcm.exists()) {
            throw new NullPointerException(String.format("the pcm dir %s does not exist?!", pathPcm));
        }
        this.createModelProviders(dirPcm);
    }

<<<<<<< HEAD
=======
    /**
     * Load all model provider.
     *
     * @param dirPcm
     *            directory of pcm
     */
>>>>>>> master
    private void createModelProviders(final File dirPcm) {
        final File[] files = dirPcm.listFiles();
        for (final File nextFile : files) {
            final String extension = this.getFileExtension(nextFile.getName());
<<<<<<< HEAD
            if (extension.equalsIgnoreCase("repository")) {
                final URI uri = this.getUri(nextFile);
                this.repositoryModelProvider = new RepositoryModelProvider(uri, this);

            } else if (extension.equalsIgnoreCase("allocation")) {
                final URI uri = this.getUri(nextFile);
                this.allocationModelProvider = new AllocationModelProvider(uri, this);

            } else if (extension.equalsIgnoreCase("resourceenvironment")) {
                final URI uri = this.getUri(nextFile);
                this.resourceEnvironmentModelProvider = new ResourceEnvironmentModelProvider(uri, this);

            } else if (extension.equalsIgnoreCase("system")) {
                final URI uri = this.getUri(nextFile);
                this.systemModelProvider = new SystemModelProvider(uri, this);

            } else if (extension.equalsIgnoreCase("usagemodel")) {
                final URI uri = this.getUri(nextFile);
                this.usageModelProvider = new UsageModelProvider(uri, this);

            } else if (extension.equalsIgnoreCase("rac")) {
                final String pathMappingFile = nextFile.getAbsolutePath();
                this.correspondenceModel = CorrespondeceModelFactory.INSTANCE.createCorrespondenceModel(pathMappingFile,
                        CorrespondeceModelFactory.INSTANCE.DEFAULT_OPERATION_SIGNATURE_MAPPER_2);
=======
            if ("repository".equalsIgnoreCase(extension)) {
                final URI uri = this.getUri(nextFile);
                this.repositoryModelProvider = new RepositoryModelProvider(this, uri);

            } else if ("allocation".equalsIgnoreCase(extension)) {
                final URI uri = this.getUri(nextFile);
                this.allocationModelProvider = new AllocationModelProvider(this, uri);

            } else if ("resourceenvironment".equalsIgnoreCase(extension)) {
                final URI uri = this.getUri(nextFile);
                this.resourceEnvironmentModelProvider = new ResourceEnvironmentModelProvider(this, uri);

            } else if ("system".equalsIgnoreCase(extension)) {
                final URI uri = this.getUri(nextFile);
                this.systemModelProvider = new SystemModelProvider(this, uri);

            } else if ("usagemodel".equalsIgnoreCase(extension)) {
                final URI uri = this.getUri(nextFile);
                this.usageModelProvider = new UsageModelProvider(this, uri);

            } else if ("rac".equalsIgnoreCase(extension)) {
                final String pathMappingFile = nextFile.getAbsolutePath();
                this.correspondenceModel = CorrespondeceModelFactory.INSTANCE.createCorrespondenceModel(pathMappingFile,
                        CorrespondeceModelFactory.DEFAULT_OPERATION_SIGNATURE_MAPPER_2);
>>>>>>> master
            }
        }
    }

<<<<<<< HEAD
    // *****************************************************************
    // GET MODEL PROVIDERS
    // *****************************************************************

=======
    /**
     * @return allocation model provider
     */
>>>>>>> master
    public AllocationModelProvider getAllocationModelProvider() {
        return this.allocationModelProvider;
    }

<<<<<<< HEAD
=======
    /**
     * @return resource environment model provider
     */
>>>>>>> master
    public ResourceEnvironmentModelProvider getResourceEnvironmentModelProvider() {
        return this.resourceEnvironmentModelProvider;
    }

<<<<<<< HEAD
=======
    /**
     * @return system model provider
     */
>>>>>>> master
    public SystemModelProvider getSystemModelProvider() {
        return this.systemModelProvider;
    }

<<<<<<< HEAD
=======
    /**
     * @return usage model provider
     */
>>>>>>> master
    public UsageModelProvider getUsageModelProvider() {
        return this.usageModelProvider;
    }

<<<<<<< HEAD
=======
    /**
     * @return correspondence model
     */
>>>>>>> master
    public ICorrespondence getCorrespondenceModel() {
        return this.correspondenceModel;
    }

<<<<<<< HEAD
=======
    /**
     * @return repository model provider
     */
>>>>>>> master
    public RepositoryModelProvider getRepositoryModelProvider() {
        return this.repositoryModelProvider;
    }

<<<<<<< HEAD
    // *****************************************************************
    // HELPER
    // *****************************************************************

=======
    /**
     * Get file extension of the given path.
     *
     * @param path
     *            path
     * @return file extension
     */
>>>>>>> master
    private String getFileExtension(final String path) {
        return path.substring(path.lastIndexOf(".") + 1, path.length());
    }

<<<<<<< HEAD
=======
    /**
     * Get uri from the given file.
     *
     * @param file
     *            file
     * @return uri
     */
>>>>>>> master
    private URI getUri(final File file) {
        return URI.createFileURI(file.getAbsolutePath());
    }

}
