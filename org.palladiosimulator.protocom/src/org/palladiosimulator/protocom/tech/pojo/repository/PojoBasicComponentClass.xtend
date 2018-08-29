package org.palladiosimulator.protocom.tech.pojo.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole
import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.DataTypes
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.Parameters
import org.palladiosimulator.protocom.tech.rmi.PojoClass
import org.palladiosimulator.protocom.tech.rmi.util.PcmRMIStubAction

/**
 * Defining the content of component implementations (classes implementing the component behavior).
 * 
 * @author Sebastian Lehrig
 */
class PojoBasicComponentClass extends PojoClass<BasicComponent> {
	
	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}
	
	override interfaces() {
		#[ JavaNames::interfaceName(pcmEntity) ]
	}
	
	override constructors() {
		#[
			new JMethod()
				.withParameters("String assemblyContextID")
				.withImplementation(
					'''
					this.assemblyContextID = assemblyContextID;
					«FOR role : pcmEntity.providedRoles_InterfaceProvidingEntity.filter[OperationProvidedRole.isInstance(it)].map[it as OperationProvidedRole] BEFORE "try {" AFTER "} catch (java.rmi.RemoteException e) {  }"»
						«JavaNames::portMemberVar(role)» = new «JavaNames::fqnPort(role)»(this, assemblyContextID);
					«ENDFOR»
					
					«FOR role : pcmEntity.providedRoles_InterfaceProvidingEntity.filter[InfrastructureProvidedRole.isInstance(it)].map[it as InfrastructureProvidedRole] BEFORE "try {" AFTER "} catch (java.rmi.RemoteException e) {  }"»
						«JavaNames::portMemberVar(role)» = new «JavaNames::fqnPort(role)»(this, assemblyContextID);
					«ENDFOR»
					
					«FOR resource : pcmEntity.passiveResource_BasicComponent»
					passive_resource_«JavaNames::javaVariableName(resource.entityName)» = new java.util.concurrent.Semaphore(de.uka.ipd.sdq.simucomframework.variables.StackContext.evaluateStatic("«JavaNames::specificationString(resource.capacity_PassiveResource.specification)»", Integer.class), true);
					«ENDFOR»
					'''
				)
		]
	}
	
	override fields() {
		val results = newLinkedList

		// Context & ComponentFrame
		results += #[
			new JField()
				.withName("myContext")
				.withType(JavaNames::fqnContextInterface(pcmEntity))
		]
		
		// Assembly context
		results += #[
			new JField()
				.withName("assemblyContextID")
				.withType("String")
		]
		
		// Provided ports
		results += pcmEntity.providedRoles_InterfaceProvidingEntity.filter[OperationProvidedRole.isInstance(it)].map[it as OperationProvidedRole].map[
			new JField()
				.withName(JavaNames::portMemberVar(it))
				.withType(JavaNames::fqn(it.providedInterface__OperationProvidedRole))
		]
		
		results += pcmEntity.providedRoles_InterfaceProvidingEntity.filter[InfrastructureProvidedRole.isInstance(it)].map[it as InfrastructureProvidedRole].map[
			new JField()
				.withName(JavaNames::portMemberVar(it))
				.withType(JavaNames::fqn(it.providedInterface__InfrastructureProvidedRole))
		]
		
		// Passive resources
		results += pcmEntity.passiveResource_BasicComponent.map[
			new JField()
				.withName("passive_resource_" + JavaNames::javaVariableName(it.entityName))
				.withType("java.util.concurrent.Semaphore")
		]

		results
	}
	
	override methods() {
		val results = newLinkedList
		
		// Context & ComponentFrame
		results += #[
			new JMethod()
				.withName("setContext")
				.withParameters("Object myContext")
				.withImplementation("this.myContext = (" + JavaNames::fqnContextInterface(pcmEntity) + ") myContext;")
		]

		// SEFFs
		results += pcmEntity.serviceEffectSpecifications__BasicComponent.map[
			new JMethod()
				.withName(JavaNames::serviceNameStub(it.describedService__SEFF))
				.withReturnType(DataTypes::getReturnDataType(it.describedService__SEFF))
				.withParameters(Parameters::getParameterList(it.describedService__SEFF))
				.withImplementation('''
					«new PcmRMIStubAction().actions((it as ResourceDemandingBehaviour).steps_Behaviour.get(0))»
					«IF (DataTypes::getReturnDataType(it.describedService__SEFF).equals("byte") || 
						 DataTypes::getReturnDataType(it.describedService__SEFF).equals("double") || 
						 DataTypes::getReturnDataType(it.describedService__SEFF).equals("int") || 
						 DataTypes::getReturnDataType(it.describedService__SEFF).equals("long")) »
						return 0;
					«ELSEIF (DataTypes::getReturnDataType(it.describedService__SEFF).equals("char")) »
						return 'A';
					«ELSEIF (DataTypes::getReturnDataType(it.describedService__SEFF).equals("boolean")) »
						return false;
					«ELSEIF (!DataTypes::getReturnDataType(it.describedService__SEFF).equals("void"))»
						return null;
					«ENDIF»
					''')
		]
		
		// Provided ports getter for OperationProvidedRoles
		results += pcmEntity.providedRoles_InterfaceProvidingEntity.filter[OperationProvidedRole.isInstance(it)].map[
			new JMethod()
				.withName(JavaNames::portGetter(it))
				.withReturnType(JavaNames::fqn((it as OperationProvidedRole).providedInterface__OperationProvidedRole))
				.withImplementation("return " + JavaNames::portMemberVar(it as OperationProvidedRole) + ";")
		]
		// Main method. 
		// TODO: extract common implementation with system.
		results += new JMethod()
			.withName("main")
			.withParameters("String[] args")
			.withStaticModifier		
			.withImplementation('''
				String ip = org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getIpFromArguments(args);
				int port = org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getPortFromArguments(args);
				
				String assemblyContext = org.palladiosimulator.protocom.framework.java.se.AbstractMain.getAssemblyContextFromArguments(args);
				
				org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.setRemoteAddress(ip);
				org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.setRegistryPort(port);
				
				new «JavaNames::fqn(pcmEntity)»(assemblyContext);
			''')
		
		results	
	}
	
}