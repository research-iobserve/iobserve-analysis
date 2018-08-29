package org.palladiosimulator.protocom.tech.pojo.repository

import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.pcm.repository.CompositeComponent
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.protocom.tech.pojo.PojoComposedStructureClass

/**
 * Provider for CompositeComponents.
 * 
 * In contrast to other ComposedStructures, the encapsulated components are created by this one.
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PojoCompositeComponentClass extends PojoComposedStructureClass<CompositeComponent> {
	
	new(CompositeComponent pcmEntity) {
		super(pcmEntity)
	}
	
	override interfaces() {
		#[JavaNames::interfaceName(pcmEntity)]
	}
	
	override constructors() {
		#[
			new JMethod()
				.withParameters("String assemblyContextID")
				.withImplementation(
					'''
					this.assemblyContextID = assemblyContextID;
					
					«FOR assemblyContext : pcmEntity.assemblyContexts__ComposedStructure»
					my«JavaNames::javaName(assemblyContext)» = new «JavaNames::fqn(assemblyContext.encapsulatedComponent__AssemblyContext)»("«assemblyContext.id»");
					«ENDFOR»
					
					initInnerComponents();
										
					«FOR role : pcmEntity.providedRoles_InterfaceProvidingEntity.filter[OperationProvidedRole.isInstance(it)].map[it as OperationProvidedRole] BEFORE "try {" AFTER "} catch (java.rmi.RemoteException e) {  }"»
						«JavaNames::portMemberVar(role)» = new «JavaNames::fqnPort(role)»(
						(«JavaNames::fqn(role.providedInterface__OperationProvidedRole)») my«JavaNames::javaName(PcmCommons::getProvidedDelegationConnector(pcmEntity, role).assemblyContext_ProvidedDelegationConnector)»,					
						this, assemblyContextID);
					«ENDFOR»
					''' 
				)
		]
	}
	
	override fields() {
		val results = newLinkedList
		results += super.fields
		
		// Assembly
		results +=  pcmEntity.assemblyContexts__ComposedStructure.map[ 
			new JField()
				.withName("my" + JavaNames::javaName(it))
				.withType(JavaNames::fqn(it.encapsulatedComponent__AssemblyContext))
		]

		results
	}
	
	override methods() {
		val results = newLinkedList
		results += super.methods
		
		// Init inner components
		results += #[
			new JMethod()
				.withName("initInnerComponents")
				.withVisibilityModifier(JavaConstants::VISIBILITY_PRIVATE)
				.withImplementation('''
					try {
						«FOR assemblyContext : pcmEntity.assemblyContexts__ComposedStructure»
						init«JavaNames::javaName(assemblyContext)»();
						«ENDFOR»
						
					} catch (java.rmi.RemoteException e) {
						e.printStackTrace();
					}
				''')
		]
		
		results	
	}
	
}