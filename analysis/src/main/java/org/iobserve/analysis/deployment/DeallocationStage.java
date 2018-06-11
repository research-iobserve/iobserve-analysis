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
package org.iobserve.analysis.deployment;

import java.rmi.activation.UnknownObjectException;
import java.util.Optional;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IDeallocationEvent;
import org.iobserve.model.factory.ResourceEnvironmentModelFactory;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * @author Reiner Jung
 *
 * @since 0.0.3
 *
 */
public class DeallocationStage extends AbstractConsumerStage<IDeallocationEvent> {

    private final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider;

    /** Relay allocation event. */
    private final OutputPort<IDeallocationEvent> deallocationOutputPort = this.createOutputPort();
    /** notify that the allocation was successful port, sends ResourceContainer. */
    private final OutputPort<ResourceContainer> deallocationNotifyOutputPort = this.createOutputPort();

    /**
     * Create a stage managing deallocation.
     *
     * @param resourceEnvironmentModelProvider
     *            resource environment model
     */
    public DeallocationStage(final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider) {
        this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
    }

    @Override
    protected void execute(final IDeallocationEvent event) throws Exception {
        final String service;
        if (event instanceof ContainerAllocationEvent) {
            service = ((ContainerAllocationEvent) event).getService();
        } else {
            throw new UnknownObjectException(event.getClass() + " is not supported by the allocation filter.");
        }
        final Optional<ResourceContainer> resourceContainer = ResourceEnvironmentModelFactory
                .getResourceContainerByName(
                        this.resourceEnvironmentModelProvider.readRootNode(ResourceEnvironment.class),
                        service);

        if (resourceContainer.isPresent()) {
            /** new provider: update the resource environment graph. */
            final ResourceEnvironment resourceEnvironmentModelGraph = this.resourceEnvironmentModelProvider
                    .readRootNode(ResourceEnvironment.class);
            resourceEnvironmentModelGraph.getResourceContainer_ResourceEnvironment().remove(resourceContainer.get());
            this.resourceEnvironmentModelProvider.updateObject(ResourceEnvironment.class,
                    resourceEnvironmentModelGraph);

            /** signal allocation update. */
            this.deallocationNotifyOutputPort.send(resourceContainer.get());
            this.deallocationOutputPort.send(event);
        } else {
            /** error deallocation already happened. */
            this.logger.error("ResourceContainer {} is missing.", service);
        }
    }

    /**
     * @return the allocationOutputPort
     */
    public OutputPort<IDeallocationEvent> getDeallocationOutputPort() {
        return this.deallocationOutputPort;
    }

    /**
     * @return allocationFinishedOutputPort
     */
    public OutputPort<ResourceContainer> getDeallocationNotifyOutputPort() {
        return this.deallocationNotifyOutputPort;
    }

}
