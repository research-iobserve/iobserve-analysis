package org.palladiosimulator.protocom.tech.rmi.repository

import org.palladiosimulator.pcm.repository.InfrastructureInterface
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.protocom.tech.rmi.PojoInterface

/**
 * Defining the content of InfrastructureInterface classes.
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PojoInfrastructureInterface extends PojoInterface<InfrastructureInterface> {
	
	new(InfrastructureInterface entity) {
		super(entity)
	}
	
	override interfaces() {
		#[ JavaConstants::RMI_REMOTE_INTERFACE ]
	}
	
	override methods() {
		pcmEntity.infrastructureSignatures__InfrastructureInterface.map[
			new JMethod()
				.withName(JavaNames::javaSignature(it))
				.withReturnType(PcmCommons::stackframeType)
				.withParameters(PcmCommons::stackContextParameterList)
				.withThrows(JavaConstants::RMI_REMOTE_EXCEPTION)
		]
	}
}