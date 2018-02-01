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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.provider.file.AllocationModelHandler;
import org.iobserve.model.provider.file.CloudProfileModelHandler;
import org.iobserve.model.provider.file.CostModelHandler;
import org.iobserve.model.provider.file.DesignDecisionModelHandler;
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

import de.uka.ipd.sdq.dsexplore.qml.declarations.QMLDeclarations.QMLDeclarations;
import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import teetime.stage.basic.AbstractTransformation;

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
        final String filePathPrefix = this.modelDirectory.getAbsolutePath() + File.separator + this.modelName;
        String filePath;

        if (model instanceof Allocation) {
            filePath = filePathPrefix + ".allocation";
            new AllocationModelHandler().save(URI.createFileURI(filePath), (Allocation) model);
        } else if (model instanceof CloudProfile) {
            filePath = filePathPrefix + ".cloudprofile";
            new CloudProfileModelHandler().save(URI.createFileURI(filePath), (CloudProfile) model);
        } else if (model instanceof CostRepository) {
            filePath = filePathPrefix + ".cost";
            new CostModelHandler().save(URI.createFileURI(filePath), (CostRepository) model);
        } else if (model instanceof DecisionSpace) {
            filePath = filePathPrefix + ".designdecision";
            new DesignDecisionModelHandler().save(URI.createFileURI(filePath), (DecisionSpace) model);
        } else if (model instanceof QMLDeclarations) {
            filePath = filePathPrefix + ".qmldeclarations";
            new QMLDeclarationsModelHandler().save(URI.createFileURI(filePath), (QMLDeclarations) model);
        } else if (model instanceof Repository) {
            filePath = filePathPrefix + ".repository";
            new RepositoryModelHandler().save(URI.createFileURI(filePath), (Repository) model);
        } else if (model instanceof ResourceEnvironment) {
            filePath = filePathPrefix + ".resourceenvironment";
            new ResourceEnvironmentModelHandler().save(URI.createFileURI(filePath), (ResourceEnvironment) model);
        } else if (model instanceof System) {
            filePath = filePathPrefix + ".system";
            new SystemModelHandler().save(URI.createFileURI(filePath), (System) model);
        } else if (model instanceof UsageModel) {
            filePath = filePathPrefix + ".usagemodel";
            new UsageModelHandler().save(URI.createFileURI(filePath), (UsageModel) model);
        } else {
            throw new IllegalArgumentException("The EObject passed to this stage must be a PCM model!");
        }

        this.getOutputPort().send(new File(filePath));
    }

}
