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

import org.iobserve.common.record.ISOCountryCode;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * @author Reiner Jung
 *
 */
public class PCMDeployedEvent implements IPCMDeploymentEvent {

    private final String service;
    private final AssemblyContext assemblyContext;
    private final String url;
    private ResourceContainer resourceContainer;
    private final ISOCountryCode countryCode;
    private final long timestamp;

    /**
     * Create a deployment event which initialized values for service, correspondent, url, and
     * countryCode.
     *
     * @param service
     *            the service on which the component is deployed
     * @param assemblyContext
     *            the corresponding element in the PCM
     * @param url
     *            the service URL
     * @param isoCountryCode
     *            the country code of the service in case that is available.
     * @param timestamp
     *            observation timestamp
     */
    public PCMDeployedEvent(final String service, final AssemblyContext assemblyContext, final String url,
            final ISOCountryCode isoCountryCode, final long timestamp) {
        this.service = service;
        this.assemblyContext = assemblyContext;
        this.url = url;
        this.countryCode = isoCountryCode;
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

    public final String getUrl() {
        return this.url;
    }

    public void setResourceContainer(final ResourceContainer resourceContainer) {
        this.resourceContainer = resourceContainer;
    }

    @Override
    public final ResourceContainer getResourceContainer() {
        return this.resourceContainer;
    }

    public final ISOCountryCode getCountryCode() {
        return this.countryCode;
    }

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }

}
