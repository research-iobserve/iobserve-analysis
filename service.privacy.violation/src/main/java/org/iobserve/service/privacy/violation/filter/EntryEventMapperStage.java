/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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

import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.service.privacy.violation.data.PCMEntryCallEvent;
import org.iobserve.stages.general.data.EntryCallEvent;

/**
 * Transforms {@link EntryCallEvent}s to model level {@link PCMEntryCallEvent}s.
 *
 * @author Reiner Jung
 *
 */
public class EntryEventMapperStage extends AbstractConsumerStage<EntryCallEvent> {

    private final ICorrespondence rac;
    private final OutputPort<PCMEntryCallEvent> outputPort = this.createOutputPort(PCMEntryCallEvent.class);

    /**
     * Entry event mapper.
     *
     * @param rac
     *            correspondence model
     */
    public EntryEventMapperStage(final ICorrespondence rac) {
        this.rac = rac;
    }

    @Override
    protected void execute(final EntryCallEvent event) throws Exception {
        // TODO KIT implement stuff
    }

    public OutputPort<PCMEntryCallEvent> getOutputPort() {
        return this.outputPort;
    }

}
