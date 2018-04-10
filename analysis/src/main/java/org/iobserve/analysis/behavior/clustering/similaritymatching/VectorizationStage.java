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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the vectorization phase of Similarity Matching.
 *
 * @author Jannis Kuckei
 */
public class VectorizationStage extends AbstractStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(VectorizationStage.class);

    private final InputPort<BehaviorModel> modelInputPort = super.createInputPort();
    private final InputPort<Long> timerInputPort = super.createInputPort();
    private final OutputPort<Double[][]> vectorsOutputPort = this.createOutputPort();
    private final OutputPort<BehaviorModel[]> modelsOutputPort = this.createOutputPort();

    private final IStructureMetricStrategy structureMetric;
    private final IParameterMetric parameterMetric;

    private List<BehaviorModel> models = new ArrayList<>();
    private List<List<Double>> distanceVectors = new ArrayList<>();

    private double maximumStructureDistance;
    private double maximumParameterDistance;

    /**
     * Constructor.
     *
     * @param structureMetric
     *            General distance function based on structure of the behavior model
     * @param parameterMetric
     *            Distance function based on call-parameters of behavior model
     */
    public VectorizationStage(final IStructureMetricStrategy structureMetric, final IParameterMetric parameterMetric) {
        super();

        this.structureMetric = structureMetric;
        this.parameterMetric = parameterMetric;
    }

    @Override
    protected void execute() {
        final BehaviorModel model = this.modelInputPort.receive();
        final Long timestamp = this.timerInputPort.receive();

        if (model != null) {
            this.vectorizeModel(model);
            this.models.add(model);
        }

        if (timestamp != null && this.models.size() > 1) {
            /** Convert vectors from List<List<Double>> to Double[][] */
            final Double[][] vectorArray = new Double[this.distanceVectors.size()][];
            int i = 0;
            for (final List<Double> v : this.distanceVectors) {
                vectorArray[i++] = v.toArray(new Double[v.size()]);
            }

            /** Convert models from list to array */
            final BehaviorModel[] modelsArray = this.models.toArray(new BehaviorModel[this.models.size()]);

            /** Send both */
            VectorizationStage.LOGGER.debug(this.vectorsAsJSON());
            VectorizationStage.LOGGER.debug(
                    "Sending vectors and models to next stage (max. parameter radius: {}, max structure radius: {}",
                    this.maximumParameterDistance, this.maximumStructureDistance);
            this.vectorsOutputPort.send(vectorArray);
            this.modelsOutputPort.send(modelsArray);

            /** Clear state to prepare for arrival of new sessions */
            this.clearModels();

        }
    }

    /**
     * Clears inner state of stage to prepare for new models arriving.
     */
    private void clearModels() {
        this.models = new ArrayList<>();
        this.distanceVectors = new ArrayList<>();
        this.maximumStructureDistance = 0;
        this.maximumParameterDistance = 0;
    }

    /**
     * Generates similarity vector for model relative to all currently stored models.
     *
     * @param newModel
     *            The model for which the vector is generated
     */
    private void vectorizeModel(final BehaviorModel newModel) {
        final List<Double> newDistanceVector = new ArrayList<>();

        final Iterator<BehaviorModel> iteratorModels = this.models.iterator();
        final Iterator<List<Double>> iteratorVectors = this.distanceVectors.iterator();
        while (iteratorModels.hasNext()) {
            final BehaviorModel model = iteratorModels.next();
            final List<Double> currentDistanceVector = iteratorVectors.next();

            final double structureDistance = this.structureMetric.getDistance(model, newModel);
            final double parameterDistance = this.parameterMetric.getDistance(model, newModel);

            this.maximumStructureDistance = Math.max(structureDistance, this.maximumStructureDistance);
            this.maximumParameterDistance = Math.max(parameterDistance, this.maximumParameterDistance);

            newDistanceVector.add(structureDistance);
            newDistanceVector.add(parameterDistance);
            currentDistanceVector.add(structureDistance);
            currentDistanceVector.add(parameterDistance);
        }

        // Add distance relative to self
        newDistanceVector.add(0.0);
        newDistanceVector.add(0.0);

        this.distanceVectors.add(newDistanceVector);
    }

    private String vectorsAsJSON() {
        String json = "[";

        final Iterator<List<Double>> vectorIterator = this.distanceVectors.iterator();
        while (vectorIterator.hasNext()) {
            final List<Double> v = vectorIterator.next();
            final Iterator<Double> componentIterator = v.iterator();
            String vecString = "[";
            while (componentIterator.hasNext()) {
                final Double d = componentIterator.next();
                vecString += d + (componentIterator.hasNext() ? ", " : "]");
            }
            json += vecString + (vectorIterator.hasNext() ? ", " : "]");
        }

        return json;
    }

    public InputPort<BehaviorModel> getModelInputPort() {
        return this.modelInputPort;
    }

    public InputPort<Long> getTimerInputPort() {
        return this.timerInputPort;
    }

    public OutputPort<Double[][]> getVectorsOutputPort() {
        return this.vectorsOutputPort;
    }

    public OutputPort<BehaviorModel[]> getModelsOutputPort() {
        return this.modelsOutputPort;
    }
}