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

import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.analysis.graph.DeploymentNode;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.TerminateAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * This class provides a factory for system adaption Actions. It provides all basic functions.
 * Initialize the static fields {@value runtimeModels} and {@value redeploymentModels} before using
 * this class.
 *
 * @author Philipp Weimann
 */
public class ResourceContainerActionFactory extends ActionFactory {

    private static ResourceContainerAction setSourceResourceContainer(final ResourceContainerAction action,
            final String resourceContainerID) {
        final ResourceEnvironment resEnvModel = ActionFactory.getRuntimeModels().getResourceEnvironmentModelProvider()
                .getModel();
        final ResourceContainer resourceContainer = ActionFactory.getResourceContainer(resourceContainerID,
                resEnvModel);
        action.setSourceResourceContainer(resourceContainer);
        return action;
    }

    public static TerminateAction generateTerminateAction(final DeploymentNode runtimeServer) {
        final systemadaptationFactory factory = systemadaptationFactory.eINSTANCE;
        final TerminateAction action = factory.createTerminateAction();

        ResourceContainerActionFactory.setSourceResourceContainer(action, runtimeServer.getResourceContainerID());

        return action;
    }

    public static AcquireAction generateAcquireAction(final DeploymentNode reDeploymentServer) {
        final systemadaptationFactory factory = systemadaptationFactory.eINSTANCE;
        final AcquireAction action = factory.createAcquireAction();

        final ResourceEnvironment reDeplResEnvModel = ActionFactory.getRedeploymentModels()
                .getResourceEnvironmentModelProvider().getModel();
        final ResourceContainer resourceContainer = ActionFactory
                .getResourceContainer(reDeploymentServer.getResourceContainerID(), reDeplResEnvModel);
        action.setSourceResourceContainer(resourceContainer);

        return action;
    }

    public static ReplicateAction generateReplicateAction(final DeploymentNode runtimeServer,
            final DeploymentNode reDeploymentServer) {
        final systemadaptationFactory factory = systemadaptationFactory.eINSTANCE;
        final ReplicateAction action = factory.createReplicateAction();

        ResourceContainerActionFactory.setSourceResourceContainer(action, runtimeServer.getResourceContainerID());

        final Allocation runtimeAllocModel = ActionFactory.getRuntimeModels().getAllocationModelProvider().getModel();
        for (final ComponentNode component : runtimeServer.getContainingComponents()) {
            final AllocationContext oldAllocationContext = ActionFactory
                    .getAllocationContext(component.getAllocationContextID(), runtimeAllocModel);
            action.getSourceAllocationContext().add(oldAllocationContext);
        }

        final Allocation reDeplAllocModel = ActionFactory.getRedeploymentModels().getAllocationModelProvider()
                .getModel();
        for (final ComponentNode component : reDeploymentServer.getContainingComponents()) {
            final AllocationContext newAllocationContext = ActionFactory
                    .getAllocationContext(component.getAllocationContextID(), reDeplAllocModel);
            action.getSourceAllocationContext().add(newAllocationContext);
        }

        final ResourceEnvironment resEnvModel = ActionFactory.getRedeploymentModels()
                .getResourceEnvironmentModelProvider().getModel();
        final ResourceContainer newResourceContainer = ActionFactory
                .getResourceContainer(reDeploymentServer.getResourceContainerID(), resEnvModel);
        action.setNewResourceContainer(newResourceContainer);

        return action;
    }

}
