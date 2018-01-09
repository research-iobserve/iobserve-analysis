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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.common.record.EJBUndeployedEvent;
import org.iobserve.common.record.IUndeployedEvent;
import org.iobserve.common.record.ServletUndeployedEvent;

/**
 * Maps technology dependent undeploy events onto model level PCM undeploy events.
 *
 * @author Reiner Jung
 *
 */
public class UndeployPCMMapper extends AbstractConsumerStage<IUndeployedEvent> {

    private static final Logger LOGGER = LogManager.getLogger(UndeployPCMMapper.class);

    private final ICorrespondence correspondence;
    private final OutputPort<PCMUndeployedEvent> outputPort = this.createOutputPort();

    public UndeployPCMMapper(final ICorrespondence correspondence) {
        this.correspondence = correspondence;
    }

    public OutputPort<PCMUndeployedEvent> getOutputPort() {
        return this.outputPort;
    }

    @Override
    protected void execute(final IUndeployedEvent event) throws Exception {
        if (event instanceof ServletUndeployedEvent) {
            this.servletMapper((ServletUndeployedEvent) event);
        } else if (event instanceof EJBUndeployedEvent) {
            this.ejbMapper((EJBUndeployedEvent) event);
        }

    }

    private void ejbMapper(final EJBUndeployedEvent event) {
        final String service = event.getService();
        final String context = event.getContext();

        final Correspondent correspondent = this.correspondence.getCorrespondent(context).get();
        if (this.correspondence.getCorrespondent(context) != null) {
            this.outputPort.send(new PCMUndeployedEvent(service, correspondent));
        } else {
            UndeployPCMMapper.LOGGER.info("No correspondent found for " + service + ".");
        }
    }

    private void servletMapper(final ServletUndeployedEvent event) {
        final String service = event.getService();
        final String context = event.getContext();

        final Correspondent correspondent = this.correspondence.getCorrespondent(context).get();
        if (this.correspondence.getCorrespondent(context) != null) {
            this.outputPort.send(new PCMUndeployedEvent(service, correspondent));
        } else {
            UndeployPCMMapper.LOGGER.info("No correspondent found for " + service + ".");
        }
    }

}
