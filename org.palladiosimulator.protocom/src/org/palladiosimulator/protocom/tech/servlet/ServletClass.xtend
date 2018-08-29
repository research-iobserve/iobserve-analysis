package org.palladiosimulator.protocom.tech.servlet

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.java.IJClass
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.ConceptMapping

class ServletClass<E extends Entity> extends ConceptMapping<E> implements IJClass {
	protected val frameworkBase = "org.palladiosimulator.protocom.framework.java.ee"
	protected val stackContext = "de.uka.ipd.sdq.simucomframework.variables.StackContext"
	protected val stackFrame = "de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe"
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override superClass() {
	}
	
	override constructors() {
		newLinkedList
	}
	
	override annotations() {
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
