package org.iobserve.analysis.behavior.clustering.hierarchical;

import java.util.Optional;

import org.iobserve.analysis.behavior.karlsruhe.data.ClusteringResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import weka.core.Instance;
import weka.core.Instances;

public class HierarchicalClusteringProcess extends AbstractConsumerStage<Instances> {

	private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalClusteringProcess.class);

    private final IHierarchicalClustering clustering;
    private final OutputPort<Instances> outputPort = this.createOutputPort();

    /**
     * constructor.
     *
     * @param clustering
     *            clustering used
     */
    public HierarchicalClusteringProcess(final IHierarchicalClustering clustering) {
        this.clustering = clustering;
    }

    @Override
    protected void execute(final Instances instances) {
        final Optional<ClusteringResults> clusteringResults = this.clustering.clusterInstances(instances);
        clusteringResults.ifPresent(this::printInstances);
        clusteringResults
                .ifPresent(results -> this.getOutputPort().send(results.getClusteringMetrics().getCentroids()));

    }

    private void printInstances(final ClusteringResults results) {
        results.printClusteringResults();
        final Instances centroids = results.getClusteringMetrics().getCentroids();
        for (int i = 0; i < centroids.numInstances(); i++) {
            String logString = "";
            logString += "***************************";
            logString += "Cluster " + i;
            logString += "***************************";
            final Instance instance = centroids.instance(i);
            for (int a = 0; a < instance.numAttributes(); a++) {
                logString += centroids.attribute(a).name() + " : " + instance.value(a);
            }
            HierarchicalClusteringProcess.LOGGER.info(logString);
        }
    }

    /**
     * getter.
     *
     * @return output port
     */
    public OutputPort<Instances> getOutputPort() {
        return this.outputPort;
    }
}
