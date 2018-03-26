package org.iobserve.analysis.clustering.filter;

import org.iobserve.analysis.clustering.XMeansClustering;
import org.iobserve.analysis.clustering.birch.SessionsToInstances;
import org.iobserve.analysis.clustering.filter.models.BehaviorModel;
import org.iobserve.analysis.clustering.filter.models.configuration.IRepresentativeStrategy;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import weka.core.ManhattanDistance;

public class XMeansClassification  extends CompositeStage {

	   private final InputPort<Object> sessionInputPort;
	    private final InputPort<Object> timerInputPort;
	    
	    private final OutputPort<BehaviorModel> outputPort;
	       
	    /**
	     * constructor.
	     *
	     * @param configuration
	     *            model configuration
	     */
	    public XMeansClassification(final long keepTime, final int minCollectionSize, 
	    		final IRepresentativeStrategy representativeStrategy, final boolean keepEmptyTransitions,
	    		final double leafThresholdValue, final int maxLeafSize, final int maxNodeSize,
	    		final int maxLeafEntries) {

	        SessionsToInstances sessionsToInstances = new SessionsToInstances(keepTime, minCollectionSize, 
	        		representativeStrategy, keepEmptyTransitions);
	        TVectorQuantizationClustering tVectorQuantizationClustering = new 
	        		TVectorQuantizationClustering(new XMeansClustering(6, 3, new ManhattanDistance()));
	        TBehaviorModelCreation tBehaviorModelCreation = new TBehaviorModelCreation("XM-");   

	        
	        this.sessionInputPort = sessionsToInstances.getSessionInputPort();
	        this.timerInputPort = sessionsToInstances.getTimerInputPort();
	        this.outputPort = tBehaviorModelCreation.getOutputPort();
	        
	        this.connectPorts(sessionsToInstances.getOutputPort(), tVectorQuantizationClustering.getInputPort());
	        this.connectPorts(tVectorQuantizationClustering.getOutputPort(), tBehaviorModelCreation.getInputPort());
	    }

	    /**
	     * get matching input port.
	     *
	     * @return input port
	     */

	    public InputPort<Object> getSessionInputPort() {
	        return this.sessionInputPort;
	    }

	    public InputPort<Object> getTimerInputPort() {
	        return this.timerInputPort;
	    }
	    
	    /**
	     * get suitable output port.
	     *
	     * @return outputPort
	     */
	    public OutputPort<BehaviorModel> getOutputPort() {
	        return this.outputPort;
	    }

	}
