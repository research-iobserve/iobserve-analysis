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

import org.iobserve.adaptation.data.graph.DeploymentNode;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.SystemadaptationFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * This class provides a factory for system adaption Actions. It provides all basic functions.
 * Initialize the static fields {@value runtimeModels} and {@value redeploymentModels} before using
 * this class.
 *
 * @author Philipp Weimann
 * @author Lars Bluemke (Refactoring of system adaptation model: terminology: "aquire/terminate" ->
 *         "(de-)allocate", removal of resource container replication, changes to sources and
 *         targets of actions)
 */
public final class ResourceContainerActionFactory { // NOPMD not a utility class, it is a factory
                                                    // class

    /**
     * Empty default constructor.
     */
    private ResourceContainerActionFactory() {
        // private factory constructor
    }

    /**
     * Initializes the targetResourceContainer and targetLinkingResources of the abstract
     * {@link ResourceContainerAction} superclass.
     *
     * @param action
     *            the action
     * @param targetNode
     *            the action's target node (may be a runtime or a redeployment node)
     * @param targetResourceEnvironment
     *            the target resource environment model
     */
    private static void initializeResourceContainerAction(final ResourceContainerAction action,
            final DeploymentNode targetNode, final ResourceEnvironment targetResourceEnvironment) {
        final ResourceContainer targetResourceContainer = ActionFactory
                .getResourceContainer(targetNode.getResourceContainerID(), targetResourceEnvironment);

        action.setTargetResourceContainer(targetResourceContainer);

        // target linking resources
        ActionFactory.getConnectedLinkingResources(targetResourceContainer, targetResourceEnvironment)
                .forEach(lr -> action.getTargetLinkingResources().add(lr));

    }

    /**
     * Create an allocate action.
     *
     * @param reDeploymentServer
     *            the node where components can be deployed on
     * @return the action
     */
    public static AllocateAction createAllocateAction(final DeploymentNode reDeploymentServer) {
        final AllocateAction action = SystemadaptationFactory.eINSTANCE.createAllocateAction();
        final ResourceEnvironment redeploymentResourceEnvironment = ActionFactory.getRedeploymentModels()
                .getResourceEnvironmentModel();

        ResourceContainerActionFactory.initializeResourceContainerAction(action, reDeploymentServer,
                redeploymentResourceEnvironment);

        return action;
    }

    /**
     * Create a deallocate action.
     *
     * @param runtimeServer
     *            the node going to be deallocated
     * @return the action
     */
    public static DeallocateAction createDeallocateAction(final DeploymentNode runtimeServer) {
        final DeallocateAction action = SystemadaptationFactory.eINSTANCE.createDeallocateAction();
        final ResourceEnvironment runtimeResourceEnvironment = ActionFactory.getRuntimeModels()
                .getResourceEnvironmentModel();

        ResourceContainerActionFactory.initializeResourceContainerAction(action, runtimeServer,
                runtimeResourceEnvironment);

        return action;
    }

}
