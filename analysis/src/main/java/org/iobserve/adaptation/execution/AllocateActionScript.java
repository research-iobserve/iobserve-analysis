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
package org.iobserve.adaptation.execution;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.utils.ModelHelper;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Action script for an allocation action.
 *
 * This action deploys an assembly context onto a node group. It looks for a script with the name
 * {@link AdaptationData#ALLOCATE_SCRIPT_NAME} in the folder
 * '{$deployablesRepository}/{$assemblyContextComponentName}/' and executes this script on each node
 * of the group to deploy the assembly context.
 *
 * @author Tobias Pöppke
 *
 */
public class AllocateActionScript extends AbstractActionScript {
    private final AllocateAction action;

    /**
     * Create a new allocate action script with the given data.
     *
     * @param data
     *            the data shared in the adaptation stage
     * @param action
     *            the action item to be executed
     */
    public AllocateActionScript(final AdaptationData data, final AllocateAction action) {
        super(data);
        this.action = action;
    }

    @Override
    public void execute() throws RunScriptOnNodesException, IOException {
        final ResourceContainer container = this.action.getNewAllocationContext()
                .getResourceContainer_AllocationContext();

        final ResourceContainerCloud cloudContainer = this.getResourceContainerCloud(container);

        final ComputeService client = this.getComputeServiceForContainer(cloudContainer);
        final String assemblyContextName = this.action.getSourceAssemblyContext().getEntityName();

        // If the assembly context has already been allocated on the group, do
        // nothing
        if (!this.data.getAllocatedContexts().contains(assemblyContextName)) {
            client.runScriptOnNodesMatching(node -> node.getGroup().equals(cloudContainer.getGroupName()),
                    this.getAllocateScript(this.action.getSourceAssemblyContext()));
            this.data.getAllocatedContexts().add(assemblyContextName);
            // TODO add possibility to open up ports defined in a config file
        }
    }

    private String getAllocateScript(final AssemblyContext assemblyCtx) throws IOException {
        final String assemblyCtxFolderName = this.getAssemblyContextFolderName(assemblyCtx);

        final URI allocationScriptURI = this.data.getDeployablesFolderURI().appendSegment(assemblyCtxFolderName)
                .appendSegment(AdaptationData.ALLOCATE_SCRIPT_NAME);

        return this.getFileContents(allocationScriptURI);
    }

    @Override
    public boolean isAutoExecutable() {
        return true;
    }

    @Override
    public String getDescription() {
        final ResourceContainerCloud sourceContainer = this.getResourceContainerCloud(
                this.action.getNewAllocationContext().getResourceContainer_AllocationContext());

        final StringBuilder builder = new StringBuilder();
        builder.append("Allocate Action: Allocate assembly context '");
        builder.append(this.action.getSourceAssemblyContext().getEntityName());
        builder.append("' to container of provider '");
        builder.append(sourceContainer.getInstanceType().getProvider().getName());
        builder.append("' of type '");
        builder.append(sourceContainer.getInstanceType());
        builder.append("' in location '");
        builder.append(sourceContainer.getInstanceType().getLocation());
        builder.append("' with name '");
        builder.append(ModelHelper.getGroupName(sourceContainer));
        builder.append("'");
        return builder.toString();
    }

}
