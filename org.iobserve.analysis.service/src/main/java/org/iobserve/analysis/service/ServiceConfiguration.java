/**
 *
 */
package org.iobserve.analysis.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.iobserve.analysis.MultiInputObservationConfiguration;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.service.updater.VisualizationUpdateStage;

/**
 * @author Reiner Jung
 *
 */
public class ServiceConfiguration extends MultiInputObservationConfiguration {

    public ServiceConfiguration(final int inputPort, final String outputHostname, final String outputPort,
            final String systemId, final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload,
            final ICorrespondence correspondenceModel, final UsageModelProvider usageModelProvider,
            final ResourceEnvironmentModelProvider resourceEvnironmentModelProvider,
            final AllocationModelProvider allocationModelProvider, final SystemModelProvider systemModelProvider)
            throws MalformedURLException {
        super(inputPort, correspondenceModel, usageModelProvider, resourceEvnironmentModelProvider,
                allocationModelProvider, systemModelProvider, varianceOfUserGroups, thinkTime, closedWorkload);

        final URL url = new URL(
                "http://" + outputHostname + ":" + outputPort + "/v1/systems/" + systemId + "/changelogs");

        final VisualizationUpdateStage visualizationUpdateStage = new VisualizationUpdateStage(url);

    }

}
