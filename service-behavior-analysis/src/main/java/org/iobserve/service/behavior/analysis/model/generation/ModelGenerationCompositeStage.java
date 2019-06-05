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
package org.iobserve.service.behavior.analysis.model.generation;

import kieker.common.exception.ConfigurationException;
import kieker.common.record.flow.IFlowRecord;

import teetime.framework.CompositeStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.analysis.behavior.filter.UserSessionGeneratorCompositeStage;
import org.iobserve.analysis.traces.TraceReconstructionCompositeStage;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.service.behavior.analysis.BehaviorAnalysisTeetimeConfiguration;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.source.FileSourceCompositeStage;
import org.iobserve.stages.general.DynamicEventDispatcher;
import org.iobserve.stages.general.IEventMatcher;
import org.iobserve.stages.general.ImplementsEventMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class ModelGenerationCompositeStage extends CompositeStage {

    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorAnalysisTeetimeConfiguration.class);

    private final OutputPort<BehaviorModelGED> modelOutputPort;

    private final OutputPort<Long> timerOutputPort;

    public ModelGenerationCompositeStage(final kieker.common.configuration.Configuration configuration)
            throws ConfigurationException {

        final String sourceClassName = configuration.getStringProperty(ConfigurationKeys.SOURCE);
        if (!sourceClassName.isEmpty()) {
            ModelGenerationCompositeStage.LOGGER.error("Initialization incomplete: No source stage specified.");
            throw new ConfigurationException("Initialization incomplete: No source stage specified.");
        }

        final FileSourceCompositeStage reader = new FileSourceCompositeStage(configuration);
        final DynamicEventDispatcher eventDispatcher = new DynamicEventDispatcher(null, true, true, false);
        final TraceReconstructionCompositeStage traceReconstructionStage = new TraceReconstructionCompositeStage(
                configuration);
        final IEventMatcher<IFlowRecord> flowRecordMatcher = new ImplementsEventMatcher<>(IFlowRecord.class, null);
        final UserSessionGeneratorCompositeStage sessionGenerator = new UserSessionGeneratorCompositeStage(
                configuration);
        final UserSessionToModelConverter sessionToModel = new UserSessionToModelConverter();

        this.connectPorts(reader.getOutputPort(), eventDispatcher.getInputPort());

        eventDispatcher.registerOutput(flowRecordMatcher);

        this.connectPorts(flowRecordMatcher.getOutputPort(), traceReconstructionStage.getInputPort());
        this.connectPorts(traceReconstructionStage.getTraceValidOutputPort(), sessionGenerator.getTraceInputPort());

        final IEventMatcher<ISessionEvent> sessionMatcher = new ImplementsEventMatcher<>(ISessionEvent.class, null);
        eventDispatcher.registerOutput(sessionMatcher);

        this.connectPorts(sessionMatcher.getOutputPort(), sessionGenerator.getSessionEventInputPort());

        this.connectPorts(sessionGenerator.getSessionOutputPort(), sessionToModel.getInputPort());

        this.modelOutputPort = this.createOutputPort(sessionToModel.getOutputPort());
        this.timerOutputPort = this.createOutputPort(sessionGenerator.getTimerOutputPort());
    }

    public OutputPort<BehaviorModelGED> getModelOutputPort() {
        return this.modelOutputPort;
    }

    public OutputPort<Long> getTimerOutputPort() {
        return this.timerOutputPort;
    }

}
