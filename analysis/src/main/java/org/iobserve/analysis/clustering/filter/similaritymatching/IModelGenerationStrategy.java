package org.iobserve.analysis.clustering.filter.similaritymatching;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;

public interface IModelGenerationStrategy {
    public BehaviorModel generateModel(final BehaviorModel[] models);
}
