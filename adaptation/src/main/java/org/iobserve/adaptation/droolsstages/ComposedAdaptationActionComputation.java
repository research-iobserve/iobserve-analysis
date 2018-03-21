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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.adaptation.data.graph.ModelGraph;
import org.iobserve.planning.systemadaptation.Action;
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
public class ComposedAdaptationActionComputation extends AbstractTransformation<AdaptationData, List<Action>> {
    static final String ADAPTATION_ACTION_LIST_ID = "composedAdaptationActions";

    final KieServices kieServices = KieServices.Factory.get();
    final KieContainer kContainer = this.kieServices.getKieClasspathContainer();
    final StatelessKieSession kSession = this.kContainer.newStatelessKieSession();
    final KieCommands kieCommands = this.kieServices.getCommands();

    @Override
    protected void execute(final AdaptationData adaptationData) throws Exception {
        final ModelGraph runtimeGraph = adaptationData.getRuntimeGraph();
        final ModelGraph redeploymentGraph = adaptationData.getReDeploymentGraph();
        final List<Command<?>> workingMemoryInserts = new LinkedList<>();
        final List<Action> composedAdaptationActions = new LinkedList<>();

        // Add empty list of composed adaptation actions (will be filled by rule engine)
        workingMemoryInserts.add(this.kieCommands.newInsert(composedAdaptationActions));

        // Add component nodes and deployment nodes of runtime and redeployment model graph
        this.addToWorkingMemoryInserts(workingMemoryInserts, runtimeGraph.getComponents());
        this.addToWorkingMemoryInserts(workingMemoryInserts, runtimeGraph.getServers());
        this.addToWorkingMemoryInserts(workingMemoryInserts, redeploymentGraph.getComponents());
        this.addToWorkingMemoryInserts(workingMemoryInserts, redeploymentGraph.getServers());

        // Execute rule engine which will add actions to composedAdaptationActions
        this.kSession.execute(this.kieCommands.newBatchExecution(workingMemoryInserts));

        for (final Action a : composedAdaptationActions) {
            System.out.println(a);
        }

        this.outputPort.send(composedAdaptationActions);
    }

    private void addToWorkingMemoryInserts(final List<Command<?>> inserts, final Set<?> graphComponents) {
        for (final Object component : graphComponents) {
            inserts.add(this.kieCommands.newInsert(component));
        }
    }

}
