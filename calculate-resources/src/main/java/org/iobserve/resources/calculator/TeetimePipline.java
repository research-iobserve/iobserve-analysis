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
package org.iobserve.resources.calculator;

import java.io.FileNotFoundException;

import kieker.analysis.source.ISourceCompositeStage;
import kieker.common.exception.ConfigurationException;
import kieker.common.record.jvm.MemoryRecord;
import kieker.common.record.system.CPUUtilizationRecord;
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
public class TeetimePipline extends Configuration {

    public TeetimePipline(final kieker.common.configuration.Configuration kiekerConfiguration, final Settings settings)
            throws ConfigurationException, FileNotFoundException {
        final ISourceCompositeStage sourceCompositeStage = SourceStageFactory
                .createSourceCompositeStage(kiekerConfiguration);

        final IEventMatcher<CPUUtilizationRecord> cpuEventMatcher = new ImplementsEventMatcher<>(
                CPUUtilizationRecord.class, null);
        final IEventMatcher<MemoryRecord> memEventMatcher = new ImplementsEventMatcher<>(MemoryRecord.class,
                cpuEventMatcher);

        final DynamicEventDispatcher eventdispatcher = new DynamicEventDispatcher(memEventMatcher, true, true, false);

        final EventMapper<CPUUtilizationRecord> cpuEventMapper = new EventMapper<>();
        final EventMapper<MemoryRecord> memEventMapper = new EventMapper<>();

        final CSVFileWriter cpuFileWriter = new CSVFileWriter(settings.getCpuUtilizationOutputFile());
        final CSVFileWriter memFileWriter = new CSVFileWriter(settings.getMemUtilizationOutputFile());

        this.connectPorts(sourceCompositeStage.getOutputPort(), eventdispatcher.getInputPort());
        this.connectPorts(cpuEventMatcher.getOutputPort(), cpuEventMapper.getInputPort());
        this.connectPorts(memEventMatcher.getOutputPort(), memEventMapper.getInputPort());

        this.connectPorts(cpuEventMapper.getOutputPort(), cpuFileWriter.getInputPort());
        this.connectPorts(memEventMapper.getOutputPort(), memFileWriter.getInputPort());
    }

}
