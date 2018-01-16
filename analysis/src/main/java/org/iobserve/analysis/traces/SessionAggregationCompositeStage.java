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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.analysis.AbstractConfigurableCompositeStage;
import org.iobserve.analysis.deployment.DeployPCMMapper;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.stages.general.IEntryCallTraceMatcher;
import org.iobserve.stages.general.TEntryCallStage;

import kieker.common.configuration.Configuration;
import kieker.common.record.flow.IFlowRecord;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;
import teetime.stage.trace.traceReconstruction.EventBasedTraceFactory;
import teetime.stage.trace.traceReconstruction.TraceReconstructionFilter;
import teetime.util.ConcurrentHashMapWithDefault;

/**
 *
 * @author Reiner Jung
 *
 */
public class SessionAggregationCompositeStage extends AbstractConfigurableCompositeStage {

    private static final Logger LOGGER = LogManager.getLogger(DeployPCMMapper.class);

    private static final String PREFIX = SessionAggregationCompositeStage.class.getCanonicalName();
    private static final String MATCHER = SessionAggregationCompositeStage.PREFIX + ".matcher";

    final ConcurrentHashMapWithDefault<Long, EventBasedTrace> traceBuffer = new ConcurrentHashMapWithDefault<>(
            EventBasedTraceFactory.INSTANCE);

    private final TEntryCallSequence entryCallSequence;

    private final TraceReconstructionFilter traceReconstructionFilter;

    public SessionAggregationCompositeStage(final Configuration configuration) {
        super(configuration);

        this.traceReconstructionFilter = new TraceReconstructionFilter(this.traceBuffer);
        final String matcherClassName = configuration.getStringProperty(SessionAggregationCompositeStage.MATCHER);
        if (matcherClassName != null) {
            final IEntryCallTraceMatcher matcher = this.instantiate(matcherClassName);
            final TEntryCallStage entryCall = new TEntryCallStage(matcher);
            this.entryCallSequence = new TEntryCallSequence();

            /** connect */
            this.connectPorts(this.traceReconstructionFilter.getTraceValidOutputPort(), entryCall.getInputPort());
            this.connectPorts(entryCall.getOutputPort(), this.entryCallSequence.getEntryCallInputPort());
        } else {
            SessionAggregationCompositeStage.LOGGER.error("Missing matcher class parameter.");
        }
    }

    public InputPort<IFlowRecord> getFlowRecordInputPort() {
        return this.traceReconstructionFilter.getInputPort();
    }

    public OutputPort<?> getUserSessionOutputPort() {
        return this.entryCallSequence.getUserSessionOutputPort();
    }

    public InputPort<ISessionEvent> getSessionEventInputPort() {
        return this.entryCallSequence.getSessionEventInputPort();
    }

}
