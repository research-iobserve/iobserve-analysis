package org.iobserve.logger;


import org.iobserve.analysis.AnalysisMain;

import teetime.framework.AbstractConsumerStage;

public aspect FilterLogging {
	
	pointcut execFilter(AbstractConsumerStage stage)
		: execution(* AbstractConsumerStage+.execute(..)) 
		&& target(stage);
	
	before(AbstractConsumerStage stage): execFilter(stage) {  
		AnalysisMain.getInstance().getTimeMemLogger().before(stage, stage.getId());
	}
	
	after(AbstractConsumerStage stage): execFilter(stage) {
		AnalysisMain.getInstance().getTimeMemLogger().after(stage, stage.getId());
	}
	
	
}