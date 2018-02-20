package org.iobserve.analysis.clustering.filter;

import org.iobserve.analysis.clustering.filter.models.BehaviorModel;

public interface IModelGenerationStrategy {
    public BehaviorModel generateModel(final BehaviorModel[] models);
}
