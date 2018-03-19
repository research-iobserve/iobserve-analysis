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

package org.iobserve.adaptation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.iobserve.adaptation.data.ActionFactory;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.adaptation.data.AssemblyContextActionFactory;
import org.iobserve.adaptation.data.ResourceContainerActionFactory;
import org.iobserve.analysis.data.graph.ComponentNode;
import org.iobserve.analysis.data.graph.DeploymentNode;
import org.iobserve.analysis.data.graph.ModelGraph;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class is the inital phase of the adaption filter stage. It compares a runtime PCM to a
 * redeployment PCM and calculates systemadaption {@link Action}s to transform the deployed system
 * towards the redeployment model.
 *
 * @author Philipp Weimann
 * @author Lars Blümke (terminology: "(de-)allocate" -> "(de-)replicate", "aquire/terminate" ->
 *         "(de-)allocate")
 *
 */
public class AdaptationCalculation extends AbstractTransformation<AdaptationData, AdaptationData> {

    private Map<String, ComponentNode> runtimeComponentNodes;
    private Map<String, DeploymentNode> runtimeDeploymentNodes;

    private Set<AssemblyContextAction> acActions;
    private Set<ResourceContainerAction> rcActions;

    public AdaptationCalculation() { // NOCS missing comment
        // empty constructor
    }

    private void init(final AdaptationData data) {
        this.runtimeComponentNodes = new HashMap<>();
        this.runtimeDeploymentNodes = new HashMap<>();

        this.acActions = new HashSet<>();
        this.rcActions = new HashSet<>();

        ActionFactory.setRuntimeModels(data.getRuntimeGraph().getPcmModels());
        ActionFactory.setRedeploymentModels(data.getReDeploymentGraph().getPcmModels());
    }

    @Override
    protected void execute(final AdaptationData element) throws Exception {

        assert element.getRuntimeGraph() != null;
        assert element.getReDeploymentGraph() != null;

        SystemAdaptation.LOG.info("Calculating system adaptation");

        this.init(element);
        this.addRuntimeData(element.getRuntimeGraph());
        this.startComparison(element.getReDeploymentGraph());

        element.setAcActions(this.acActions.stream().collect(Collectors.toList()));
        element.setRcActions(this.rcActions.stream().collect(Collectors.toList()));

        this.outputPort.send(element);
    }

    private void addRuntimeData(final ModelGraph graph) {
        for (final ComponentNode component : graph.getComponents()) {
            this.runtimeComponentNodes.put(component.getAssemblyContextID(), component);
        }

        for (final DeploymentNode server : graph.getServers()) {
            if (server.getContainingComponents().size() > 0) {
                // Don't add servers which don't host any components
                this.runtimeDeploymentNodes.put(server.getResourceContainerID(), server);
            }
        }
    }

    private void startComparison(final ModelGraph redeploymentGraph) {
        this.compareComponents(redeploymentGraph.getComponents());
        this.compareServers(redeploymentGraph.getServers());
    }

    private void compareComponents(final Set<ComponentNode> components) {
        for (final ComponentNode reDeplComp : components) {

            final ComponentNode runComp = this.runtimeComponentNodes.get(reDeplComp.getAssemblyContextID());

            if (runComp == null) {
                // Replicate, since ID does not yet exits
                final AssemblyContextAction action = AssemblyContextActionFactory.generateReplicateAction(runComp,
                        reDeplComp);
                this.acActions.add(action);
            } else if (!runComp.equals(reDeplComp)) {
                // Components differ, so check what actions need to be done!
                if (!runComp.getRepositoryComponentID().equals(reDeplComp.getRepositoryComponentID())) {
                    // AssemblyContexts contain different RepositoryComponents
                    final AssemblyContextAction action = AssemblyContextActionFactory
                            .generateChangeRepositoryComponentAction(runComp, reDeplComp);
                    this.acActions.add(action);
                }
                if (!runComp.getHostServer().getResourceContainerID()
                        .equals(reDeplComp.getHostServer().getResourceContainerID())) {
                    // AssemblyContexts are hosted on different Servers
                    final AssemblyContextAction action = AssemblyContextActionFactory.generateMigrateAction(runComp,
                            reDeplComp);
                    this.acActions.add(action);
                }
            }

            this.runtimeComponentNodes.remove(reDeplComp.getAssemblyContextID(), runComp);
        }

        for (final ComponentNode runComp : this.runtimeComponentNodes.values()) {
            // AssemblyContext does not exist anymore in redeployment model!
            final AssemblyContextAction action = AssemblyContextActionFactory.generateDereplicateAction(runComp);
            this.acActions.add(action);
        }
    }

    private void compareServers(final Set<DeploymentNode> servers) {
        for (final DeploymentNode reDeplServer : servers) {

            if (reDeplServer.getContainingComponents().size() == 0) {
                // If the server dosn't contain any components => IGNORE
                continue;
            }

            final DeploymentNode runServer = this.runtimeDeploymentNodes.get(reDeplServer.getResourceContainerID());

            if (runServer == null) {
                // It is an so far unused server!
                final AllocateAction action = ResourceContainerActionFactory.createAllocateAction(reDeplServer);
                this.rcActions.add(action);
            } else {
                // Server was and is still in use
                this.runtimeDeploymentNodes.remove(runServer.getResourceContainerID(), runServer);
            }
        }

        for (final DeploymentNode runServer : this.runtimeDeploymentNodes.values()) {
            // AssemblyContext does not exist anymore in redeployment model!
            final DeallocateAction action = ResourceContainerActionFactory.createDeallocateAction(runServer);
            this.rcActions.add(action);
        }
    }
}
