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
package org.iobserve.adaptation.configurations;

import java.io.File;

import teetime.framework.Configuration;

import org.iobserve.adaptation.cli.ModelFileSink;
import org.iobserve.legacyadaptation.AdaptationCalculation;
import org.iobserve.legacyadaptation.AdaptationPlanning;
import org.iobserve.legacyadaptation.SystemAdaptation;
import org.iobserve.stages.source.SingleConnectionTcpReaderStage;

/**
 * Configuration for the stages of the adaptation service.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationConfigurationDebug extends Configuration {

    public AdaptationConfigurationDebug() {
        new SystemAdaptation(new AdaptationCalculation(), new AdaptationPlanning());

        // Implement a way to pass data to the following stages
        // Path Adaptation => Execution
        // this.connectPorts(systemAdaptor.getOutputPort(), adaptationExecution.getInputPort());

        // Debugging
        final SingleConnectionTcpReaderStage modelReader = new SingleConnectionTcpReaderStage(12346,
                new File("/Users/LarsBlumke/Documents/CAU/Masterarbeit/working-dir-adaptation"));

        final ModelFileSink modelProducerSink = new ModelFileSink();

        this.connectPorts(modelReader.getOutputPort(), modelProducerSink.getInputPort());
    }

}
