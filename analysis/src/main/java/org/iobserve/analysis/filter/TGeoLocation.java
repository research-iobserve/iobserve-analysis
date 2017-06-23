package org.iobserve.analysis.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.privacyanalysis.PrivacyAnalysis;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.common.record.ServerGeoLocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.impl.ResourceContainerPrivacyImpl;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * TGeoLocation updates the ResourceContainers geo-location and invokes the
 * output port if the geo-location changes.
 * 
 * @author Philipp Weimann
 * @author Robert Heinrich
 *
 */
public class TGeoLocation extends AbstractConsumerStage<ServerGeoLocation> {

	protected static final Logger LOG = LogManager.getLogger(TGeoLocation.class);

	ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;

	private final OutputPort<Boolean> outputPortSnapshot = this.createOutputPort();

	/**
	 * The Constructor
	 * 
	 * @param resourceEnvironmentModelProvider
	 *            the resource environment model provider
	 */
	public TGeoLocation(ResourceEnvironmentModelProvider resourceEnvironmentModelProvider) {
		this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
	}

	@Override
	protected void execute(ServerGeoLocation element) throws Exception {
		LOG.info("New GeoLocationRecord: " + element.getHostname() + "\t ->\t" + element.getCountryCode());
		ResourceEnvironment resourceEnvironment = resourceEnvironmentModelProvider.getModel();
		EList<ResourceContainer> resContainers = resourceEnvironment.getResourceContainer_ResourceEnvironment();
		Boolean makeSnapshot = false;
		boolean foundServer = false;

		for (ResourceContainer resContainer : resContainers) {
			String entityName = resContainer.getEntityName();
			if (entityName.equals(element.getHostname()) && resContainer instanceof ResourceContainerPrivacyImpl) {

				ResourceContainerPrivacyImpl resContainerPrivacy = (ResourceContainerPrivacyImpl) resContainer;
				if (resContainerPrivacy.getGeolocation() != element.getCountryCode()) {
					resContainerPrivacy.setGeolocation(element.getCountryCode());
					makeSnapshot = true;
					SnapshotBuilder.setSnapshotFlag();
				}
				foundServer = true;
				break;
			}
		}

		if (!foundServer)
			LOG.warn(String.format("Server '%s' was not found!\n", element.getHostname()));
		this.outputPortSnapshot.send(makeSnapshot);
	}

	/**
	 * @return output port for snapshot
	 */
	public OutputPort<Boolean> getOutputPortSnapshot() {
		return this.outputPortSnapshot;
	}

}
