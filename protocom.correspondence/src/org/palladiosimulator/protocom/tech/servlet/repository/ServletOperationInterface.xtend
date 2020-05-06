package org.palladiosimulator.protocom.tech.servlet.repository

import org.palladiosimulator.protocom.tech.servlet.ServletInterface
import org.palladiosimulator.pcm.repository.OperationInterface
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.impl.JMethod

class ServletOperationInterface extends ServletInterface<OperationInterface> {
	new(OperationInterface pcmEntity) {
		super(pcmEntity)
	}
	
	override methods() {
		pcmEntity.signatures__OperationInterface.map[
			new JMethod()
				.withName(JavaNames::javaSignature(it))
				//.withReturnType('''de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object>''')
				//.withParameters('''de.uka.ipd.sdq.simucomframework.variables.StackContext ctx''')
				.withReturnType('''«stackFrame»<Object>''')
				.withParameters('''«stackContext» ctx''')
		]
	}
}
