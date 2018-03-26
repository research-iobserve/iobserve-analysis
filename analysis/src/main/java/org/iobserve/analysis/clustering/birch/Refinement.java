package org.iobserve.analysis.clustering.birch;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.iobserve.analysis.clustering.birch.model.ClusteringFeature;
import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.session.data.UserSession;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import weka.core.Instance;
import weka.core.Instances;

public class Refinement extends AbstractConsumerStage<Object> {
   
    private final OutputPort<Instances> outputPort = this.createOutputPort();
    
	private Instances instances;
	
    private List<ClusteringFeature> clustering;
    
//	@Override
//	protected void execute(Object object) throws Exception {
//        if (object instanceof Instances) {
//            final Instances instances = (Instances) object;
//            this.instances = instances;
//
//        } else if (object instanceof List<?>) {
//            @SuppressWarnings("unchecked")
//			final List<ClusteringFeature> clustering = (List<ClusteringFeature>) object;
//            this.clustering = clustering;
//        }
//        
//        if(this.clustering != null && this.instances != null) {
//        	instances.delete();
//        	for(ClusteringFeature cf : this.clustering) {
//        		instances.add(new Instance(1.0, cf.getCentroid()));
//        	}
//        	this.outputPort.send(instances);
//        }
//	}
	
	@Override
	protected void execute(Object object) throws Exception {
        if (object instanceof Instances) {
            final Instances instances = (Instances) object;
            this.instances = instances;

        } else if (object instanceof List<?>) {
            @SuppressWarnings("unchecked")
			final List<ClusteringFeature> clustering = (List<ClusteringFeature>) object;
            this.clustering = clustering;
        }
        
        if(this.clustering != null && this.instances != null) {
        	refineClustering();
        }
	}
	
	private void refineClustering() {
		//for every cf in clustering: create new empty cf (Map)
		HashMap<ClusteringFeature, ClusteringFeature> finalClustering = new HashMap<>();
		for(ClusteringFeature cf : this.clustering)
			finalClustering.put(cf, new ClusteringFeature(cf.getDimension()));
		//for every instance:
		for(int i = 0; i < this.instances.numInstances(); i++) {
			//calculate cf
			ClusteringFeature cf = new ClusteringFeature(this.instances.instance(i));
			//find cfs NN in clustering
			TreeMap<Double, ClusteringFeature> ranking = new TreeMap<>();
			for (ClusteringFeature centroid : this.clustering) {
					ranking.put(centroid.compareD0(cf), centroid);
			}
			ClusteringFeature nearestNeighbor = ranking.firstEntry().getValue();
			//add to correct bucket
			finalClustering.get(nearestNeighbor).add(cf);
		}
		//Send new centroids on in instance format
    	instances.delete();
    	for(ClusteringFeature cf : finalClustering.values()) {
    		instances.add(new Instance(1.0, cf.getCentroid()));
    	}
    	this.outputPort.send(instances);
	}
    
	public OutputPort<Instances> getOutputPort() {
		return outputPort;
	}

}
