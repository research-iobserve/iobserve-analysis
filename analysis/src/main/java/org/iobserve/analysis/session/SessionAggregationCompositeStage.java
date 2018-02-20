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
package org.iobserve.analysis.session;

import kieker.common.configuration.Configuration;
import kieker.common.record.flow.IFlowRecord;

import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;
import teetime.stage.trace.traceReconstruction.EventBasedTraceFactory;
import teetime.stage.trace.traceReconstruction.TraceReconstructionFilter;
import teetime.util.ConcurrentHashMapWithDefault;

import org.iobserve.analysis.AbstractConfigurableCompositeStage;
import org.iobserve.analysis.ConfigurationException;
import org.iobserve.analysis.InstantiationFactory;
import org.iobserve.analysis.deployment.DeployPCMMapper;
import org.iobserve.analysis.traces.EntryCallSequence;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.stages.general.EntryCallStage;
import org.iobserve.stages.general.IEntryCallTraceMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Reiner Jung
 *
 */
public class SessionAggregationCompositeStage extends AbstractConfigurableCompositeStage {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployPCMMapper.class);

    private static final String PREFIX = SessionAggregationCompositeStage.class.getCanonicalName();
    private static final String MATCHER = SessionAggregationCompositeStage.PREFIX + ".matcher";

    private final ConcurrentHashMapWithDefault<Long, EventBasedTrace> traceBuffer = new ConcurrentHashMapWithDefault<>(
            EventBasedTraceFactory.INSTANCE);

    private final EntryCallSequence entryCallSequence;

    private final TraceReconstructionFilter traceReconstructionFilter;

    /**
     * Create a session aggregation stage.
     *
     * @param configuration
     *            configuration parameters
     * @throws ConfigurationException
     *             on configuration error
     */
    public SessionAggregationCompositeStage(final Configuration configuration) throws ConfigurationException {
        super(configuration);

        this.traceReconstructionFilter = new TraceReconstructionFilter(this.traceBuffer);
        final String matcherClassName = configuration.getStringProperty(SessionAggregationCompositeStage.MATCHER);
        if (matcherClassName != null) {
            final IEntryCallTraceMatcher matcher = InstantiationFactory
                    .createWithConfiguration(IEntryCallTraceMatcher.class, matcherClassName, configuration);
            final EntryCallStage entryCall = new EntryCallStage(matcher);
            this.entryCallSequence = new EntryCallSequence();

            /** connect */
            this.connectPorts(this.traceReconstructionFilter.getTraceValidOutputPort(), entryCall.getInputPort());
            this.connectPorts(entryCall.getOutputPort(), this.entryCallSequence.getEntryCallInputPort());
        } else {
            SessionAggregationCompositeStage.LOGGER.error("Missing matcher class parameter.");
            throw new ConfigurationException("Missing matcher class parameter.");
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
