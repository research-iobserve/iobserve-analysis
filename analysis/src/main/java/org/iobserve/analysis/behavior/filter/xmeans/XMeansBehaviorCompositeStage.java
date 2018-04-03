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
package org.iobserve.analysis.behavior.filter.xmeans;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

import org.iobserve.analysis.behavior.filter.similaritymatching.BehaviorCompositeStage;
import org.iobserve.analysis.feature.IBehaviorCompositeStage;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.stages.general.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
@ReceiveUnfilteredConfiguration
public class XMeansBehaviorCompositeStage extends CompositeStage implements IBehaviorCompositeStage {

    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorCompositeStage.class);

    private final InputPort<EventBasedTrace> eventBasedTraceInputPort = null;
    private final InputPort<ISessionEvent> sessionEventInputPort = null;

    public XMeansBehaviorCompositeStage(final Configuration configuration) throws ConfigurationException {

    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.feature.IBehaviorCompositeStage#getEventBasedTracePort()
     */
    @Override
    public InputPort<EventBasedTrace> getEventBasedTracePort() {
        return this.eventBasedTraceInputPort;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.feature.IBehaviorCompositeStage#getSessionEventInputPort()
     */
    @Override
    public InputPort<ISessionEvent> getSessionEventInputPort() {
        return this.sessionEventInputPort;
    }

}
