/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.response.time.calculator;

import java.io.FileNotFoundException;

import kieker.analysis.source.ISourceCompositeStage;
import kieker.common.exception.ConfigurationException;
import kieker.common.record.flow.IFlowRecord;
import kieker.tools.source.SourceStageFactory;

import teetime.framework.Configuration;

import org.iobserve.stages.general.DynamicEventDispatcher;
import org.iobserve.stages.general.IEventMatcher;
import org.iobserve.stages.general.ImplementsEventMatcher;
import org.iobserve.stages.sink.CSVFileWriter;

/**
 * @author Reiner Jung
 *
 */
public class PiplineConfiguration extends Configuration {

    public PiplineConfiguration(final kieker.common.configuration.Configuration kiekerConfiguration,
            final Settings configuration) throws ConfigurationException, FileNotFoundException {
        final ISourceCompositeStage sourceCompositeStage = SourceStageFactory
                .createSourceCompositeStage(kiekerConfiguration);

        final IEventMatcher<IFlowRecord> rootEventMatcher = new ImplementsEventMatcher<>(IFlowRecord.class, null);

        final DynamicEventDispatcher eventdispatcher = new DynamicEventDispatcher(rootEventMatcher, true, true, false);

        final CalculateResponseTimeStage calculateResponseTimeStage = new CalculateResponseTimeStage();

        final CSVFileWriter fileWriter = new CSVFileWriter(configuration.getOutputFile());

        this.connectPorts(sourceCompositeStage.getOutputPort(), eventdispatcher.getInputPort());
        this.connectPorts(rootEventMatcher.getOutputPort(), calculateResponseTimeStage.getInputPort());
        this.connectPorts(calculateResponseTimeStage.getOutputPort(), fileWriter.getInputPort());
    }

}
