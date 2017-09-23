package org.iobserve.analysis.filter;

import java.util.Date;

import teetime.framework.AbstractProducerStage;
import teetime.framework.OutputPort;

/**
 * This approach will not provide exact intervals, as thread switches and
 * other effects will add to the interval.
 * 
 * @author Reiner Jung
 *
 */
public class TimeTriggerFilter extends AbstractProducerStage<Long> {

	private long interval;

	private final OutputPort<Long> outputPort = this.createOutputPort();
	
	public TimeTriggerFilter(long interval) {
		this.interval = interval;
	}
	
	@Override
	protected void execute() throws Exception {
		Thread.sleep(interval);
		this.outputPort.send(new Date().getTime());
	}

}
