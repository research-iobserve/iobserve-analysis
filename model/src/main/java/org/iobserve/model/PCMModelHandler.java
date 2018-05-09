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
import org.iobserve.model.correspondence.ICorrespondence;
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
 * Will load all model and {@link ICorrespondence} model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class PCMModelHandler {

    public static final String ALLOCATION_SUFFIX = "allocation";

    public static final String CLOUD_PROFILE_SUFFIX = "cloudprofile";

    public static final String COST_SUFFIX = "cost";

    public static final String DESIGN_DECISION_SUFFIX = "designdecision";

    public static final String REPOSITORY_SUFFIX = "repository";

    public static final String RESOURCE_ENVIRONMENT_SUFFIX = "resourceenvironment";

    public static final String SYSTEM_SUFFIX = "system";

    public static final String USAGE_MODEL_SUFFIX = "usagemodel";

    private static final String CORRESPONDENCE_SUFFIX = "rac";

    private static final String QML_DECLARATIONS_MODEL = "qmldeclarations";

    private static final Logger LOGGER = LoggerFactory.getLogger(PCMModelHandler.class);

    private static final String PRIVACY_MODEL = "privacy";

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
     * @param pcmModelsDirectory
     *            directory of pcm models.
     */
    public PCMModelHandler(final File pcmModelsDirectory) {

        final File[] files = pcmModelsDirectory.listFiles();
        for (final File nextFile : files) {
            PCMModelHandler.LOGGER.debug("Reading model {}", nextFile.toString());
            final String extension = this.getFileExtension(nextFile.getName());
            final URI uri = this.getUri(nextFile);
            if ("repository".equalsIgnoreCase(extension)) {
                this.repositoryModel = new RepositoryModelHandler().load(uri);
            } else if (PCMModelHandler.ALLOCATION_SUFFIX.equalsIgnoreCase(extension)) {
                this.allocationModel = new AllocationModelHandler().load(uri);
            } else if (PCMModelHandler.RESOURCE_ENVIRONMENT_SUFFIX.equalsIgnoreCase(extension)) {
                this.resourceEnvironmentModel = new ResourceEnvironmentModelHandler().load(uri);
            } else if (PCMModelHandler.SYSTEM_SUFFIX.equalsIgnoreCase(extension)) {
                this.systemModel = new SystemModelHandler().load(uri);
            } else if (PCMModelHandler.USAGE_MODEL_SUFFIX.equalsIgnoreCase(extension)) {
                this.usageModel = new UsageModelHandler().load(uri);
            } else if (PCMModelHandler.CORRESPONDENCE_SUFFIX.equalsIgnoreCase(extension)) {
                this.correspondenceModel = new CorrespondenceModelHandler().load(uri);
            } else if (PCMModelHandler.CLOUD_PROFILE_SUFFIX.equalsIgnoreCase(extension)) {
                this.cloudProfileModel = new CloudProfileModelHandler().load(uri);
            } else if (PCMModelHandler.COST_SUFFIX.equalsIgnoreCase(extension)) {
                this.costModel = new CostModelHandler().load(uri);
            } else if (PCMModelHandler.DESIGN_DECISION_SUFFIX.equalsIgnoreCase(extension)) {
                this.designDecisionModel = new DesignDecisionModelHandler().load(uri);
            } else if (PCMModelHandler.QML_DECLARATIONS_MODEL.equalsIgnoreCase(extension)) {
                this.qmlDeclarationsModel = new QMLDeclarationsModelHandler().load(uri);
            } else if (PCMModelHandler.PRIVACY_MODEL.equalsIgnoreCase(extension)) {
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
        new AllocationModelHandler().save(fileLocationURI.appendFileExtension(PCMModelHandler.ALLOCATION_SUFFIX),
                this.allocationModel);
        new CloudProfileModelHandler().save(fileLocationURI.appendFileExtension(PCMModelHandler.CLOUD_PROFILE_SUFFIX),
                this.cloudProfileModel);
        new CostModelHandler().save(fileLocationURI.appendFileExtension(PCMModelHandler.COST_SUFFIX), this.costModel);
        new DesignDecisionModelHandler().save(
                fileLocationURI.appendFileExtension(PCMModelHandler.DESIGN_DECISION_SUFFIX), this.designDecisionModel);
        new RepositoryModelHandler().save(fileLocationURI.appendFileExtension(PCMModelHandler.REPOSITORY_SUFFIX),
                this.repositoryModel);
        new ResourceEnvironmentModelHandler().save(
                fileLocationURI.appendFileExtension(PCMModelHandler.RESOURCE_ENVIRONMENT_SUFFIX),
                this.resourceEnvironmentModel);
        new SystemModelHandler().save(fileLocationURI.appendFileExtension(PCMModelHandler.SYSTEM_SUFFIX),
                this.systemModel);
        new UsageModelHandler().save(fileLocationURI.appendFileExtension(PCMModelHandler.USAGE_MODEL_SUFFIX),
                this.usageModel);
        new QMLDeclarationsModelHandler().save(
                fileLocationURI.appendFileExtension(PCMModelHandler.QML_DECLARATIONS_MODEL), this.qmlDeclarationsModel);
        new PrivacyModelHandler().save(fileLocationURI.appendFileExtension(PCMModelHandler.PRIVACY_MODEL),
                this.privacyModel);
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
