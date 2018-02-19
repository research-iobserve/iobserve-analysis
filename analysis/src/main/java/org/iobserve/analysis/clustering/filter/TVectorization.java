package org.iobserve.analysis.clustering.filter;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.clustering.filter.models.BehaviorModel;

public class TVectorization extends AbstractStage {
    private final InputPort<BehaviorModel> modelInputPort = super.createInputPort();
    private final InputPort<Long> timerInputPort = super.createInputPort();
      private final OutputPort<Double[][]> outputPort = this.createOutputPort();
      
      private IStructureMetricStrategy structureMetric;
      private IParameterMetricStrategy parameterMetric;
      
      private List<BehaviorModel> models = new ArrayList<BehaviorModel>();
      private List<List<Double>> distanceVectors = new ArrayList<List<Double>>();
      
      public TVectorization(final IStructureMetricStrategy structureMetric, final IParameterMetricStrategy parameterMetric) {
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
        Double[][] vectorArray = new Double[this.distanceVectors.size()][];
        int i = 0;
        for (List<Double> v: this.distanceVectors) {
          vectorArray[++i] = v.toArray(new Double[v.size()]);
        }
        
        this.outputPort.send(vectorArray);
        
        this.clearModels();
      }
      }
    
    private void clearModels() {
      this.models = new ArrayList<BehaviorModel>();
      this.distanceVectors = new ArrayList<List<Double>>();
    }
    
    private void vectorizeModel(final BehaviorModel newModel) {
        List<Double> newDistanceVector = new ArrayList<Double>();
        
        Iterator<BehaviorModel> iteratorModels = this.models.iterator();
        Iterator<List<Double>> iteratorVectors = this.distanceVectors.iterator();
        while(iteratorModels.hasNext()) {
          BehaviorModel model = iteratorModels.next();
          List<Double> currentDistanceVector = iteratorVectors.next();
          
          double structureDistance = this.structureMetric.getDistance(model, newModel);
          double parameterDistance = this.parameterMetric.getDistance(model, newModel);
          
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
    
      public OutputPort<Double[][]> getOutputPort() {
        return this.outputPort;
      }
    }