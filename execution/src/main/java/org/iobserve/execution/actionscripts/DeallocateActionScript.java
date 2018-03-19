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
package org.iobserve.execution.actionscripts;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.execution.utils.ModelHelper;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action script for deallocating a group of cloud resource containers.
 *
 * @author Tobias Pöppke
 * @author Lars Blümke (terminology: "terminate" -> "deallocate")
 *
 * @since 0.0.2
 */
public class DeallocateActionScript extends AbstractActionScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeallocateActionScript.class);

    private final DeallocateAction action;

    /**
     * Create a new deallocate action script with the given data.
     *
     * @param data
     *            the data shared in the adaptation stage
     * @param action
     *            the action item to be executed
     */
    public DeallocateActionScript(final AdaptationData data, final DeallocateAction action) {
        super(data);
        this.action = action;
    }

    @Override
    public void execute() throws RunScriptOnNodesException {
        final ResourceContainer container = this.action.getSourceResourceContainer();

        final ResourceContainerCloud cloudContainer = this.getResourceContainerCloud(container);

        final ComputeService client = this.getComputeServiceForContainer(cloudContainer);

        // If the container group has already been terminated, do nothing
        if (!this.data.getTerminatedGroups().contains(ModelHelper.getGroupName(cloudContainer))) {
            client.runScriptOnNodesMatching(node -> node.getGroup().equals(cloudContainer.getGroupName()),
                    this.getScript(AdaptationData.NODE_PRE_TERMINATE_SCRIPT_NAME));
            client.destroyNodesMatching(node -> node.getGroup().equals(cloudContainer.getGroupName()));
            this.data.getTerminatedGroups().add(cloudContainer.getGroupName());
        }
    }

    private String getScript(final String scriptName) {
        final URI terminationScriptURI = this.data.getDeployablesFolderURI().appendSegment(scriptName);

        try {
            return this.getFileContents(terminationScriptURI);
        } catch (final IOException e) {
            // No script found, so we can not execute anything
            DeallocateActionScript.LOGGER
                    .warn("Could not find script for node deallocation. No script will be executed.");
            return "";
        }
    }

    @Override
    public boolean isAutoExecutable() {
        return true;
    }

    @Override
    public String getDescription() {
        final ResourceContainerCloud container = this
                .getResourceContainerCloud(this.action.getSourceResourceContainer());

        final StringBuilder builder = new StringBuilder();
        builder.append("Deallocate Action: Deallocate container group from provider '");
        builder.append(container.getInstanceType().getProvider().getName());
        builder.append("' of type '");
        builder.append(container.getInstanceType());
        builder.append("' in location '");
        builder.append(container.getInstanceType().getLocation());
        builder.append("' with name '");
        builder.append(ModelHelper.getGroupName(container));
        return builder.toString();
    }

}
