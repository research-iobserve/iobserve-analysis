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
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

import org.iobserve.analysis.behavior.clustering.similaritymatching.PreprocessingCompositeStage;
import org.iobserve.analysis.behavior.filter.BehaviorModelPrepratation;
import org.iobserve.analysis.behavior.models.data.configuration.EntryCallFilterRules;
import org.iobserve.analysis.behavior.models.data.configuration.GetLastXSignatureStrategy;
import org.iobserve.analysis.behavior.models.data.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.behavior.models.data.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.feature.IBehaviorCompositeStage;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.stages.data.trace.EventBasedTrace;
import org.iobserve.stages.general.ConfigurationException;

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

    private final PreprocessingCompositeStage preprocessingStage;

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
        final IRepresentativeStrategy representativeStrategy = InstantiationFactory
                .create(IRepresentativeStrategy.class, representativeStrategyClassName, null);
        final boolean keepEmptyTransitions = true;

        this.preprocessingStage = new PreprocessingCompositeStage(configuration);

        final UserSessionModelAggregator userSessionModelAggregator = new UserSessionModelAggregator();

        final BehaviorModelPrepratation behaviorModelPreparation = new BehaviorModelPrepratation(modelGenerationFilter,
                representativeStrategy, keepEmptyTransitions);

        /** aggregation setup. */
        final String namePrefix = configuration.getStringProperty(EMBehaviorCompositeStage.NAME_PREFIX);
        final ISignatureCreationStrategy signatureCreationStrategy = new GetLastXSignatureStrategy(Integer.MAX_VALUE);
        final String visualizationUrl = configuration.getStringProperty(EMBehaviorCompositeStage.OUTPUT_URL);
        final EMBehaviorModelAggregation tBehaviorModelAggregation = new EMBehaviorModelAggregation(namePrefix,
                visualizationUrl, signatureCreationStrategy);

        this.connectPorts(this.preprocessingStage.getSessionOutputPort(),
                userSessionModelAggregator.getUserSessionInputPort());
        this.connectPorts(this.preprocessingStage.getTimerOutputPort(),
                userSessionModelAggregator.getTimeTriggerInputPort());
        this.connectPorts(userSessionModelAggregator.getOutputPort(), behaviorModelPreparation.getInputPort());
        this.connectPorts(behaviorModelPreparation.getOutputPort(), tBehaviorModelAggregation.getInputPort());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.feature.IBehaviorCompositeStage#getEventBasedTracePort()
     */
    @Override
    public InputPort<EventBasedTrace> getEventBasedTracePort() {
        return this.preprocessingStage.getTraceInputPort();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.analysis.feature.IBehaviorCompositeStage#getSessionEventInputPort()
     */
    @Override
    public InputPort<ISessionEvent> getSessionEventInputPort() {
        return this.preprocessingStage.getSessionEventInputPort();
    }

}
