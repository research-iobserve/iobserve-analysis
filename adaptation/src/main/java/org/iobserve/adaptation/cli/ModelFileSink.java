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
package org.iobserve.adaptation.cli;

import java.io.File;

import teetime.framework.AbstractConsumerStage;

import org.iobserve.model.PCMModelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Only for debugging.
 *
 * @author Lars Bluemke
 *
 */
public class ModelFileSink extends AbstractConsumerStage<File> {
    private static final Logger LOG = LoggerFactory.getLogger(ModelFileSink.class);
    private static final File PCM_MODELS_DIRECTORY = new File(
            "/Users/LarsBlumke/Documents/CAU/Masterarbeit/working-dir-adaptation");

    @Override
    protected void execute(final File modelFile) throws Exception {

        final PCMModelHandler modelHandler = new PCMModelHandler(ModelFileSink.PCM_MODELS_DIRECTORY);

        modelHandler.getRepositoryModel();
        modelHandler.getUsageModel();
        modelHandler.getSystemModel();
        modelHandler.getResourceEnvironmentModel();
        modelHandler.getAllocationModel();

        ModelFileSink.LOG.debug("I'm done");
    }

}
