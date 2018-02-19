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
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.Privacy_EJBDeployedEvent;
import org.iobserve.common.record.Privacy_ServletDeployedEvent;
import org.iobserve.common.record.ServletDeployedEvent;
import org.iobserve.model.correspondence.Correspondent;
import org.iobserve.model.correspondence.ICorrespondence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps technology dependent deploy events up to model level PCM deploy events.
 *
 * @author Reiner Jung
 *
 */
public class DeployPCMMapper extends AbstractConsumerStage<IDeployedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployPCMMapper.class);

    private final ICorrespondence correspondence;
    private final OutputPort<PCMDeployedEvent> outputPort = this.createOutputPort();

    /**
     * Create a deployed event mapper.
     *
     * @param correspondence
     *            correspondence model handler
     */
    public DeployPCMMapper(final ICorrespondence correspondence) {
        this.correspondence = correspondence;
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
        if (event instanceof ServletDeployedEvent) {
            this.servletMapper((ServletDeployedEvent) event);
        } else if (event instanceof EJBDeployedEvent) {
            this.ejbMapper((EJBDeployedEvent) event);
        }

    }

    private void ejbMapper(final EJBDeployedEvent event) {
        final String service = event.getService();
        final String context = event.getContext();

        // build the url for the containerAllocationEvent
        final String urlContext = context.replaceAll("\\.", "/");
        final String url = "http://" + service + '/' + urlContext;

        final Correspondent correspondent = this.correspondence.getCorrespondent(context).get();
        if (correspondent != null) {

            if (event instanceof Privacy_EJBDeployedEvent) {
                this.outputPort.send(new PCMDeployedEvent(service, correspondent, url,
                        ((Privacy_EJBDeployedEvent) event).getCountryCode()));
            } else {
                this.outputPort.send(new PCMDeployedEvent(service, correspondent, url, (short) 0));
            }
        } else {
            DeployPCMMapper.LOGGER.warn("No correspondent found for {}.", service);
        }
    }

    private void servletMapper(final ServletDeployedEvent event) {
        final String service = event.getService();
        final String context = event.getContext();

        // build the containerAllocationEvent
        final String urlContext = context.replaceAll("\\.", "/");
        final String url = "http://" + service + '/' + urlContext;

        final Correspondent correspondent = this.correspondence.getCorrespondent(context).get();
        if (correspondent != null) {
            if (event instanceof Privacy_ServletDeployedEvent) {
                this.outputPort.send(new PCMDeployedEvent(service, correspondent, url,
                        ((Privacy_ServletDeployedEvent) event).getCountryCode()));
            } else {
                this.outputPort.send(new PCMDeployedEvent(service, correspondent, url, (short) 0));
            }
        } else {
            DeployPCMMapper.LOGGER.info("No correspondent found for {}.", service);
        }
    }

}
