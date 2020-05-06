package org.palladiosimulator.protocom.traverse.jse.resourceenvironment

import org.palladiosimulator.protocom.traverse.framework.resourceenvironment.XResourceEnvironment
import org.palladiosimulator.protocom.lang.java.impl.JClass
import org.palladiosimulator.protocom.tech.rmi.resourceenvironment.PojoResourceEnvironment

/**
 * Resource Environments for JSE are a config file used for calibration of active resources.
 * 
 * @author Thomas Zolynski
 */
class JseResourceEnvironment extends XResourceEnvironment {
	
	override generate() {
		
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoResourceEnvironment(entity)))
			
	}
	
}