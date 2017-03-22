package org.iobserve.logger;


import org.iobserve.analysis.AnalysisMain;

import teetime.framework.AbstractConsumerStage;

/**
 * Aspect to log all filters execution time and memory usage.
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public aspect FilterLogging {
	
	/**
	 * Join-Point for the execute-method of {@link AbstractConsumerStage}.
	 * @param stage stage
	 */
	pointcut execFilter(AbstractConsumerStage stage)
		: execution(* AbstractConsumerStage+.execute(..)) 
		&& target(stage);
	
	/**
	 * Called before the execute(..) is invoked.
	 * @param stage stage
	 */
	before(AbstractConsumerStage stage): execFilter(stage) {  
		AnalysisMain.getInstance().getTimeMemLogger().before(stage, stage.getId());
	}
	
	/**
	 * Called after the execute(..) is invoked.
	 * @param stage stage
	 */
	after(AbstractConsumerStage stage): execFilter(stage) {
		AnalysisMain.getInstance().getTimeMemLogger().after(stage, stage.getId());
	}
}