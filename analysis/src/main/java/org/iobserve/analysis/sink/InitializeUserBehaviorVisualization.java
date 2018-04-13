/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.sink;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.impl.ProvidedDelegationConnectorImpl;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * @author Josefine Wegert -- initial contribution
 * @author Reiner Jung
 *
 */
public class InitializeUserBehaviorVisualization {

    private final IModelProvider<UsageModel> usageModelProvider;
    private Object usageScenarios;
    private final IModelProvider<System> systemModelProvider;

    /**
     * Create the user bevahor visualization.
     *
     * @param systemModelGraphProvider
     *            system model provider
     * @param usageModelGraphProvider
     *            usage model provier
     */
    public InitializeUserBehaviorVisualization(
            final IModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final IModelProvider<UsageModel> usageModelGraphProvider) {
        this.systemModelProvider = systemModelGraphProvider;
        this.usageModelProvider = usageModelGraphProvider;
    }

    /**
     * Initialize the diagram.
     */
    public void initialize() {
        // TODO not working yet

        // map elements in entryLevelSystemCalls to assemblyContexts

        final List<AssemblyContext> userInvokedServices = new ArrayList<>();
        List<EntryLevelSystemCall> entryLevelSystemCalls = new ArrayList<>();

        entryLevelSystemCalls = this.collectEntryLevelSystemCalls(this.usageScenarios);

        for (int m = 0; m < entryLevelSystemCalls.size(); m++) {
            final EntryLevelSystemCall userStep = entryLevelSystemCalls.get(m);

            final String providedRoleId = userStep.getProvidedRole_EntryLevelSystemCall().getId();

            final List<EObject> usergroupConnectors = this.systemModelProvider
                    .readOnlyReferencingComponentsById(OperationProvidedRole.class, providedRoleId);
            final ProvidedDelegationConnectorImpl usergroupConnector = (ProvidedDelegationConnectorImpl) usergroupConnectors
                    .get(0);

            final AssemblyContext assemblyContext = usergroupConnector.getAssemblyContext_ProvidedDelegationConnector();

            userInvokedServices.add(assemblyContext);
        }

        if (!userInvokedServices.isEmpty()) { // NOCS NOPMD

            this.usageModelProvider.readComponentById(UsageModel.class, "0");
            // SendHttpRequest.post(Changelog.create(
            // this.usergroupService.createUsergroup(this.systemService.getSystemId(),
            // userInvokedServices)),
            // this.systemUrl, this.changelogUrl);
            // TODO fixme
        }

    }

    private List<EntryLevelSystemCall> collectEntryLevelSystemCalls(final Object usageScenarios2) {
        // TODO Auto-generated method stub
        return null;
    }
}
