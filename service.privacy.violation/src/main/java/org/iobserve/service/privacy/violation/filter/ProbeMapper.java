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

import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.service.privacy.violation.data.IProbeManagement;

/**
 * Translate model level {@link IProbeManagement} events to code level events.
 *
 * @author Reiner Jung
 *
 */
public class ProbeMapper extends AbstractConsumerStage<IProbeManagement> {

    private final OutputPort<String> outputPort = this.createOutputPort();
    private final IModelProvider<AssemblyEntry> rac;

    /**
     * Initialize probe mapper from model to code level.
     *
     * @param rac
     *            correspondence model used for mapping
     */
    public ProbeMapper(final IModelProvider<AssemblyEntry> rac) {
        this.rac = rac;
    }

    @Override
    protected void execute(final IProbeManagement element) throws Exception {
        // TODO Auto-generated method stub

    }

    public OutputPort<String> getOutputPort() {
        return this.outputPort;
    }

}
