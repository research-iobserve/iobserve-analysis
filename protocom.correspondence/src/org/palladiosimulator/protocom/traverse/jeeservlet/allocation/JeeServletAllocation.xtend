package org.palladiosimulator.protocom.traverse.jeeservlet.allocation

import org.palladiosimulator.protocom.traverse.framework.allocation.XAllocation
import org.palladiosimulator.protocom.lang.java.impl.JClass
import org.palladiosimulator.protocom.tech.servlet.allocation.ServletAllocationStorage
import org.palladiosimulator.protocom.model.allocation.AllocationAdapter

class JeeServletAllocation extends XAllocation {
	override protected generate() {
		val adapter = new AllocationAdapter(entity)
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new ServletAllocationStorage(adapter, entity)))
	}
}
