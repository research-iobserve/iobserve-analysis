package org.palladiosimulator.protocom.tech.iiop

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.manifest.IJeeManifest
import org.palladiosimulator.protocom.tech.ConceptMapping

class JavaEEIIOPMFFile <E extends Entity> extends ConceptMapping<E> implements IJeeManifest {
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override classPath() {
	}
	
	override manifestVersion() {
	}
	
	override filePath() {
	}
	
	override projectName() {
	}
	
}