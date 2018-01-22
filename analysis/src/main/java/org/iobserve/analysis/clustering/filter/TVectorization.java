package org.iobserve.analysis.clustering.filter;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.clustering.filter.models.BehaviorModelTable;

public class TVectorization extends AbstractConsumerStage<BehaviorModelTable> {
	  private final OutputPort<List<List<Double>>> outputPort = this.createOutputPort();
	  
	  private IStructureMetricStrategy structureMetric;
	  private IParameterMetricStrategy parameterMetric;
	  
	  private List<BehaviorModelTable> tables = new ArrayList<BehaviorModelTable>();
	  private List<List<Double>> distanceVectors = new ArrayList<List<Double>>();
	  
	  public TVectorization(final IStructureMetricStrategy structureMetric, final IParameterMetricStrategy parameterMetric) {
	    super();
	    
	    this.structureMetric = structureMetric;
	    this.parameterMetric = parameterMetric;
	  }
	  
	  @Override
	  protected void execute(final BehaviorModelTable newTable) {
	    List<Double> newDistanceVector = new ArrayList<Double>();
	    
	    Iterator<BehaviorModelTable> iteratorTables = this.tables.iterator();
	    Iterator<List<Double>> iteratorVectors = this.distanceVectors.iterator();
	    while(iteratorTables.hasNext()) {
	      // TODO: Use iterators for both, syntax is wrong anyway
	      BehaviorModelTable table = iteratorTables.next();
	      List<Double> currentDistanceVector = iteratorVectors.next();
	      
	      double structureDistance = this.structureMetric.getDistance(table.getTransitions(), newTable.getTransitions());
	      double parameterDistance = this.parameterMetric.getDistance(table.getSignatures(), newTable.getSignatures());
	      
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
	  
	  @Override
	  public void onTerminating() {
	    this.outputPort.send(this.distanceVectors);

	    super.onTerminating();
	  }
	  
	  public OutputPort<List<List<Double>>> getOutputPort() {
	    return this.outputPort;
	  }
	}