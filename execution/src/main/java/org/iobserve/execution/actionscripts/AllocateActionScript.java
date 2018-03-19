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
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.compute.options.TemplateOptions.Builder;
import org.jclouds.scriptbuilder.domain.Statement;
import org.jclouds.scriptbuilder.statements.login.AdminAccess;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.VMType;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;

/**
 * Action script for allocating a new cloud resource container.
 *
 * @author Tobias Pöppke
 * @author Lars Blümke (terminology: "aquire" -> "allocate")
 *
 * @since 0.0.2
 */
public class AllocateActionScript extends AbstractActionScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllocateActionScript.class);

    private final AllocateAction action;

    /**
     * Create a new acquire action script with the given data.
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
    public void execute() throws RunNodesException {
        final ResourceContainer container = this.action.getSourceResourceContainer();

        final ResourceContainerCloud cloudContainer = this.getResourceContainerCloud(container);

        final ComputeService client = this.getComputeServiceForContainer(cloudContainer);

        final TemplateBuilder templateBuilder = client.templateBuilder();

        final VMType instanceType = cloudContainer.getInstanceType();

        templateBuilder.hardwareId(instanceType.getName());
        templateBuilder.locationId(instanceType.getLocation());
        // TODO maybe make this configurable
        templateBuilder.osFamily(OsFamily.UBUNTU);

        final Statement setupAdminInstructions = AdminAccess.standard();

        // Necessary to set hostname to allow mapping for later events
        final TemplateOptions options = Builder.runScript(setupAdminInstructions)
                .runScript(AllocateActionScript.getChangeHostnameScript(cloudContainer))
                .runScript(this.getStartupScript());
        templateBuilder.options(options);

        final Template template = templateBuilder.build();

        final String groupName = ModelHelper.getGroupName(cloudContainer);

        final NodeMetadata node = Iterables.getOnlyElement(client.createNodesInGroup(groupName, 1, template));

        AllocateActionScript.LOGGER.info(
                String.format("Allocated node for resource container '%s'. NodeID: %s, Hostname: %s, Adresses: %s",
                        cloudContainer.getEntityName(), node.getId(), node.getHostname(),
                        Iterables.concat(node.getPrivateAddresses(), node.getPublicAddresses())));

        // TODO write resource container to original model to enable mapping

    }

    private static String getChangeHostnameScript(final ResourceContainerCloud cloudContainer) {
        // This only works on a *nix OS and is valid until rebooting the node
        return String.format("hostname %s", cloudContainer.getEntityName());
    }

    private String getStartupScript() {
        final URI nodeStartupScriptURI = this.data.getDeployablesFolderURI()
                .appendSegment(AdaptationData.NODE_STARTUP_SCRIPT_NAME);

        try {
            return this.getFileContents(nodeStartupScriptURI);
        } catch (final IOException e) {
            // No script found, so we can not execute anything
            AllocateActionScript.LOGGER.warn("Could not find script for node startup. No script will be executed.");
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
        builder.append("Allocate Action: Allocate container from provider '");
        builder.append(container.getInstanceType().getProvider().getName());
        builder.append("' of type '");
        builder.append(container.getInstanceType());
        builder.append("' in location '");
        builder.append(container.getInstanceType().getLocation());
        builder.append('\'');
        return builder.toString();

    }
}
