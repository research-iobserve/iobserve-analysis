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
import org.iobserve.model.persistence.neo4j.IModelProvider;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * Maps technology dependent deploy events up to model level PCM deploy events.
 *
 * @author Reiner Jung
 *
 */
public class DeployPCMMapperStage extends AbstractConsumerStage<IDeployedEvent> {

    private final IModelProvider<AssemblyEntry> correspondenceModelProvider;
    private final IModelProvider<AssemblyContext> assemblyContextModelProvider;

    private final OutputPort<PCMDeployedEvent> outputPort = this.createOutputPort();

    /**
     * Create a deployed event mapper.
     *
     * @param correspondenceModelProvider
     *            correspondence model handler
     * @param assemblyContextModelProvider
     *            assembly context model provider
     */
    public DeployPCMMapperStage(final IModelProvider<AssemblyEntry> correspondenceModelProvider,
            final IModelProvider<AssemblyContext> assemblyContextModelProvider) {
        this.correspondenceModelProvider = correspondenceModelProvider;
        this.assemblyContextModelProvider = assemblyContextModelProvider;
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
    protected void execute(final IDeployedEvent event) throws Exception {
        this.logger.debug("Received deployment event {}", event);
        if (event instanceof ServletDeployedEvent) {
            this.servletMapper((ServletDeployedEvent) event);
        } else if (event instanceof EJBDeployedEvent) {
            this.ejbMapper((EJBDeployedEvent) event);
        }

    }

    private void ejbMapper(final EJBDeployedEvent event) {
        final String service = event.getService();
        final String context = event.getContext();

        this.performMapping(event, service, context);
    }

    private void servletMapper(final ServletDeployedEvent event) {
        final String service = event.getService();
        final String context = event.getContext();

        this.performMapping(event, service, context);
    }

    private void performMapping(final IDeployedEvent event, final String service, final String context) {
        final List<AssemblyEntry> assemblyEntry = this.correspondenceModelProvider
                .getObjectsByTypeAndName(AssemblyEntry.class, context);

        // build the containerAllocationEvent
        final String urlContext = context.replaceAll("\\.", "/");
        final String url = "http://" + service + '/' + urlContext;

        if (assemblyEntry.size() == 1) {
            final AssemblyContext assemblyContext = this.assemblyContextModelProvider
                    .getObjectByTypeAndId(AssemblyContext.class, assemblyEntry.get(0).getAssembly().getId());
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
