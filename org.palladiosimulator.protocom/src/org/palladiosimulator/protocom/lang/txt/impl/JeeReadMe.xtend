package org.palladiosimulator.protocom.lang.txt.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.txt.IReadMe

class JeeReadMe extends GeneratedFile<IReadMe> implements IReadMe{
	
	@Inject
	@Named("ProjectURI")
	String projectURI
	
	override generate() {
		'''The generated Java EE performance prototype contains the method "setContext" and the "throw RemoteException"
statement which have to be removed before the deployment on a real server. The generation of these is needed 
because of the ProtoCom framework that isn't changed to support Java EE. It is fit to Java SE. 

The "setContext" method, which has to be removed, is in the classes that represent the BasicComponents:
«FOR basicComponentClassProject: basicComponentClassName.keySet»
The class «basicComponentClassName.get(basicComponentClassProject)» in the project «projectURI»«basicComponentClassProject». 
«ENDFOR»

The "throw RemoteException" has to be removed from the Port classes of the BasicComponents: 
«FOR basicComponentPortClassProject:basicComponentPortClassName.keySet»
	«FOR basicComponentPortClass:basicComponentPortClassName.get(basicComponentPortClassProject)»
	The class «basicComponentPortClass» in the project «projectURI»«basicComponentPortClassProject».
	«ENDFOR»
«ENDFOR»

Also note that the current EJB ProtoType is tied to a Glassfish 4 runtime environment. We currently require to have such an
environment with the name "GlassFish 4" installed. Check your Eclipse settings under "Server -> Runtime Environments" to
check this requirement. In future versions, we plan to parametrize the target runtime; see:
org.palladiosimulator.protocom/src/org/palladiosimulator/protocom/tech/iiop/repository/JavaEEIIOPFacetCore.xtend
'''

	}
	
	override basicComponentClassName() {
		provider.basicComponentClassName
	}
	
	override basicComponentPortClassName() {
		provider.basicComponentPortClassName
	}
	
}