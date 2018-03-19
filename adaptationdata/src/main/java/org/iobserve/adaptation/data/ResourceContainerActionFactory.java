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

import org.iobserve.analysis.data.graph.DeploymentNode;
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
 * @author Lars Blümke (terminology: "aquire/terminate" -> "(de-)allocate", removal of resource
 *         container replication)
 */
public final class ResourceContainerActionFactory {

    private ResourceContainerActionFactory() {
        // private factory constructor
    }

    private static ResourceContainerAction setSourceResourceContainer(final ResourceContainerAction action,
            final String resourceContainerID) {
        final ResourceEnvironment resEnvModel = ActionFactory.getRuntimeModels().getResourceEnvironmentModel();
        final ResourceContainer resourceContainer = ActionFactory.getResourceContainer(resourceContainerID,
                resEnvModel);
        action.setSourceResourceContainer(resourceContainer);
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
        final SystemadaptationFactory factory = SystemadaptationFactory.eINSTANCE;
        final DeallocateAction action = factory.createDeallocateAction();

        ResourceContainerActionFactory.setSourceResourceContainer(action, runtimeServer.getResourceContainerID());

        return action;
    }

    /**
     * Create an allocate action.
     *
     * @param reDeploymentServer
     *            the node where components can be deployed on
     * @return the action
     */
    public static AllocateAction createAllocateAction(final DeploymentNode reDeploymentServer) {
        final SystemadaptationFactory factory = SystemadaptationFactory.eINSTANCE;
        final AllocateAction action = factory.createAllocateAction();

        final ResourceEnvironment reDeplResEnvModel = ActionFactory.getRedeploymentModels()
                .getResourceEnvironmentModel();
        final ResourceContainer resourceContainer = ActionFactory
                .getResourceContainer(reDeploymentServer.getResourceContainerID(), reDeplResEnvModel);
        action.setSourceResourceContainer(resourceContainer);

        return action;
    }

}
