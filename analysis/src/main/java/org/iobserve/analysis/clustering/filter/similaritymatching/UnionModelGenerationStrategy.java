package org.iobserve.analysis.clustering.filter.similaritymatching;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;
import org.iobserve.analysis.clustering.behaviormodels.EntryCallEdge;

public class UnionModelGenerationStrategy implements IModelGenerationStrategy {

    @Override
    public BehaviorModel generateModel(final BehaviorModel[] models) {
        final BehaviorModel newModel = new BehaviorModel();

        /** Magic happens here */
        for (final BehaviorModel model : models) {
            /** Adding edges will also add nodes of edges */
            for (final EntryCallEdge edge : model.getEdges()) {
                newModel.addEdge(edge);
            }
        }

        return newModel;
    }

}
