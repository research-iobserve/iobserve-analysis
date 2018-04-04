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

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.iobserve.analysis.configurations.ConfigurationKeys;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.stages.general.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;
import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * This stage performs Similarity Matching when configured properly
 *
 * @author J
 *
 */
@ReceiveUnfilteredConfiguration
public class SimilarityMatchingStage extends CompositeStage implements IClassificationStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorCompositeStage.class);

    private final InputPort<UserSession> sessionInputPort;
    private final InputPort<Long> timerInputPort;
    private final OutputPort<BehaviorModel[]> outputPort;

    /**
     * Constructor
     *
     * @param configuration
     *
     * @throws ConfigurationException
     *             Throws an exception if configuration lacks parameters
     */
    public SimilarityMatchingStage(final Configuration configuration) throws ConfigurationException {
        /** Instantiate configurable objects/properties */

        /** For TVectorization */
        final String structureMetricClassName = configuration
                .getStringProperty(ConfigurationKeys.SIM_MATCH_STRUCTURE_STRATEGY);
        if (structureMetricClassName.isEmpty()) {
            SimilarityMatchingStage.LOGGER.error("Initialization incomplete: No structure metric strategy specified.");
            throw new ConfigurationException("Initialization incomplete: No structure metric strategy specified.");
        }
        final IStructureMetricStrategy structureMetric = InstantiationFactory.create(IStructureMetricStrategy.class,
                structureMetricClassName, null);

        /** For TVectorization */
        final String parameterMetricClassName = configuration
                .getStringProperty(ConfigurationKeys.SIM_MATCH_PARAMETER_STRATEGY);
        if (parameterMetricClassName.isEmpty()) {
            SimilarityMatchingStage.LOGGER.error("Initialization incomplete: No parameter metric strategy specified.");
            throw new ConfigurationException("Initialization incomplete: No parameter metric strategy specified.");
        }
        final IParameterMetric parameterMetric = InstantiationFactory.create(IParameterMetric.class,
                parameterMetricClassName, null);

        /** For TModelGeneration */
        final String modelStrategyClassName = configuration
                .getStringProperty(ConfigurationKeys.SIM_MATCH_MODEL_STRATEGY);
        if (modelStrategyClassName.isEmpty()) {
            SimilarityMatchingStage.LOGGER.error("Initialization incomplete: No model generation strategy specified.");
            throw new ConfigurationException("Initialization incomplete: No model generation strategy specified.");
        }
        final IModelGenerationStrategy modelGenerationStrategy = InstantiationFactory
                .create(IModelGenerationStrategy.class, modelStrategyClassName, null);

        /** For TGroupingStage */
        final double parameterSimilarityRadius = configuration
                .getDoubleProperty(ConfigurationKeys.SIM_MATCH_RADIUS_PARAMS, -1);
        if (parameterSimilarityRadius < 0) {
            SimilarityMatchingStage.LOGGER.error("Initialization incomplete: No parameter similarity radius specified.");
            throw new ConfigurationException("Initialization incomplete: No parameter similarity radius specified.");
        }

        final double structureSimilarityRadius = configuration
                .getDoubleProperty(ConfigurationKeys.SIM_MATCH_RADIUS_STRUCTURE, -1);
        if (structureSimilarityRadius < 0) {
            SimilarityMatchingStage.LOGGER.error("Initialization incomplete: No structure similarity radius specified.");
            throw new ConfigurationException("Initialization incomplete: No structure similarity radius specified.");
        }

        /** Create individual stages */
        final SessionToModelTransformation sessionToModel = new SessionToModelTransformation();
        final VectorizationStage vectorization = new VectorizationStage(structureMetric, parameterMetric);
        vectorization.declareActive();
        final GroupingBehaviorStage groupingStage = new GroupingBehaviorStage(structureSimilarityRadius, parameterSimilarityRadius);
        final ModelGenerationTransformation modelGeneration = new ModelGenerationTransformation(modelGenerationStrategy);
        modelGeneration.declareActive();

        /** Connect ports */
        this.sessionInputPort = sessionToModel.getInputPort();
        this.timerInputPort = vectorization.getTimerInputPort();
        this.connectPorts(sessionToModel.getOutputPort(), vectorization.getModelInputPort());
        this.connectPorts(vectorization.getVectorsOutputPort(), groupingStage.getInputPort());
        this.connectPorts(groupingStage.getOutputPort(), modelGeneration.getGroupsInputPort());
        this.connectPorts(vectorization.getModelsOutputPort(), modelGeneration.getModelsInputPort());
        this.outputPort = modelGeneration.getOutputPort();
    }

    @Override
    public InputPort<UserSession> getSessionInputPort() {
        return this.sessionInputPort;
    }

    @Override
    public InputPort<Long> getTimerInputPort() {
        return this.timerInputPort;
    }

    @Override
    public OutputPort<BehaviorModel[]> getOutputPort() {
        return this.outputPort;
    }
}
