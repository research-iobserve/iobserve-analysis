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

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.eclipse.emf.common.util.EList;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;

/**
 * Update the geo location of a resource container for a given.
 *
 * @author unknown
 * @author Reiner Jung
 *
 */
public class GeoLocation extends AbstractConsumerStage<PCMDeployedEvent> {

    private final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider;

    private final OutputPort<PCMDeployedEvent> outputPort = this.createOutputPort();

    /**
     * Create a geo location filter.
     *
     * @param resourceEnvironmentModelProvider
     *            the corresponding resource environment
     */
    public GeoLocation(final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider) {
        this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
    }

    @Override
    protected void execute(final PCMDeployedEvent element) throws Exception {
        // TODO this might be better implemented using query functions addressing RescourceContainer
        final ResourceEnvironment resourceEnvironment = this.resourceEnvironmentModelProvider
                .readRootComponent(ResourceEnvironment.class);
        final EList<ResourceContainer> resContainers = resourceEnvironment.getResourceContainer_ResourceEnvironment();

        for (final ResourceContainer resContainer : resContainers) {
            if (resContainer.getEntityName().equals(element.getService())
                    && resContainer instanceof ResourceContainerPrivacy) {

                final ResourceContainerPrivacy resContainerPrivacy = (ResourceContainerPrivacy) resContainer;
                final int geolocation = resContainerPrivacy.getGeolocation();
                // TODO wait for update to Kieker 1.15 snapshot and then reactive code snippet
                // if (geolocation != element.getCountryCode().getValue()) {
                // resContainerPrivacy.setGeolocation(element.getCountryCode().getValue());
                // this.outputPort.send(element);
                // }
                break;
            }
        }
    }

    public OutputPort<PCMDeployedEvent> getOutputPort() {
        return this.outputPort;
    }

}
