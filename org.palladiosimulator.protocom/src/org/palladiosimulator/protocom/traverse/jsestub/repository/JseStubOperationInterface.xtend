package org.palladiosimulator.protocom.traverse.jsestub.repository

import org.palladiosimulator.protocom.traverse.framework.repository.XOperationInterface
import org.palladiosimulator.protocom.lang.java.impl.JInterface
import org.palladiosimulator.protocom.tech.pojo.repository.PojoOperationInterface

/**
 * An Operation Interface translates into the following Java compilation units:
 * <ul>
 * 	<li> an interface.
 * </ul>
 * 
 * @author Sebastian Lehrig
 */
class JseStubOperationInterface extends XOperationInterface {
	
	override generate() {

		generatedFiles.add(injector.getInstance(typeof(JInterface)).createFor(new PojoOperationInterface(entity)))

	}
}