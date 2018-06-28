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

import java.util.List;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.common.record.EJBUndeployedEvent;
import org.iobserve.common.record.IUndeployedEvent;
import org.iobserve.common.record.ServletUndeployedEvent;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Maps technology dependent undeploy events onto model level PCM undeploy events.
 *
 * @author Reiner Jung
 *
 */
public class UndeployPCMMapperStage extends AbstractConsumerStage<IUndeployedEvent> {

    private final IModelProvider<AssemblyEntry> correspondenceModelProvider;
    private final IModelProvider<AssemblyContext> assemblyContextModelProvider;
    private final IModelProvider<ResourceContainer> resourceContainerModelProvider;

    private final OutputPort<PCMUndeployedEvent> outputPort = this.createOutputPort();

    /**
     * Creates and undeploy event mapper.
     *
     * @param correspondenceModelProvider
     *            correspondence model handler
     * @param assemblyContextModelProvider
     *            assembly context model provider
     * @param resourceContainerModelProvider
     *            resource container model provider
     */
    public UndeployPCMMapperStage(final IModelProvider<AssemblyEntry> correspondenceModelProvider,
            final IModelProvider<AssemblyContext> assemblyContextModelProvider,
            final IModelProvider<ResourceContainer> resourceContainerModelProvider) {
        this.correspondenceModelProvider = correspondenceModelProvider;
        this.assemblyContextModelProvider = assemblyContextModelProvider;
        this.resourceContainerModelProvider = resourceContainerModelProvider;
    }

    public OutputPort<PCMUndeployedEvent> getOutputPort() {
        return this.outputPort;
    }

    @Override
    protected void execute(final IUndeployedEvent event) throws Exception {
        this.logger.debug("received undeployment event {}", event);
        if (event instanceof ServletUndeployedEvent) {
            this.servletMapper((ServletUndeployedEvent) event);
        } else if (event instanceof EJBUndeployedEvent) {
            this.ejbMapper((EJBUndeployedEvent) event);
        }
    }

    private void servletMapper(final ServletUndeployedEvent event) {
        final String service = event.getService();
        final String context = event.getContext();

        this.performMapping(service, context);
    }

    private void ejbMapper(final EJBUndeployedEvent event) {
        final String service = event.getService();
        final String context = event.getContext();

        this.performMapping(service, context);

    }

    private void performMapping(final String service, final String context) {
        final List<AssemblyEntry> assemblyEntry = this.correspondenceModelProvider
                .getObjectsByTypeAndName(AssemblyEntry.class, context);

        final List<ResourceContainer> resourceContainers = this.resourceContainerModelProvider
                .getObjectsByTypeAndName(ResourceContainer.class, service);

        if (assemblyEntry.size() == 1) {
            final ResourceContainer resourceContainer = resourceContainers.get(0);
            final AssemblyContext assemblyContext = this.assemblyContextModelProvider
                    .getObjectByTypeAndId(AssemblyContext.class, assemblyEntry.get(0).getAssembly().getId());
            this.outputPort.send(new PCMUndeployedEvent(service, assemblyContext, resourceContainer));
        } else if (assemblyEntry.isEmpty()) {
            this.logger.error("Undeplyoment failed: No corresponding assembly context {} found on {}.", context,
                    service);
        } else if (assemblyEntry.size() > 1) {
            this.logger.error("Undeplyoment failed: Multiple corresponding assembly contexts {} found on {}.", context,
                    service);
        }
    }

}
