package org.palladiosimulator.protocom.tech.rmi.repository

import org.palladiosimulator.pcm.repository.ProvidedRole
import org.palladiosimulator.pcm.repository.OperationProvidedRole

import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.protocom.tech.rmi.PojoClass
import org.palladiosimulator.pcm.repository.SinkRole

/**
 * Provider for port classes based on provided roles. Please note that InfrastructureProvidedRoles
 * were - to some degree - hacked into the PCM such that cumbersome case distinction for
 * Operation and Infrastructure is necessary.
 * 
 * Possible TODO is to split PojoBasicComponentPortClass into two classes with a common super type.
 * Keep in mind though that InfrastructureComponents in ProtoCom are not generated, but rather calls 
 * to the real infrastructure!
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PojoBasicComponentPortClass extends PojoClass<ProvidedRole> {
	
	new(ProvidedRole pcmEntity) {
		super(pcmEntity)
	}
	
	override superClass() {
		'''org.palladiosimulator.protocom.framework.java.se.port.AbstractPerformancePrototypeBasicPort<«JavaNames::fqnInterface(pcmEntity.providingEntity_ProvidedRole)»>'''
	}
	
	override packageName() {
		JavaNames::fqnPortPackage(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::portClassName(pcmEntity)
	}
	
	override interfaces() {
		#[ providedRoleInterface(pcmEntity)	]
	}
	
	override constructors() {
		#[
			new JMethod()
				.withParameters(JavaNames::fqnInterface(pcmEntity.providingEntity_ProvidedRole) + " myComponent, String assemblyContext")
				.withThrows("java.rmi.RemoteException")
				.withImplementation('''
					addVisitor(org.palladiosimulator.protocom.framework.java.se.visitor.SensorFrameworkVisitor.getInstance());
				
					this.myComponent = myComponent;
					org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.registerPort(org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getRemoteAddress(),
					org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getRegistryPort(), this, "«JavaNames::portClassName(pcmEntity)»_" + assemblyContext);
					''')
		]
	}
	
	override methods() {
		providedRoleMethods(pcmEntity)
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
				.withImplementation('''
					preCallVisitor(ctx, "«JavaNames::serviceName(it)»");
					de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> result = myComponent.«JavaNames::serviceName(it)»(ctx);
					postCallVisitor(ctx, "«JavaNames::serviceName(it)»");

					return result;
				''')
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
	
	/**
	 * TODO Implement SinkRoles?
	 */
	def dispatch providedRoleMethods(SinkRole role) {
	}
		
	
	def dispatch providedRoleInterface(OperationProvidedRole role) {
		JavaNames::fqn(role.providedInterface__OperationProvidedRole)
	}
	
	def dispatch providedRoleInterface(InfrastructureProvidedRole role) {
		JavaNames::fqn(role.providedInterface__InfrastructureProvidedRole)
	}
	
	/**
	 * TODO Implement SinkRoles?
	 */
	def dispatch providedRoleInterface(SinkRole role) {
		""
	}
}