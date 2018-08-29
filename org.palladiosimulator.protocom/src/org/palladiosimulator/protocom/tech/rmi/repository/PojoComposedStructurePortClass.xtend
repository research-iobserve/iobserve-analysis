package org.palladiosimulator.protocom.tech.rmi.repository

import org.palladiosimulator.pcm.repository.ProvidedRole
import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.protocom.tech.rmi.PojoClass

/**
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PojoComposedStructurePortClass extends PojoClass<ProvidedRole> {
	
	new(ProvidedRole pcmEntity) {
		super(pcmEntity)
	}
	
	override superClass() {
		JavaConstants::RMI_REMOTE_OBJECT_CLASS
	}
	
	override fields() {
		#[
			new JField()
				.withName("myCompositeComponent")
				.withType(JavaNames::fqnInterface(pcmEntity.providingEntity_ProvidedRole)),
			new JField()
				.withName("myInnerPort")
				.withType(JavaNames::fqn((pcmEntity as OperationProvidedRole).providedInterface__OperationProvidedRole))
		]
	}
	
	override packageName() {
		JavaNames::fqnPortPackage(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::portClassName(pcmEntity)
	}
	
	override interfaces() {
		#[ 
			"org.palladiosimulator.protocom.framework.java.se.port.IPerformancePrototypePort<" + JavaNames::fqnInterface(pcmEntity.providingEntity_ProvidedRole) + ">",
			JavaNames::fqn((pcmEntity as OperationProvidedRole).providedInterface__OperationProvidedRole),
			JavaConstants::RMI_REMOTE_INTERFACE, 
			JavaConstants::SERIALIZABLE_INTERFACE
		]
	}
	
	override constructors() {
		#[
			new JMethod()
				.withParameters(JavaNames::fqn((pcmEntity as OperationProvidedRole).providedInterface__OperationProvidedRole) + " myInnerPort, " + JavaNames::fqnInterface(pcmEntity.providingEntity_ProvidedRole) + " myComponent, String assemblyContext")
				.withThrows(JavaConstants::RMI_REMOTE_EXCEPTION)
				.withImplementation('''
					this.myInnerPort = myInnerPort;
					this.myCompositeComponent = myComponent;
					org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.registerPort(org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getRemoteAddress(),
					org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getRegistryPort(), this, "«JavaNames::portClassName(pcmEntity)»_" + assemblyContext);
					''')
		]
	}

	override methods() {
		val results = newLinkedList
		
		results += providedRoleMethods(pcmEntity)
		
		results += #[
			
			new JMethod()
				.withName("setContext")
				.withParameters("Object myContext")
				.withThrows(JavaConstants::RMI_REMOTE_EXCEPTION)
				.withImplementation("myCompositeComponent.setContext(myContext);"),
				
			new JMethod()
				.withName("setComponentFrame")
				.withParameters(PcmCommons::stackframeParameterList)
				.withThrows(JavaConstants::RMI_REMOTE_EXCEPTION)
				.withImplementation(""),
				
			new JMethod()
				.withName("getComponent")
				.withReturnType(JavaNames::fqnInterface(pcmEntity.providingEntity_ProvidedRole))
				.withThrows(JavaConstants::RMI_REMOTE_EXCEPTION)
				.withImplementation("return myCompositeComponent;")
			
		]
		
		results
		
	}
	
	
	
	override filePath() {
		"/src/" + JavaNames::fqnToDirectoryPath(JavaNames::fqnPortPackage(pcmEntity)) + "/" + JavaNames::portClassName(pcmEntity) + ".java"
	}
	
	
	def dispatch providedRoleMethods(OperationProvidedRole role) {

		role.providedInterface__OperationProvidedRole.signatures__OperationInterface.map[
			
			new JMethod()
				.withName(JavaNames::javaSignature(it))
				.withReturnType(PcmCommons::stackframeType)
				.withParameters(PcmCommons::stackContextParameterList)
				.withImplementation("return myInnerPort." + JavaNames::javaSignature(it) + "(ctx);")
				.withThrows(JavaConstants::RMI_REMOTE_EXCEPTION)
			
		] 

	}
	
	def dispatch providedRoleMethods(InfrastructureProvidedRole role) {

		role.providedInterface__InfrastructureProvidedRole.infrastructureSignatures__InfrastructureInterface.map[
			
			new JMethod()
				.withName(JavaNames::javaSignature(it))
				.withReturnType(PcmCommons::stackframeType)
				.withParameters(PcmCommons::stackContextParameterList)
				.withImplementation("return null;")
				.withThrows(JavaConstants::RMI_REMOTE_EXCEPTION)
			
		] 
		
	}
	
	
}