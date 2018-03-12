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

import org.iobserve.model.correspondence.Correspondent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * @author Reiner Jung
 *
 */
public class PCMDeployedEvent {

    private final String service;
    private final Correspondent correspondent;
    private final String url;
    private ResourceContainer resourceContainer;
    private final short countryCode; // NOPMD country code is short

    /**
     * Create a deployment event which initialized values for service, correspondent, url, and
     * countryCode.
     *
     * @param service
     *            the service on which the component is deployed
     * @param correspondent
     *            the corresponding elemen in the PCM
     * @param url
     *            the service URL
     * @param countryCode
     *            the country code of the service in case that is available.
     */
    public PCMDeployedEvent(final String service, final Correspondent correspondent, final String url,
            final short countryCode) { // NOPMD country code is short
        this.service = service;
        this.correspondent = correspondent;
        this.url = url;
        this.countryCode = countryCode;
    }

    public final String getService() {
        return this.service;
    }

    public final Correspondent getCorrespondent() {
        return this.correspondent;
    }

    public final String getUrl() {
        return this.url;
    }

    public void setResourceContainer(final ResourceContainer resourceContainer) {
        this.resourceContainer = resourceContainer;
    }

    public final ResourceContainer getResourceContainer() {
        return this.resourceContainer;
    }

    public final short getCountryCode() { // NOPMD country code is short
        return this.countryCode;
    }

}
