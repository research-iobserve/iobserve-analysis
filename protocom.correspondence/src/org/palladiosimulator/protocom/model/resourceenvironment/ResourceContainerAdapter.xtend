package org.palladiosimulator.protocom.model.resourceenvironment

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification

/**
 * Adapter class for PCM ResourceContainer entities.
 * @author Christian Klaussner
 */
class ResourceContainerAdapter extends ModelAdapter<ResourceContainer> {
	new(ResourceContainer entity) {
		super(entity)
	}
	
	/**
	 * Gets the ID.
	 * @return a string containing the ID
	 */
	def getId() {
		entity.id
	}
	
	/**
	 * Gets the CPU processing rate.
	 * @return a string containing the specification of the CPU processing rate
	 */
	def getCpuRate() {
		getRateForPattern("cpu")
	}
	
	/**
	 * Gets the HDD processing rate.
	 * @return a string containing the specification of the HDD processing rate
	 */
	def getHddRate() {
		getRateForPattern("hdd")
	}
	
	/**
	 * Gets the processing rate whose type contains the specified pattern.
	 * @return a string containing the specification of the processing rate
	 * @param pattern the type pattern to search for
	 */
	private def getRateForPattern(String pattern) {
		var String rate
		val specifications = entity.activeResourceSpecifications_ResourceContainer
		
		for (ProcessingResourceSpecification spec : specifications) {
			val type = spec.activeResourceType_ActiveResourceSpecification
			
			if (type.toString.toLowerCase.contains(pattern)) {
				rate = spec.processingRate_ProcessingResourceSpecification.specification.toString
			}
		}

		rate
	}
}
