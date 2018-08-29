package org.palladiosimulator.protocom.tech.pojo.repository

import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.pcm.repository.OperationInterface
import org.palladiosimulator.protocom.lang.java.util.DataTypes
import org.palladiosimulator.protocom.lang.java.util.Parameters
import org.palladiosimulator.protocom.lang.java.util.JavaNames

/**
 * Defining the content of OperationInterface classes.
 * 
 * @author Sebastian Lehrig
 */
class PojoOperationInterface extends org.palladiosimulator.protocom.tech.rmi.PojoInterface<OperationInterface> {
	
	new(OperationInterface entity) {
		super(entity)
	}
	
	override interfaces() {
		#[ JavaConstants::RMI_REMOTE_INTERFACE ]
	}
	
	override methods() {
		pcmEntity.signatures__OperationInterface.map[
			new JMethod()
				.withName(JavaNames::javaName(it))
				.withReturnType(DataTypes::getDataType(it.returnType__OperationSignature))
				.withParameters(Parameters::getParameterList(it))
				.withThrows(JavaConstants::RMI_REMOTE_EXCEPTION)
		]
	}
}