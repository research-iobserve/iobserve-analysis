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
package teetime.stage.trace.traceReconstruction;

import java.util.concurrent.TimeUnit;

import org.iobserve.analysis.configurations.AnalysisConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.flow.IFlowRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import teetime.util.ConcurrentHashMapWithDefault;
import teetime.util.ISendTraceBuffer;
import teetime.util.TraceReconstructor;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
public class TraceReconstructionFilter extends AbstractConsumerStage<IFlowRecord> implements ISendTraceBuffer {

    private final OutputPort<EventBasedTrace> traceValidOutputPort = this.createOutputPort();
    private final OutputPort<EventBasedTrace> traceInvalidOutputPort = this.createOutputPort();
    
    
    private TimeUnit timeunit;
    private long maxTraceDuration = Long.MAX_VALUE;
    private long maxTraceTimeout = Long.MAX_VALUE;
    private long maxEncounteredLoggingTimestamp = -1;

    private final TraceReconstructor reconstructor;
    /**
     * Reconstruct traces filter.
     * 
     * @param traceId2trace
     *            trace id to trace map
     */
    public TraceReconstructionFilter(final ConcurrentHashMapWithDefault<Long, EventBasedTrace> traceId2trace) {
        super();
        this.reconstructor = new TraceReconstructor(traceId2trace, this);
    }

    @Override
    protected void execute(final IFlowRecord element) {
        this.reconstructor.execute(element);
    }

    @Override
    public void onTerminating() {
        this.reconstructor.terminate();

        super.onTerminating();
    }

    @Override
    public void sendTraceBuffer(final EventBasedTrace traceBufferList) {
        final OutputPort<EventBasedTrace> outputPort = traceBufferList.isInvalid() ? this.traceInvalidOutputPort
                : this.traceValidOutputPort;
        outputPort.send(traceBufferList);
    }

    public TimeUnit getTimeunit() {
        return this.timeunit;
    }

    public void setTimeunit(final TimeUnit timeunit) {
        this.timeunit = timeunit;
    }

    public long getMaxTraceDuration() {
        return this.maxTraceDuration;
    }

    public void setMaxTraceDuration(final long maxTraceDuration) {
        this.maxTraceDuration = maxTraceDuration;
    }

    public long getMaxTraceTimeout() {
        return this.maxTraceTimeout;
    }

    public void setMaxTraceTimeout(final long maxTraceTimeout) {
        this.maxTraceTimeout = maxTraceTimeout;
    }

    public long getMaxEncounteredLoggingTimestamp() {
        return this.maxEncounteredLoggingTimestamp;
    }

    public void setMaxEncounteredLoggingTimestamp(final long maxEncounteredLoggingTimestamp) {
        this.maxEncounteredLoggingTimestamp = maxEncounteredLoggingTimestamp;
    }

    public OutputPort<EventBasedTrace> getTraceValidOutputPort() {
        return this.traceValidOutputPort;
    }

    public OutputPort<EventBasedTrace> getTraceInvalidOutputPort() {
        return this.traceInvalidOutputPort;
    }

}
