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

import org.iobserve.analysis.clustering.filter.models.configuration.BehaviorModelConfiguration;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

/**
 *
 * @author Christoph Dornieden
 *
 */
public class TBehaviorModel extends CompositeStage {

    private final InputPort<EntryCallSequenceModel> inputPort;

    /**
     * constructor.
     *
     * @param configuration
     *            configuration
     */
    public TBehaviorModel(final BehaviorModelConfiguration configuration) {
        final TBehaviorModelPreprocessing tBehaviorModelProcessing = new TBehaviorModelPreprocessing(configuration);
        this.inputPort = tBehaviorModelProcessing.getInputPort();

        final TBehaviorModelAggregation tBehaviorModelAggregation = new TBehaviorModelAggregation(configuration);

        this.connectPorts(tBehaviorModelProcessing.getOutputPort(), tBehaviorModelAggregation.getInputPort());

    }

    public InputPort<EntryCallSequenceModel> getInputPort() {
        return this.inputPort;
    }

}
