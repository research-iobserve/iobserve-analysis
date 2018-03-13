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
package org.iobserve.analysis.clustering.filter.similaritymatching;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;
import org.iobserve.analysis.configurations.MJConfiguration;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.stages.general.ConfigurationException;
import org.iobserve.stages.source.TimeTriggerFilter;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public class TSimilarityMatching extends CompositeStage {
    private final InputPort<UserSession> sessionInputPort;
    private final InputPort<Long> timerInputPort;
    private final OutputPort<BehaviorModel[]> outputPort;

    public TSimilarityMatching(final IStructureMetricStrategy structureMetric,
            final IParameterMetricStrategy parameterMetric, final IModelGenerationStrategy modelGenerationStrategy,
            final double similarityRadius) {
        /**
         * Create Clock. Default value of -1 was selected because the method
         * getLongProperty states it will return "null" if no value was specified, but
         * long is a primitive type ...
         */
        final Long triggerInterval = configuration.getLongProperty(MJConfiguration.TRIGGER_INTERVAL, -1);
        if (triggerInterval < 0) {
            MJConfiguration.LOGGER.error("Initialization incomplete: No time trigger interval specified.");
            throw new ConfigurationException("Initialization incomplete: No time trigger interval specified.");
        }
        final TimeTriggerFilter sessionCollectionTimer = new TimeTriggerFilter(triggerInterval);

        /** Create individual stages */
        final TSessionToModel sessionToModel = new TSessionToModel();
        final TVectorization vectorization = new TVectorization(structureMetric, parameterMetric);
        final TGroupingStage groupingStage = new TGroupingStage(similarityRadius);
        final TModelGeneration modelGeneration = new TModelGeneration(modelGenerationStrategy);

        /** Connect ports */
        this.sessionInputPort = sessionToModel.getInputPort();
        this.timerInputPort = vectorization.getTimerInputPort();
        this.connectPorts(sessionToModel.getOutputPort(), vectorization.getModelInputPort());
        this.connectPorts(vectorization.getVectorsOutputPort(), groupingStage.getInputPort());
        this.connectPorts(groupingStage.getOutputPort(), modelGeneration.getGroupsInputPort());
        this.connectPorts(vectorization.getModelsOutputPort(), modelGeneration.getModelsInputPort());
        this.outputPort = modelGeneration.getOutputPort();
    }

    public InputPort<UserSession> getSessionInputPort() {
        return this.sessionInputPort;
    }

    public InputPort<Long> getTimerInputPort() {
        return this.timerInputPort;
    }

    public OutputPort<BehaviorModel[]> getOutputPort() {
        return this.outputPort;
    }
}
