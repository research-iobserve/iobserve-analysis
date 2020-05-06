package org.palladiosimulator.protocom.tech.iiop

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.xml.IJeeEjbDescriptor
import org.palladiosimulator.protocom.lang.xml.IJeeGlassfishEjbDescriptor
import org.palladiosimulator.protocom.tech.ConceptMapping

class JavaEEIIOPDescriptor <E extends Entity> extends ConceptMapping<E> implements IJeeEjbDescriptor, IJeeGlassfishEjbDescriptor{
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override ejbName() {
	}
	
	override ejbRefName() {
		newLinkedList
	}
	
	override displayName() {
	}
	
	override filePath() {
	}
	
	override ejbClientJar() {
	}
	
	override jndiName() {
	}
	
	override projectName() {
	}
	
	override ipAddress(){
	}
	
	override requiredComponentsAndResourceContainerIPAddress() {
	}
	
}