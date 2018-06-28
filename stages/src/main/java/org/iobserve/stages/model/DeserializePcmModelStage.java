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

import teetime.stage.basic.AbstractTransformation;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.iobserve.model.provider.file.AllocationModelHandler;
import org.iobserve.model.provider.file.CloudProfileModelHandler;
import org.iobserve.model.provider.file.CostModelHandler;
import org.iobserve.model.provider.file.DesignDecisionModelHandler;
import org.iobserve.model.provider.file.QMLDeclarationsModelHandler;
import org.iobserve.model.provider.file.RepositoryModelHandler;
import org.iobserve.model.provider.file.ResourceEnvironmentModelHandler;
import org.iobserve.model.provider.file.SystemModelHandler;
import org.iobserve.model.provider.file.UsageModelHandler;

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
        final String modelFileExtension = modelFileName.substring(modelFileName.lastIndexOf(".") + 1);
        final EObject model;

        if (".allocation".equals(modelFileExtension)) {
            model = new AllocationModelHandler(resourceSet).load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (".cloudprofile".equals(modelFileExtension)) {
            model = new CloudProfileModelHandler(resourceSet).load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (".cost".equals(modelFileExtension)) {
            model = new CostModelHandler(resourceSet).load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (".designdecision".equals(modelFileExtension)) {
            model = new DesignDecisionModelHandler(resourceSet).load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (".qmldeclarations".equals(modelFileExtension)) {
            model = new QMLDeclarationsModelHandler(resourceSet).load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (".repository".equals(modelFileExtension)) {
            model = new RepositoryModelHandler(resourceSet).load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (".resourceenvironment".equals(modelFileExtension)) {
            model = new ResourceEnvironmentModelHandler(resourceSet).load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (".system".equals(modelFileExtension)) {
            model = new SystemModelHandler(resourceSet).load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else if (".usagemodel".equals(modelFileExtension)) {
            model = new UsageModelHandler(resourceSet).load(URI.createFileURI(modelFile.getAbsolutePath()));
        } else {
            throw new IllegalArgumentException("File extension does not look like a PCM model!");
        }

        this.getOutputPort().send(model);
    }

}
