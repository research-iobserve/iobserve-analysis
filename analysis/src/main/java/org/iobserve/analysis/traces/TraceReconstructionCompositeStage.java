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
package org.iobserve.analysis.traces;

import kieker.common.configuration.Configuration;
import kieker.common.record.flow.IFlowRecord;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.feature.ITraceCompositeStage;
import org.iobserve.analysis.traces.traceReconstruction.TraceReconstructionFilter;
import org.iobserve.stages.data.trace.ConcurrentHashMapWithCreate;
import org.iobserve.stages.data.trace.EventBasedTrace;
import org.iobserve.stages.data.trace.EventBasedTraceFactory;

/**
 * Composite stage to encapsulate trace reconstruction.
 *
 * Note: This composite stage only exists to be able to express all features as composite stages.
 *
 * @author Reiner Jung
 *
 */
public class TraceReconstructionCompositeStage extends CompositeStage implements ITraceCompositeStage {

    private final TraceReconstructionFilter traceReconstructionFilter;
    private final TranslateCallOperationEventsStage translateCallOperationEventsStage;

    /**
     * Create a trace reconstruction composite stage.
     *
     * @param configuration
     *            configuration object
     */
    public TraceReconstructionCompositeStage(final Configuration configuration) {
        final ConcurrentHashMapWithCreate<Long, EventBasedTrace> traceMap = new ConcurrentHashMapWithCreate<>(
                EventBasedTraceFactory.INSTANCE);

        this.translateCallOperationEventsStage = new TranslateCallOperationEventsStage();
        this.traceReconstructionFilter = new TraceReconstructionFilter(traceMap);

        this.connectPorts(this.translateCallOperationEventsStage.getOutputPort(),
                this.traceReconstructionFilter.getInputPort());
    }

    public InputPort<IFlowRecord> getInputPort() {
        return this.translateCallOperationEventsStage.getInputPort();
    }

    public OutputPort<EventBasedTrace> getTraceValidOutputPort() {
        return this.traceReconstructionFilter.getTraceValidOutputPort();
    }

    public OutputPort<EventBasedTrace> getTraceInvalidOutputPort() {
        return this.traceReconstructionFilter.getTraceInvalidOutputPort();
    }

}
