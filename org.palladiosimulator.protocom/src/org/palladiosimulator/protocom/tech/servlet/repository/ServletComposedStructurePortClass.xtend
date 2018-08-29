package org.palladiosimulator.protocom.tech.servlet.repository

import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole
import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.pcm.repository.ProvidedRole
import java.util.List
import org.palladiosimulator.protocom.lang.java.impl.JAnnotation
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.servlet.ServletClass

class ServletComposedStructurePortClass extends ServletClass<ProvidedRole> {
	new(ProvidedRole pcmEntity) {
		super(pcmEntity)
	}
	
	override packageName() {
		JavaNames::fqnPortPackage(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::portClassName(pcmEntity)
	}
	
	override interfaces() {
		#[JavaNames::fqn((pcmEntity as OperationProvidedRole).providedInterface__OperationProvidedRole)]
	}
	
	override superClass() {
		'''«frameworkBase».prototype.PortServlet<«JavaNames::fqnInterface(pcmEntity.providingEntity_ProvidedRole)»>'''
	}
	
	override annotations() {
		#[
			new JAnnotation()
				.withName("javax.servlet.annotation.WebServlet")
				.withValues(#['''urlPatterns = "/«compilationUnitName»"''', "loadOnStartup = 0"])
		]
	}
	
	override fields() {
		#[
			new JField()
				.asDefaultSerialVersionUID(), 
			
			new JField()
				.withName("compositeComponentOrSystem")
				.withType(JavaNames::fqnInterface(pcmEntity.providingEntity_ProvidedRole)),
			
			new JField()
				.withName("innerPort")
				.withType(JavaNames::fqn((pcmEntity as OperationProvidedRole).providedInterface__OperationProvidedRole))
		]
	}
	
	override constructors() {
		#[
			new JMethod()
				.withImplementation('''
				''')
		]
	}
	
	override methods() {
		var String iface
		var result = newLinkedList
		
		if (pcmEntity instanceof OperationProvidedRole) {
			iface = JavaNames::fqn(pcmEntity.providedInterface__OperationProvidedRole)
		}
		
		result +=
			new JMethod()
				.withVisibilityModifier("public")
				.withParameters("String componentId, String assemblyContext")
				.withName("start")
				.withThrows('''«frameworkBase».modules.ModuleStartException''')
				.withImplementation('''
					this.component = («JavaNames::fqnInterface(pcmEntity.providingEntity_ProvidedRole)») «frameworkBase».prototype.LocalComponentRegistry.getInstance().getComponent(assemblyContext);
					
					try {
						innerPort = («JavaNames::fqn((pcmEntity as OperationProvidedRole).providedInterface__OperationProvidedRole)») «frameworkBase».protocol.Registry.getInstance().lookup(componentId);
					} catch («frameworkBase».protocol.RegistryException e) {
						e.printStackTrace();
					}
					
					try {
						Class<?>[] interfaces = new Class<?>[] {«iface».class, «frameworkBase».prototype.IPort.class};
						«frameworkBase».protocol.Registry.getInstance().register("«JavaNames::portClassName(pcmEntity)»", interfaces, location, "/«JavaNames::portClassName(pcmEntity)»");
					} catch («frameworkBase».protocol.RegistryException e) {
						throw new «frameworkBase».modules.ModuleStartException();
					}
				''')
		
		result +=
			new JMethod()
				.withVisibilityModifier("public")
				.withParameters("Object context")
				.withName("setContext")
				.withImplementation('''
				''')
				
		result += providedRoleMethods(pcmEntity)
						
		result
	}
	
	override filePath() {
		"/src/" + JavaNames::fqnToDirectoryPath(JavaNames::fqnPortPackage(pcmEntity)) + "/" + JavaNames::portClassName(pcmEntity) + ".java"
	}
	
	def dispatch providedRoleMethods(OperationProvidedRole role) {
		role.providedInterface__OperationProvidedRole.signatures__OperationInterface.map[
			new JMethod()
				.withName(JavaNames::javaSignature(it))
				//.withReturnType('''de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object>''')
				//.withParameters('''de.uka.ipd.sdq.simucomframework.variables.StackContext ctx''')
				.withReturnType('''«stackFrame»<Object>''')
				.withParameters('''«stackContext» ctx''')
				.withImplementation("return innerPort." + JavaNames::javaSignature(it) + "(ctx);")			
		]
	}
	
	def dispatch List<JMethod> providedRoleMethods(InfrastructureProvidedRole role) {
		#[]
	}
}
