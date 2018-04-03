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

import org.iobserve.analysis.behavior.filter.models.configuration.BehaviorModelConfiguration;
import org.iobserve.analysis.behavior.filter.models.configuration.EntryCallFilterRules;
import org.iobserve.analysis.behavior.filter.models.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.data.UserSessionCollectionModel;

import weka.core.Instances;

/**
 *
 * @author Christoph Dornieden
 */

public class TBehaviorModelPreprocessing extends CompositeStage {

    private final SessionOperationCleanupFilter tEntryCallSequenceFilter;

    private final TInstanceTransformations tInstanceTransformations;

    /**
     * constructor.
     *
     * @param configuration
     *            model configuration
     */
    public TBehaviorModelPreprocessing(final BehaviorModelConfiguration configuration) {
        this(configuration.getModelGenerationFilter(), configuration.getRepresentativeStrategy(),
                configuration.isKeepEmptyTransitions());
    }

    public TBehaviorModelPreprocessing(final EntryCallFilterRules modelGenerationFilter,
            final IRepresentativeStrategy representativeStrategy, final boolean keepEmptyTransitions) {
        this.tEntryCallSequenceFilter = new SessionOperationCleanupFilter(modelGenerationFilter);
        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        final Distributor<UserSessionCollectionModel> distributor = new Distributor<>(strategy);

        final IMergerStrategy mergerStrategy = new BlockingBusyWaitingRoundRobinMergerStrategy();
        final Merger<Object> merger = new Merger<>(mergerStrategy);

        final TBehaviorModelTableGeneration tBehaviorModelTableGeneration = new TBehaviorModelTableGeneration(
                representativeStrategy, keepEmptyTransitions);

        final TBehaviorModelPreperation tBehaviorModelPreperation = new TBehaviorModelPreperation(keepEmptyTransitions);

        this.tInstanceTransformations = new TInstanceTransformations();

        this.connectPorts(this.tEntryCallSequenceFilter.getOutputPort(), distributor.getInputPort());
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
        return this.tEntryCallSequenceFilter.getInputPort();
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
