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

    public ServiceConfiguration(final int inputPort, final String outputHostname, final String outputPort,
            final String systemId, final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload,
            final ModelProviderPlatform platform) throws MalformedURLException {
        super(inputPort, platform, varianceOfUserGroups, thinkTime, closedWorkload);

        final URL url = new URL(
                "http://" + outputHostname + ":" + outputPort + "/v1/systems/" + systemId + "/changelogs");

        final VisualizationUpdateStage visualizationUpdateStage = new VisualizationUpdateStage(url);

    }

}
