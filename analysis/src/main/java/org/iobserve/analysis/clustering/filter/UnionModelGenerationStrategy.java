package org.iobserve.analysis.clustering.filter;

import org.iobserve.analysis.clustering.filter.models.BehaviorModel;
import org.iobserve.analysis.clustering.filter.models.EntryCallEdge;
import org.iobserve.analysis.clustering.filter.models.EntryCallNode;

public class UnionModelGenerationStrategy implements IModelGenerationStrategy {

    @Override
    public BehaviorModel generateModel(final BehaviorModel[] models) {
        final BehaviorModel newModel = new BehaviorModel();

        /** Magic happens here */
        for (final BehaviorModel model : models) {
            for (final EntryCallNode node : model.getNodes()) {
                newModel.addNode(node);
            }

            for (final EntryCallEdge edge : model.getEdges()) {
                newModel.addEdge(edge);
            }
        }

        return newModel;
    }

}
