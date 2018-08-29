package org.palladiosimulator.protocom.tech.servlet.repository

import org.palladiosimulator.protocom.tech.servlet.ServletInterface
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.pcm.repository.InfrastructureInterface

/**
 * @author Sebastian Lehrig
 */
class ServletInfrastructureInterface extends ServletInterface<InfrastructureInterface> {
	
	new(InfrastructureInterface pcmEntity) {
		super(pcmEntity)
	}
	
	override methods() {
		pcmEntity.infrastructureSignatures__InfrastructureInterface.map[
			new JMethod()
				.withName(JavaNames::javaSignature(it))
				//.withReturnType('''de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object>''')
				//.withParameters('''de.uka.ipd.sdq.simucomframework.variables.StackContext ctx''')
				.withReturnType('''«stackFrame»<Object>''')
				.withParameters('''«stackContext» ctx''')
		]
	}
}
