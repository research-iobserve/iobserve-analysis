package org.palladiosimulator.protocom.tech.pojo.repository

import org.palladiosimulator.pcm.repository.InfrastructureInterface
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.protocom.lang.java.util.Parameters
import org.palladiosimulator.protocom.tech.rmi.PojoInterface
import org.palladiosimulator.protocom.lang.java.util.JavaNames

/**
 * Defining the content of InfrastructureInterface classes.
 * 
 * @author Sebastian Lehrig
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
				.withName(JavaNames::javaName(it))
				.withReturnType("void")
				.withParameters(Parameters::getParameterList(it))
				.withThrows(JavaConstants::RMI_REMOTE_EXCEPTION)
		]
	}
}