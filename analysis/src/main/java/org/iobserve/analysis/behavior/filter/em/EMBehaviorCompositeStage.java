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
package org.iobserve.analysis.behavior.filter.em;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

import org.iobserve.analysis.behavior.filter.TBehaviorModelPreprocessing;
import org.iobserve.analysis.behavior.filter.models.configuration.EntryCallFilterRules;
import org.iobserve.analysis.behavior.filter.models.configuration.GetLastXSignatureStrategy;
import org.iobserve.analysis.behavior.filter.models.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.behavior.filter.models.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.behavior.filter.models.configuration.examples.FirstCallInformationCodeStrategy;
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
public class EMBehaviorCompositeStage extends CompositeStage implements IBehaviorCompositeStage {

    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorCompositeStage.class);

    private static final String PREFIX = EMBehaviorCompositeStage.class.getCanonicalName() + ".";

    private static final String OUTPUT_URL = EMBehaviorCompositeStage.PREFIX + "output.url";

    private static final String NAME_PREFIX = EMBehaviorCompositeStage.PREFIX + "prefix";

    private final InputPort<EventBasedTrace> eventBasedTraceInputPort;
    private final InputPort<ISessionEvent> sessionEventInputPort;

    public EMBehaviorCompositeStage(final Configuration configuration) throws ConfigurationException {

        // TODO make these options
        final EntryCallFilterRules modelGenerationFilter = new EntryCallFilterRules(false).addFilterRule(".*");
        final IRepresentativeStrategy representativeStrategy = new FirstCallInformationCodeStrategy();
        final boolean keepEmptyTransitions = true;

        final UserSessionModelAggregator userSessionModelAggregator = new UserSessionModelAggregator();
        this.eventBasedTraceInputPort = userSessionModelAggregator.getTraceInputPort();
        this.sessionEventInputPort = userSessionModelAggregator.getSessionEventInputPort();

        final TBehaviorModelPreprocessing tBehaviorModelProcessing = new TBehaviorModelPreprocessing(
                modelGenerationFilter, representativeStrategy, keepEmptyTransitions);

        final String namePrefix = configuration.getStringProperty(EMBehaviorCompositeStage.NAME_PREFIX);
        final ISignatureCreationStrategy signatureCreationStrategy = new GetLastXSignatureStrategy(Integer.MAX_VALUE);
        final String visualizationUrl = configuration.getStringProperty(EMBehaviorCompositeStage.OUTPUT_URL);
        final TBehaviorModelAggregation tBehaviorModelAggregation = new TBehaviorModelAggregation(namePrefix,
                visualizationUrl, signatureCreationStrategy);

        this.connectPorts(userSessionModelAggregator.getOutputPort(), tBehaviorModelProcessing.getInputPort());
        this.connectPorts(tBehaviorModelProcessing.getOutputPort(), tBehaviorModelAggregation.getInputPort());
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
