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
package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;
import org.palladiosimulator.pcm.usagemodel.UserData;

/**
 * Model provider to provide a {@link UsageModel}.
 *
 * @author Robert Heinrich
 * @author Nicolas Boltz
 * @author Alessandro Giusa
 *
 */
public final class UsageModelProvider extends AbstractModelProvider<UsageModel> {

    /**
     * Create usage model provider.
     *
     * @param uriUsageModel
     *            uri to model
     */
    public UsageModelProvider(final URI uriUsageModel) {
        super(uriUsageModel);
    }

    @Override
    protected EPackage getPackage() {
        return UsagemodelPackage.eINSTANCE;
    }

    /**
     * Reset the model. This will delete all {@link UsageScenario} and {@link UserData} instances.
     */
    @Override
    public void resetModel() {
        final UsageModel model = this.getModel();
        model.getUsageScenario_UsageModel().clear();
        model.getUserData_UsageModel().clear();
    }
    
    /**
     * Updates the values of the current model to the values of the passed model.
     * 
     * @param newModel
     *            UsageModel with new {@link UsageScenario} and {@link UserData} to be set.
     *            
     * @see resetModel() 
     */
    public void updateModel(UsageModel newModel) {
    	this.resetModel();
    	final UsageModel model = this.getModel();
        model.getUsageScenario_UsageModel().addAll(newModel.getUsageScenario_UsageModel());
        model.getUserData_UsageModel().addAll(newModel.getUserData_UsageModel());
    }
}
