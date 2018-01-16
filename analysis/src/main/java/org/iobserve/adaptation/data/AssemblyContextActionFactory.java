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
package org.iobserve.adaptation.data;

import org.iobserve.analysis.data.graph.ComponentNode;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

/**
 * This class provides a factory for system adaption Actions. It provides all basic functions.
 * Initialize the static fields {@value runtimeModels} and {@value redeploymentModels} before using
 * this class.
 *
 * @author Philipp Weimann
 */
public class AssemblyContextActionFactory extends ActionFactory {

    private static AssemblyContextAction setSourceAssemblyContext(final AssemblyContextAction action,
            final String assemblyContextID) {
        final org.palladiosimulator.pcm.system.System systemModel = ActionFactory.getRuntimeModels()
                .getSystemModelProvider().getModel();
        final AssemblyContext assemblyContext = ActionFactory.getAssemblyContext(assemblyContextID, systemModel);
        action.setSourceAssemblyContext(assemblyContext);
        return action;
    }

    /**
     * Create a change repository component action.
     *
     * @param runtimeNode
     *            node to be changed
     * @param reDeploymentNode
     *            target node
     * @return returns the composed action
     */
    public static ChangeRepositoryComponentAction generateChangeRepositoryComponentAction(
            final ComponentNode runtimeNode, final ComponentNode reDeploymentNode) {
        final systemadaptationFactory factory = systemadaptationFactory.eINSTANCE;
        final ChangeRepositoryComponentAction action = factory.createChangeRepositoryComponentAction();

        AssemblyContextActionFactory.setSourceAssemblyContext(action, runtimeNode.getAssemblyContextID());

        final Repository repositoryModel = ActionFactory.getRedeploymentModels().getRepositoryModelProvider()
                .getModel();
        final RepositoryComponent repositoryComponent = repositoryModel.getComponents__Repository().stream()
                .filter(s -> s.getId().equals(reDeploymentNode.getRepositoryComponentID())).findFirst().get();
        action.setNewRepositoryComponent(repositoryComponent);

        return action;
    }

    /**
     * Create a migration action.
     *
     * @param runtimeNode
     *            source node
     * @param reDeploymentNode
     *            target node
     * @return returns the migration action
     */
    public static MigrateAction generateMigrateAction(final ComponentNode runtimeNode,
            final ComponentNode reDeploymentNode) {
        final systemadaptationFactory factory = systemadaptationFactory.eINSTANCE;
        final MigrateAction action = factory.createMigrateAction();

        AssemblyContextActionFactory.setSourceAssemblyContext(action, runtimeNode.getAssemblyContextID());

        final Allocation runAllocation = ActionFactory.getRuntimeModels().getAllocationModelProvider().getModel();
        action.setSourceAllocationContext(
                ActionFactory.getAllocationContext(runtimeNode.getAllocationContextID(), runAllocation));

        final Allocation reDeplAllocation = ActionFactory.getRedeploymentModels().getAllocationModelProvider()
                .getModel();
        action.setNewAllocationContext(
                ActionFactory.getAllocationContext(reDeploymentNode.getAllocationContextID(), reDeplAllocation));
        return action;
    }

    /**
     * Create a deallocation action.
     *
     * @param runtimeNode
     *            node to be deallocated
     * @return returns the deallocation action
     */
    public static DeallocateAction generateDeallocateAction(final ComponentNode runtimeNode) {
        final systemadaptationFactory factory = systemadaptationFactory.eINSTANCE;
        final DeallocateAction action = factory.createDeallocateAction();

        AssemblyContextActionFactory.setSourceAssemblyContext(action, runtimeNode.getAssemblyContextID());

        final Allocation runAllocation = ActionFactory.getRuntimeModels().getAllocationModelProvider().getModel();
        action.setOldAllocationContext(
                ActionFactory.getAllocationContext(runtimeNode.getAllocationContextID(), runAllocation));

        return action;
    }

    /**
     * Create an allocation action.
     *
     * @param runtimeNode
     *            node to be allocated
     * @param reDeploymentNode
     *            node to be deployed
     * @return returns the allocation action
     */
    public static AllocateAction generateAllocateAction(final ComponentNode runtimeNode,
            final ComponentNode reDeploymentNode) {
        final systemadaptationFactory factory = systemadaptationFactory.eINSTANCE;
        final AllocateAction action = factory.createAllocateAction();

        // Allcotaion has no runtime component
        // AssemblyContextActionFactory.setSourceAssemblyContext(action,
        // runtimeNode.getAssemblyContextID());

        final org.palladiosimulator.pcm.system.System reDeplSystem = ActionFactory.getRedeploymentModels()
                .getSystemModelProvider().getModel();
        action.setSourceAssemblyContext(
                ActionFactory.getAssemblyContext(reDeploymentNode.getAssemblyContextID(), reDeplSystem));

        final Allocation reDeplAllocation = ActionFactory.getRedeploymentModels().getAllocationModelProvider()
                .getModel();
        action.setNewAllocationContext(
                ActionFactory.getAllocationContext(reDeploymentNode.getAllocationContextID(), reDeplAllocation));
        return action;
    }
}
