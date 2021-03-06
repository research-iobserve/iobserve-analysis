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
package org.iobserve.analysis.behavior.clustering.em;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.common.util.classpath.InstantiationFactory;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

import org.iobserve.analysis.behavior.filter.BehaviorModelPrepratationStage;
import org.iobserve.analysis.behavior.filter.UserSessionGeneratorCompositeStage;
import org.iobserve.analysis.behavior.models.data.configuration.EntryCallFilterRules;
import org.iobserve.analysis.behavior.models.data.configuration.GetLastXSignatureStrategy;
import org.iobserve.analysis.behavior.models.data.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.behavior.models.data.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.feature.IBehaviorCompositeStage;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.stages.data.trace.EventBasedTrace;

/**
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
@ReceiveUnfilteredConfiguration
public class EMBehaviorCompositeStage extends CompositeStage implements IBehaviorCompositeStage {

    private static final String PREFIX = EMBehaviorCompositeStage.class.getCanonicalName() + ".";

    private static final String OUTPUT_URL = EMBehaviorCompositeStage.PREFIX + "output.url";

    private static final String NAME_PREFIX = EMBehaviorCompositeStage.PREFIX + "prefix";

    private static final String REPRESENTATIVE_STRATEGY = EMBehaviorCompositeStage.PREFIX + "representativeStrategy";

    private final UserSessionGeneratorCompositeStage userSessionGeneratorCompositeStage;

    /**
     * Create the EM behavior clustering composite stage.
     *
     * @param configuration
     *            configuration for all stages
     * @throws ConfigurationException
     *             on errors with the configuration
     */
    public EMBehaviorCompositeStage(final Configuration configuration) throws ConfigurationException {

        // TODO make these options
        final EntryCallFilterRules modelGenerationFilter = new EntryCallFilterRules(false).addFilterRule(".*");
        final String representativeStrategyClassName = configuration
                .getStringProperty(EMBehaviorCompositeStage.REPRESENTATIVE_STRATEGY);
        final IRepresentativeStrategy representativeStrategy = InstantiationFactory.getInstance(configuration)
                .create(IRepresentativeStrategy.class, representativeStrategyClassName, null);
        final boolean keepEmptyTransitions = true;

        this.userSessionGeneratorCompositeStage = new UserSessionGeneratorCompositeStage(configuration);

        final UserSessionModelAggregator userSessionModelAggregator = new UserSessionModelAggregator();

        final BehaviorModelPrepratationStage behaviorModelPreparation = new BehaviorModelPrepratationStage(
                modelGenerationFilter, representativeStrategy, keepEmptyTransitions);

        /** aggregation setup. */
        final String namePrefix = configuration.getStringProperty(EMBehaviorCompositeStage.NAME_PREFIX);
        final ISignatureCreationStrategy signatureCreationStrategy = new GetLastXSignatureStrategy(Integer.MAX_VALUE);
        final String visualizationUrl = configuration.getStringProperty(EMBehaviorCompositeStage.OUTPUT_URL);
        final EMBehaviorModelAggregation emBehaviorModelAggregation = new EMBehaviorModelAggregation(namePrefix,
                visualizationUrl, signatureCreationStrategy);

        this.connectPorts(this.userSessionGeneratorCompositeStage.getSessionOutputPort(),
                userSessionModelAggregator.getUserSessionInputPort());
        this.connectPorts(this.userSessionGeneratorCompositeStage.getTimerOutputPort(),
                userSessionModelAggregator.getTimeTriggerInputPort());
        this.connectPorts(userSessionModelAggregator.getOutputPort(), behaviorModelPreparation.getInputPort());
        this.connectPorts(behaviorModelPreparation.getOutputPort(), emBehaviorModelAggregation.getInputPort());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.feature.IBehaviorCompositeStage#getEventBasedTracePort()
     */
    @Override
    public InputPort<EventBasedTrace> getEventBasedTraceInputPort() {
        return this.userSessionGeneratorCompositeStage.getTraceInputPort();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.feature.IBehaviorCompositeStage#getSessionEventInputPort()
     */
    @Override
    public InputPort<ISessionEvent> getSessionEventInputPort() {
        return this.userSessionGeneratorCompositeStage.getSessionEventInputPort();
    }

}
