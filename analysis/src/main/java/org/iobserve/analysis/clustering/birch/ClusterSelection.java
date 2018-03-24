package org.iobserve.analysis.clustering.birch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iobserve.analysis.clustering.birch.model.ClusteringFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

public class ClusterSelection extends AbstractConsumerStage<List<ClusteringFeature>> {

	private final OutputPort<List<ClusteringFeature>> outputPort = this.createOutputPort();
	private final int expectedNumberOfClusters = 7;
	private final ArrayList<List<ClusteringFeature>> list = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterSelection.class);
    
	@Override
	protected void execute(List<ClusteringFeature> element) throws Exception {
		ClusterSelection.LOGGER.debug("Received clustering of size " + element.size());
		this.list.add(element);
		Optional<List<ClusteringFeature>> result= this.list.stream().filter(
				clustering -> clustering.size() == this.expectedNumberOfClusters).findFirst();
		if(result.isPresent())
			this.outputPort.send(result.get());
	}
	
    public OutputPort<List<ClusteringFeature>> getOutputPort() {
        return this.outputPort;
    }
}
