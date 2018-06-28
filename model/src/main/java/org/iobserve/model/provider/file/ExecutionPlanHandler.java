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
package org.iobserve.model.provider.file;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;

/**
 * Model handler to provide the {@link ExecutionPlan}.
 *
 * @author Lars Bluemke
 *
 */
public class ExecutionPlanHandler extends AbstractModelHandler<ExecutionPlan> {

    public ExecutionPlanHandler(final ResourceSet resourceSet) {
		super(resourceSet);
	}

	@Override
    protected EPackage getPackage() {
        return ExecutionplanPackage.eINSTANCE;
    }

	@Override
	protected String getSuffix() {
		return "exp";
	}

}
