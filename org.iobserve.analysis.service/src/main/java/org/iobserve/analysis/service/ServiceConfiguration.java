/**
 *
 */
package org.iobserve.analysis.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.iobserve.analysis.MultiInputObservationConfiguration;
import org.iobserve.analysis.model.ModelProviderPlatform;
import org.iobserve.analysis.service.updater.VisualizationUpdateStage;

/**
 * @author Reiner Jung
 *
 */
public class ServiceConfiguration extends MultiInputObservationConfiguration {

	public ServiceConfiguration(int inputPort, String outputHostname, String outputPort,
			String systemId, ModelProviderPlatform platform) throws MalformedURLException {
		super(inputPort, platform);

		final URL url = new URL("http://" + outputHostname + ":" + outputPort + "/v1/systems/" + systemId + "/changelogs");

		final VisualizationUpdateStage visualizationUpdateStage = new VisualizationUpdateStage(url);

		// TODO connect this filter and add one for the model update
	}

}
