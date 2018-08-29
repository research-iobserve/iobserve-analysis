package org.palladiosimulator.protocom.traverse.jeeservlet.repository

import org.palladiosimulator.protocom.traverse.framework.repository.XOperationInterface
import org.palladiosimulator.protocom.lang.java.impl.JInterface
import org.palladiosimulator.protocom.tech.servlet.repository.ServletOperationInterface

class JeeServletOperationInterface extends XOperationInterface {
	override protected generate() {
		generatedFiles.add(injector.getInstance(typeof(JInterface)).createFor(new ServletOperationInterface(entity)))
	}
}
