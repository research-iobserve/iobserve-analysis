package org.iobserve.analysis.filter.cdoruserbehavior;

import java.util.Random;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import weka.clusterers.XMeans;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.NormalizableDistance;

public class TClustering extends AbstractConsumerStage<Instances> {
	private final OutputPort<Instances> outputPort = createOutputPort();
	
	private final int minClusters;
	private final int maxClusters;
	
	public TClustering(final int userGroups, final int variance){
		minClusters = (userGroups - variance) < 2 ? 1 : userGroups - variance;
		maxClusters = (userGroups + variance) < 2 ? 2 : userGroups - variance; 
	}
	
	@Override
	protected void execute(Instances instances) {
		
		XMeans xMeansClusterer = new XMeans();		
		xMeansClusterer.setSeed(new Random().nextInt(Integer.MAX_VALUE));
		
		final NormalizableDistance manhattenDistance = new ManhattanDistance();
        manhattenDistance.setDontNormalize(false);
        manhattenDistance.setInstances(instances);
        xMeansClusterer.setDistanceF(manhattenDistance);
        
        xMeansClusterer.setMinNumClusters(minClusters);
        xMeansClusterer.setMaxNumClusters(maxClusters);
        
        final Instances centroids = xMeansClusterer.getClusterCenters();
        outputPort.send(centroids);
        
        try {
			xMeansClusterer.buildClusterer(instances);
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       
		
	}
	
	/**
	 * getter 
	 * @return output port
	 */
	public OutputPort<Instances> getOutputPort() {
		return outputPort;
	}
	

	
	

}
