package org.iobserve.analysis.clustering.filter;

import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.clustering.filter.models.BehaviorModel;
import org.iobserve.analysis.clustering.filter.models.BehaviorModelTable;
import org.iobserve.analysis.clustering.filter.models.EntryCallEdge;
import org.iobserve.analysis.clustering.filter.models.EntryCallNode;

import teetime.framework.AbstractStage;
import teetime.framework.OutputPort;

public class TModelGeneration extends AbstractStage<BehaviorModelTable[],List<List<Integer>>> {
    private final OutputPort<BehaviorModel[]> outputPort = this.createOutputPort();
    
    private BehaviorModelTable[] tables;
    
    @Override
    protected void execute(final BehaviorModelTable[] tables, final List<List<Integer>> groups) {
      BehaviorModel[] models = new BehaviorModel[groups.length()];
      
      Iterator<List<Integer>> iteratorGroup = groups.iterator();
      for (int i = 0; i < models.length; i++) {
        generateModel(iteratorGroup.next(), models[i]);
      }
      
      this.outputPort.send(models);
    }
    
    // TODO: parameters...
    private void generateModel(List<Integer> group, BehaviorModel model) {
      // Generate EntryCallNodes
      BehaviorModelTable table = this.tables[group.get(0)];
      String[] signatures = table.getInverseSignatures(); // TODO: add this to table class
      EntryCallNode[] nodes = new EntryCallNode[signatures.length];
      
      for (int i = 0; i < signatures.length; i++) {
        nodes[i] = new EntryCallNode(signatures[i]);
        model.addNode(nodes[i]);
      }
      
      // Initialize edge counter
      int[][] edges = new int[signatures.length][signatures.length];
      
      model = new BehaviorModel();
      for (Integer index: group) {
        table = this.tables[index];
        
        Integer[][] transitions = table.getTransitions();
        for (int i = 0; i < signatures.length; i++) {
          for (int j = 0; j < signatures.length; j++) {
            edges[i][j] += transitions[i][j];
          }
        }    
      }
      
      for (int i = 0; i < signatures.length; i++) {
        for (int j = 0; j < signatures.length; j++) {
          EntryCallEdge edge = new EntryCallEdge(nodes[i], nodes[j], edges[i][j]); // TODO: Check order of args
          model.addEdge(edge);
        }
      }
    }
    
    public OutputPort<BehaviorModel[]> getOutputPort() {
      return this.outputPort;
    }
  }