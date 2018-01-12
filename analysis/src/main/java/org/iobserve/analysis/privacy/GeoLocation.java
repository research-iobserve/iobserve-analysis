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
import org.iobserve.analysis.model.provider.file.ResourceEnvironmentModelProvider;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;

/**
 * TODO describe me.
 *
 * @author unknown
 *
 */
public class GeoLocation extends AbstractConsumerStage<PCMDeployedEvent> {

    private final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;

    private final OutputPort<Boolean> outputPortSnapshot = this.createOutputPort();

    public GeoLocation(final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider) {
        this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
    }

    @Override
    protected void execute(final PCMDeployedEvent element) throws Exception {
        final ResourceEnvironment resourceEnvironment = this.resourceEnvironmentModelProvider.getModel();
        final EList<ResourceContainer> resContainers = resourceEnvironment.getResourceContainer_ResourceEnvironment();
        final Boolean makeSnapshot = false;

        for (final ResourceContainer resContainer : resContainers) {
            if (resContainer.getEntityName().equals(element.getService())
                    && resContainer instanceof ResourceContainerPrivacy) {

                final ResourceContainerPrivacy resContainerPrivacy = (ResourceContainerPrivacy) resContainer;
                final int geolocation = resContainerPrivacy.getGeolocation();
                if (geolocation != element.getCountryCode()) {
                    resContainerPrivacy.setGeolocation(element.getCountryCode());
                }
                break;
            }
        }
        this.outputPortSnapshot.send(makeSnapshot);
    }

    public OutputPort<PCMDeployedEvent> getOutputPort() {
        return null;
    }

}
