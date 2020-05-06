package org.palladiosimulator.protocom.tech.rmi

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.properties.IBuildProperties
import org.palladiosimulator.protocom.tech.ConceptMapping

class PojoBuildPropertiesFile <E extends Entity> extends ConceptMapping<E> implements IBuildProperties {
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override output() {
	}
	
	override source() {
	}
	
	override binIncludes() {
	}
	
	override filePath() {
	}
	
	override projectName() {
	}
	
}