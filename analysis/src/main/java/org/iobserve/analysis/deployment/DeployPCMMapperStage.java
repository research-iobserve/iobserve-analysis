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

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.common.record.Privacy;
import org.iobserve.common.record.ServletDeployedEvent;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.persistence.neo4j.DBException;
import org.iobserve.model.persistence.neo4j.InvocationException;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.system.System;

/**
 * Maps technology dependent deploy events up to model level PCM deploy events.
 *
 * @author Reiner Jung
 *
 */
public class DeployPCMMapperStage extends AbstractConsumerStage<IDeployedEvent> {

    private final ModelResource<CorrespondenceModel> correspondenceModelResource;
    private final ModelResource<System> systemModelResource;

    private final OutputPort<PCMDeployedEvent> outputPort = this.createOutputPort();

    /**
     * Create a deployed event mapper.
     *
     * @param correspondenceModelResource
     *            correspondence model handler
     * @param systemModelResource
     *            assembly context model provider
     */
    public DeployPCMMapperStage(final ModelResource<CorrespondenceModel> correspondenceModelResource,
            final ModelResource<System> systemModelResource) {
        this.correspondenceModelResource = correspondenceModelResource;
        this.systemModelResource = systemModelResource;
    }

    /**
     * Output port the mapped events.
     *
     * @return returns the port for mapped events
     */
    public OutputPort<PCMDeployedEvent> getOutputPort() {
        return this.outputPort;
    }

    @Override
    protected void execute(final IDeployedEvent event) throws InvocationException, DBException {
        this.logger.debug("Received deployment event {}", event);
        if (event instanceof ServletDeployedEvent) {
            this.servletMapper((ServletDeployedEvent) event);
        } else if (event instanceof EJBDeployedEvent) {
            this.ejbMapper((EJBDeployedEvent) event);
        }

    }

    private void ejbMapper(final EJBDeployedEvent event) throws InvocationException, DBException {
        final String service = event.getService();
        final String context = event.getContext();

        this.performMapping(event, service, context);
    }

    private void servletMapper(final ServletDeployedEvent event) throws InvocationException, DBException {
        final String service = event.getService();
        final String context = event.getContext();

        this.performMapping(event, service, context);
    }

    private void performMapping(final IDeployedEvent event, final String service, final String context)
            throws InvocationException, DBException {
        final List<AssemblyEntry> assemblyEntry = this.correspondenceModelResource.findObjectsByTypeAndName(
                AssemblyEntry.class, CorrespondencePackage.Literals.ASSEMBLY_ENTRY, "implementationId", context);

        // build the containerAllocationEvent
        final String urlContext = context.replaceAll("\\.", "/");
        final String url = "http://" + service + '/' + urlContext;

        if (assemblyEntry.size() == 1) {
            final AssemblyContext assemblyContext = this.systemModelResource
                    .resolve(assemblyEntry.get(0).getAssembly());
            if (event instanceof Privacy) {
                this.logger.debug("privacy {}", event);
                this.outputPort
                        .send(new PCMDeployedEvent(service, assemblyContext, url, ((Privacy) event).getCountryCode()));
            } else {
                this.logger.debug("evil {}", event);
                this.outputPort.send(new PCMDeployedEvent(service, assemblyContext, url, ISOCountryCode.EVIL_EMPIRE));
            }
        } else if (assemblyEntry.isEmpty()) {
            this.logger.error("Deplyoment failed: No corresponding assembly context {} found on {}.", context, service);
        } else if (assemblyEntry.size() > 1) {
            this.logger.error("Deplyoment failed: Multiple corresponding assembly context {} found on {}.", context,
                    service);
        }
    }

}
