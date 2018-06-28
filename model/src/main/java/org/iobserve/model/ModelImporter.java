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
import java.io.IOException;

import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarations;
import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
 * Load all models located in a file.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Reiner Jung
 */
public final class ModelImporter implements IModelImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelImporter.class);

    private final ResourceSet resourceSet = new ResourceSetImpl();

    private final Repository repositoryModel;
    private final Allocation allocationModel;
    private final ResourceEnvironment resourceEnvironmentModel;
    private final System systemModel;
    private final UsageModel usageModel;
    private final CorrespondenceModel correspondenceModel;
    private final CloudProfile cloudProfileModel;
    private final CostRepository costModel;
    private final DecisionSpace designDecisionModel;
    private final QMLDeclarations qmlDeclarationsModel;

    private final PrivacyModel privacyModel;

    /**
     * Create model provider.
     *
     * @param modelsDirectory
     *            directory of pcm models.
     * @throws IOException
     *             on missing models
     */
    public ModelImporter(final File modelsDirectory) throws IOException {

        final File[] files = modelsDirectory.listFiles();

        URI uri = this.getUriFileModelType(files, RepositoryModelHandler.SUFFIX, false);
        this.repositoryModel = new RepositoryModelHandler(this.resourceSet).load(uri);

        uri = this.getUriFileModelType(files, ResourceEnvironmentModelHandler.SUFFIX, false);
        this.resourceEnvironmentModel = new ResourceEnvironmentModelHandler(this.resourceSet).load(uri);

        uri = this.getUriFileModelType(files, SystemModelHandler.SUFFIX, false);
        this.systemModel = new SystemModelHandler(this.resourceSet).load(uri);

        uri = this.getUriFileModelType(files, AllocationModelHandler.SUFFIX, false);
        this.allocationModel = new AllocationModelHandler(this.resourceSet).load(uri);

        uri = this.getUriFileModelType(files, UsageModelHandler.SUFFIX, true);
        if (uri != null) {
            this.usageModel = new UsageModelHandler(this.resourceSet).load(uri);
        } else {
            this.usageModel = null;
        }

        uri = this.getUriFileModelType(files, CloudProfileModelHandler.SUFFIX, true);
        if (uri != null) {
            this.cloudProfileModel = new CloudProfileModelHandler(this.resourceSet).load(uri);
        } else {
            this.cloudProfileModel = null;
        }

        uri = this.getUriFileModelType(files, CostModelHandler.SUFFIX, true);
        if (uri != null) {
            this.costModel = new CostModelHandler(this.resourceSet).load(uri);
        } else {
            this.costModel = null;
        }

        uri = this.getUriFileModelType(files, DesignDecisionModelHandler.SUFFIX, true);
        if (uri != null) {
            this.designDecisionModel = new DesignDecisionModelHandler(this.resourceSet).load(uri);
        } else {
            this.designDecisionModel = null;
        }

        uri = this.getUriFileModelType(files, QMLDeclarationsModelHandler.SUFFIX, true);
        if (uri != null) {
            this.qmlDeclarationsModel = new QMLDeclarationsModelHandler(this.resourceSet).load(uri);
        } else {
            this.qmlDeclarationsModel = null;
        }

        uri = this.getUriFileModelType(files, PrivacyModelHandler.SUFFIX, true);
        if (uri != null) {
            this.privacyModel = new PrivacyModelHandler(this.resourceSet).load(uri);
        } else {
            this.privacyModel = null;
        }

        uri = this.getUriFileModelType(files, CorrespondenceModelHandler.SUFFIX, false);
        this.correspondenceModel = new CorrespondenceModelHandler(this.resourceSet).load(uri);

        EcoreUtil.resolveAll(this.resourceSet);
    }

    private URI getUriFileModelType(final File[] files, final String suffix, final boolean optional)
            throws IOException {
        for (final File nextFile : files) {
            final String extension = this.getFileExtension(nextFile.getName());
            if (suffix.equals(extension)) {
                ModelImporter.LOGGER.debug("Reading model {}", nextFile.toString());
                return this.getUri(nextFile);
            }
        }
        if (optional) {
            return null;
        } else {
            throw new IOException("Missing " + suffix + " model.");
        }
    }

    public ResourceSet getResourceSet() {
        return this.resourceSet;
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
        new AllocationModelHandler(this.resourceSet).save(fileLocationURI, this.allocationModel);
        new CloudProfileModelHandler(this.resourceSet).save(fileLocationURI, this.cloudProfileModel);
        new CostModelHandler(this.resourceSet).save(fileLocationURI, this.costModel);
        new DesignDecisionModelHandler(this.resourceSet).save(fileLocationURI, this.designDecisionModel);
        new RepositoryModelHandler(this.resourceSet).save(fileLocationURI, this.repositoryModel);
        new ResourceEnvironmentModelHandler(this.resourceSet).save(fileLocationURI, this.resourceEnvironmentModel);
        new SystemModelHandler(this.resourceSet).save(fileLocationURI, this.systemModel);
        new UsageModelHandler(this.resourceSet).save(fileLocationURI, this.usageModel);
        new QMLDeclarationsModelHandler(this.resourceSet).save(fileLocationURI, this.qmlDeclarationsModel);
        new PrivacyModelHandler(this.resourceSet).save(fileLocationURI, this.privacyModel);
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

    public void printModelParititionAsTree(final EObject object, final String offset) {
        java.lang.System.err.println(offset + "object " + object.eClass().getInstanceTypeName());
        for (final EAttribute attr : object.eClass().getEAllAttributes()) {
            final Object value = object.eGet(attr);
            if (value != null) {
                java.lang.System.err.println(offset + "\tattr " + attr.getName() + " " + value.toString());
            }
        }

        for (final EReference ref : object.eClass().getEAllReferences()) {
            if (ref.isContainment()) {
                this.iterateContainment(object, ref, offset);
            } else {
                this.iterateReference(object, ref, offset);
            }
        }
    }

    private void iterateReference(final EObject object, final EReference ref, final String offset) {
        final Object refRepresentation = object.eGet(ref);
        if (refRepresentation instanceof EList<?>) {
            final EList<?> refs = (EList<?>) object.eGet(ref);
            for (int i = 0; i < refs.size(); i++) {
                java.lang.System.err.println(offset + "\tref " + ref.getName() + "[" + i + "]" + " " + ref.eIsProxy());
                this.iterateAttributes((EObject) refs.get(i), offset + "\t\t");
            }
        } else if (refRepresentation != null) {
            java.lang.System.err.println(offset + "\tref " + ref.getName() + " " + ref.eIsProxy());
            this.iterateAttributes((EObject) refRepresentation, offset + "\t\t");
        }
    }

    private void iterateAttributes(final EObject object, final String offset) {
        java.lang.System.err.println(offset + "object " + object.eClass().getInstanceTypeName());
        for (final EAttribute attr : object.eClass().getEAllAttributes()) {
            final Object value = object.eGet(attr);
            if (value != null) {
                java.lang.System.err.println(offset + "\tattr " + attr.getName() + " " + value.toString());
            }
        }
    }

    private void iterateContainment(final EObject object, final EReference ref, final String offset) {
        final Object refRepresentation = object.eGet(ref);
        if (refRepresentation instanceof EList<?>) {
            final EList<?> refs = (EList<?>) object.eGet(ref);
            for (int i = 0; i < refs.size(); i++) {
                java.lang.System.err.println(offset + "\tcon " + ref.getName() + "[" + i + "]" + " " + ref.eIsProxy());
                this.printModelParititionAsTree((EObject) refs.get(i), offset + "\t\t");
            }
        } else if (refRepresentation != null) {
            java.lang.System.err.println(offset + "\tcon " + ref.getName() + " " + ref.eIsProxy());
            this.printModelParititionAsTree((EObject) refRepresentation, offset + "\t\t");
        }
    }
}
