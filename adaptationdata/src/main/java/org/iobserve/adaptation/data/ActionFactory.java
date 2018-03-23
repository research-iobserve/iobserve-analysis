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

import org.iobserve.model.IPCMModelHandler;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * This class provides a factory for system adaption Actions. It provides all basic functions.
 * Initialize the static fields {@value runtimeModels} and {@value redeploymentModels} before using
 * this class. It is recommended not to use this class directly, but
 * {@link AssemblyContextActionFactory} and {@link ResourceContainerActionFactory} directly.
 *
 * @author Philipp Weimann
 * @author Reiner Jung - fix naming and api change
 */
public final class ActionFactory {

    private static IPCMModelHandler runtimeModelHandler;
    private static IPCMModelHandler redeploymentModelHandler;

    private ActionFactory() {

    }

    /**
     * TODO .
     *
     * @param contextID
     *            .
     * @param systemModel
     *            .
     * @return .
     */
    public static AssemblyContext getAssemblyContext(final String contextID, final System systemModel) {
        return systemModel.getAssemblyContexts__ComposedStructure().stream().filter(s -> s.getId().equals(contextID))
                .findFirst().get();
    }

    /**
     * ToDo .
     *
     * @param allocationID
     *            .
     * @param allocationModel
     *            .
     * @return .
     */
    public static AllocationContext getAllocationContext(final String allocationID, final Allocation allocationModel) {
        return allocationModel.getAllocationContexts_Allocation().stream().filter(s -> s.getId().equals(allocationID))
                .findFirst().get();
    }

    /**
     * ToDo .
     *
     * @param resourceContainerID
     *            .
     * @param resEnvModel
     *            .
     * @return .
     */
    public static ResourceContainer getResourceContainer(final String resourceContainerID,
            final ResourceEnvironment resEnvModel) {
        return resEnvModel.getResourceContainer_ResourceEnvironment().stream()
                .filter(s -> s.getId().equals(resourceContainerID)).findFirst().get();
    }

    public static IPCMModelHandler getRuntimeModels() {
        return ActionFactory.runtimeModelHandler;
    }

    public static void setRuntimeModels(final IPCMModelHandler runtimeModels) {
        ActionFactory.runtimeModelHandler = runtimeModels;
    }

    public static IPCMModelHandler getRedeploymentModels() {
        return ActionFactory.redeploymentModelHandler;
    }

    public static void setRedeploymentModels(final IPCMModelHandler redeploymentModels) {
        ActionFactory.redeploymentModelHandler = redeploymentModels;
    }
}
