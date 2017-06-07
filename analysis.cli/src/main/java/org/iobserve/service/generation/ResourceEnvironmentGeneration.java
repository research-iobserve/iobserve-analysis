package org.iobserve.service.generation;

import java.util.concurrent.ThreadLocalRandom;

import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceenvironmentPrivacyFactory;

public class ResourceEnvironmentGeneration {

	private static ResourceenvironmentFactory RES_ENV_FACTORY = ResourceenvironmentFactory.eINSTANCE;
	private static ResourceenvironmentPrivacyFactory RES_ENV_PRIVACY_FACTORY = ResourceenvironmentPrivacyFactory.eINSTANCE;
	private ResourceEnvironment resourceEnvironment;

	public ResourceEnvironmentGeneration(String modelName) {
		this.resourceEnvironment = RES_ENV_FACTORY.createResourceEnvironment();
		this.resourceEnvironment.setEntityName(modelName);
	}

	public ResourceEnvironmentGeneration(ResourceEnvironment resEnvModel) {
		this.resourceEnvironment = resEnvModel;
	}

	public ResourceEnvironment craeteResourceEnvironment(int resourceContainerCount) {
		for (int i = 0; i < resourceContainerCount; i++) {
			String prefix = Integer.toString(i);
			ResourceContainerPrivacy resContainer = createResourceContainer(prefix);
			this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(resContainer);
		}
		return this.resourceEnvironment;
	}

	public void addResourceContainers(int resourceContainerCount, String postPrefix) {
		for (int i = 0; i < resourceContainerCount; i++) {
			String prefix = Integer.toString(i);
			ResourceContainerPrivacy resContainer = createResourceContainer(prefix + "_" + postPrefix);
			this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(resContainer);
		}
	}

	private ResourceContainerPrivacy createResourceContainer(String prefix) {
		ResourceContainerPrivacy resContainer = RES_ENV_PRIVACY_FACTORY.createResourceContainerPrivacy();
		resContainer.setEntityName(prefix + "_ResCon");

		int randGeoLocation = ThreadLocalRandom.current().nextInt(1000);
		resContainer.setGeolocation(randGeoLocation);
		return resContainer;
	}

}
