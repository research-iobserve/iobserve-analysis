package org.palladiosimulator.protocom.traverse.jse.allocation

import org.palladiosimulator.protocom.traverse.framework.allocation.XAllocation
import org.palladiosimulator.protocom.lang.java.impl.JClass
import org.palladiosimulator.protocom.tech.rmi.allocation.PojoAllocationStorage

/**
 * An Allocation for JSE translates into one additional container information file.
 * 
 * @author Thomas Zolynski
 */
class JseAllocation extends XAllocation {
	
	override generate() {
		
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoAllocationStorage(entity)))
		
	}
	
}