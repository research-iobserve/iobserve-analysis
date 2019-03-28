/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.behavior.clustering.birch;

import kieker.analysis.common.ConfigurationException;
import kieker.common.configuration.Configuration;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.analysis.behavior.clustering.birch.model.ICFComparisonStrategy;
import org.iobserve.analysis.behavior.filter.BehaviorModelCreationStage;
import org.iobserve.analysis.behavior.filter.IClassificationStage;
import org.iobserve.analysis.behavior.models.data.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.analysis.systems.jpetstore.JPetstoreRepresentativeStrategy;
import org.iobserve.service.InstantiationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles the classification process with the birch algorithm. Transforms user sessions
 * to behavior models.
 *
 * @author Melf Lorenzen
 *
 */
@ReceiveUnfilteredConfiguration
public class BirchClassificaton extends CompositeStage implements IClassificationStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(BirchClassificaton.class);
    private final InputPort<UserSession> sessionInputPort;
    private final InputPort<Long> timerInputPort;

    private final OutputPort<BehaviorModel> outputPort;

    /**
     * Create an birch classification instance.
     *
     * @param configuration
     *            configuration
     * @throws ConfigurationException
     *             on configuration error
     */
    public BirchClassificaton(final Configuration configuration) throws ConfigurationException {
        /** Get keep time for user sessions */
        final long keepTime = configuration.getLongProperty(ConfigurationKeys.KEEP_TIME, -1);
        if (keepTime < 0) {
            BirchClassificaton.LOGGER.error("Initialization incomplete: No keep time interval specified.");
            throw new ConfigurationException("Initialization incomplete: No keep time interval specified.");
        }

        final int minCollectionSize = configuration.getIntProperty(ConfigurationKeys.MIN_SIZE, -1);
        if (minCollectionSize < 0) {
            BirchClassificaton.LOGGER.error("Initialization incomplete: No min size for user sessions specified.");
            throw new ConfigurationException("Initialization incomplete: No min size for user sessions specified.");
        }

        final boolean keepEmptyTransitions = configuration.getBooleanProperty(ConfigurationKeys.KEEP_EMPTY_TRANS, true);

        final boolean useClusterNumberMetric = configuration.getBooleanProperty(ConfigurationKeys.USE_CNM, true);

        /** TODO incorporate into configuration */
        final IRepresentativeStrategy representativeStrategy = new JPetstoreRepresentativeStrategy();

        final String clusterComparisonStrategyClassName = configuration
                .getStringProperty(ConfigurationKeys.CLUSTER_METRIC_STRATEGY);
        BirchClassificaton.LOGGER.error("clusterComparisonStrategyClassName: {}", clusterComparisonStrategyClassName);
        final ICFComparisonStrategy clusterComparisonStrategy = InstantiationFactory.create(ICFComparisonStrategy.class,
                clusterComparisonStrategyClassName, null);
        final String lmethodStrategyClassName = configuration
                .getStringProperty(ConfigurationKeys.LMETHOD_EVAL_STRATEGY);
        BirchClassificaton.LOGGER.error("lmethodStrategyClassName: {}", lmethodStrategyClassName);
        final ILMethodEvalStrategy evalStrategy = InstantiationFactory.create(ILMethodEvalStrategy.class,
                lmethodStrategyClassName, null);

        final double leafThresholdValue = configuration.getDoubleProperty(ConfigurationKeys.LEAF_TH, -1.0);
        if (leafThresholdValue < 0) {
            BirchClassificaton.LOGGER.error("Initialization incomplete: No threshold for leafs specified.");
            throw new ConfigurationException("Initialization incomplete: No threshold for leafs specified.");
        }

        final int maxLeafSize = configuration.getIntProperty(ConfigurationKeys.MAX_LEAF_SIZE, 7);
        if (maxLeafSize < 0) {
            BirchClassificaton.LOGGER.error("Initialization incomplete: No max leaf size specified.");
            throw new ConfigurationException("Initialization incomplete: No max leaf size specified.");
        }

        final int maxNodeSize = configuration.getIntProperty(ConfigurationKeys.MAX_NODE_SIZE, -1);
        if (maxNodeSize < 0) {
            BirchClassificaton.LOGGER.error("Initialization incomplete: No max node size specified.");
            throw new ConfigurationException("Initialization incomplete: No max node size specified..");
        }

        final int maxLeafEntries = configuration.getIntProperty(ConfigurationKeys.MAX_LEAF_ENTRIES, -1);
        if (maxLeafEntries < 0) {
            BirchClassificaton.LOGGER.error("Initialization incomplete: No max number of leaf entries specified.");
            throw new ConfigurationException("Initialization incomplete: No max number of leaf entries specified.");
        }

        final int expectedNumberOfClusters = configuration.getIntProperty(ConfigurationKeys.EXP_NUM_OF_CLUSTERS, -1);
        if (expectedNumberOfClusters < 0) {
            BirchClassificaton.LOGGER.error("Initialization incomplete: No expected numbers of clusters specified.");
            throw new ConfigurationException("Initialization incomplete: No expected numbers of clusters specified.");
        }

        final SessionsToInstancesStage sessionsToInstancesStage = new SessionsToInstancesStage(keepTime,
                minCollectionSize, representativeStrategy, keepEmptyTransitions);
        final BirchClusteringStage birchClustering = new BirchClusteringStage(leafThresholdValue, maxLeafSize,
                maxNodeSize, maxLeafEntries, expectedNumberOfClusters, useClusterNumberMetric,
                clusterComparisonStrategy, evalStrategy);
        final BehaviorModelCreationStage behaviorModelCreationStage = new BehaviorModelCreationStage("birch-");

        this.sessionInputPort = sessionsToInstancesStage.getSessionInputPort();
        this.timerInputPort = sessionsToInstancesStage.getTimerInputPort();
        this.outputPort = behaviorModelCreationStage.getOutputPort();

        this.connectPorts(sessionsToInstancesStage.getOutputPort(), birchClustering.getInputPort());
        this.connectPorts(birchClustering.getOutputPort(), behaviorModelCreationStage.getInputPort());
    }

    /**
     * get matching input port.
     *
     * @return input port
     */

    @Override
    public InputPort<UserSession> getSessionInputPort() {
        return this.sessionInputPort;
    }

    @Override
    public InputPort<Long> getTimerInputPort() {
        return this.timerInputPort;
    }

    /**
     * get suitable output port.
     *
     * @return outputPort
     */
    @Override
    public OutputPort<BehaviorModel> getOutputPort() {
        return this.outputPort;
    }

}
