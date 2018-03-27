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
package org.iobserve.adaptation.droolsstages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iobserve.adaptation.data.ActionFactory;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.adaptation.data.graph.ModelGraph;
import org.iobserve.adaptation.testmodel.AdaptationTestModel;
import org.iobserve.model.PCMModelHandler;
import org.iobserve.model.PCMModelHandlerMockup;
import org.iobserve.planning.systemadaptation.SystemAdaptation;
import org.iobserve.planning.systemadaptation.SystemadaptationFactory;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import teetime.stage.basic.AbstractTransformation;

/**
 * Receives an AdaptationData record containing the runtime model graph as well as the redeployment
 * model graph. Inserts both graphs into the drools rule engine and receives a list of composed
 * (high-level) adaptation actions which is passed to the output port.
 *
 * @author Lars Bluemke
 *
 */
public class ComposedActionComputation extends AbstractTransformation<AdaptationData, SystemAdaptation> {
    static final String ADAPTATION_ACTION_LIST_ID = "composedAdaptationActions";

    final KieServices kieServices = KieServices.Factory.get();
    final KieContainer kContainer = this.kieServices.getKieClasspathContainer();
    final StatelessKieSession kSession = this.kContainer.newStatelessKieSession();
    final KieCommands kieCommands = this.kieServices.getCommands();

    final boolean isTestRun;

    /**
     * Creates a new instance of this filter.
     */
    public ComposedActionComputation() {
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
    public ComposedActionComputation(final AdaptationTestModel runtimeTestModel,
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
        final ModelGraph runtimeGraph = adaptationData.getRuntimeGraph();
        final ModelGraph redeploymentGraph = adaptationData.getReDeploymentGraph();
        final List<Command<?>> workingMemoryInserts = new ArrayList<>();
        final SystemAdaptation systemAdaptationModel = SystemadaptationFactory.eINSTANCE.createSystemAdaptation();

        // Set up ActionFactory because the rule engine will invoke it
        if (!this.isTestRun) {
            ActionFactory.setRuntimeModels(new PCMModelHandler(adaptationData.getRuntimeModelDir()));
            ActionFactory.setRedeploymentModels(new PCMModelHandler(adaptationData.getReDeploymentModelDir()));
        }

        // Add empty list of composed adaptation actions (will be filled by rule engine)
        workingMemoryInserts.add(this.kieCommands.newInsert(systemAdaptationModel));

        // Add component nodes and deployment nodes of runtime and redeployment model graph
        this.addToWorkingMemoryInserts(workingMemoryInserts, runtimeGraph.getComponents());
        this.addToWorkingMemoryInserts(workingMemoryInserts, runtimeGraph.getServers());
        this.addToWorkingMemoryInserts(workingMemoryInserts, redeploymentGraph.getComponents());
        this.addToWorkingMemoryInserts(workingMemoryInserts, redeploymentGraph.getServers());

        // Execute rule engine which will add actions to composedAdaptationActions
        this.kSession.execute(this.kieCommands.newBatchExecution(workingMemoryInserts));

        this.outputPort.send(systemAdaptationModel);
    }

    private void addToWorkingMemoryInserts(final List<Command<?>> inserts, final Set<?> graphComponents) {
        for (final Object component : graphComponents) {
            inserts.add(this.kieCommands.newInsert(component));
        }
    }

}
