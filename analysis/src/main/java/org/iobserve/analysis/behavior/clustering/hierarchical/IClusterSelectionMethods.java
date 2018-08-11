package org.iobserve.analysis.behavior.clustering.hierarchical;

import java.util.List;
import java.util.Map;

import org.eclipse.net4j.util.collection.Pair;

import weka.clusterers.HierarchicalClusterer;
import weka.core.Instance;
import weka.core.Instances;

public interface IClusterSelectionMethods {

    Map<Integer, List<Pair<Instance, Double>>> analyze(HierarchicalClusterer hierarchicalClusterer,
            Instances instances);
}
