package org.iobserve.analysis.privacy;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public abstract class AbstractLinearComposition<I, O> extends CompositeStage {
	
	/** composite input port */
	private InputPort<I> inputPort;
	/** composite output port, which sends whether privacy is violated */
	private OutputPort<O> outputPort;
	
	
	
	public AbstractLinearComposition(InputPort<I> inputPort, OutputPort<O> outputPort)
	{
		this.inputPort = inputPort;
		this.outputPort = outputPort;
	}
	
	
	/**
	 * @return the input port for the complete composite stage
	 */
	public InputPort<I> getInputPort()
	{
		return this.inputPort;
	}
	
	/**
	 * @return the output port for the complete composite stage
	 */
	public OutputPort<O> getOutputPort()
	{
		return this.outputPort;
	}

}
