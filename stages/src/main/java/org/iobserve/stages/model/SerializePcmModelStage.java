/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.stages.model;

import java.io.File;

import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarations;
import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarationsPackage;
import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.cost.costPackage;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import de.uka.ipd.sdq.pcm.designdecision.designdecisionPackage;

import teetime.stage.basic.AbstractTransformation;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.iobserve.model.persistence.file.FileModelHandler;
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

/**
 * This stage gets a PCM model EObject and serializes it in a File. The file is provided via the
 * stages output port.
 *
 * @author Lars Bluemke
 *
 */
public class SerializePcmModelStage extends AbstractTransformation<EObject, File> {

    private final File modelDirectory;
    private final String modelName;

    /**
     * Creates a new instance of this class.
     *
     * @param modelDirectory
     *            Directory for serialized models
     * @param modelName
     *            Name for serialized models
     */
    public SerializePcmModelStage(final File modelDirectory, final String modelName) {
        this.modelDirectory = modelDirectory;
        this.modelName = modelName;
    }

    @Override
    protected void execute(final EObject model) throws Exception {
        final ResourceSet resourceSet = model.eResource().getResourceSet();
        final String filePathPrefix = this.modelDirectory.getAbsolutePath() + File.separator + this.modelName;
        final String filePath;

        if (model instanceof Allocation) {
            filePath = filePathPrefix + "." + AllocationPackage.eNAME;
            new FileModelHandler<Allocation>(resourceSet, AllocationPackage.eINSTANCE)
                    .save(URI.createFileURI(filePath), (Allocation) model);
        } else if (model instanceof CloudProfile) {
            filePath = filePathPrefix + "." + CloudprofilePackage.eNAME;
            new FileModelHandler<CloudProfile>(resourceSet, CloudprofilePackage.eINSTANCE)
                    .save(URI.createFileURI(filePath), (CloudProfile) model);
        } else if (model instanceof CostRepository) {
            filePath = filePathPrefix + "." + costPackage.eNAME;
            new FileModelHandler<CostRepository>(resourceSet, costPackage.eINSTANCE)
                    .save(URI.createFileURI(filePath), (CostRepository) model);
        } else if (model instanceof DecisionSpace) {
            filePath = filePathPrefix + "." + designdecisionPackage.eNAME;
            new FileModelHandler<DecisionSpace>(resourceSet, designdecisionPackage.eINSTANCE)
                    .save(URI.createFileURI(filePath), (DecisionSpace) model);
        } else if (model instanceof QMLDeclarations) {
            filePath = filePathPrefix + "." + QMLDeclarationsPackage.eNAME;
            new FileModelHandler<QMLDeclarations>(resourceSet, QMLDeclarationsPackage.eINSTANCE)
                    .save(URI.createFileURI(filePath), (QMLDeclarations) model);
        } else if (model instanceof Repository) {
            filePath = filePathPrefix + "." + RepositoryPackage.eNAME;
            new FileModelHandler<Repository>(resourceSet, RepositoryPackage.eINSTANCE)
                    .save(URI.createFileURI(filePath), (Repository) model);
        } else if (model instanceof ResourceEnvironment) {
            filePath = filePathPrefix + "." + ResourceenvironmentPackage.eNAME;
            new FileModelHandler<ResourceEnvironment>(resourceSet, ResourceenvironmentPackage.eINSTANCE)
                    .save(URI.createFileURI(filePath), (ResourceEnvironment) model);
        } else if (model instanceof System) {
            filePath = filePathPrefix + "." + SystemPackage.eNAME;
            new FileModelHandler<System>(resourceSet, SystemPackage.eINSTANCE).save(URI.createFileURI(filePath),
                    (System) model);
        } else if (model instanceof UsageModel) {
            filePath = filePathPrefix + UsagemodelPackage.eNAME;
            new FileModelHandler<UsageModel>(resourceSet, UsagemodelPackage.eINSTANCE)
                    .save(URI.createFileURI(filePath), (UsageModel) model);
        } else {
            throw new IllegalArgumentException("The EObject passed to this stage must be a PCM model!");
        }

        this.getOutputPort().send(new File(filePath));
    }

}
