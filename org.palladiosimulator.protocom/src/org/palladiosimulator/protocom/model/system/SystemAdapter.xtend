package org.palladiosimulator.protocom.model.system

import org.palladiosimulator.pcm.system.System
import org.palladiosimulator.protocom.model.allocation.AssemblyContextAdapter
import org.palladiosimulator.protocom.model.repository.InterfaceProvidingRequiringEntityAdapter

/**
 * Adapter for PCM System entities.
 * @author Christian Klaussner
 */
class SystemAdapter extends InterfaceProvidingRequiringEntityAdapter<System> {
	new(System entity) {
		super(entity)
	}
	
	def getAssemblyContexts() {
		entity.assemblyContexts__ComposedStructure.map[
			new AssemblyContextAdapter(it)
		]
	}
	
	// Translation methods
	
	def getInterfaceName() {
		"I" + entity.safeName
	}
}
