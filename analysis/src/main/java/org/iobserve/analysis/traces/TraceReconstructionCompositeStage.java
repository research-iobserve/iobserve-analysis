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

import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;
import teetime.stage.trace.traceReconstruction.EventBasedTraceFactory;
import teetime.stage.trace.traceReconstruction.TraceReconstructionFilter;
import teetime.util.ConcurrentHashMapWithDefault;

import org.iobserve.analysis.AbstractConfigurableCompositeStage;
import org.iobserve.analysis.ITraceCompositeStage;

/**
 * Composite stage to encapsulate trace reconstruction.
 *
 * Note: Currently this mainly serves the necessity to follow the general concept of feature-based
 * modularization.
 *
 * @author Reiner Jung
 *
 */
public class TraceReconstructionCompositeStage extends AbstractConfigurableCompositeStage
        implements ITraceCompositeStage {

    private final TraceReconstructionFilter traceReconstructionFilter;

    /**
     * Create a trace reconstruction composite stage.
     *
     * @param configuration
     *            configuration object
     */
    public TraceReconstructionCompositeStage(final Configuration configuration) {
        super(configuration);
        final ConcurrentHashMapWithDefault<Long, EventBasedTrace> traceBuffer = new ConcurrentHashMapWithDefault<>(
                EventBasedTraceFactory.INSTANCE);

        this.traceReconstructionFilter = new TraceReconstructionFilter(traceBuffer);
    }

    public InputPort<IFlowRecord> getInputPort() {
        return this.traceReconstructionFilter.getInputPort();
    }

    public OutputPort<EventBasedTrace> getTraceValidOutputPort() {
        return this.traceReconstructionFilter.getTraceValidOutputPort();
    }

    public OutputPort<EventBasedTrace> getTraceInvalidOutputPort() {
        return this.traceReconstructionFilter.getTraceInvalidOutputPort();
    }

}
