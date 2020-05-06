package org.palladiosimulator.protocom.lang.xml.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.xml.IJeeGlassfishEjbDescriptor

class JeeGlassfishEjbDescriptor extends GeneratedFile<IJeeGlassfishEjbDescriptor> implements IJeeGlassfishEjbDescriptor{
	@Inject
	@Named("ProjectURI")
	String projectURI
	
	def header() {
		'''
		<?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPE glassfish-ejb-jar PUBLIC "-//GlassFish.org//DTD GlassFish Application Server 3.1 EJB 3.1//EN" "http://glassfish.org/dtds/glassfish-ejb-jar_3_1-1.dtd">'''
	}
	
	def body(){
	'''
	<glassfish-ejb-jar>
	«IF requiredComponentsAndResourceContainerIPAddress.isEmpty»
		<enterprise-beans/>
	«ELSE»
	«FOR r : requiredComponentsAndResourceContainerIPAddress.keySet»
<enterprise-beans>
	<ejb>
		<ejb-name>«ejbName»</ejb-name>
		<ejb-ref>
			<ejb-ref-name>«JavaNames::javaName(r.providedRole_AssemblyConnector.providingEntity_ProvidedRole).toFirstLower +
							".interfaces.ejb." +
							JavaNames::javaName(r.providedRole_AssemblyConnector.providedInterface__OperationProvidedRole)»</ejb-ref-name>
			<jndi-name>corbaname:iiop:«requiredComponentsAndResourceContainerIPAddress.get(r)»#java:global/«projectURI»«JavaNames::fqnJavaEEBasicComponentProjectName(r.providedRole_AssemblyConnector.providingEntity_ProvidedRole)»/«JavaNames::portClassName(r.providedRole_AssemblyConnector)»!«projectURI».«JavaNames::javaName(r.providedRole_AssemblyConnector.providingEntity_ProvidedRole).toFirstLower +
							".interfaces.ejb." +
							JavaNames::javaName(r.providedRole_AssemblyConnector.providedInterface__OperationProvidedRole)»</jndi-name>
       </ejb-ref>
     </ejb>
</enterprise-beans>
    	«ENDFOR»
	«ENDIF»
	</glassfish-ejb-jar>'''	
	}
	
	override ejbName() {
		provider.ejbName
	}
	
	override ejbRefName() {
		provider.ejbRefName
	}
	
	override jndiName() {
		provider.jndiName
	}
	
	override generate() {
		'''
		«header»
		«body»
		'''
	}
	
	override displayName() {
		provider.displayName
	}
	
	override ipAddress(){
		provider.ipAddress
	}
	
	override requiredComponentsAndResourceContainerIPAddress(){
		provider.requiredComponentsAndResourceContainerIPAddress
	}
}
