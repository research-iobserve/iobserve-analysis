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
package org.iobserve.analysis.mockup;

import java.io.File;

import teetime.framework.AbstractProducerStage;

/**
 * Only for debugging.
 *
 * @author Lars Bluemke
 *
 */
public class ModelProducer extends AbstractProducerStage<File> {

    private static final File PCM_MODELS_DIRECTORY = new File(
            "/Users/LarsBlumke/Documents/CAU/Masterarbeit/iObserve/working-dir-analysis");

    @Override
    protected void execute() throws Exception {

        for (final File modelFile : ModelProducer.PCM_MODELS_DIRECTORY.listFiles()) {
            this.outputPort.send(modelFile);
        }

        this.workCompleted();
    }

}
