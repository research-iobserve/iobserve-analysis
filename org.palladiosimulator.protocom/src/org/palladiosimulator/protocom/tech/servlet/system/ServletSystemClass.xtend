package org.palladiosimulator.protocom.tech.servlet.system

import org.palladiosimulator.pcm.core.entity.ComposedProvidingRequiringEntity
import org.palladiosimulator.pcm.repository.OperationRequiredRole
import org.palladiosimulator.protocom.lang.java.IJClass
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.PcmCalls
import org.palladiosimulator.protocom.tech.servlet.ServletClass
import org.palladiosimulator.protocom.lang.java.impl.JAnnotation
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.pcm.core.composition.AssemblyConnector
import org.palladiosimulator.protocom.model.system.SystemAdapter

class ServletSystemClass<E extends ComposedProvidingRequiringEntity> extends ServletClass<E> implements IJClass {
	private val SystemAdapter entity
	
	new(SystemAdapter entity, E pcmEntity) {
		super(pcmEntity)
		this.entity = entity
	}
	
	override interfaces() {
		#[entity.interfaceName]
	}
	
	override fields() {
		var result = newLinkedList
		
		// Port classes.
		result += entity.assemblyContexts.map[
			new JField()
				.withName("my" + it.safeName)
				.withType('''«frameworkBase».prototype.IPort<«it.encapsulatedComponent.classFqn»>''')
		]
		
		// Port IDs.
		for (assemblyContext : entity.assemblyContexts) {
			if(assemblyContext.encapsulatedComponent.operationProvidedRoles.size > 0) { // check for Infrastructure components
				result += new JField()
					.withName(assemblyContext.safeName + "ID")
					.withType("String")
					//«JavaNames::portClassName(assemblyContext.encapsulatedComponent.entity.providedRoles_InterfaceProvidingEntity.filter[OperationProvidedRole.isInstance(it)].get(0) as OperationProvidedRole)»_«assemblyContext.id»
					.withInitialization('''"«assemblyContext.encapsulatedComponent.operationProvidedRoles.get(0).portClassName»_«assemblyContext.id»"''')
			}
		} 
		
		result
	}
	
	override constructors() {
		#[
			new JMethod()
				.withParameters("String location, String id")
				.withThrows('''«frameworkBase».protocol.RegistryException''')
				.withImplementation(
					'''
					«frameworkBase».prototype.LocalComponentRegistry.getInstance().addComponent(id, this);
					
					initInnerComponents();
					
					«FOR role : entity.operationProvidedRoles»
						startPort(location, "«role.portClassName»", id, «JavaNames::javaName(PcmCommons::getProvidedDelegationConnector(pcmEntity, role.entity).assemblyContext_ProvidedDelegationConnector)»ID);
					«ENDFOR»
					'''
				)
		]
	}
	
	override methods() {
		var result = newLinkedList
		
		// Port starting helper method.
		result +=
			new JMethod()
				.withVisibilityModifier("private")
				.withName("startPort")
				.withParameters("String location, String portName, String id, String innerId")
				.withImplementation('''
					java.util.ArrayList<«frameworkBase».protocol.Parameter> params = new java.util.ArrayList<«frameworkBase».protocol.Parameter>(4);
					params.add(new «frameworkBase».protocol.Parameter("action", "start"));
					params.add(new «frameworkBase».protocol.Parameter("location", location));
					params.add(new «frameworkBase».protocol.Parameter("assemblyContext", id));
					params.add(new «frameworkBase».protocol.Parameter("componentId", innerId));
					
					«frameworkBase».protocol.Request.get(location, "/" + portName, params);
				''')
		
		// Assembly init methods.
		//result += pcmEntity.assemblyContexts__ComposedStructure.map[
		result += entity.assemblyContexts.map[
			val x = it.entity
			new JMethod()
				.withName("init" + it.safeName)
				.withVisibilityModifier("private")
				.withImplementation('''
					«JavaNames::fqnContext(x.encapsulatedComponent__AssemblyContext)» context = new «JavaNames::fqnContext(x.encapsulatedComponent__AssemblyContext)»(
						«FOR requiredRole : x.encapsulatedComponent__AssemblyContext.requiredRoles_InterfaceRequiringEntity.filter[OperationRequiredRole.isInstance(it)].map[it as OperationRequiredRole] SEPARATOR ", \n"»
							«JavaNames::javaName((PcmCalls::getConnector(pcmEntity, x, requiredRole) as AssemblyConnector).providingAssemblyContext_AssemblyConnector)»ID
						«ENDFOR»
					);
					
					my«it.safeName».setContext(context);
				''')
		]
		
		// System init method.
		result += 
			new JMethod()
				.withName("initInnerComponents")
				.withVisibilityModifier("private")
				.withThrows('''«frameworkBase».protocol.RegistryException''')
				.withAnnotations(#[
					new JAnnotation()
						.withName("SuppressWarnings")
						.withValues(#['''"unchecked"'''])
				])
				.withImplementation('''
					try {
						«FOR assemblyContext : entity.assemblyContexts»
							«IF assemblyContext.encapsulatedComponent.operationProvidedRoles.size > 0»
								my«assemblyContext.safeName» = («frameworkBase».prototype.IPort<«assemblyContext.encapsulatedComponent.classFqn»>) «frameworkBase».protocol.Registry.getInstance().lookup("«assemblyContext.encapsulatedComponent.operationProvidedRoles.get(0).portClassName»_«assemblyContext.id»");
							«ENDIF»
						«ENDFOR»
						
						«FOR assemblyContext : entity.assemblyContexts»
							init«assemblyContext.safeName»();
						«ENDFOR»
					} catch («frameworkBase».protocol.RegistryException e) {
						throw e;
					}
				''')
				
			result
	}
}
