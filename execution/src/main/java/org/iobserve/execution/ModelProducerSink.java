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
package org.iobserve.execution;

import java.io.File;

import org.iobserve.model.PCMModelHandler;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.framework.AbstractConsumerStage;

/**
 * Only for debugging.
 *
 * @author Lars Bluemke
 *
 */
public class ModelProducerSink extends AbstractConsumerStage<File> {
    private static final Logger LOG = LoggerFactory.getLogger(ModelProducerSink.class);
    private static final File PCM_MODELS_DIRECTORY = new File(
            "/Users/LarsBlumke/Documents/CAU/Masterarbeit/working-dir-output");

    @Override
    protected void execute(final File modelFile) throws Exception {

        final PCMModelHandler modelHandler = new PCMModelHandler(ModelProducerSink.PCM_MODELS_DIRECTORY);

        final Repository repositoryModel = modelHandler.getRepositoryModel();
        final UsageModel usageModel = modelHandler.getUsageModel();
        final System systemModel = modelHandler.getSystemModel();
        final ResourceEnvironment resourceEnvironmentModel = modelHandler.getResourceEnvironmentModel();
        final Allocation allocationModel = modelHandler.getAllocationModel();

        ModelProducerSink.LOG.debug("I'm done");
    }

}
