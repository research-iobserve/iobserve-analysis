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

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.TerminateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory for constructing execution scripts from adaptation actions.
 *
 * @author Tobias Pöppke
 *
 * @since 0.0.2
 */
public class ActionScriptFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionScriptFactory.class);

    private final AdaptationData data;

    /**
     * Creates a new action script factory with the given shared data.
     *
     * @param data
     *            shared data of the adaptation stage
     */
    public ActionScriptFactory(final AdaptationData data) {
        this.data = data;
    }

    /**
     * Constructs a new execution script according to the type of the adaptation action.
     *
     * @param action
     *            the action for which an execution script should be created
     * @return the execution script
     * @throws IllegalArgumentException
     *             if the adaptation action could not be mapped to an execution script
     */
    public AbstractActionScript getExecutionScript(final Action action) throws IllegalArgumentException {
        if (action instanceof ChangeRepositoryComponentAction) {
            return this.createChangeRepositoryComponentActionScript((ChangeRepositoryComponentAction) action);
        } else if (action instanceof AllocateAction) {
            return this.createAllocateActionScript((AllocateAction) action);
        } else if (action instanceof DeallocateAction) {
            return this.createDeallocateActionScript((DeallocateAction) action);
        } else if (action instanceof MigrateAction) {
            return this.createMigrateActionScript((MigrateAction) action);
        } else if (action instanceof AcquireAction) {
            return this.createAcquireActionScript((AcquireAction) action);
        } else if (action instanceof TerminateAction) {
            return this.createTerminateActionScript((TerminateAction) action);
        } else if (action instanceof ReplicateAction) {
            return this.createReplicateActionScript((ReplicateAction) action);
        } else {
            final String errorMsg = String.format(
                    "Could not create action script for adaptationAction '%s', no suitable class could be found",
                    action);
            ActionScriptFactory.LOGGER.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    private AbstractActionScript createReplicateActionScript(final ReplicateAction adaptationAction) {
        return new ReplicateActionScript(this.data, adaptationAction);
    }

    private AbstractActionScript createTerminateActionScript(final TerminateAction adaptationAction) {
        return new TerminateActionScript(this.data, adaptationAction);
    }

    private AbstractActionScript createAcquireActionScript(final AcquireAction adaptationAction) {
        return new AcquireActionScript(this.data, adaptationAction);
    }

    private AbstractActionScript createMigrateActionScript(final MigrateAction adaptationAction) {
        return new MigrateActionScript(this.data, adaptationAction);
    }

    private AbstractActionScript createDeallocateActionScript(final DeallocateAction adaptationAction) {
        return new DeallocateActionScript(this.data, adaptationAction);
    }

    private AbstractActionScript createAllocateActionScript(final AllocateAction adaptationAction) {
        return new AllocateActionScript(this.data, adaptationAction);
    }

    private AbstractActionScript createChangeRepositoryComponentActionScript(
            final ChangeRepositoryComponentAction adaptationAction) {
        return new ChangeRepositoryComponentActionScript(this.data, adaptationAction);
    }
}
