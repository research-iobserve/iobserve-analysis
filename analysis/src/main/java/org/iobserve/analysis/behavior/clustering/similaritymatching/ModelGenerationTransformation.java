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
package org.iobserve.analysis.behavior.clustering.similaritymatching;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the model generation phase of Similarity Matching.
 *
 * @author Jannis Kuckei
 *
 */
public class ModelGenerationTransformation extends AbstractStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelGenerationTransformation.class);

    private final OutputPort<BehaviorModel[]> outputPort = this.createOutputPort();
    private final InputPort<BehaviorModel[]> modelsInputPort = this.createInputPort(BehaviorModel[].class);
    private final InputPort<Integer[][]> groupsInputPort = this.createInputPort();

    private final IModelGenerationStrategy modelGenerationStrategy;
    private BehaviorModel[] models;
    private Integer[][] groups;

    /**
     * Constructor
     *
     * @param modelGenerationStrategy
     *            A strategy that generates a representative model from an array of models
     */
    public ModelGenerationTransformation(final IModelGenerationStrategy modelGenerationStrategy) {
        this.modelGenerationStrategy = modelGenerationStrategy;
    }

    @Override
    protected void execute() {
        final BehaviorModel[] receivedModels = this.modelsInputPort.receive();
        final Integer[][] receivedGroups = this.groupsInputPort.receive();

        if (receivedModels != null) {
            this.models = receivedModels;
        }

        if (receivedGroups != null) {
            this.groups = receivedGroups;
        }

        if (this.models == null || this.groups == null) {
            return;
        }

        final BehaviorModel[] representativeModels = new BehaviorModel[this.groups.length];

        for (int i = 0; i < this.groups.length; i++) {
            /** Put all models from this group into an array */
            final BehaviorModel[] groupModels = new BehaviorModel[this.groups[i].length];
            for (int j = 0; j < groupModels.length; j++) {
                groupModels[j] = this.models[this.groups[i][j]];
            }

            /** Find representative model with strategy */
            representativeModels[i] = this.modelGenerationStrategy.generateModel(groupModels);
            representativeModels[i].setName("model_" + i);
        }

        this.outputPort.send(representativeModels);
        ModelGenerationTransformation.LOGGER.debug("Sent {} generated models", representativeModels.length);

        /** Clear state */
        this.models = null;
        this.groups = null;
    }

    public OutputPort<BehaviorModel[]> getOutputPort() {
        return this.outputPort;
    }

    public InputPort<BehaviorModel[]> getModelsInputPort() {
        return this.modelsInputPort;
    }

    public InputPort<Integer[][]> getGroupsInputPort() {
        return this.groupsInputPort;
    }
}