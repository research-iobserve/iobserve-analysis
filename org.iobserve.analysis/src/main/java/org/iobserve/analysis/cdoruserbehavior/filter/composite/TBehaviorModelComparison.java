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
package org.iobserve.analysis.cdoruserbehavior.filter.composite;

import org.iobserve.analysis.cdoruserbehavior.filter.TIObserveUBM;
import org.iobserve.analysis.cdoruserbehavior.filter.TUsageModel;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.BehaviorModelConfiguration;
import org.iobserve.analysis.filter.TEntryEventSequence;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;

/**
 * Distribute EntryCallSequences to both BehaviorModel generations for evaluation purposes
 *
 *
 * @author Christoph Dornieden
 *
 */
public class TBehaviorModelComparison extends CompositeStage {

    private final Distributor<EntryCallSequenceModel> distributor;

    /**
     * constructor
     *
     * @param configuration
     *            configuration
     * @param correspondenceModel
     *            correspondenceModel
     * @param usageModelProvider
     *            usageModelProvicer
     * @param repositoryModelProvider
     *            repositoryModelProvider
     * @param varianceOfUserGroups
     *            varianceOfUserGroups
     * @param thinkTime
     *            thinkTime
     * @param closedWorkload
     *            closedWorkload
     */

    public TBehaviorModelComparison(BehaviorModelConfiguration configuration, ICorrespondence correspondenceModel,
            UsageModelProvider usageModelProvider, RepositoryModelProvider repositoryModelProvider,
            int varianceOfUserGroups, int thinkTime, boolean closedWorkload) {

        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        this.distributor = new Distributor<>(strategy);

        final TBehaviorModel tBehaviorModel = new TBehaviorModel(configuration);

        final TEntryEventSequence tEntryEventSequence = new TEntryEventSequence(correspondenceModel, usageModelProvider,
                repositoryModelProvider, varianceOfUserGroups, thinkTime, closedWorkload);

        final TUsageModel tUsageModel = new TUsageModel();
        final TIObserveUBM tIObserveUBM = new TIObserveUBM();

        this.connectPorts(this.distributor.getNewOutputPort(), tBehaviorModel.getInputPort());
        this.connectPorts(this.distributor.getNewOutputPort(), tEntryEventSequence.getInputPort());
        this.connectPorts(tEntryEventSequence.getOutputPort(), tUsageModel.getInputPort());
        this.connectPorts(tUsageModel.getOutputPort(), tIObserveUBM.getInputPort());

    }

    /**
     * getter
     *
     * @return input port
     */
    public InputPort<EntryCallSequenceModel> getInputPort() {
        return this.distributor.getInputPort();
    }

}
