package org.palladiosimulator.protocom.model.resourceenvironment

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment

/**
 * Adapter class for PCM ResourceEnvironment entities.
 * @author Christian Klaussner
 */
class ResourceEnvironmentAdapter extends ModelAdapter<ResourceEnvironment> {
	new(ResourceEnvironment entity) {
		super(entity)
	}
	
	/**
	 * Gets the resource containers.
	 * @return a list of adapters for the resource containers
	 */
	def getResourceContainers() {
		entity.resourceContainer_ResourceEnvironment.map[
			new ResourceContainerAdapter(it)
		]
	}
}
