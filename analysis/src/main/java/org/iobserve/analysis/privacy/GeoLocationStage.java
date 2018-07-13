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
package org.iobserve.analysis.privacy;

import java.util.List;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.privacy.EISOCode;
import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.PrivacyFactory;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.PrivacyPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Update the geo location of a resource container for a given.
 *
 * @author unknown
 * @author Reiner Jung
 *
 */
public class GeoLocationStage extends AbstractConsumerStage<PCMDeployedEvent> {

    private final OutputPort<PCMDeployedEvent> outputPort = this.createOutputPort();

    private final ModelResource privacyModelResource;

    /**
     * Create a geo location filter.
     *
     * @param privacyModelResource
     *            privacy model resource
     */
    public GeoLocationStage(final ModelResource privacyModelResource) {
        this.privacyModelResource = privacyModelResource;
    }

    @Override
    protected void execute(final PCMDeployedEvent event) throws Exception {
        this.logger.debug("event received assmeblyContext={} countryCode={} resourceContainer={} service={} url={}",
                event.getAssemblyContext().getEntityName(), event.getCountryCode(),
                event.getResourceContainer().getEntityName(), event.getService(), event.getUrl());
        final List<GeoLocation> geoLocations = this.privacyModelResource.collectAllObjectsByType(GeoLocation.class,
                PrivacyPackage.Literals.GEO_LOCATION);

        for (final GeoLocation geoLocation : geoLocations) {
            final String containerId = geoLocation.getResourceContainer().getId();
            if (event.getResourceContainer().getId().equals(containerId)) {
                geoLocation.setIsocode(EISOCode.get(event.getCountryCode().getValue()));
                this.privacyModelResource.updatePartition(geoLocation);
                /** container has already a location: update the location. */
                this.outputPort.send(event);
                return;
            }
        }

        /** container has no geolocation: create a location instance. */
        final PrivacyModel privacyModel = this.privacyModelResource.getModelRootNode(PrivacyModel.class,
                PrivacyPackage.Literals.PRIVACY_MODEL);
        privacyModel.getResourceContainerLocations()
                .add(this.createGeoLocation(event.getResourceContainer(), event.getCountryCode()));
        this.privacyModelResource.updatePartition(privacyModel);

        this.outputPort.send(event);
    }

    private GeoLocation createGeoLocation(final ResourceContainer resourceContainer, final ISOCountryCode countryCode) {
        final GeoLocation geoLocation = PrivacyFactory.eINSTANCE.createGeoLocation();
        geoLocation.setIsocode(EISOCode.get(countryCode.getValue()));
        geoLocation.setResourceContainer(resourceContainer);

        return geoLocation;
    }

    public OutputPort<PCMDeployedEvent> getOutputPort() {
        return this.outputPort;
    }

}
