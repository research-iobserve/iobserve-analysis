package org.iobserve.analysis.behavior.clustering.hierarchical;

import java.util.List;
import java.util.Map;

import org.eclipse.net4j.util.collection.Pair;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import weka.core.Instance;
import weka.core.Instances;

public class HierarchicalClusteringStage extends AbstractConsumerStage<Instances> {

    private final IHierarchicalClustering clustering;
    private final OutputPort<Map<Integer, List<Pair<Instance, Double>>>> outputPort = this.createOutputPort();

    /**
     * constructor.
     *
     * @param clustering
     *            clustering used
     */
    public HierarchicalClusteringStage(final IHierarchicalClustering clustering) {
        this.clustering = clustering;
    }

    @Override
    protected void execute(final Instances instances) {
        final Map<Integer, List<Pair<Instance, Double>>> clusteringResults = this.clustering
                .clusterInstances(instances);
        this.getOutputPort().send(clusteringResults);

        // final String clusteringMethodName =
        // clusteringResults.map(ClusteringResults::getClusteringMethod).orElse(null);
        // final int numOfClusters =
        // clusteringResults.map(ClusteringResults::getNumberOfClusters).orElse(null);
        //
        // final FastVector f = new FastVector();
        // for (int j = 0; j < instance.numAttributes(); j++) {
        // attributes.addElement(instance.attribute(j));
        // }
        //
        // clusteringResults.ifPresent(this::printInstances);
        // clusteringResults
        // .ifPresent(results ->
        // this.getOutputPort().send(results.getClusteringMetrics().getCentroids()));

    }

    /**
     * getter.
     *
     * @return output port
     */
    public OutputPort<Map<Integer, List<Pair<Instance, Double>>>> getOutputPort() {
        return this.outputPort;
    }
}
