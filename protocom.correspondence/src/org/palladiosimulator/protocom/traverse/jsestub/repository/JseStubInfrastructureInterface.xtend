package org.palladiosimulator.protocom.traverse.jsestub.repository

import org.palladiosimulator.protocom.lang.java.impl.JInterface
import org.palladiosimulator.protocom.tech.pojo.repository.PojoInfrastructureInterface
import org.palladiosimulator.protocom.traverse.framework.repository.XInfrastructureInterface

/**
 * An Infrastructure Interface translates into the following Java compilation units:
 * <ul>
 * 	<li> an interface.
 * </ul>
 * Note that this interface, nor the infrastructure component and ports are used by ProtoCom.
 * Infrastructure component calls in PCM should not be simulated but rather directly executed.
 * 
 * @author Sebastian Lehrig
 */
class JseStubInfrastructureInterface extends XInfrastructureInterface {
	
	override generate() {

		generatedFiles.add(injector.getInstance(typeof(JInterface)).createFor(new PojoInfrastructureInterface(entity)))

	}
}