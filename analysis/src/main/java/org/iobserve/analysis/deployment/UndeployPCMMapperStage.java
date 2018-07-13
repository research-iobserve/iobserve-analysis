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
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

/**
 * Maps technology dependent undeploy events onto model level PCM undeploy events.
 *
 * @author Reiner Jung
 *
 */
public class UndeployPCMMapperStage extends AbstractConsumerStage<IUndeployedEvent> {

    private final ModelResource correspondenceModelResource;
    private final ModelResource systemModelResource;
    private final ModelResource resourceEnvironmentResource;

    private final OutputPort<PCMUndeployedEvent> outputPort = this.createOutputPort();

    /**
     * Creates and undeploy event mapper.
     *
     * @param correspondenceModelResource
     *            correspondence model resource
     * @param systemModelResource
     *            assembly context model resource
     * @param resourceEnvironmentResource
     *            resource container model resource
     */
    public UndeployPCMMapperStage(final ModelResource correspondenceModelResource,
            final ModelResource systemModelResource, final ModelResource resourceEnvironmentResource) {
        this.correspondenceModelResource = correspondenceModelResource;
        this.systemModelResource = systemModelResource;
        this.resourceEnvironmentResource = resourceEnvironmentResource;
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
        final List<AssemblyEntry> assemblyEntry = this.correspondenceModelResource.findObjectsByTypeAndName(
                AssemblyEntry.class, CorrespondencePackage.Literals.ASSEMBLY_ENTRY, "entityName", context);

        final List<ResourceContainer> resourceContainers = this.resourceEnvironmentResource.findObjectsByTypeAndName(
                ResourceContainer.class, ResourceenvironmentPackage.Literals.RESOURCE_CONTAINER, "entityName", service);

        if (assemblyEntry.size() == 1) {
            final ResourceContainer resourceContainer = resourceContainers.get(0);
            final AssemblyContext assemblyContext = this.systemModelResource.findObjectByTypeAndId(
                    AssemblyContext.class, CompositionPackage.Literals.ASSEMBLY_CONTEXT,
                    this.systemModelResource.getInternalId(assemblyEntry.get(0).getAssembly()));
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
