package org.iobserve.analysis;

import java.io.IOException;

import org.iobserve.analysis.model.ModelProviderPlatform;

public class MultiInputObservationConfiguration extends AbstractObservationConfiguration {

	public MultiInputObservationConfiguration(ModelProviderPlatform platform)
			throws IOException, ClassNotFoundException {
		super(platform);
	}

}
