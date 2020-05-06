package org.palladiosimulator.protocom.tech.rmi

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.xml.IClasspath
import org.palladiosimulator.protocom.tech.ConceptMapping

class PojoClasspathFile <E extends Entity> extends ConceptMapping<E> implements IClasspath{
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override classPathEntries() {
	}
	
	override filePath() {
	}
	
	override projectName() {
	}
	
}