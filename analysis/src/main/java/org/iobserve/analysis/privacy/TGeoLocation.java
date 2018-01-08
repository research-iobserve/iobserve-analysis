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
import org.iobserve.analysis.model.provider.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.common.record.IDeployed;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;

/**
 * TODO describe me.
 *
 * @author unknown
 *
 */
public class TGeoLocation extends AbstractConsumerStage<IDeployed> {

    private final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;

    private final OutputPort<Boolean> outputPortSnapshot = this.createOutputPort();

    public TGeoLocation(final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider) {
        this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
    }

    @Override
    protected void execute(final IDeployed element) throws Exception {
        final ResourceEnvironment resourceEnvironment = this.resourceEnvironmentModelProvider.getModel();
        final EList<ResourceContainer> resContainers = resourceEnvironment.getResourceContainer_ResourceEnvironment();
        Boolean makeSnapshot = false;

        for (final ResourceContainer resContainer : resContainers) {
            if (resContainer.getEntityName().equals(element.getHostname())
                    && resContainer instanceof ResourceContainerPrivacy) {

                final ResourceContainerPrivacy resContainerPrivacy = (ResourceContainerPrivacy) resContainer;
                final int geolocation = resContainerPrivacy.getGeolocation();
                if (geolocation != element.getCountryCode()) {
                    resContainerPrivacy.setGeolocation(element.getCountryCode());
                    makeSnapshot = true;
                    SnapshotBuilder.setSnapshotFlag();
                }
                break;
            }
        }
        this.outputPortSnapshot.send(makeSnapshot);
    }

    /**
     * @return output port for snapshot
     */
    public OutputPort<Boolean> getOutputPortSnapshot() {
        return this.outputPortSnapshot;
    }

}
