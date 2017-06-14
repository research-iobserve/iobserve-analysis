package org.iobserve.service.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * Modifies a PCM Resource Environment
 * 
 * @author Philipp Weimann
 * @author Robert Heinrich
 */
public class ResourceEnvironmentModification {

	private static final Logger LOG = LogManager.getLogger(ModelModification.class);

	private ResourceEnvironment resourceEnvironment;

	/**
	 * The Constructor
	 * 
	 * @param resourceEnvironment
	 *            the Resource Environment to modifiy
	 */
	public ResourceEnvironmentModification(ResourceEnvironment resourceEnvironment) {
		this.resourceEnvironment = resourceEnvironment;
	}

	/**
	 * Generates a certain amount of Resource Container terminations
	 * 
	 * @param terminations
	 *            the amount of terminations to generate
	 * @return the terminated Resource Containers
	 */
	public List<ResourceContainer> modifyResEnv_terminate(int terminations) {

		List<ResourceContainer> removedResourceContainers = new ArrayList<ResourceContainer>();
		EList<ResourceContainer> resourceContainers = this.resourceEnvironment.getResourceContainer_ResourceEnvironment();

		for (int i = 0; i < terminations; i++) {
			int randomIndex = ThreadLocalRandom.current().nextInt(resourceContainers.size());
			ResourceContainer removedResCon = resourceContainers.remove(randomIndex);
			removedResourceContainers.add(removedResCon);

			LOG.info("REMOVING: \tResourceContainer: \t" + removedResCon.getId());
		}

		return removedResourceContainers;
	}

	/**
	 * Generates a certain amount of Resource Container acquisitions
	 * 
	 * @param acquires
	 *            the amount of acquisistions
	 */
	public void modifyResEnv_acquire(int acquires) {
		ResourceEnvironmentGeneration resEnvGen = new ResourceEnvironmentGeneration(this.resourceEnvironment);
		resEnvGen.addResourceContainers(acquires, "mod");
	}

}
