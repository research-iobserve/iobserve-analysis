package org.iobserve.analysis.clustering.filter.similaritymatching;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public class TVectorization extends AbstractStage {
    private final InputPort<BehaviorModel> modelInputPort = super.createInputPort();
    private final InputPort<Long> timerInputPort = super.createInputPort();
    private final OutputPort<Double[][]> vectorsOutputPort = this.createOutputPort();
    private final OutputPort<BehaviorModel[]> modelsOutputPort = this.createOutputPort();

    private final IStructureMetricStrategy structureMetric;
    private final IParameterMetricStrategy parameterMetric;

    private List<BehaviorModel> models = new ArrayList<>();
    private List<List<Double>> distanceVectors = new ArrayList<>();

    public TVectorization(final IStructureMetricStrategy structureMetric,
            final IParameterMetricStrategy parameterMetric) {
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
        }

        if (timestamp != null) {
            /** Convert vectors from List<List<Double>> to Double[][] */
            final Double[][] vectorArray = new Double[this.distanceVectors.size()][];
            int i = 0;
            for (final List<Double> v : this.distanceVectors) {
                vectorArray[++i] = v.toArray(new Double[v.size()]);
            }

            /** Convert models from list to array */
            final BehaviorModel[] modelsArray = this.models.toArray(new BehaviorModel[this.models.size()]);

            /** Send both */
            this.vectorsOutputPort.send(vectorArray);
            this.modelsOutputPort.send(modelsArray);

            /** Clear state to prepare for arrival of new sessions */
            this.clearModels();
        }
    }

    private void clearModels() {
        this.models = new ArrayList<>();
        this.distanceVectors = new ArrayList<>();
    }

    private void vectorizeModel(final BehaviorModel newModel) {
        final List<Double> newDistanceVector = new ArrayList<>();

        final Iterator<BehaviorModel> iteratorModels = this.models.iterator();
        final Iterator<List<Double>> iteratorVectors = this.distanceVectors.iterator();
        while (iteratorModels.hasNext()) {
            final BehaviorModel model = iteratorModels.next();
            final List<Double> currentDistanceVector = iteratorVectors.next();

            final double structureDistance = this.structureMetric.getDistance(model, newModel);
            final double parameterDistance = this.parameterMetric.getDistance(model, newModel);

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