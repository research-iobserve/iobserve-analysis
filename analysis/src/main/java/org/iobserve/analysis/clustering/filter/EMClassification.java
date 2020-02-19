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
package org.iobserve.analysis.clustering.filter;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.analysis.behavior.clustering.birch.SessionsToInstancesStage;
import org.iobserve.analysis.behavior.clustering.em.EMClusteringProcess;
import org.iobserve.analysis.behavior.clustering.em.ExpectationMaximizationClustering;
import org.iobserve.analysis.behavior.filter.BehaviorModelCreationStage;
import org.iobserve.analysis.behavior.filter.IClassificationStage;
import org.iobserve.analysis.behavior.models.data.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.analysis.systems.jpetstore.JPetstoreRepresentativeStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles the classification process with the em algorithm. Transforms user sessions to
 * behavior models.
 *
 * @author Melf Lorenzen
 *
 * @deprecated must be integrated with the actual EM clustering setup.
 */
@Deprecated
public class EMClassification extends CompositeStage implements IClassificationStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(EMClassification.class);
    private final InputPort<UserSession> sessionInputPort;
    private final InputPort<Long> timerInputPort;
    private final OutputPort<BehaviorModel> outputPort;

    public EMClassification(final Configuration configuration) throws ConfigurationException {
        /** Get keep time for user sessions */
        final long keepTime = configuration.getLongProperty(ConfigurationKeys.KEEP_TIME, -1);
        if (keepTime < 0) {
            EMClassification.LOGGER.error("Initialization incomplete: No keep time interval specified.");
            throw new ConfigurationException("Initialization incomplete: No keep time interval specified.");
        }

        final int minCollectionSize = configuration.getIntProperty(ConfigurationKeys.MIN_SIZE, -1);
        if (minCollectionSize < 0) {
            EMClassification.LOGGER.error("Initialization incomplete: No min size for user sessions specified.");
            throw new ConfigurationException("Initialization incomplete: No min size for user sessions specified.");
        }

        final boolean keepEmptyTransitions = configuration.getBooleanProperty(ConfigurationKeys.KEEP_EMPTY_TRANS, true);

        /** Todo: incoperate to config */
        final IRepresentativeStrategy representativeStrategy = new JPetstoreRepresentativeStrategy();

        final SessionsToInstancesStage sessionsToInstances = new SessionsToInstancesStage(keepTime, minCollectionSize,
                representativeStrategy, keepEmptyTransitions);
        final EMClusteringProcess tVectorQuantizationClustering = new EMClusteringProcess(
                new ExpectationMaximizationClustering());
        final BehaviorModelCreationStage behaviorModelCreationStage = new BehaviorModelCreationStage("EM-");

        this.sessionInputPort = sessionsToInstances.getSessionInputPort();
        this.timerInputPort = sessionsToInstances.getTimerInputPort();
        this.outputPort = behaviorModelCreationStage.getOutputPort();

        this.connectPorts(sessionsToInstances.getOutputPort(), tVectorQuantizationClustering.getInputPort());
        this.connectPorts(tVectorQuantizationClustering.getOutputPort(), behaviorModelCreationStage.getInputPort());
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