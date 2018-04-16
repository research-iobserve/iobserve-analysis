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
package org.iobserve.analysis.clustering.birch;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;
import teetime.stage.basic.merger.Merger;
import teetime.stage.basic.merger.strategy.BlockingBusyWaitingRoundRobinMergerStrategy;

import org.iobserve.analysis.behavior.filter.BehaviorModelPreparation;
import org.iobserve.analysis.behavior.filter.BehaviorModelTableGenerationStage;
import org.iobserve.analysis.behavior.filter.CreateWekaInstancesStage;
import org.iobserve.analysis.behavior.models.data.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.clustering.shared.UserSessionCollector;
import org.iobserve.analysis.data.UserSessionCollectionModel;
import org.iobserve.analysis.session.data.UserSession;

import weka.core.Instances;

/**
 * @author Melf Lorenzen
 *
 */
public class SessionsToInstances extends CompositeStage {

    private final InputPort<UserSession> sessionInputPort;

    private final InputPort<Long> timerInputPort;

    private final OutputPort<Instances> outputPort;

    /**
     * constructor for the SessionsToInstances class.
     *
     * @param keepTime
     *            the time interval to keep user sessions
     * @param minCollectionSize
     *            minimal number of collected user session
     * @param representativeStrategy
     *            representative strategy for behavior model table generation
     * @param keepEmptyTransitions
     *            allows behavior model table generation to keep empty transitions
     */
    public SessionsToInstances(final long keepTime, final int minCollectionSize,
            final IRepresentativeStrategy representativeStrategy, final boolean keepEmptyTransitions) {

        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        final Distributor<UserSessionCollectionModel> distributor = new Distributor<>(strategy);

        final Merger<Object> merger = new Merger<>(new BlockingBusyWaitingRoundRobinMergerStrategy());
        /** Reuse once filters have been adapted to use timetrigger */
        // final CollectUserSessionsFilter collectUserSessionsFilter =
        // new CollectUserSessionsFilter(keepTime, minCollectionSize);

        final UserSessionCollector collectUserSessionsFilter = new UserSessionCollector(minCollectionSize);

        final CreateWekaInstancesStage tInstanceTransformations = new CreateWekaInstancesStage();

        final BehaviorModelTableGenerationStage tBehaviorModelTableGeneration = new BehaviorModelTableGenerationStage(
                representativeStrategy, keepEmptyTransitions);

        final BehaviorModelPreparation tBehaviorModelPreperation = new BehaviorModelPreparation(keepEmptyTransitions);
        final Merger<Long> merger1 = new Merger<>(new BlockingBusyWaitingRoundRobinMergerStrategy());

        this.timerInputPort = merger1.getNewInputPort();
        this.sessionInputPort = collectUserSessionsFilter.getInputPort();
        /** Reuse once filters have been adapted to use timetrigger */
        // this.sessionInputPort = collectUserSessionsFilter.getUserSessionInputPort();
        // this.timerInputPort = collectUserSessionsFilter.getTimeTriggerInputPort();
        this.outputPort = tInstanceTransformations.getOutputPort();

        this.connectPorts(collectUserSessionsFilter.getOutputPort(), distributor.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), tBehaviorModelTableGeneration.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), merger.getNewInputPort());

        this.connectPorts(tBehaviorModelTableGeneration.getOutputPort(), merger.getNewInputPort());

        this.connectPorts(merger.getOutputPort(), tBehaviorModelPreperation.getInputPort());
        this.connectPorts(tBehaviorModelPreperation.getOutputPort(), tInstanceTransformations.getInputPort());
    }

    public InputPort<Long> getTimerInputPort() {
        return this.timerInputPort;
    }

    public InputPort<UserSession> getSessionInputPort() {
        return this.sessionInputPort;
    }

    public OutputPort<Instances> getOutputPort() {
        return this.outputPort;
    }
}
