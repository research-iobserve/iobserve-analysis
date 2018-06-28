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


import org.iobserve.model.ModelImporter;

import java.util.List;
import java.util.stream.Collectors;

import org.iobserve.model.IPCMModelHandler;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
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
 * @author Lars Bluemke - added getAllocationContextContainingAssemblyContext(),
 *         getProvidingAssemblyContexts(), getRequiringAssemblyContexts() and
 *         getConnectedLinkingResources
 */
public final class ActionFactory {

    private static IPCMModelHandler runtimeModelHandler;
    private static IPCMModelHandler redeploymentModelHandler;

    private ActionFactory() {

    }

    /**
     * Returns the assembly context with the given ID from the system model.
     *
     * @param contextID
     *            The ID
     * @param systemModel
     *            The system model
     * @return The assembly context
     */
    public static AssemblyContext getAssemblyContext(final String contextID, final System systemModel) {
        return systemModel.getAssemblyContexts__ComposedStructure().stream().filter(s -> s.getId().equals(contextID))
                .findFirst().get();
    }

    /**
     * Returns the allocation context with the given ID form the allocation model.
     *
     * @param allocationID
     *            The ID
     * @param allocationModel
     *            The allocation model
     * @return The allocation context
     */
    public static AllocationContext getAllocationContext(final String allocationID, final Allocation allocationModel) {
        return allocationModel.getAllocationContexts_Allocation().stream().filter(s -> s.getId().equals(allocationID))
                .findFirst().get();
    }

    /**
     * Returns the allocation context containing the assembly context with the given ID from the
     * allocation model.
     *
     * @param assemblyID
     *            The ID
     * @param allocationModel
     *            The allocation model
     * @return The allocation context
     */
    public static AllocationContext getAllocationContextContainingAssemblyContext(final String assemblyID,
            final Allocation allocationModel) {
        return allocationModel.getAllocationContexts_Allocation().stream()
                .filter(alc -> alc.getAssemblyContext_AllocationContext().getId().equals(assemblyID)).findFirst().get();
    }

    /**
     * Returns a list of assembly contexts providing something to the one with the given ID from the
     * system model.
     *
     * @param assemblyID
     *            The ID
     * @param systemModel
     *            The system model
     * @return The providing assembly contexts
     */
    public static List<AssemblyContext> getProvidingAssemblyContexts(final String assemblyID,
            final System systemModel) {
        return systemModel.getConnectors__ComposedStructure().stream().filter(c -> c instanceof AssemblyConnector)
                .map(c -> (AssemblyConnector) c)
                .filter(c -> c.getRequiringAssemblyContext_AssemblyConnector().getId().equals(assemblyID))
                .map(c -> c.getProvidingAssemblyContext_AssemblyConnector()).collect(Collectors.toList());
    }

    /**
     * Returns a list of assembly contexts requiring something to the one with the given ID from the
     * system model.
     *
     * @param assemblyID
     *            The ID
     * @param systemModel
     *            The system model
     * @return The requiring assembly contexts
     */
    public static List<AssemblyContext> getRequiringAssemblyContexts(final String assemblyID,
            final System systemModel) {
        return systemModel.getConnectors__ComposedStructure().stream().filter(c -> c instanceof AssemblyConnector)
                .map(c -> (AssemblyConnector) c)
                .filter(c -> c.getProvidingAssemblyContext_AssemblyConnector().getId().equals(assemblyID))
                .map(c -> c.getRequiringAssemblyContext_AssemblyConnector()).collect(Collectors.toList());
    }

    /**
     * Returns the resource container with the given ID from the resource environment model.
     *
     * @param resourceContainerID
     *            The ID
     * @param resEnvModel
     *            The resource environment model
     * @return The resource container
     */
    public static ResourceContainer getResourceContainer(final String resourceContainerID,
            final ResourceEnvironment resEnvModel) {
        return resEnvModel.getResourceContainer_ResourceEnvironment().stream()
                .filter(s -> s.getId().equals(resourceContainerID)).findFirst().get();
    }

    /**
     * Returns a list of linking resources connected to the given resource container.
     *
     * @param resourceContainer
     *            The resource container
     * @param resEnvModel
     *            The resource environment model
     * @return The connected linking resources
     */
    public static List<LinkingResource> getConnectedLinkingResources(final ResourceContainer resourceContainer,
            final ResourceEnvironment resEnvModel) {
        return resEnvModel.getLinkingResources__ResourceEnvironment().stream()
                .filter(lr -> lr.getConnectedResourceContainers_LinkingResource().contains(resourceContainer))
                .collect(Collectors.toList());
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
