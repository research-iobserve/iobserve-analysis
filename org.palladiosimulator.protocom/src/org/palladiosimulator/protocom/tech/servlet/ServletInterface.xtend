package org.palladiosimulator.protocom.tech.servlet

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.tech.ConceptMapping
import org.palladiosimulator.protocom.lang.java.IJInterface
import org.palladiosimulator.protocom.lang.java.util.JavaNames

class ServletInterface<E extends Entity> extends ConceptMapping<E> implements IJInterface {
	protected val frameworkBase = "org.palladiosimulator.protocom.framework.java.ee"
	protected val stackContext = "de.uka.ipd.sdq.simucomframework.variables.StackContext"
	protected val stackFrame = "de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe"
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override packageName() {
		JavaNames::implementationPackage(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::javaName(pcmEntity)
	}
	
	override interfaces() {
		newLinkedList
	}
	
	override methods() {
		newLinkedList
	}
	
	override fields() {
		newLinkedList
	}
	
	override filePath() {
		JavaNames::getFileName(pcmEntity)
	}
	
	override projectName() {
	}
}
