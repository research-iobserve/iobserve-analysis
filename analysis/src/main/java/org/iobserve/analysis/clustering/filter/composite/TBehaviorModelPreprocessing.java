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
package org.iobserve.analysis.clustering.filter.composite;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;
import teetime.stage.basic.merger.Merger;
import teetime.stage.basic.merger.strategy.BlockingBusyWaitingRoundRobinMergerStrategy;
import teetime.stage.basic.merger.strategy.IMergerStrategy;

import org.iobserve.analysis.clustering.filter.TBehaviorModelPreperation;
import org.iobserve.analysis.clustering.filter.TBehaviorModelTableGeneration;
import org.iobserve.analysis.clustering.filter.TEntryCallSequenceFilter;
import org.iobserve.analysis.clustering.filter.TInstanceTransformations;
import org.iobserve.analysis.clustering.filter.models.configuration.BehaviorModelConfiguration;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;

import weka.core.Instances;

/**
 *
 * @author Christoph Dornieden
 */

public class TBehaviorModelPreprocessing extends CompositeStage {

    private final Distributor<EntryCallSequenceModel> distributor;
    private final Merger<Object> merger;
    private final TEntryCallSequenceFilter tEntryCallSequenceFilter;
    private final TBehaviorModelTableGeneration tBehaviorModelTableGeneration;
    private final TBehaviorModelPreperation tBehaviorModelPreperation;

    private final TInstanceTransformations tInstanceTransformations;

    /**
     * constructor.
     *
     * @param configuration
     *            model configuration
     */
    public TBehaviorModelPreprocessing(final BehaviorModelConfiguration configuration) {

        this.tEntryCallSequenceFilter = new TEntryCallSequenceFilter(configuration.getModelGenerationFilter());
        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        this.distributor = new Distributor<>(strategy);

        final IMergerStrategy mergerStrategy = new BlockingBusyWaitingRoundRobinMergerStrategy();
        this.merger = new Merger<>(mergerStrategy);

        this.tBehaviorModelTableGeneration = new TBehaviorModelTableGeneration(
                configuration.getRepresentativeStrategy(), configuration.keepEmptyTransitions());

        this.tBehaviorModelPreperation = new TBehaviorModelPreperation(configuration.keepEmptyTransitions());

        this.tInstanceTransformations = new TInstanceTransformations();

        this.connectPorts(this.tEntryCallSequenceFilter.getOutputPort(), this.distributor.getInputPort());
        this.connectPorts(this.distributor.getNewOutputPort(), this.tBehaviorModelTableGeneration.getInputPort());
        this.connectPorts(this.distributor.getNewOutputPort(), this.merger.getNewInputPort());

        this.connectPorts(this.tBehaviorModelTableGeneration.getOutputPort(), this.merger.getNewInputPort());

        this.connectPorts(this.merger.getOutputPort(), this.tBehaviorModelPreperation.getInputPort());
        this.connectPorts(this.tBehaviorModelPreperation.getOutputPort(), this.tInstanceTransformations.getInputPort());

    }

    /**
     * get matching input port.
     *
     * @return input port
     */
    public InputPort<EntryCallSequenceModel> getInputPort() {
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
