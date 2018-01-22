/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.traces;

import java.io.IOException;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.userbehavior.UserBehaviorTransformation;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.RepositoryLookupModelProvider;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Represents the TEntryEventSequence Transformation in the paper <i>Run-time Architecture Models
 * for Dynamic Adaptation and Evolution of Cloud Applications</i> Triggers the user behavior
 * modeling process that creates a PCM usage model from an EntryCallSequenceModel.
 *
 * @author Robert Heinrich
 * @author Alessandro Guisa
 * @author Nicolas Boltz
 * @author David Peter
 * @author Christoph Dornieden
 *
 * @version 1.0
 */
public final class TEntryEventSequence extends AbstractConsumerStage<EntryCallSequenceModel> {

    /** reference to the correspondence model. */
    private final ICorrespondence correspondenceModel;
    /** usage model provider. */
    private final ModelProvider<UsageModel> usageModelProvider;

    private final int varianceOfUserGroups;

    private final int thinkTime;

    private final boolean closedWorkload;

    private final OutputPort<UsageModel> outputPort;

    private final OutputPort<Boolean> outputPortSnapshot;
    private final RepositoryLookupModelProvider repositoryLookupModel;

    private static final Log LOG = LogFactory.getLog(TEntryEventSequence.class);

    /**
     * Create a entry event sequence filter.
     *
     * @param correspondenceModel
     *            the model mapping model elements to code
     * @param usageModelProvider
     *            provider for the usage model
     * @param repositoryLookupModel
     *            repository lookup model provider
     * @param varianceOfUserGroups
     *            variance of user groups for the behavior detection
     * @param thinkTime
     *            think time used for behavior detection
     * @param closedWorkload
     *            type of workload
     */
    public TEntryEventSequence(final ICorrespondence correspondenceModel,
            final ModelProvider<UsageModel> usageModelProvider,
            final RepositoryLookupModelProvider repositoryLookupModel, final int varianceOfUserGroups,
            final int thinkTime, final boolean closedWorkload) {
        this.correspondenceModel = correspondenceModel;
        this.usageModelProvider = usageModelProvider;
        this.repositoryLookupModel = repositoryLookupModel;

        this.varianceOfUserGroups = varianceOfUserGroups;
        this.thinkTime = thinkTime;
        this.closedWorkload = closedWorkload;

        this.outputPort = this.createOutputPort();
        this.outputPortSnapshot = this.createOutputPort();
    }

    @Override
    protected void execute(final EntryCallSequenceModel entryCallSequenceModel) {
        // Resets the current usage model
        final UsageModel model = this.usageModelProvider.readRootComponent(UsageModel.class);
        int numberOfUserGroups = model.getUsageScenario_UsageModel().size();
        TEntryEventSequence.LOG.debug("EntryEventSequence found: numberOfUserGroups before: " + numberOfUserGroups);

        // Executes the user behavior modeling procedure
        final UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(entryCallSequenceModel,
                numberOfUserGroups, this.varianceOfUserGroups, this.closedWorkload, this.thinkTime,
                this.repositoryLookupModel, this.correspondenceModel);
        try {
            behaviorModeling.modelUserBehavior();
            model.getUsageScenario_UsageModel().clear();
            model.getUserData_UsageModel().clear();

            final UsageModel newModel = behaviorModeling.getPcmUsageModel();

            model.getUsageScenario_UsageModel().addAll(newModel.getUsageScenario_UsageModel());
            model.getUserData_UsageModel().addAll(newModel.getUserData_UsageModel());

            numberOfUserGroups = model.getUsageScenario_UsageModel().size();
            TEntryEventSequence.LOG.debug("Model changed: numberOfUserGroups after: " + numberOfUserGroups);

        } catch (final IOException e) {
            e.printStackTrace();
        }

        // Sets the new usage model within iObserve
        this.usageModelProvider.clearGraph();
        this.usageModelProvider.createComponent(model);

        this.outputPort.send(behaviorModeling.getPcmUsageModel());
        this.outputPortSnapshot.send(false);
    }

    public OutputPort<UsageModel> getOutputPort() {
        return this.outputPort;
    }

    /**
     * @return output port for snapshot stage
     */
    public OutputPort<Boolean> getOutputPortSnapshot() {
        return this.outputPortSnapshot;
    }
}
