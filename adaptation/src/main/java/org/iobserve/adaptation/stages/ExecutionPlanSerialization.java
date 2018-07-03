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
package org.iobserve.adaptation.stages;

import java.io.File;

import teetime.stage.basic.AbstractTransformation;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;
import org.iobserve.model.persistence.file.FileModelHandler;

/**
 * Serializes the execution plan in an xmi file format.
 *
 * @author Lars Bluemke
 *
 */
public class ExecutionPlanSerialization extends AbstractTransformation<ExecutionPlan, File> {

    private final File executionPlanURI;
    private final ResourceSet resourceSet;

    /**
     * Creates an instance of this class.
     *
     * @param executionPlanURI
     *            URI for execution plan
     */
    public ExecutionPlanSerialization(final File executionPlanURI) {
        this.executionPlanURI = executionPlanURI;
        this.resourceSet = new ResourceSetImpl();
    }

    @Override
    protected void execute(final ExecutionPlan executionPlan) throws Exception {
        new FileModelHandler<ExecutionPlan>(this.resourceSet, ExecutionplanPackage.eINSTANCE)
                .save(URI.createFileURI(this.executionPlanURI.getAbsolutePath()), executionPlan);

        this.outputPort.send(this.executionPlanURI);
    }

}
