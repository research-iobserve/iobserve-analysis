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
package org.iobserve.legacyexecution.actionscripts;

import java.io.File;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.legacyexecution.utils.ModelHelper;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action script for migrating an assembly context from one cloud resource container group to
 * another.
 *
 * This action migrates an assembly context from one node group to another. It looks for a script
 * with the name {@link AdaptationData#PRE_MIGRATE_SCRIPT_NAME} in the folder
 * '{$deployablesRepository}/{$assemblyContextComponentName}/' and executes this script on each node
 * of the group to prepare the migration. After that, the script
 * {@link AdaptationData#POST_MIGRATE_SCRIPT_NAME} in the same folder is executed on the target node
 * group to complete the migration.
 *
 * @author Tobias Pöppke
 * @author Lars Blümke (Refactoring of system adaptation model: changes to sources and targets of
 *         actions)
 *
 * @since 0.0.2
 */
public class MigrateActionScript extends AbstractActionScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(MigrateActionScript.class);

    private final MigrateAction action;

    /**
     * Create a new migration action script with the given data.
     *
     * @param data
     *            the data shared in the adaptation stage
     * @param action
     *            the action item to be executed
     */
    public MigrateActionScript(final AdaptationData data, final MigrateAction action) {
        super(data);
        this.action = action;
    }

    @Override
    public void execute() throws RunScriptOnNodesException {
        final ResourceContainer sourceContainer = this.action.getSourceAllocationContext()
                .getResourceContainer_AllocationContext();
        final ResourceContainer targetContainer = this.action.getTargetAllocationContext()
                .getResourceContainer_AllocationContext();

        final ResourceContainerCloud sourceCloudContainer = this.getResourceContainerCloud(sourceContainer);
        final ResourceContainerCloud targetCloudContainer = this.getResourceContainerCloud(targetContainer);

        final ComputeService client = this.getComputeServiceForContainer(sourceCloudContainer);
        final String assemblyContextName = this.action.getTargetAllocationContext()
                .getAssemblyContext_AllocationContext().getEntityName();

        // If the assembly context has already been migrated, do nothing
        if (!this.data.getMigratedContexts().contains(assemblyContextName)) {
            client.runScriptOnNodesMatching(node -> node.getGroup().equals(sourceCloudContainer.getGroupName()),
                    this.getScript(AdaptationData.PRE_MIGRATE_SCRIPT_NAME,
                            this.action.getTargetAllocationContext().getAssemblyContext_AllocationContext()));
            client.runScriptOnNodesMatching(node -> node.getGroup().equals(targetCloudContainer.getGroupName()),
                    this.getScript(AdaptationData.POST_MIGRATE_SCRIPT_NAME,
                            this.action.getTargetAllocationContext().getAssemblyContext_AllocationContext()));
            // TODO add possibility to open up ports defined in a config file
            this.data.getMigratedContexts().add(assemblyContextName);
        }
    }

    private String getScript(final String scriptName, final AssemblyContext assemblyCtx) {
        final String assemblyCtxFolderName = this.getAssemblyContextFolderName(assemblyCtx);

        final String scriptURI = this.data.getDeployablesDir() + File.separator + assemblyCtxFolderName + File.separator
                + scriptName;
        try {
            return scriptURI;
        } catch (final NullPointerException e) {
            // No script found, so we can not execute anything
            MigrateActionScript.LOGGER
                    .warn(String.format("Could not find script '%s'. No script will be executed.", scriptURI));
            return "";
        }
    }

    @Override
    public boolean isAutoExecutable() {
        return true;
    }

    @Override
    public String getDescription() {
        final ResourceContainerCloud sourceContainer = this.getResourceContainerCloud(
                this.action.getSourceAllocationContext().getResourceContainer_AllocationContext());
        final ResourceContainerCloud targetContainer = this.getResourceContainerCloud(
                this.action.getTargetAllocationContext().getResourceContainer_AllocationContext());

        final StringBuilder builder = new StringBuilder();
        builder.append("Migrate Action: Migrate assembly context '");
        builder.append(this.action.getTargetAllocationContext().getAssemblyContext_AllocationContext().getEntityName());
        builder.append("' from container of provider '");
        builder.append(sourceContainer.getInstanceType().getProvider().getName());
        builder.append("' of type '");
        builder.append(sourceContainer.getInstanceType());
        builder.append("' in location '");
        builder.append(sourceContainer.getInstanceType().getLocation());
        builder.append("' with name '");
        builder.append(ModelHelper.getGroupName(sourceContainer));
        builder.append(" to container from provider '");
        builder.append(targetContainer.getInstanceType().getProvider().getName());
        builder.append("' of type '");
        builder.append(targetContainer.getInstanceType());
        builder.append("' in location '");
        builder.append(targetContainer.getInstanceType().getLocation());
        builder.append("' with name '");
        builder.append(ModelHelper.getGroupName(targetContainer));
        return builder.toString();
    }

}
