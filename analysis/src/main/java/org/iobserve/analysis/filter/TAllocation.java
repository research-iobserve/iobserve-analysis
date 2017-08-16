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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.utils.ExecutionTimeLogger;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.IAllocationRecord;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * This class processes allocation events. TAllocation creates a new {@link ResourceContainer} if
 * and only if there is no corresponding container already available.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author jweg
 */
public final class TAllocation extends AbstractConsumerStage<IAllocationRecord> {

    /** reference to {@link ResourceEnvironment} provider. */
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

    /** output ports */
    private final OutputPort<IAllocationRecord> allocationOutputPort = this.createOutputPort();
    private final OutputPort<IAllocationRecord> allocationFinishedOutputPort = this.createOutputPort();

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param resourceEnvironmentModelGraphProvider
     *            the resource environment model
     */
    public TAllocation(final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider) {
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
    }

    /**
     * @return the allocationOutputPort
     */
    public OutputPort<IAllocationRecord> getAllocationOutputPort() {
        return this.allocationOutputPort;
    }

    /**
     * @return allocationFinishedOutputPort
     */
    public OutputPort<IAllocationRecord> getAllocationFinishedOutputPort() {
        return this.allocationFinishedOutputPort;
    }

    /**
     * This method is triggered for every allocation event.
     *
     * @param event
     *            one allocation event to be processed
     * @throws MalformedURLException
     */
    @Override
    protected void execute(final IAllocationRecord event) throws MalformedURLException {

        ExecutionTimeLogger.getInstance().startLogging(event);

        final URL url = new URL(event.toArray()[0].toString());
        final String hostName = url.getHost();
        this.updateModel(hostName, event);

        ExecutionTimeLogger.getInstance().stopLogging(event);

        // signal allocation finished
        this.allocationFinishedOutputPort.send(event);
    }

    /**
     * Update the resource environment model with the given server-name if necessary.
     *
     * @param serverName
     *            server name
     * @param event
     *            allocation event
     */
    private void updateModel(final String serverName, final IAllocationRecord event) {

        final Optional<ResourceContainer> optResourceContainer = ResourceEnvironmentModelBuilder
                .getResourceContainerByName(
                        this.resourceEnvironmentModelGraphProvider.readOnlyRootComponent(ResourceEnvironment.class),
                        serverName);

        Opt.of(optResourceContainer).ifNotPresent().apply(() -> {
            // new provider: update the resource environment graph
            final ResourceEnvironment resourceEnvironmentModelGraph = this.resourceEnvironmentModelGraphProvider
                    .readOnlyRootComponent(ResourceEnvironment.class);
            ResourceEnvironmentModelBuilder.createResourceContainer(resourceEnvironmentModelGraph, serverName);
            this.resourceEnvironmentModelGraphProvider.updateComponent(ResourceEnvironment.class,
                    resourceEnvironmentModelGraph);

            // signal allocation update
            this.allocationOutputPort.send(event);
        }).elseApply(serverNamePresent -> {
            System.out.printf("ResourceContainer %s was available.\n", serverName);
            final List<ProcessingResourceSpecification> procResSpec = serverNamePresent
                    .getActiveResourceSpecifications_ResourceContainer();
            for (int i = 0; i < procResSpec.size(); i++) {
                final String nodeGroupName = procResSpec.get(i).getActiveResourceType_ActiveResourceSpecification()
                        .getEntityName();
                System.out.println(nodeGroupName);
            }
        });

    }

}
