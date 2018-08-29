package org.palladiosimulator.protocom.tech.iiop

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.xml.IJeeComponentFile
import org.palladiosimulator.protocom.tech.ConceptMapping

class JavaEEIIOPComponentFile <E extends Entity> extends ConceptMapping<E> implements IJeeComponentFile  {
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override wbModuleDeployName() {
	}
	
	override wbResource() {
	}
	
	override property() {
	}
	
	override filePath() {
	}
	
	override projectName() {
	}
	
}