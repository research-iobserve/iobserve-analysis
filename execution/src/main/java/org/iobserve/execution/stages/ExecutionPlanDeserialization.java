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
package org.iobserve.execution.stages;

import java.io.File;

import teetime.stage.basic.AbstractTransformation;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.model.provider.file.ExecutionPlanHandler;

/**
 * Deserializes a serialized execution plan.
 *
 * @author Lars Bluemke
 *
 */
public class ExecutionPlanDeserialization extends AbstractTransformation<File, ExecutionPlan> {

    private final ResourceSet resouceSet = new ResourceSetImpl();

	@Override
    protected void execute(final File executionPlanFile) throws Exception {
        final ExecutionPlan executionPlan = new ExecutionPlanHandler(resouceSet)
                .load(URI.createFileURI(executionPlanFile.getAbsolutePath()));

        this.outputPort.send(executionPlan);
    }

}
