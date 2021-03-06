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
package org.iobserve.analysis.deployment.data;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * @author Reiner Jung
 *
 */
public class PCMUndeployedEvent implements IPCMDeploymentEvent {

    private final String service;
    private final AssemblyContext assemblyContext;
    private final ResourceContainer resourceContainer;
    private final long timestamp;

    /**
     * Create an model level undeployed event.
     *
     * @param service
     *            the deployed service
     * @param assemblyContext
     *            the correspondent for the component to be removed
     * @param resourceContainer
     *            resource container
     * @param timestamp
     *            observation timestamp
     */
    public PCMUndeployedEvent(final String service, final AssemblyContext assemblyContext,
            final ResourceContainer resourceContainer, final long timestamp) {
        this.service = service;
        this.assemblyContext = assemblyContext;
        this.resourceContainer = resourceContainer;
        this.timestamp = timestamp;
    }

    @Override
    public final String getService() {
        return this.service;
    }

    @Override
    public final AssemblyContext getAssemblyContext() {
        return this.assemblyContext;
    }

    @Override
    public final ResourceContainer getResourceContainer() {
        return this.resourceContainer;
    }

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }
}
