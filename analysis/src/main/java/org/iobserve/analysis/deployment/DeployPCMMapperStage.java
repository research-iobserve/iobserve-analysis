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
import org.iobserve.common.record.ObservationPoint;
import org.iobserve.common.record.Privacy;
import org.iobserve.common.record.ServletDeployedEvent;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.EServiceTechnology;
import org.iobserve.model.persistence.neo4j.DBException;
import org.iobserve.model.persistence.neo4j.InvocationException;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.stages.data.ExperimentLogging;
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
        ExperimentLogging.measure(event, ObservationPoint.CODE_TO_MODEL_ENTRY);
        this.logger.debug("Received deployment event {}", event);
        final ISOCountryCode countryCode;
        if (event instanceof Privacy) {
            countryCode = ((Privacy) event).getCountryCode();
        } else {
            countryCode = ISOCountryCode.EVIL_EMPIRE;
        }
        if (event instanceof ServletDeployedEvent) {
            final ServletDeployedEvent servletEvent = (ServletDeployedEvent) event;
            final String service = servletEvent.getService();
            final String context = servletEvent.getContext();
            this.performMapping(EServiceTechnology.SERVLET, service, context, countryCode, event.getTimestamp());
        } else if (event instanceof EJBDeployedEvent) {
            final EJBDeployedEvent ejbEvent = (EJBDeployedEvent) event;
            final String service = ejbEvent.getService();
            final String context = ejbEvent.getContext();
            this.performMapping(EServiceTechnology.EJB, service, context, countryCode, event.getTimestamp());
        } else {
            throw new InternalError("Deployment event type " + event.getClass().getCanonicalName() + " not supported.");
        }
        ExperimentLogging.measure(event, ObservationPoint.CODE_TO_MODEL_EXIT);
    }

    private void performMapping(final EServiceTechnology technology, final String service, final String context,
            final ISOCountryCode countryCode, final long timestamp) throws InvocationException, DBException {
        final List<AssemblyEntry> assemblyEntry = this.correspondenceModelResource.findObjectsByTypeAndName(
                AssemblyEntry.class, CorrespondencePackage.Literals.ASSEMBLY_ENTRY, "implementationId", context);

        // build the containerAllocationEvent
        final String urlContext = context.replaceAll("\\.", "/");
        final String url = "http://" + service + '/' + urlContext;

        if (assemblyEntry.size() == 1) {
            final AssemblyContext assemblyContext = this.systemModelResource
                    .resolve(assemblyEntry.get(0).getAssembly());
            this.outputPort
                    .send(new PCMDeployedEvent(technology, service, assemblyContext, url, countryCode, timestamp));

        } else if (assemblyEntry.isEmpty()) {
            this.logger.error("Deplyoment failed: No corresponding assembly context {} found on {}.", context, service);
        } else if (assemblyEntry.size() > 1) {
            this.logger.error("Deplyoment failed: Multiple corresponding assembly context {} found on {}.", context,
                    service);
        }
    }

}
