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

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.common.util.classpath.InstantiationFactory;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.analysis.behavior.filter.IClassificationStage;
import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.stages.general.DecollectorStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This stage performs Similarity Matching when configured properly.
 *
 * @author Jannis
 *
 */
@ReceiveUnfilteredConfiguration
public class SimilarityMatchingStage extends CompositeStage implements IClassificationStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimilarityMatchingStage.class);

    private final InputPort<UserSession> sessionInputPort;
    private final InputPort<Long> timerInputPort;
    private final OutputPort<BehaviorModel> outputPort;

    /**
     * Constructor.
     *
     * @param configuration
     *            configuration for the similarity stage
     *
     * @throws ConfigurationException
     *             Throws an exception if configuration lacks parameters
     */
    public SimilarityMatchingStage(final Configuration configuration) throws ConfigurationException {
        /** Instantiate configurable objects/properties */

        /** For TGroupingStage */
        final double parameterSimilarityRadius = configuration
                .getDoubleProperty(ConfigurationKeys.SIM_MATCH_RADIUS_PARAMS, -1);
        if (parameterSimilarityRadius < 0) {
            SimilarityMatchingStage.LOGGER
                    .error("Initialization incomplete: No parameter similarity radius specified.");
            throw new ConfigurationException("Initialization incomplete: No parameter similarity radius specified.");
        }

        final double structureSimilarityRadius = configuration
                .getDoubleProperty(ConfigurationKeys.SIM_MATCH_RADIUS_STRUCTURE, -1);
        if (structureSimilarityRadius < 0) {
            SimilarityMatchingStage.LOGGER
                    .error("Initialization incomplete: No structure similarity radius specified.");
            throw new ConfigurationException("Initialization incomplete: No structure similarity radius specified.");
        }

        /** For TVectorization */
        final String structureMetricClassName = configuration
                .getStringProperty(ConfigurationKeys.SIM_MATCH_STRUCTURE_STRATEGY);
        if (structureMetricClassName.isEmpty()) {
            SimilarityMatchingStage.LOGGER.error("Initialization incomplete: No structure metric strategy specified.");
            throw new ConfigurationException("Initialization incomplete: No structure metric strategy specified.");
        }
        final IStructureMetricStrategy structureMetric = InstantiationFactory.getInstance(configuration)
                .create(IStructureMetricStrategy.class, structureMetricClassName, null);

        /** For TVectorization */
        final String parameterMetricClassName = configuration
                .getStringProperty(ConfigurationKeys.SIM_MATCH_PARAMETER_STRATEGY);
        if (parameterMetricClassName.isEmpty()) {
            SimilarityMatchingStage.LOGGER.error("Initialization incomplete: No parameter metric strategy specified.");
            throw new ConfigurationException("Initialization incomplete: No parameter metric strategy specified.");
        }
        final IParameterMetric parameterMetric = InstantiationFactory.getInstance(configuration)
                .create(IParameterMetric.class, parameterMetricClassName, null);

        /** For TModelGeneration */
        final String modelStrategyClassName = configuration
                .getStringProperty(ConfigurationKeys.SIM_MATCH_MODEL_STRATEGY);
        if (modelStrategyClassName.isEmpty()) {
            SimilarityMatchingStage.LOGGER.error("Initialization incomplete: No model generation strategy specified.");
            throw new ConfigurationException("Initialization incomplete: No model generation strategy specified.");
        }
        final IModelGenerationStrategy modelGenerationStrategy = InstantiationFactory.getInstance(configuration)
                .create(IModelGenerationStrategy.class, modelStrategyClassName, null);

        final String prefix = "similarity";

        /** Create individual stages */
        final SessionToBehaviorModelTransformation sessionToBehaviorModelTransformation = new SessionToBehaviorModelTransformation(
                prefix);
        final VectorizationStage vectorizationStage = new VectorizationStage(structureMetric, parameterMetric);
        vectorizationStage.declareActive();
        final GroupingBehaviorStage groupingStage = new GroupingBehaviorStage(structureSimilarityRadius,
                parameterSimilarityRadius);
        final ModelGenerationTransformation modelGenerationTransformation = new ModelGenerationTransformation(
                modelGenerationStrategy, prefix);
        modelGenerationTransformation.declareActive();
        final DecollectorStage<BehaviorModel> decollectorStage = new DecollectorStage<>();

        /** Connect ports */
        this.sessionInputPort = sessionToBehaviorModelTransformation.getInputPort();
        this.timerInputPort = vectorizationStage.getTimerInputPort();
        this.connectPorts(sessionToBehaviorModelTransformation.getOutputPort(), vectorizationStage.getModelInputPort());
        this.connectPorts(vectorizationStage.getVectorsOutputPort(), groupingStage.getInputPort());
        this.connectPorts(groupingStage.getOutputPort(), modelGenerationTransformation.getGroupsInputPort());
        this.connectPorts(vectorizationStage.getModelsOutputPort(), modelGenerationTransformation.getModelsInputPort());
        this.connectPorts(modelGenerationTransformation.getOutputPort(), decollectorStage.getInputPort());
        this.outputPort = decollectorStage.getOutputPort();
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
    public OutputPort<BehaviorModel> getOutputPort() {
        return this.outputPort;
    }
}
