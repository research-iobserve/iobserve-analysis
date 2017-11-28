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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.TerminateAction;
import org.iobserve.planning.utils.ModelHelper;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunScriptOnNodesException;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Action script for terminating a group of cloud resource containers.
 *
 * @author Tobias Pöppke
 *
 */
public class TerminateActionScript extends AbstractActionScript {
    private static final Logger LOG = LogManager.getLogger();

    private final TerminateAction action;

    /**
     * Create a new terminate action script with the given data.
     *
     * @param data
     *            the data shared in the adaptation stage
     * @param action
     *            the action item to be executed
     */
    public TerminateActionScript(final AdaptationData data, final TerminateAction action) {
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
            TerminateActionScript.LOG.warn("Could not find script for node termination. No script will be executed.");
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
        builder.append("Terminate Action: Terminate container group from provider '");
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
