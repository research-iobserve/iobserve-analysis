package org.iobserve.analysis.clustering.birch;

import org.iobserve.analysis.clustering.filter.TBehaviorModelCreation;
import org.iobserve.analysis.clustering.filter.TBehaviorModelPreperation;
import org.iobserve.analysis.clustering.filter.TBehaviorModelTableGeneration;
import org.iobserve.analysis.clustering.filter.TInstanceTransformations;
import org.iobserve.analysis.clustering.filter.models.BehaviorModel;
import org.iobserve.analysis.clustering.filter.models.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.session.data.UserSession;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;
import teetime.stage.basic.merger.Merger;
import teetime.stage.basic.merger.strategy.BlockingBusyWaitingRoundRobinMergerStrategy;
import teetime.stage.basic.merger.strategy.IMergerStrategy;
import weka.core.Instances;

public class SessionsToInstances extends CompositeStage{ 

	private final InputPort<Object> sessionInputPort;
    
	private final InputPort<Object> timerInputPort;
    
    private final OutputPort<Instances> outputPort;
    
	public SessionsToInstances(final long keepTime, final int minCollectionSize, 
    		final IRepresentativeStrategy representativeStrategy, 
    		final boolean keepEmptyTransitions) {
		
        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        final Distributor<EntryCallSequenceModel> distributor = new Distributor<>(strategy);

        final IMergerStrategy mergerStrategy = new BlockingBusyWaitingRoundRobinMergerStrategy();
        final Merger<Object> merger1 = new Merger<>(mergerStrategy);
		final Merger<Object> merger2 = new Merger<>(new BlockingBusyWaitingRoundRobinMergerStrategy());
		final MyCollectUserSessionsFilter collectUserSessionsFilter = 
				new MyCollectUserSessionsFilter(keepTime, minCollectionSize);
		
		final TInstanceTransformations tInstanceTransformations = new TInstanceTransformations(); 
                
        final TBehaviorModelTableGeneration tBehaviorModelTableGeneration = new TBehaviorModelTableGeneration(
        		representativeStrategy, keepEmptyTransitions);
        		
        		//configuration.getRepresentativeStrategy(), configuration.isKeepEmptyTransitions());

        final TBehaviorModelPreperation tBehaviorModelPreperation = new TBehaviorModelPreperation(
        		keepEmptyTransitions);
                //configuration.isKeepEmptyTransitions());

        
        //this.sessionInputPort = merger1.getNewInputPort();
        this.sessionInputPort = collectUserSessionsFilter.getInputPort();
        this.timerInputPort = merger1.getNewInputPort();
        this.outputPort = tInstanceTransformations.getOutputPort();
        
        //this.connectPorts(merger1.getOutputPort(), collectUserSessionsFilter.getInputPort());
        this.connectPorts(collectUserSessionsFilter.getOutputPort(), distributor.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), tBehaviorModelTableGeneration.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), merger2.getNewInputPort());

        this.connectPorts(tBehaviorModelTableGeneration.getOutputPort(), merger2.getNewInputPort());

        this.connectPorts(merger2.getOutputPort(), tBehaviorModelPreperation.getInputPort());
        this.connectPorts(tBehaviorModelPreperation.getOutputPort(), tInstanceTransformations.getInputPort());
	}
	    
    public InputPort<Object> getTimerInputPort() {
		return timerInputPort;
	}
	
	public InputPort<Object> getSessionInputPort() {
		return this.sessionInputPort;
	}

	public OutputPort<Instances> getOutputPort() {
		return this.outputPort;
	}
}
