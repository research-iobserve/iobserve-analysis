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
package org.iobserve.analysis.model.provider;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;

import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import de.uka.ipd.sdq.pcm.designdecision.designdecisionPackage;

/**
 * Model provider to provide a {@link DecisionSpace} model.
 *
 * @author Tobias Pöppke
 *
 */
public class DesignDecisionModelProvider extends AbstractModelProvider<DecisionSpace> {

    /**
     * Create a new provider with the given model file.
     *
     * @param theUriModelInstance
     *            path to the model file
     */
    public DesignDecisionModelProvider(final URI theUriModelInstance) {
        super(theUriModelInstance);
    }

    @Override
    protected EPackage getPackage() {
        return designdecisionPackage.eINSTANCE;
    }

    @Override
    public void resetModel() {
        this.getModel().getDegreesOfFreedom().clear();
    }
}
