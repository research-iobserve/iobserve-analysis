package org.iobserve.service.generation;

import java.util.concurrent.ThreadLocalRandom;

import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceenvironmentPrivacyFactory;

import com.neovisionaries.i18n.CountryCode;

/**
 * Generates a PCM Privacy compliant Resource Environment
 * 
 * @author Philipp Weimann
 * @author Robert Heinrich
 */
public class ResourceEnvironmentGeneration {

	private static ResourceenvironmentFactory RES_ENV_FACTORY = ResourceenvironmentFactory.eINSTANCE;
	private static ResourceenvironmentPrivacyFactory RES_ENV_PRIVACY_FACTORY = ResourceenvironmentPrivacyFactory.eINSTANCE;
	private ResourceEnvironment resourceEnvironment;
	private CountryCode[] countryCodes = CountryCode.values();

	/**
	 * Constructor for a NEW Resource Environment
	 * 
	 * @param modelName
	 */
	public ResourceEnvironmentGeneration(String modelName) {
		this.resourceEnvironment = RES_ENV_FACTORY.createResourceEnvironment();
		this.resourceEnvironment.setEntityName(modelName);
	}

	/**
	 * Constructor for given Resource Environment
	 * 
	 * @param resEnvModel
	 *            the Resource Environment Model
	 */
	public ResourceEnvironmentGeneration(ResourceEnvironment resEnvModel) {
		this.resourceEnvironment = resEnvModel;
	}

	/**
	 * Creates a certain amount of Resource Containers
	 * 
	 * @param resourceContainerCount
	 *            the amount of resource containers
	 * @return The Resource Environment model
	 */
	public ResourceEnvironment craeteResourceEnvironment(int resourceContainerCount) {
		for (int i = 0; i < resourceContainerCount; i++) {
			String prefix = Integer.toString(i);
			ResourceContainerPrivacy resContainer = createResourceContainer(prefix);
			this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(resContainer);
		}
		return this.resourceEnvironment;
	}

	/**
	 * Creates a given amount of new Resource Containers with the given prefix
	 * 
	 * @param resourceContainerCount
	 *            the amount of resource container to create
	 * @param postPrefix
	 *            the prefix
	 */
	public void addResourceContainers(int resourceContainerCount, String postPrefix) {
		for (int i = 0; i < resourceContainerCount; i++) {
			String prefix = Integer.toString(i);
			ResourceContainerPrivacy resContainer = createResourceContainer(prefix + "_" + postPrefix);
			this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(resContainer);
		}
	}

	/*
	 * Creates a new resource container
	 */
	private ResourceContainerPrivacy createResourceContainer(String prefix) {
		ResourceContainerPrivacy resContainer = RES_ENV_PRIVACY_FACTORY.createResourceContainerPrivacy();
		resContainer.setEntityName(prefix + "_ResCon");

		int randGeoLocation = ThreadLocalRandom.current().nextInt(this.countryCodes.length);
		resContainer.setGeolocation(this.countryCodes[randGeoLocation].getNumeric());

		return resContainer;
	}

}
