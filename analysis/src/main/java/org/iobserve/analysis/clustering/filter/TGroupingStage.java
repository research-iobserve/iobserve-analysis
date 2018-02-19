package org.iobserve.analysis.clustering.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

public class TGroupingStage extends AbstractConsumerStage<List<List<Double>>> {
    private final OutputPort<List<List<Integer>>> outputPort = this.createOutputPort();
    
    private double similarityRadius;
    
    private List<List<Integer>> groups = new ArrayList<List<Integer>>();
    
    private List<List<Double>> vectors;
    
    public TGroupingStage(double similarityRadius) {
      super();
      
      // TODO: check arg validity (non-negative)
      this.similarityRadius = similarityRadius;
    }
    
    @Override
    protected void execute(final List<List<Double>> vectors) {
      this.vectors = vectors;
      
      int index = 0;
      Iterator<List<Double>> iterator = vectors.iterator();
      List<Double> vector;
      
      while (iterator.hasNext()) {
        vector = iterator.next();
        List<Integer> group = findGroup(vector);
        
        if (group == null) {
          List<Integer> newGroup = new ArrayList<Integer>();
          newGroup.add(index);
          this.groups.add(newGroup);
        } else {
          group.add(index);
        }
        
        index++;
      }
      
      this.outputPort.send(this.groups);
    }
    
    private List<Integer> findGroup(List<Double> vector) {
      // TODO: randomize
      for (List<Integer> group: this.groups) {
        boolean match = true;
        for (Integer index: group) {
          if (!match(vector, this.vectors.get(index))) {
            match = false;
            break;
          }
        }
        
        if (match) {
          return group;
        }
      }
      
      return null;
    }
    
    private boolean match(List<Double> a, List<Double> b) {
      Iterator<Double> iteratorA = a.iterator();
      Iterator<Double> iteratorB = b.iterator();
      
      while (iteratorA.hasNext()) {
        if (Math.abs(iteratorA.next() - iteratorB.next()) >= this.similarityRadius) {
          return false;
        }
      }
      
      return true;
    }
    
    public OutputPort<List<List<Integer>>> getOutputPort() {
      return this.outputPort;
    }
  }