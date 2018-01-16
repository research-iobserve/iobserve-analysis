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

import org.iobserve.analysis.model.correspondence.Correspondent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * @author Reiner Jung
 *
 */
public class PCMUndeployedEvent {

    private final String service;
    private final Correspondent correspondent;
    private ResourceContainer resourceContainer;

    /**
     * Create an model level undeployed event.
     * 
     * @param service
     *            the deployed service
     * @param correspondent
     *            the correspondent for the component to be removed
     */
    public PCMUndeployedEvent(final String service, final Correspondent correspondent) {
        this.service = service;
        this.correspondent = correspondent;
    }

    public final String getService() {
        return this.service;
    }

    public final Correspondent getCorrespondent() {
        return this.correspondent;
    }

    public void setResourceContainer(final ResourceContainer resourceContainer) {
        this.resourceContainer = resourceContainer;
    }

    public final ResourceContainer getResourceContainer() {
        return this.resourceContainer;
    }
}
