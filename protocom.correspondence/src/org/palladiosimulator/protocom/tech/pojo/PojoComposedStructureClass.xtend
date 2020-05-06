package org.palladiosimulator.protocom.tech.pojo

import org.palladiosimulator.protocom.lang.java.IJClass
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.pcm.core.entity.ComposedProvidingRequiringEntity
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.util.PcmCalls
import org.palladiosimulator.pcm.repository.OperationRequiredRole
import org.palladiosimulator.protocom.tech.rmi.PojoClass

/**
 * Common provider for System and CompositeComponent elements.
 * 
 * The most important difference between a System and a CompositeComponent is that the 
 * BasicComponents of a CompositeComponent are always deployed on the same ResourceEnvironment
 * and therefore can be initialized by the enclosing child component.
 * 
 * This does NOT hold for Systems, as these use the RMI registry to assembly their enclosed
 * child components.
 * 
 * @author Sebastian Lehrig
 */
abstract class PojoComposedStructureClass<E extends ComposedProvidingRequiringEntity> extends PojoClass<E> implements IJClass {
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override fields() {
		var results = newLinkedList
		
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
				.withType(JavaConstants::TYPE_STRING)
		]
		
		// Provided ports
		results += pcmEntity.providedRoles_InterfaceProvidingEntity.filter[OperationProvidedRole.isInstance(it)].map[it as OperationProvidedRole].map[
			new JField()
				.withName(JavaNames::portMemberVar(it))
				.withType(JavaNames::fqn(it.providedInterface__OperationProvidedRole))
		]
		
		results
	}
	
	override methods() {
		var results = newLinkedList
		
		// Context & ComponentFrame
		results += #[
			new JMethod()
				.withName("setContext")
				.withParameters("Object myContext")
				.withImplementation("this.myContext = (" + JavaNames::fqnContextInterface(pcmEntity) + ") myContext;")

		]
						
		// Init methods for each assembly context.
		results += pcmEntity.assemblyContexts__ComposedStructure.map[
			new JMethod()
				.withName("init" + JavaNames::javaName(it))
				.withVisibilityModifier(JavaConstants::VISIBILITY_PRIVATE)
				.withThrows(JavaConstants::RMI_REMOTE_EXCEPTION)
				.withImplementation('''
					«JavaNames::fqnContext(it.encapsulatedComponent__AssemblyContext)» context = new «JavaNames::fqnContext(it.encapsulatedComponent__AssemblyContext)»(
					«FOR requiredRole : it.encapsulatedComponent__AssemblyContext.requiredRoles_InterfaceRequiringEntity.filter[OperationRequiredRole.isInstance(it)].map[it as OperationRequiredRole] SEPARATOR ", "»
						«PcmCalls::portQuery(requiredRole, pcmEntity, it)»
					«ENDFOR»);					
					my«JavaNames::javaName(it)».setContext(context);
				''')
		]
		
		
		// Provided ports getter for OperationProvidedRoles
		results += pcmEntity.providedRoles_InterfaceProvidingEntity.filter[OperationProvidedRole.isInstance(it)].map[it as OperationProvidedRole].map[
			new JMethod()
				.withName(JavaNames::portGetter(it))
				.withReturnType(JavaNames::fqn(it.providedInterface__OperationProvidedRole))
				.withImplementation("return " + JavaNames::portMemberVar(it) + ";")
		]
		
		results
	}
}