package org.palladiosimulator.protocom.lang.xml

import org.palladiosimulator.pcm.core.composition.AssemblyConnector
import java.util.HashMap

interface IJeeGlassfishEjbDescriptor extends IJeeDescriptor {
	 
	def String jndiName()
	
	def String ipAddress()
	
	def HashMap<AssemblyConnector,String> requiredComponentsAndResourceContainerIPAddress()
}