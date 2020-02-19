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
package org.iobserve.analysis.sink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.iobserve.analysis.behavior.models.data.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes a behavior model into a file.
 *
 * @author unknown
 *
 */
public class BehaviorModelSink extends AbstractBehaviorModelOutputSink {

    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorModelSink.class);

    private final ObjectMapper objectMapper;

    private final String baseUrl;

    /**
     * Create behavior model writer.
     *
     * @param baseUrl
     *            base url
     * @param signatureStrategy
     *            signature strategy
     */
    public BehaviorModelSink(final String baseUrl, final ISignatureCreationStrategy signatureStrategy) {
        this.objectMapper = new ObjectMapper();
        this.baseUrl = baseUrl;
    }

    @Override
    protected void execute(final BehaviorModel model) throws IOException {
        final String filename = this.baseUrl + File.separator + model.getName();
        BehaviorModelSink.LOGGER.info("Write models to {}", filename);
        final BufferedWriter bw = Files.newBufferedWriter(new File(filename).toPath(), StandardOpenOption.CREATE);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.writeValue(bw, model);
        bw.close();
    }

}
