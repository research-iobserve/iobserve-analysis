/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.deployment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.model.builder.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.IAllocationEvent;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * This class processes allocation events. TAllocation creates a new {@link ResourceContainer} if
 * and only if there is no corresponding container already available.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Josefine Wegert
 * @author Reiner Jung
 */
public final class AllocationStage extends AbstractConsumerStage<IAllocationEvent> {

    /** reference to {@link ResourceEnvironment} provider. */
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

    /** Relay allocation event. */
    private final OutputPort<IAllocationEvent> allocationOutputPort = this.createOutputPort();
    /** notify that the allocation was successful port, sends ResourceContainer. */
    private final OutputPort<ResourceContainer> allocationNotifyOutputPort = this.createOutputPort();

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param resourceEnvironmentModelGraphProvider
     *            the resource environment model
     */
    public AllocationStage(final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider) {
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
    }

    /**
     * @return the allocationOutputPort
     */
    public OutputPort<IAllocationEvent> getAllocationOutputPort() {
        return this.allocationOutputPort;
    }

    /**
     * @return allocationFinishedOutputPort
     */
    public OutputPort<ResourceContainer> getAllocationNotifyOutputPort() {
        return this.allocationNotifyOutputPort;
    }

    /**
     * This method is triggered for every allocation event.
     *
     * @param event
     *            one allocation event to be processed
     * @throws MalformedURLException
     *             malformed url exception
     */
    @Override
    protected void execute(final IAllocationEvent event) throws MalformedURLException {
        final URL url = new URL(event.toArray()[0].toString());
        final String hostName = url.getHost();

        final ResourceContainer resourceContainer = ResourceEnvironmentModelBuilder.getResourceContainerByName(
                this.resourceEnvironmentModelGraphProvider.readOnlyRootComponent(ResourceEnvironment.class), hostName)
                .get();

        if (resourceContainer == null) {
            /** new provider: update the resource environment graph. */
            final ResourceEnvironment resourceEnvironmentModelGraph = this.resourceEnvironmentModelGraphProvider
                    .readOnlyRootComponent(ResourceEnvironment.class);
            ResourceEnvironmentModelBuilder.createResourceContainer(resourceEnvironmentModelGraph, hostName);
            this.resourceEnvironmentModelGraphProvider.updateComponent(ResourceEnvironment.class,
                    resourceEnvironmentModelGraph);

            /** signal allocation update. */
            this.allocationNotifyOutputPort.send(resourceContainer);
            this.allocationOutputPort.send(event);
        } else {
            /** error allocation already happened. */
            this.logger.debug("ResourceContainer %s was available." + hostName);
            final List<ProcessingResourceSpecification> procResSpec = resourceContainer
                    .getActiveResourceSpecifications_ResourceContainer();
            for (int i = 0; i < procResSpec.size(); i++) {
                final String nodeGroupName = procResSpec.get(i).getActiveResourceType_ActiveResourceSpecification()
                        .getEntityName();
                this.logger.debug(nodeGroupName);
            }

            /** Notify with existing container. */
            this.allocationNotifyOutputPort.send(resourceContainer);
        }
    }

}
