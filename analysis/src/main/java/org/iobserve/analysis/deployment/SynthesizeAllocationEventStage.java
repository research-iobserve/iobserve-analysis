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

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.model.factory.ResourceEnvironmentModelFactory;
import org.iobserve.analysis.model.provider.neo4j.ModelProvider;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationEvent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * The filter checks if there exists an ResourceContainer for the deployment specified in the
 * PCMDeployedEvent. In case it exists the ResourceContainer is added to the PCMDeployedEvent. In
 * case no such container exists, an allocation event is generated and send to the allocation event
 * output port and the incomplete PCMDeployedEvent is relayed via the deployedRelayOutputPort.
 *
 * Ports:
 * <ul>
 * <li>allocationOutputPort = synthesized allocation event</li>
 * <li>deployedOutputPort = updated PCMDeployedEvent</li>
 * <li>deployedRelayOutputPort = the unchanged PCMDeployedEvent</li>
 * </ul>
 *
 * @author Reiner Jung
 *
 */
public class SynthesizeAllocationEventStage extends AbstractConsumerStage<PCMDeployedEvent> {

    /** reference to resource environment model provider. */
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

    private final OutputPort<IAllocationEvent> allocationOutputPort = this.createOutputPort();
    private final OutputPort<PCMDeployedEvent> deployedOutputPort = this.createOutputPort();
    private final OutputPort<PCMDeployedEvent> deployedRelayOutputPort = this.createOutputPort();

    /**
     * Create an allocation event synthesizer stage.
     *
     * @param resourceEnvironmentModelGraphProvider
     *            the resource environment which is tested for a proper allocation
     */
    public SynthesizeAllocationEventStage(
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider) {
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
    }

    @Override
    protected void execute(final PCMDeployedEvent event) throws Exception {
        final ResourceContainer resourceContainer = ResourceEnvironmentModelFactory.getResourceContainerByName(
                this.resourceEnvironmentModelGraphProvider.readOnlyRootComponent(ResourceEnvironment.class),
                event.getService()).get();

        if (resourceContainer != null) {
            /** execution environment exists. Can deploy. */
            event.setResourceContainer(resourceContainer);
            this.deployedOutputPort.send(event);
        } else {
            /**
             * If the resource container with this serverName is not available, send an event to
             * TAllocation (creating the resource container) and forward the deployment event to
             * TDeployment (deploying on created resource container).
             */
            this.allocationOutputPort.send(new ContainerAllocationEvent(event.getUrl()));
            this.deployedRelayOutputPort.send(event);
        }
    }

    /**
     * @return allocationOutputPort
     */
    public OutputPort<IAllocationEvent> getAllocationOutputPort() {
        return this.allocationOutputPort;
    }

    /**
     * @return deployedOutputPort
     */
    public OutputPort<PCMDeployedEvent> getDeployedOutputPort() {
        return this.deployedOutputPort;
    }

    public OutputPort<PCMDeployedEvent> getRelayDeployedOutputPort() {
        return this.deployedRelayOutputPort;
    }

}
