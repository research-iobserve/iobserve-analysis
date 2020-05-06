package org.palladiosimulator.protocom.tech.iiop.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPInterface

class JavaEEIIOPComponentClassInterface extends JavaEEIIOPInterface<BasicComponent> {
	
	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}
	
	/*override compilationUnitName() {
		JavaNames::interfaceName(pcmEntity)
	}*/
	
	override interfaces() {
		#["org.palladiosimulator.protocom.framework.java.se.IPerformancePrototypeComponent"]
	}
	
	override methods() {
		val results = newLinkedList
		
		// ComponentFrame
		results += #[
			new JMethod()
				.withName("setComponentFrame")
				.withParameters(PcmCommons::stackframeParameterList)
			]
			
		// From operation interfaces	
		results += pcmEntity.serviceEffectSpecifications__BasicComponent.map[
			new JMethod()
				.withName(JavaNames::serviceName(it.describedService__SEFF))
				.withReturnType(PcmCommons::stackframeType)
				.withParameters(PcmCommons::stackContextParameterList)
		]
	
		results
	}
	
	override filePath() {
		JavaNames::fqnJavaEEComponentInterfacePath(pcmEntity)
	}
	
	override projectName(){
		JavaNames::fqnJavaEEBasicComponentProjectName(pcmEntity)
	}
	
	override jeeInterfaceAnnotation() {
		JavaConstants::JEE_INTERFACE_ANNOTATION_LOCAL
	}
	
	
}