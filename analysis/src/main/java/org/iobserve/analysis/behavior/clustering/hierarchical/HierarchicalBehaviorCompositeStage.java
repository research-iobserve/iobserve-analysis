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

package org.iobserve.analysis.behavior.clustering.hierarchical;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

import org.iobserve.analysis.behavior.clustering.em.UserSessionModelAggregator;
import org.iobserve.analysis.behavior.filter.BehaviorModelPrepratationStage;
import org.iobserve.analysis.behavior.filter.UserSessionGeneratorCompositeStage;
import org.iobserve.analysis.behavior.models.data.configuration.EntryCallFilterRules;
import org.iobserve.analysis.behavior.models.data.configuration.GetLastXSignatureStrategy;
import org.iobserve.analysis.behavior.models.data.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.behavior.models.data.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.feature.IBehaviorCompositeStage;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.stages.data.trace.EventBasedTrace;

/**
 * Composite Stage for the Hierarchical Clustering Behavior.
 *
 * @author SL
 * @since 0.0.3
 */
@ReceiveUnfilteredConfiguration
public class HierarchicalBehaviorCompositeStage extends CompositeStage implements IBehaviorCompositeStage {
    private static final String PREFIX = HierarchicalBehaviorCompositeStage.class.getCanonicalName() + ".";

    private static final String OUTPUT_URL = HierarchicalBehaviorCompositeStage.PREFIX + "outputUrl";

    private static final String NAME_PREFIX = HierarchicalBehaviorCompositeStage.PREFIX + "prefix";

    private static final String DISTANCE_METRIC = HierarchicalBehaviorCompositeStage.PREFIX + "distanceMetric";

    private static final String SEL_METHOD = HierarchicalBehaviorCompositeStage.PREFIX + "clusterSelectionMethod";

    private static final String LINKAGE = HierarchicalBehaviorCompositeStage.PREFIX + "linkage";

    private static final String REPRESENTATIVE_STRATEGY = HierarchicalBehaviorCompositeStage.PREFIX
            + "representativeStrategy";

    private final UserSessionGeneratorCompositeStage userSessionGeneratorCompositeStage;

    /**
     * Create hierarchical behavior clustering composite stage.
     *
     * @param configuration
     *            Configuration for all stages.
     * @throws ConfigurationException
     *             On errors with the configuration.
     */
    public HierarchicalBehaviorCompositeStage(final Configuration configuration) throws ConfigurationException {
        final EntryCallFilterRules modelGenerationFilter = new EntryCallFilterRules(false).addFilterRule(".*");
        final String representativeStrategyClassName = configuration
                .getStringProperty(HierarchicalBehaviorCompositeStage.REPRESENTATIVE_STRATEGY);
        final IRepresentativeStrategy representativeStrategy = InstantiationFactory
                .create(IRepresentativeStrategy.class, representativeStrategyClassName, null);
        final boolean keepEmptyTransitions = true;

        this.userSessionGeneratorCompositeStage = new UserSessionGeneratorCompositeStage(configuration);

        final UserSessionModelAggregator userSessionModelAggregator = new UserSessionModelAggregator();
        userSessionModelAggregator.declareActive();

        final BehaviorModelPrepratationStage hierarchicalBehaviorModelPreparation = new BehaviorModelPrepratationStage(
                modelGenerationFilter, representativeStrategy, keepEmptyTransitions);

        /** aggregation setup. */
        final String namePrefix = configuration.getStringProperty(HierarchicalBehaviorCompositeStage.NAME_PREFIX);
        final ISignatureCreationStrategy signatureCreationStrategy = new GetLastXSignatureStrategy(Integer.MAX_VALUE);
        final String visualizationUrl = configuration.getStringProperty(HierarchicalBehaviorCompositeStage.OUTPUT_URL);
        final String distanceMetric = configuration
                .getStringProperty(HierarchicalBehaviorCompositeStage.DISTANCE_METRIC);
        final String clusterSelectionMethod = configuration
                .getStringProperty(HierarchicalBehaviorCompositeStage.SEL_METHOD);
        final String linkage = configuration.getStringProperty(HierarchicalBehaviorCompositeStage.LINKAGE);

        final HierarchicalBehaviorModelAggregation hierarchicalBehaviorModelAggregation = new HierarchicalBehaviorModelAggregation(
                namePrefix, visualizationUrl, signatureCreationStrategy, distanceMetric, clusterSelectionMethod,
                linkage);

        this.connectPorts(this.userSessionGeneratorCompositeStage.getSessionOutputPort(),
                userSessionModelAggregator.getUserSessionInputPort());
        this.connectPorts(this.userSessionGeneratorCompositeStage.getTimerOutputPort(),
                userSessionModelAggregator.getTimeTriggerInputPort());
        this.connectPorts(userSessionModelAggregator.getOutputPort(),
                hierarchicalBehaviorModelPreparation.getInputPort());
        this.connectPorts(hierarchicalBehaviorModelPreparation.getOutputPort(),
                hierarchicalBehaviorModelAggregation.getInputPort());

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
