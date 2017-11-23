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

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.utils.ModelHelper;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;

/**
 * Action script for replicating an assembly context.
 *
 * Currently not supported automatically, so the execute method will raise an exception to trigger
 * operator interaction.
 *
 * @author Tobias Pöppke
 *
 */
public class ReplicateActionScript extends AbstractActionScript {

    private final ReplicateAction action;

    /**
     * Create a new replicate action script with the given data.
     *
     * @param data
     *            the data shared in the adaptation stage
     * @param action
     *            the action item to be executed
     */
    public ReplicateActionScript(final AdaptationData data, final ReplicateAction action) {
        super(data);
        this.action = action;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException(
                "Automated replication is currently not possible. Please use the acquire and allocate actions to replicate components!");
    }

    @Override
    public boolean isAutoExecutable() {
        return false;
    }

    @Override
    public String getDescription() {
        final ResourceContainerCloud sourceContainer = this
                .getResourceContainerCloud(this.action.getSourceResourceContainer());
        final ResourceContainerCloud targetContainer = this
                .getResourceContainerCloud(this.action.getNewResourceContainer());

        final StringBuilder builder = new StringBuilder();
        builder.append("Replicate Action: Replicate container from provider '");
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
