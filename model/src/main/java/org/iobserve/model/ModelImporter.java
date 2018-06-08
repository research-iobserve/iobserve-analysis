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
package org.iobserve.model;

import java.io.File;

import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarations;
import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;

import org.eclipse.emf.common.util.URI;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.provider.file.AllocationModelHandler;
import org.iobserve.model.provider.file.CloudProfileModelHandler;
import org.iobserve.model.provider.file.CorrespondenceModelHandler;
import org.iobserve.model.provider.file.CostModelHandler;
import org.iobserve.model.provider.file.DesignDecisionModelHandler;
import org.iobserve.model.provider.file.PrivacyModelHandler;
import org.iobserve.model.provider.file.QMLDeclarationsModelHandler;
import org.iobserve.model.provider.file.RepositoryModelHandler;
import org.iobserve.model.provider.file.ResourceEnvironmentModelHandler;
import org.iobserve.model.provider.file.SystemModelHandler;
import org.iobserve.model.provider.file.UsageModelHandler;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudProfile;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Load all models located in a file
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Reiner Jung
 */
public final class ModelImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelImporter.class);

    private Repository repositoryModel;
    private Allocation allocationModel;
    private ResourceEnvironment resourceEnvironmentModel;
    private System systemModel;
    private UsageModel usageModel;
    private CorrespondenceModel correspondenceModel;
    private CloudProfile cloudProfileModel;
    private CostRepository costModel;
    private DecisionSpace designDecisionModel;
    private QMLDeclarations qmlDeclarationsModel;

    private PrivacyModel privacyModel;

    /**
     * Create model provider.
     *
     * @param modelsDirectory
     *            directory of pcm models.
     */
    public ModelImporter(final File modelsDirectory) {

        final File[] files = modelsDirectory.listFiles();
        for (final File nextFile : files) {
            ModelImporter.LOGGER.debug("Reading model {}", nextFile.toString());
            final String extension = this.getFileExtension(nextFile.getName());
            final URI uri = this.getUri(nextFile);
            if (RepositoryModelHandler.SUFFIX.equalsIgnoreCase(extension)) {
                this.repositoryModel = new RepositoryModelHandler().load(uri);
            } else if (AllocationModelHandler.SUFFIX.equalsIgnoreCase(extension)) {
                this.allocationModel = new AllocationModelHandler().load(uri);
            } else if (ResourceEnvironmentModelHandler.SUFFIX.equalsIgnoreCase(extension)) {
                this.resourceEnvironmentModel = new ResourceEnvironmentModelHandler().load(uri);
            } else if (SystemModelHandler.SUFFIX.equalsIgnoreCase(extension)) {
                this.systemModel = new SystemModelHandler().load(uri);
            } else if (UsageModelHandler.SUFFIX.equalsIgnoreCase(extension)) {
                this.usageModel = new UsageModelHandler().load(uri);
            } else if (CorrespondenceModelHandler.SUFFIX.equalsIgnoreCase(extension)) {
                this.correspondenceModel = new CorrespondenceModelHandler().load(uri);
            } else if (CloudProfileModelHandler.SUFFIX.equalsIgnoreCase(extension)) {
                this.cloudProfileModel = new CloudProfileModelHandler().load(uri);
            } else if (CostModelHandler.SUFFIX.equalsIgnoreCase(extension)) {
                this.costModel = new CostModelHandler().load(uri);
            } else if (DesignDecisionModelHandler.SUFFIX.equalsIgnoreCase(extension)) {
                this.designDecisionModel = new DesignDecisionModelHandler().load(uri);
            } else if (QMLDeclarationsModelHandler.SUFFIX.equalsIgnoreCase(extension)) {
                this.qmlDeclarationsModel = new QMLDeclarationsModelHandler().load(uri);
            } else if (PrivacyModelHandler.SUFFIX.equalsIgnoreCase(extension)) {
                this.privacyModel = new PrivacyModelHandler().load(uri);
            }
        }
    }

    /**
     * @return allocation model
     */
    public Allocation getAllocationModel() {
        return this.allocationModel;
    }

    /**
     * @return resource environment model
     */
    public ResourceEnvironment getResourceEnvironmentModel() {
        return this.resourceEnvironmentModel;
    }

    /**
     * @return system model
     */
    public System getSystemModel() {
        return this.systemModel;
    }

    /**
     * @return usage model
     */
    public UsageModel getUsageModel() {
        return this.usageModel;
    }

    /**
     * @return correspondence model
     */
    public CorrespondenceModel getCorrespondenceModel() {
        return this.correspondenceModel;
    }

    /**
     * @return repository model provider
     */
    public Repository getRepositoryModel() {
        return this.repositoryModel;
    }

    /**
     * @return cloud profile model provider
     */
    public CloudProfile getCloudProfileModel() {
        return this.cloudProfileModel;
    }

    /**
     * @return cost model
     */
    public CostRepository getCostModel() {
        return this.costModel;
    }

    /**
     * @return design decision model
     */
    public DecisionSpace getDesignDecisionModel() {
        return this.designDecisionModel;
    }

    /**
     * @return QML declarations model provider
     */
    public QMLDeclarations getQMLDeclarationsModel() {
        return this.qmlDeclarationsModel;
    }

    /**
     * @return PrivacyModel
     */
    public PrivacyModel getPrivacyModel() {
        return this.privacyModel;
    }

    /**
     * Saves all currently available models in this provider into the snapshot location.
     *
     * @param fileLocationURI
     *            the location directory for the snapshot
     */
    public void save(final URI fileLocationURI) {
        new AllocationModelHandler().save(fileLocationURI, this.allocationModel);
        new CloudProfileModelHandler().save(fileLocationURI, this.cloudProfileModel);
        new CostModelHandler().save(fileLocationURI, this.costModel);
        new DesignDecisionModelHandler().save(fileLocationURI, this.designDecisionModel);
        new RepositoryModelHandler().save(fileLocationURI, this.repositoryModel);
        new ResourceEnvironmentModelHandler().save(fileLocationURI, this.resourceEnvironmentModel);
        new SystemModelHandler().save(fileLocationURI, this.systemModel);
        new UsageModelHandler().save(fileLocationURI, this.usageModel);
        new QMLDeclarationsModelHandler().save(fileLocationURI, this.qmlDeclarationsModel);
        new PrivacyModelHandler().save(fileLocationURI, this.privacyModel);
    }

    /**
     * Get file extension of the given path.
     *
     * @param path
     *            path
     * @return file extension
     */
    private String getFileExtension(final String path) {
        return path.substring(path.lastIndexOf('.') + 1, path.length());
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
