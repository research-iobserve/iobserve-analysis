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
import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarationsPackage;
import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.cost.costPackage;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import de.uka.ipd.sdq.pcm.designdecision.designdecisionPackage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.persistence.file.FileModelHandler;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.PrivacyPackage;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudProfile;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudprofilePackage;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;
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

        this.repositoryModel = this.readRequiredModel(files, RepositoryPackage.eINSTANCE);

        this.resourceEnvironmentModel = this.readRequiredModel(files, ResourceenvironmentPackage.eINSTANCE);

        this.systemModel = this.readRequiredModel(files, SystemPackage.eINSTANCE);

        this.allocationModel = this.readRequiredModel(files, AllocationPackage.eINSTANCE);

        this.usageModel = this.readOptionalModel(files, UsagemodelPackage.eINSTANCE);

        this.cloudProfileModel = this.readOptionalModel(files, CloudprofilePackage.eINSTANCE);

        this.costModel = this.readOptionalModel(files, costPackage.eINSTANCE);

        this.designDecisionModel = this.readOptionalModel(files, designdecisionPackage.eINSTANCE);

        this.qmlDeclarationsModel = this.readOptionalModel(files, QMLDeclarationsPackage.eINSTANCE);

        this.privacyModel = this.readOptionalModel(files, PrivacyPackage.eINSTANCE);

        this.correspondenceModel = this.readRequiredModel(files, CorrespondencePackage.eINSTANCE);

        EcoreUtil.resolveAll(this.resourceSet);
    }

    /**
     * Read a model form the array of files which contains a model specified by the given package.
     *
     * @param files
     *            array
     * @param ePackage
     *            the model's root package
     * @return returns the loaded model, if such model exists, else null
     * @throws IOException
     *             on io errors
     */
    private <T extends EObject> T readOptionalModel(final File[] files, final EPackage ePackage) throws IOException {
        final URI uri = this.getUriFileModelType(files, ePackage.getName(), false);
        if (uri != null) {
            return new FileModelHandler<T>(this.resourceSet, ePackage).load(uri);
        } else {
            return null;
        }
    }

    /**
     * Read a model form the array of files which contains a model specified by the given package.
     *
     * @param files
     *            array
     * @param ePackage
     *            the model's root package
     * @return returns the loaded model
     * @throws IOException
     *             on io errors
     */
    private <T extends EObject> T readRequiredModel(final File[] files, final EPackage ePackage) throws IOException {
        final URI uri = this.getUriFileModelType(files, ePackage.getName(), false);

        return new FileModelHandler<T>(this.resourceSet, ePackage).load(uri);
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
    @Override
    public Allocation getAllocationModel() {
        return this.allocationModel;
    }

    /**
     * @return resource environment model
     */
    @Override
    public ResourceEnvironment getResourceEnvironmentModel() {
        return this.resourceEnvironmentModel;
    }

    /**
     * @return system model
     */
    @Override
    public System getSystemModel() {
        return this.systemModel;
    }

    /**
     * @return usage model
     */
    @Override
    public UsageModel getUsageModel() {
        return this.usageModel;
    }

    /**
     * @return correspondence model
     */
    @Override
    public CorrespondenceModel getCorrespondenceModel() {
        return this.correspondenceModel;
    }

    /**
     * @return repository model provider
     */
    @Override
    public Repository getRepositoryModel() {
        return this.repositoryModel;
    }

    /**
     * @return cloud profile model provider
     */
    @Override
    public CloudProfile getCloudProfileModel() {
        return this.cloudProfileModel;
    }

    /**
     * @return cost model
     */
    @Override
    public CostRepository getCostModel() {
        return this.costModel;
    }

    /**
     * @return design decision model
     */
    @Override
    public DecisionSpace getDesignDecisionModel() {
        return this.designDecisionModel;
    }

    /**
     * @return QML declarations model provider
     */
    @Override
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
    @Override
    public void save(final URI fileLocationURI) {
        new FileModelHandler<Allocation>(this.resourceSet, AllocationPackage.eINSTANCE).save(fileLocationURI,
                this.allocationModel);
        new FileModelHandler<CloudProfile>(this.resourceSet, CloudprofilePackage.eINSTANCE).save(fileLocationURI,
                this.cloudProfileModel);
        new FileModelHandler<CostRepository>(this.resourceSet, costPackage.eINSTANCE).save(fileLocationURI, this.costModel);
        new FileModelHandler<DecisionSpace>(this.resourceSet, designdecisionPackage.eINSTANCE).save(fileLocationURI,
                this.designDecisionModel);
        new FileModelHandler<Repository>(this.resourceSet, RepositoryPackage.eINSTANCE).save(fileLocationURI,
                this.repositoryModel);
        new FileModelHandler<ResourceEnvironment>(this.resourceSet, ResourceenvironmentPackage.eINSTANCE)
                .save(fileLocationURI, this.resourceEnvironmentModel);
        new FileModelHandler<System>(this.resourceSet, SystemPackage.eINSTANCE).save(fileLocationURI, this.systemModel);
        new FileModelHandler<UsageModel>(this.resourceSet, UsagemodelPackage.eINSTANCE).save(fileLocationURI,
                this.usageModel);
        new FileModelHandler<QMLDeclarations>(this.resourceSet, QMLDeclarationsPackage.eINSTANCE).save(fileLocationURI,
                this.qmlDeclarationsModel);
        new FileModelHandler<PrivacyModel>(this.resourceSet, PrivacyPackage.eINSTANCE).save(fileLocationURI,
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

    /**
     * Print a given model partition as tree on stderr.
     *
     * @param object
     *            the root object
     * @param offset
     *            character offset
     */
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
