package org.iobserve.analysis.filter;

import org.eclipse.emf.common.util.EList;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.common.record.ServerGeoLocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.impl.ResourceContainerPrivacyImpl;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

public class TGeoLocation extends AbstractConsumerStage<ServerGeoLocation> {

	ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;

	private final OutputPort<Boolean> outputPortSnapshot = this.createOutputPort();

	public TGeoLocation(ResourceEnvironmentModelProvider resourceEnvironmentModelProvider) {
		this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
	}

	@Override
	protected void execute(ServerGeoLocation element) throws Exception {
		ResourceEnvironment resourceEnvironment = resourceEnvironmentModelProvider.getModel();
		EList<ResourceContainer> resContainers = resourceEnvironment.getResourceContainer_ResourceEnvironment();
		Boolean makeSnapshot = false;

		for (ResourceContainer resContainer : resContainers) {
			String entityName = resContainer.getEntityName();
			if (entityName.equals(element.getHostname()) && resContainer instanceof ResourceContainerPrivacyImpl) {
				
				ResourceContainerPrivacyImpl resContainerPrivacy = (ResourceContainerPrivacyImpl) resContainer;
				if (resContainerPrivacy.getGeolocation() != element.getCountryCode()) {
					resContainerPrivacy.setGeolocation(element.getCountryCode());
					makeSnapshot = true;
					SnapshotBuilder.setSnapshotFlag();
				}
				break;
			}
		}
		this.outputPortSnapshot.send(makeSnapshot);
	}

	/**
	 * @return output port for snapshot
	 */
	public OutputPort<Boolean> getOutputPortSnapshot() {
		return this.outputPortSnapshot;
	}

}
