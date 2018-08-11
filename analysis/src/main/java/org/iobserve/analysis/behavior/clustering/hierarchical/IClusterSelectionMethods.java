package org.iobserve.analysis.behavior.clustering.hierarchical;

import java.util.List;
import java.util.Map;

import org.eclipse.net4j.util.collection.Pair;

import weka.core.Instance;

public interface IClusterSelectionMethods {

    Map<Integer, List<Pair<Instance, Double>>> analyze(Map<Integer, List<Pair<Instance, Double>>> clustering);
}
