package org.palladiosimulator.protocom.traverse.jeeservlet.repository

import org.palladiosimulator.protocom.lang.java.impl.JInterface
import org.palladiosimulator.protocom.traverse.framework.repository.XInfrastructureInterface
import org.palladiosimulator.protocom.tech.servlet.repository.ServletInfrastructureInterface

/**
 * @author Sebastian Lehrig
 */
class JeeServletInfrastructureInterface extends XInfrastructureInterface {
	
	override protected generate() {
		generatedFiles.add(injector.getInstance(typeof(JInterface)).createFor(new ServletInfrastructureInterface(entity)))
	}
	
}
