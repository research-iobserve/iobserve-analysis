/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.compare.behavior.models;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import teetime.framework.AbstractConsumerStage;

import org.iobserve.service.behavior.analysis.model.EventSerializer;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

/**
 * Sync all incoming records with a Kieker writer to a text file log.
 *
 * @author Lars Jürgensen
 *
 */
public class ComparisonOutputStage extends AbstractConsumerStage<BehaviorModelDifference> {

    private final File outputFile;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Configure and setup a file writer for the output of the result comparison.
     *
     * @param outputFile
     *            file descriptor for the output file
     */
    public ComparisonOutputStage(final File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    protected void execute(final BehaviorModelDifference result) throws IOException {

        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // this custom serializer just prints the values array of an event
        final SimpleModule module = new SimpleModule();
        module.addSerializer(PayloadAwareEntryCallEvent.class, new EventSerializer());
        this.objectMapper.registerModule(module);
        this.objectMapper.writeValue(this.outputFile, result);

    }
}
