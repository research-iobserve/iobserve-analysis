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
package org.iobserve.service.behavior.analysis;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import teetime.framework.AbstractConsumerStage;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.EventSerializer;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class ClusterMedoidSink extends AbstractConsumerStage<BehaviorModelGED> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterMedoidSink.class);

    private final ObjectMapper objectMapper;

    private final String filename;

    private int clusterNumber = 0;

    /**
     * Create behavior model writer.
     *
     * @param baseUrl
     *            base url
     */
    public ClusterMedoidSink(final String filename) {
        this.objectMapper = new ObjectMapper();
        this.filename = filename;
    }

    @Override
    protected void execute(final BehaviorModelGED model) throws Exception {
        final String numberedFilename = this.filename + "_cluster_" + this.clusterNumber;
        this.clusterNumber++;

        ClusterMedoidSink.LOGGER.info("Write cluster medoid to " + numberedFilename);
        final FileWriter fw = new FileWriter(numberedFilename);
        final BufferedWriter bw = new BufferedWriter(fw);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // this custom serializer just prints the values array of an event
        final SimpleModule module = new SimpleModule();
        module.addSerializer(PayloadAwareEntryCallEvent.class, new EventSerializer());
        this.objectMapper.registerModule(module);

        this.objectMapper.writeValue(bw, model);
        fw.close();

    }

}
