package org.palladiosimulator.protocom.traverse.jse.repository

import org.palladiosimulator.protocom.traverse.framework.repository.XCompositeComponent
import org.palladiosimulator.protocom.lang.java.impl.JInterface
import org.palladiosimulator.protocom.lang.java.impl.JClass
import org.palladiosimulator.protocom.tech.rmi.repository.PojoCompositeComponentClass
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureContextClass
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureContextInterface
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureInterface
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructurePortClass

/**
 * An CompositeComponent translates into the following Java compilation units:
 * <ul>
 * 	<li> a class used to setup the assembly (a CompositeComponent is a ComposedStructure),
 * 	<li> an interface for this component's class,
 * 	<li> a context class for assembly,
 * 	<li> an interface for the context class,
 *  <li> a class for each component's port. TODO: Move to traverse
 * </ul>
 * 
 * @author Thomas Zolynski
 */
class JseCompositeComponent extends XCompositeComponent {
	
	override generate() {

		// Interface. Necessity of this one is debatable. For now, it is included, because the current ProtoCom uses it as well.
		generatedFiles.add(injector.getInstance(typeof(JInterface)).createFor(new PojoComposedStructureInterface(entity)))

		// Class for this component.
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoCompositeComponentClass(entity))) 
	
		// Context pattern.
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoComposedStructureContextClass(entity)))
		generatedFiles.add(injector.getInstance(typeof(JInterface)).createFor(new PojoComposedStructureContextInterface(entity)))

		// Ports. See TODO above.
		entity.providedRoles_InterfaceProvidingEntity.forEach[
			generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoComposedStructurePortClass(it)))
		]
		
	}
}