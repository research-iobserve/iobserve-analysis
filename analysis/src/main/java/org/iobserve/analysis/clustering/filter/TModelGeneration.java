package org.iobserve.analysis.clustering.filter;

import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.clustering.filter.models.BehaviorModel;
import org.iobserve.analysis.clustering.filter.models.BehaviorModelTable;
import org.iobserve.analysis.clustering.filter.models.EntryCallEdge;
import org.iobserve.analysis.clustering.filter.models.EntryCallNode;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

//<BehaviorModelTable[],List<List<Integer>>>
public class TModelGeneration extends AbstractStage {
    private final OutputPort<BehaviorModel[]> outputPort = this.createOutputPort();
    private final InputPort<BehaviorModel[]> inputTables = this.createInputPort(BehaviorModelTable[].class);
    private final InputPort<Integer[][]> inputGroups = this.createInputPort();

    private BehaviorModelTable[] tables;
    private Integer[][] groups;

    @Override
    protected void execute() {
        if (this.tables == null) {
            this.tables = this.inputTables.receive();
        }

        if (this.groups == null) {
            this.groups = this.inputGroups.receive();
        }

        if ((this.tables == null) || (this.groups == null)) {
            return;
        }

        final BehaviorModel[] models = new BehaviorModel[this.groups.size()];

        final Iterator<List<Integer>> iteratorGroup = this.groups.iterator();
        for (final BehaviorModel model : models) {
            this.generateModel(iteratorGroup.next(), model);
        }

        this.outputPort.send(models);
    }

    // TODO: parameters...
    private void generateModel(final List<Integer> group, BehaviorModel model) {
        // Generate EntryCallNodes
        BehaviorModelTable table = this.tables[group.get(0)];
        final String[] signatures = table.getInverseSignatures(); // TODO: add this to table class
        final EntryCallNode[] nodes = new EntryCallNode[signatures.length];

        for (int i = 0; i < signatures.length; i++) {
            nodes[i] = new EntryCallNode(signatures[i]);
            model.addNode(nodes[i]);
        }

        // Initialize edge counter
        final int[][] edges = new int[signatures.length][signatures.length];

        model = new BehaviorModel();
        for (final Integer index : group) {
            table = this.tables[index];

            final Integer[][] transitions = table.getTransitions();
            for (int i = 0; i < signatures.length; i++) {
                for (int j = 0; j < signatures.length; j++) {
                    edges[i][j] += transitions[i][j];
                }
            }
        }

        for (int i = 0; i < signatures.length; i++) {
            for (int j = 0; j < signatures.length; j++) {
                final EntryCallEdge edge = new EntryCallEdge(nodes[i], nodes[j], edges[i][j]); // TODO: Check order of
                                                                                               // args
                model.addEdge(edge);
            }
        }
    }

    public OutputPort<BehaviorModel[]> getOutputPort() {
        return this.outputPort;
    }
}