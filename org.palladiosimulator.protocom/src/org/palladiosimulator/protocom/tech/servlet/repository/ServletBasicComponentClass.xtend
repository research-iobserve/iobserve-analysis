package org.palladiosimulator.protocom.tech.servlet.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.model.repository.BasicComponentAdapter
import org.palladiosimulator.protocom.tech.servlet.ServletClass
import org.palladiosimulator.protocom.tech.servlet.util.PcmServletProtoAction

class ServletBasicComponentClass extends ServletClass<BasicComponent> {
	private val BasicComponentAdapter entity
	
	new(BasicComponentAdapter entity, BasicComponent pcmEntity) {
		super(pcmEntity)
		this.entity = entity
	}
	
	override interfaces() {
		#[entity.interfaceName]
	}
	
	override constructors() {
		#[
			new JMethod()
				.withParameters("String location, String assemblyContext")
				.withImplementation('''
					«frameworkBase».prototype.LocalComponentRegistry.getInstance().addComponent(assemblyContext, this);
					
					java.util.ArrayList<«frameworkBase».protocol.Parameter> params = new java.util.ArrayList<«frameworkBase».protocol.Parameter>(3);
					params.add(new «frameworkBase».protocol.Parameter("action", "start"));
					params.add(new «frameworkBase».protocol.Parameter("location", location));
					params.add(new «frameworkBase».protocol.Parameter("assemblyContext", assemblyContext));
					
					«FOR role : entity.operationProvidedRoles»
						«frameworkBase».protocol.Request.get(location, "/«role.portClassName»", params);
					«ENDFOR»
				''')
		]
	}
	
	override fields() {
		var result = newLinkedList
		
		result += #[
			new JField()
				.withName("context")
				.withType(entity.contextInterfaceFqn)
		]
		
		result
	}
	
	override methods() {
		var result = newLinkedList
		
		result +=
			new JMethod()
				.withName("setContext")
				.withParameters("Object context")
				.withImplementation("this.context = (" + entity.contextInterfaceFqn + ") context;")
		
		// Generate SEFFs.
		result += entity.serviceEffectSpecifications.map[
			val signature = it.signature
			
			new JMethod()
				.withName(signature.serviceName)
				//.withReturnType('''de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object>''')
				//.withParameters('''de.uka.ipd.sdq.simucomframework.variables.StackContext ctx''')
				.withReturnType('''«stackFrame»<Object>''')
				.withParameters('''«stackContext» ctx''')
				.withImplementation('''
					org.apache.log4j.Logger.getRootLogger().info("Invoking '«signature.serviceName»'");
					ctx.getStack().createAndPushNewStackFrame();
					«new PcmServletProtoAction().actions(it.start)»
					return null;
				''')
				
				//new PcmServletProtoAction().actions((it.entity as ResourceDemandingBehaviour).steps_Behaviour.get(0))
		]
		
		result
	}
}
