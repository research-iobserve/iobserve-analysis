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
package org.iobserve.analysis.filter;

import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.utils.ExecutionTimeLogger;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.IAllocationRecord;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * This class processes allocation events. TAllocation creates a new resource container if and only
 * if there is no corresponding container already available.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class TAllocation extends AbstractConsumerStage<IAllocationRecord> {

    /** reference to {@link ResourceEnvironment} provider. */
    private final ResourceEnvironmentModelProvider resourceEnvModelProvider;

    /** output ports. */
    private final OutputPort<IAllocationRecord> allocationOutputPort = this.createOutputPort();
    private final OutputPort<IAllocationRecord> allocationFinishedOutputPort = this.createOutputPort();

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param resourceEvnironmentModelProvider
     *            the resource environment model provider
     */
    public TAllocation(final ResourceEnvironmentModelProvider resourceEvnironmentModelProvider) {
        this.resourceEnvModelProvider = resourceEvnironmentModelProvider;
    }

    /**
     * @return the allocationOutputPort
     */
    public OutputPort<IAllocationRecord> getAllocationOutputPort() {
        return this.allocationOutputPort;
    }

    /**
     *
     * @return allocationFinishedOutputPort
     */
    public OutputPort<IAllocationRecord> getAllocationFinishedOutputPort() {
        return this.allocationFinishedOutputPort;
    }

    /**
     * This method is triggered for every deployment event.
     *
     * @param event
     *            one deployment event to be processed
     */
    @Override
    protected void execute(final IAllocationRecord event) {
        ExecutionTimeLogger.getInstance().startLogging(event);

        System.out.printf("structure of IAllocationRecord: %s", event.toString());
        final String serverNames = event.getValueNames().toString();
        System.out.printf("serverName in TAllocation: %s", serverNames);
        // this.updateModel(serverName);

        // if (event instanceof ServletDeployedEvent) {
        // final String serivce = ((ServletDeployedEvent) event).getSerivce();
        // this.updateModel(serivce);
        // } else if (event instanceof EJBDeployedEvent) {
        // final String service = ((EJBDeployedEvent) event).getSerivce();
        // this.updateModel(service);
        // }

        ExecutionTimeLogger.getInstance().stopLogging(event);

        // forward the event
        this.allocationFinishedOutputPort.send(event);
    }

    /**
     * Update the allocation model with the given server-name if necessary.
     *
     * @param serverName
     *            server name
     */
    private void updateModel(final String serverName) {

        Opt.of(ResourceEnvironmentModelBuilder.getResourceContainerByName(this.resourceEnvModelProvider.getModel(),
                serverName)).ifNotPresent().apply(() -> {
                    TAllocation.this.resourceEnvModelProvider.loadModel();
                    final ResourceEnvironment model = TAllocation.this.resourceEnvModelProvider.getModel();
                    ResourceEnvironmentModelBuilder.createResourceContainer(model, serverName);
                    TAllocation.this.resourceEnvModelProvider.save();
                }).elseApply(serverNamePresent -> System.out.printf("ResourceContainer %s was available.", serverName));
    }

}
