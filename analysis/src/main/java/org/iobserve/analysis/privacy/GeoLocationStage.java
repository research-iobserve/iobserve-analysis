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

import org.iobserve.analysis.deployment.DeploymentLock;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.IModelResource;
import org.iobserve.model.persistence.neo4j.InvocationException;
import org.iobserve.model.privacy.EISOCode;
import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.PrivacyFactory;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.PrivacyPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * Update the geo location of a resource container for a given.
 *
 * @author Reiner Jung
 *
 */
public class GeoLocationStage extends AbstractConsumerStage<PCMDeployedEvent> {

    private final OutputPort<PCMDeployedEvent> outputPort = this.createOutputPort();

    private final IModelResource<PrivacyModel> privacyModelResource;

    private final IModelResource<ResourceEnvironment> resourceEnvironmentResource;

    /**
     * Create a geo location filter.
     *
     * @param resourceEnvironmentResource
     *            resource environment resource
     * @param privacyModelResource
     *            privacy model resource
     */
    public GeoLocationStage(final IModelResource<ResourceEnvironment> resourceEnvironmentResource,
            final IModelResource<PrivacyModel> privacyModelResource) {
        this.resourceEnvironmentResource = resourceEnvironmentResource;
        this.privacyModelResource = privacyModelResource;
    }

    @Override
    protected void execute(final PCMDeployedEvent event) throws Exception {
        DeploymentLock.lock();
        this.logger.debug("event received assmeblyContext={} countryCode={} resourceContainer={} service={} url={}",
                event.getAssemblyContext().getEntityName(), event.getCountryCode(),
                event.getResourceContainer().getEntityName(), event.getService(), event.getUrl());

        final GeoLocation geoLocation = this.getGeoLocationForContainer(event.getResourceContainer());
        if (geoLocation == null) {
            /** create a geo location. */
            final PrivacyModel privacyModel = this.privacyModelResource.getModelRootNode(PrivacyModel.class,
                    PrivacyPackage.Literals.PRIVACY_MODEL);
            privacyModel.getResourceContainerLocations()
                    .add(this.createGeoLocation(event.getResourceContainer(), event.getCountryCode()));
            this.privacyModelResource.updatePartition(privacyModel);
        } else {
            /** update the geo location. */
            geoLocation.setIsocode(EISOCode.get(event.getCountryCode().getValue()));
            this.privacyModelResource.updatePartition(geoLocation);
        }

        DeploymentLock.unlock();
        this.outputPort.send(event);
    }

    /**
     * Get the geolocation for the given resource container.
     *
     * @param resourceContainer
     *            resource container
     * @return returns the geolocation object or null
     * @throws InvocationException
     * @throws DBException
     */
    private GeoLocation getGeoLocationForContainer(final ResourceContainer resourceContainer)
            throws InvocationException, DBException {
        final List<GeoLocation> geoLocations = this.privacyModelResource.collectAllObjectsByType(GeoLocation.class,
                PrivacyPackage.Literals.GEO_LOCATION);

        for (final GeoLocation geoLocation : geoLocations) {
            final ResourceContainer existingContainer = this.resourceEnvironmentResource
                    .resolve(geoLocation.getResourceContainer());
            if (existingContainer != null) {
                if (resourceContainer.getId().equals(existingContainer.getId())) { // NOPMD
                                                                                   // existingContainer
                                                                                   // could be null
                    return geoLocation;
                }
            }
        }

        return null;
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
