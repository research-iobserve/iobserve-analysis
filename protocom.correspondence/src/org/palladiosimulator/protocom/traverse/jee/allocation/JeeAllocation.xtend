package org.palladiosimulator.protocom.traverse.jee.allocation

import org.palladiosimulator.protocom.lang.xml.impl.JeeGlassfishEjbDescriptor
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPGlassfishEjbDescriptor
import org.palladiosimulator.protocom.traverse.framework.allocation.XAllocation

class JeeAllocation extends XAllocation{
	
		override generate() {
		entity.allocationContexts_Allocation.forEach[generatedFiles.add(injector.getInstance(typeof(JeeGlassfishEjbDescriptor)).createFor(new JavaEEIIOPGlassfishEjbDescriptor(it)))]
	}
}