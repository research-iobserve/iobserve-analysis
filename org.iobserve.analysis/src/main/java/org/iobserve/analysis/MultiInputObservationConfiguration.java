package org.iobserve.analysis;

import org.iobserve.analysis.model.ModelProviderPlatform;

import teetime.stage.io.network.TcpReaderStage;

public class MultiInputObservationConfiguration extends AbstractObservationConfiguration {

	private static int CAPACITY = 1024*1024;
	
	public MultiInputObservationConfiguration(int inputPort, ModelProviderPlatform platform) {
		super(platform);
		
		// TODO we need a multi input reader (issue exists with TeeTime)
		
		TcpReaderStage reader = new TcpReaderStage(inputPort, CAPACITY, inputPort+1);
		connectPorts(reader.getOutputPort(), this.recordSwitch.getInputPort());
	}

}
