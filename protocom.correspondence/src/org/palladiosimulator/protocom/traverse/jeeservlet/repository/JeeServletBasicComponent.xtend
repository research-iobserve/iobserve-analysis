package org.palladiosimulator.protocom.traverse.jeeservlet.repository

import org.palladiosimulator.protocom.traverse.framework.repository.XBasicComponent
import org.palladiosimulator.protocom.lang.java.impl.JClass
import org.palladiosimulator.protocom.tech.servlet.repository.ServletBasicComponentClass
import org.palladiosimulator.protocom.lang.java.impl.JInterface
import org.palladiosimulator.protocom.tech.servlet.repository.ServletComponentClassInterface
import org.palladiosimulator.protocom.tech.servlet.repository.ServletBasicComponentContextClass
import org.palladiosimulator.protocom.tech.servlet.repository.ServletBasicComponentContextInterface
import org.palladiosimulator.protocom.tech.servlet.repository.ServletBasicComponentPortClass
import org.palladiosimulator.protocom.model.repository.BasicComponentAdapter

/**
 * @author Christian Klaussner
 * @author Sebastian Lehrig
 */
class JeeServletBasicComponent extends XBasicComponent {
	override protected generate() {
		
		// interface and class for the component.
		val adapter = new BasicComponentAdapter(entity)
		generatedFiles.add(injector.getInstance(typeof(JInterface)).createFor(new ServletComponentClassInterface(entity)))
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new ServletBasicComponentClass(adapter, entity)))
		
		
		// interface and class for the context.
		generatedFiles.add(injector.getInstance(typeof(JInterface)).createFor(new ServletBasicComponentContextInterface(entity)))
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new ServletBasicComponentContextClass(adapter, entity)))
		entity.providedRoles_InterfaceProvidingEntity.forEach[
			generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new ServletBasicComponentPortClass(it)))
		]
		
	}	
}
