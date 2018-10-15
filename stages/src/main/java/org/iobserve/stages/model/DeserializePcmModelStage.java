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
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
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
 * This stage gets a serialized PCM model's File and loads the model EObject from it. The EObject is
 * provided via the stages output port.
 *
 * @author Lars Bluemke
 *
 */
public class DeserializePcmModelStage extends AbstractTransformation<File, EObject> {

    private final ResourceSet resourceSet = new ResourceSetImpl();

    @Override
    protected void execute(final File modelFile) throws Exception {
        final String modelFileName = modelFile.getName();
        final String modelFileExtension = modelFileName.substring(modelFileName.lastIndexOf('.') + 1);
        final EObject model;

        if (AllocationPackage.eNAME.equals(modelFileExtension)) {
            model = new FileModelHandler<Allocation>(this.resourceSet, AllocationPackage.eINSTANCE)
                    .load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (CloudprofilePackage.eNAME.equals(modelFileExtension)) {
            model = new FileModelHandler<CloudProfile>(this.resourceSet, CloudprofilePackage.eINSTANCE)
                    .load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (costPackage.eNAME.equals(modelFileExtension)) {
            model = new FileModelHandler<CostRepository>(this.resourceSet, costPackage.eINSTANCE)
                    .load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (designdecisionPackage.eNAME.equals(modelFileExtension)) {
            model = new FileModelHandler<DecisionSpace>(this.resourceSet, designdecisionPackage.eINSTANCE)
                    .load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (QMLDeclarationsPackage.eNAME.equals(modelFileExtension)) {
            model = new FileModelHandler<QMLDeclarations>(this.resourceSet, QMLDeclarationsPackage.eINSTANCE)
                    .load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (RepositoryPackage.eNAME.equals(modelFileExtension)) {
            model = new FileModelHandler<Repository>(this.resourceSet, RepositoryPackage.eINSTANCE)
                    .load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (ResourceenvironmentPackage.eNAME.equals(modelFileExtension)) {
            model = new FileModelHandler<ResourceEnvironment>(this.resourceSet, ResourceenvironmentPackage.eINSTANCE)
                    .load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (SystemPackage.eNAME.equals(modelFileExtension)) {
            model = new FileModelHandler<System>(this.resourceSet, SystemPackage.eINSTANCE)
                    .load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (UsagemodelPackage.eNAME.equals(modelFileExtension)) {
            model = new FileModelHandler<UsageModel>(this.resourceSet, UsagemodelPackage.eINSTANCE)
                    .load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else {
            throw new IllegalArgumentException("File extension does not look like a PCM model!");
        }

        this.getOutputPort().send(model);
    }

}
