package org.iobserve.service.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.gradle.internal.impldep.com.esotericsoftware.minlog.Log;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

public class ResourceEnvironmentModification {

	private static final Logger LOG = LogManager.getLogger(ModelModification.class);
	
	private ResourceEnvironment resourceEnvironment;

	public ResourceEnvironmentModification(ResourceEnvironment resourceEnvironment) {
		this.resourceEnvironment = resourceEnvironment;
	}

	/**
	 * 
	 * @param terminations
	 * @return
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
	 * 
	 * @param acquires
	 */
	public void modifyResEnv_acquire(int acquires) {
		ResourceEnvironmentGeneration resEnvGen = new ResourceEnvironmentGeneration(this.resourceEnvironment);
		resEnvGen.addResourceContainers(acquires, "mod");
	}

}
