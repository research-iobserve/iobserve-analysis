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

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;

/**
 * Action script for changing the repository component of an assembly context.
 *
 * Currently not supported automatically, so the execute method will raise an exception to trigger
 * operator interaction.
 *
 * @author Tobias Pöppke
 * @author Lars Blümke (Refactoring of system adaptation model: changes to sources and targets of
 *         actions)
 *
 */
public class ChangeRepositoryComponentActionScript extends AbstractActionScript {

    private final ChangeRepositoryComponentAction action;

    /**
     * Create a new change repository component action script with the given data.
     *
     * @param data
     *            the data shared in the adaptation stage
     * @param action
     *            the action item to be executed
     */
    public ChangeRepositoryComponentActionScript(final AdaptationData data,
            final ChangeRepositoryComponentAction action) {
        super(data);
        this.action = action;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException(
                "Automated change of repository components currently not possible. Please change the repository component manually!");
    }

    @Override
    public boolean isAutoExecutable() {
        return false;
    }

    @Override
    public String getDescription() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Change repository component Action: Change repository component of assembly context '");
        builder.append(this.action.getSourceAllocationContext().getAssemblyContext_AllocationContext().getEntityName());
        builder.append("' to component '");
        builder.append(this.action.getTargetAllocationContext().getAssemblyContext_AllocationContext()
                .getEncapsulatedComponent__AssemblyContext().getEntityName());
        builder.append('\'');
        return builder.toString();
    }

}
