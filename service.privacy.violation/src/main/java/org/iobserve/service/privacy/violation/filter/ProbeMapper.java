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
package org.iobserve.service.privacy.violation.filter;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;

/**
 * Translate model level {@link ProbeManagementData} events to code level events. Gets real system
 * information through model entries.
 *
 *
 * @author Marc Adolf
 *
 */
public class ProbeMapper extends AbstractConsumerStage<ProbeManagementData> {

    private final OutputPort<AbstractTcpControlEvent> outputPort = this.createOutputPort();

    /**
     * Initialize probe mapper from model to code level.
     *
     * @param rac
     *            correspondence model used for mapping
     */
    public ProbeMapper(final ModelResource<CorrespondenceModel> correspondenceModelResource) {
    }

    @Override
    protected void execute(final ProbeManagementData element) throws Exception {
        // TODO Auto-generated method stub

    }

    public OutputPort<AbstractTcpControlEvent> getOutputPort() {
        return this.outputPort;
    }

}
