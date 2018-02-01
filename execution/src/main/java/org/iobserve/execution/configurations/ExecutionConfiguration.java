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
package org.iobserve.execution.configurations;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.iobserve.evaluation.ModelComparer;
import org.iobserve.evaluation.SystemEvaluation;
import org.iobserve.execution.AdaptationExecution;
import org.iobserve.execution.IAdaptationEventListener;
import org.iobserve.execution.ModelProducerSink;
import org.iobserve.stages.source.SingleConnectionTcpReaderStage;

import teetime.framework.Configuration;

/**
 * Configuration for the stages of the execution service.
 *
 * @author Lars Bluemke
 *
 */
public class ExecutionConfiguration extends Configuration {

    public ExecutionConfiguration(final IAdaptationEventListener eventListener, final URI deployablesFolder) {

        // There is an AdaptionEventListener class, but in the previous implementation null was
        // used instead.
        if (deployablesFolder != null) {
            final AdaptationExecution adaptationExecution = new AdaptationExecution(eventListener, deployablesFolder);
            final SystemEvaluation systemEvaluator = new SystemEvaluation(new ModelComparer());
        }

        // Debugging
        final SingleConnectionTcpReaderStage modelReader = new SingleConnectionTcpReaderStage(12345,
                new File("/Users/LarsBlumke/Documents/CAU/Masterarbeit/working-dir-output"));

        final ModelProducerSink modelProducerSink = new ModelProducerSink();

        this.connectPorts(modelReader.getOutputPort(), modelProducerSink.getInputPort());
    }
}
