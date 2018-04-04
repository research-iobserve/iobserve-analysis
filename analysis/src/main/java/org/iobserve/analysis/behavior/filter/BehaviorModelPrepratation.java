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
package org.iobserve.analysis.behavior.filter;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;
import teetime.stage.basic.merger.Merger;
import teetime.stage.basic.merger.strategy.BlockingBusyWaitingRoundRobinMergerStrategy;
import teetime.stage.basic.merger.strategy.IMergerStrategy;

import org.iobserve.analysis.behavior.models.data.configuration.EntryCallFilterRules;
import org.iobserve.analysis.behavior.models.data.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.data.UserSessionCollectionModel;

import weka.core.Instances;

/**
 *
 * @author Christoph Dornieden
 */

public class BehaviorModelPrepratation extends CompositeStage {

    private final SessionOperationCleanupFilter sessionOperationCleanupFilter;

    private final CreateWekaInstancesStage tInstanceTransformations;

    /**
     * Setup model preparation steps. Used with EM and XMeans.
     *
     * @param entryCallFilterRules
     *            rules applied to entry calls to filter utility calls out of the stream
     * @param representativeStrategy
     *            computes how call information is represented
     * @param keepEmptyTransitions
     *            keep transitions which are empty
     */
    public BehaviorModelPrepratation(final EntryCallFilterRules entryCallFilterRules,
            final IRepresentativeStrategy representativeStrategy, final boolean keepEmptyTransitions) {
        this.sessionOperationCleanupFilter = new SessionOperationCleanupFilter(entryCallFilterRules);
        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        final Distributor<UserSessionCollectionModel> distributor = new Distributor<>(strategy);

        final IMergerStrategy mergerStrategy = new BlockingBusyWaitingRoundRobinMergerStrategy();
        final Merger<Object> merger = new Merger<>(mergerStrategy);

        final BehaviorModelTableGenerationStage tBehaviorModelTableGeneration = new BehaviorModelTableGenerationStage(
                representativeStrategy, keepEmptyTransitions);

        final BehaviorModelPreperation tBehaviorModelPreperation = new BehaviorModelPreperation(keepEmptyTransitions);

        this.tInstanceTransformations = new CreateWekaInstancesStage();

        this.connectPorts(this.sessionOperationCleanupFilter.getOutputPort(), distributor.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), tBehaviorModelTableGeneration.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), merger.getNewInputPort());

        this.connectPorts(tBehaviorModelTableGeneration.getOutputPort(), merger.getNewInputPort());

        this.connectPorts(merger.getOutputPort(), tBehaviorModelPreperation.getInputPort());
        this.connectPorts(tBehaviorModelPreperation.getOutputPort(), this.tInstanceTransformations.getInputPort());

    }

    /**
     * get matching input port.
     *
     * @return input port
     */
    public InputPort<UserSessionCollectionModel> getInputPort() {
        return this.sessionOperationCleanupFilter.getInputPort();
    }

    /**
     * get suitable output port.
     *
     * @return outputPort
     */
    public OutputPort<Instances> getOutputPort() {
        return this.tInstanceTransformations.getOutputPort();
    }

}
