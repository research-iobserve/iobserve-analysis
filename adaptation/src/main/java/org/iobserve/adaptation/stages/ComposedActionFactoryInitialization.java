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

import teetime.stage.basic.AbstractFilter;

import org.iobserve.adaptation.data.ActionFactory;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.adaptation.testmodel.AdaptationTestModel;
import org.iobserve.model.ModelImporter;
import org.iobserve.model.PCMModelHandlerMockup;

/**
 * Initializes the {@link ActionFactory} for composed actions with runtime and redeployment model
 * handlers.
 * 
 * @author Lars Bluemke
 *
 */
public class ComposedActionFactoryInitialization extends AbstractFilter<AdaptationData> {

    private final boolean isTestRun;

    /**
     * Creates a new instance of this filter.
     */
    public ComposedActionFactoryInitialization() {
        this.isTestRun = false;
    }

    /**
     * Creates a new instance of this filter. This constructor is intended for testing and
     * initializes a {@link PCMModelProviderMockup} for the action creation which does not read the
     * models from the file system.
     *
     * @param runtimeTestModel
     *            test runtime model
     * @param redeploymentTestModel
     *            test redeployment model
     */
    public ComposedActionFactoryInitialization(final AdaptationTestModel runtimeTestModel,
            final AdaptationTestModel redeploymentTestModel) {
        this.isTestRun = true;
        ActionFactory.setRuntimeModels(new PCMModelHandlerMockup(runtimeTestModel.getAllocation(),
                runtimeTestModel.getResEnvironment(), runtimeTestModel.getSystem(), null, null,
                runtimeTestModel.getRepository(), null, null, null, null));
        ActionFactory.setRedeploymentModels(new PCMModelHandlerMockup(redeploymentTestModel.getAllocation(),
                redeploymentTestModel.getResEnvironment(), redeploymentTestModel.getSystem(), null, null,
                redeploymentTestModel.getRepository(), null, null, null, null));
    }

    @Override
    protected void execute(final AdaptationData adaptationData) throws Exception {

        // Set up ActionFactory because the rule engine will invoke it in ComposedActionComputation
        if (!this.isTestRun) {
            ActionFactory.setRuntimeModels(new ModelImporter(adaptationData.getRuntimeModelDir()));
            ActionFactory.setRedeploymentModels(new ModelImporter(adaptationData.getReDeploymentModelDir()));
        }

        this.outputPort.send(adaptationData);
    }

}
