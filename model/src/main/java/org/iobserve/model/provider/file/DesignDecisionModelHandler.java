/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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

import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import de.uka.ipd.sdq.pcm.designdecision.designdecisionPackage;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Model provider to provide a {@link DecisionSpace} model.
 *
 * @author Tobias Pöppke
 * @author Reiner Jung - refactoring & api change
 */
public class DesignDecisionModelHandler extends AbstractModelHandler<DecisionSpace> {

    public static final String SUFFIX = "designdecision";

    /**
     * Create a new provider with the given model file.
     * 
     * @param resourceSet
     *            set the resource set for the resource
     */
    public DesignDecisionModelHandler(final ResourceSet resourceSet) {
        super(resourceSet);
    }

    @Override
    protected EPackage getPackage() {
        return designdecisionPackage.eINSTANCE;
    }

    @Override
    protected String getSuffix() {
        return DesignDecisionModelHandler.SUFFIX;
    }
}
