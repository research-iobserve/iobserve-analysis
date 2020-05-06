package org.palladiosimulator.protocom.traverse.jsestub.system

import org.palladiosimulator.protocom.lang.java.impl.JClass
import org.palladiosimulator.protocom.lang.java.impl.JInterface
import org.palladiosimulator.protocom.lang.manifest.impl.JseManifest
import org.palladiosimulator.protocom.lang.properties.impl.BuildProperties
import org.palladiosimulator.protocom.lang.xml.impl.Classpath
import org.palladiosimulator.protocom.lang.xml.impl.PluginXml
import org.palladiosimulator.protocom.tech.pojo.repository.PojoComposedStructurePortClass
import org.palladiosimulator.protocom.tech.pojo.system.PojoBuildProperties
import org.palladiosimulator.protocom.tech.pojo.system.PojoManifest
import org.palladiosimulator.protocom.tech.pojo.system.PojoPluginXml
import org.palladiosimulator.protocom.tech.pojo.system.PojoSystemClass
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureContextClass
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureContextInterface
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureInterface
import org.palladiosimulator.protocom.tech.rmi.system.PojoClasspath
import org.palladiosimulator.protocom.traverse.framework.system.XSystem

/**
 * An System translates into the following Java compilation units:
 * <ul>
 * 	<li> a class used to setup the assembly (a System is a Composed Structure),
 * 	<li> an interface for this component's class,
 * 	<li> a context class for assembly (basically unused, can be removed?),
 * 	<li> an interface for the context class,
 *  <li> a class for each component's port, used by the Usage Scenario. TODO: Move to traverse
 * </ul>
 * 
 * @author Sebastian Lehrig
 */
class JseStubSystem extends XSystem {

	override generate() {

		// Interface. Necessity of this one is debatable. For now, it is included, because the current ProtoCom uses it as well.
		generatedFiles.add(injector.getInstance(typeof(JInterface)).createFor(new PojoComposedStructureInterface(entity)))

		// Class for this component.
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoSystemClass(entity))) 
	
		// Context pattern.
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoComposedStructureContextClass(entity)))
		generatedFiles.add(injector.getInstance(typeof(JInterface)).createFor(new PojoComposedStructureContextInterface(entity)))

		// Ports. See TODO above.
		entity.providedRoles_InterfaceProvidingEntity.forEach[
			generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoComposedStructurePortClass(it)))
		]
		
		//Manifest Files
		generatedFiles.add(injector.getInstance(typeof(JseManifest)).createFor(new PojoManifest(entity)))
		
		//Plugin.xml file
		generatedFiles.add(injector.getInstance(typeof(PluginXml)).createFor(new PojoPluginXml(entity)))
		
		//Build.properties file
		generatedFiles.add(injector.getInstance(typeof(BuildProperties)).createFor(new PojoBuildProperties(entity)))
		
		//Classpath file
		generatedFiles.add(injector.getInstance(typeof(Classpath)).createFor(new PojoClasspath(entity)))
	}
}
