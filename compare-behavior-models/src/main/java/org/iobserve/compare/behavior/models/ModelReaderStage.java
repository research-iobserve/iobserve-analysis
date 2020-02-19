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
package org.iobserve.compare.behavior.models;

import java.io.File;
import java.nio.charset.StandardCharsets;

import teetime.framework.AbstractProducerStage;

import org.apache.commons.io.FileUtils;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read a JSON serialized behavior model.
 *
 * @author Lars Jürgensen
 *
 */
public class ModelReaderStage extends AbstractProducerStage<BehaviorModelGED> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelReaderStage.class);

    private final File inputFile;

    /**
     * Create a JSON reader stage.
     *
     * @param inputFile
     *            JSON file source
     */
    public ModelReaderStage(final File inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    protected void execute() throws Exception {

        final String content = FileUtils.readFileToString(this.inputFile, StandardCharsets.UTF_8);
        final BehaviorModelDeserialization deserializer = new BehaviorModelDeserialization(content);
        final BehaviorModelGED model = deserializer.deserialize();

        this.outputPort.send(model);
        ModelReaderStage.LOGGER.info("model was deserialized");

        this.workCompleted();
    }

}
