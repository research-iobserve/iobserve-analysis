package org.palladiosimulator.protocom.traverse.jee.system

import org.palladiosimulator.pcm.core.composition.AssemblyConnector
import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.impl.JeeClass
import org.palladiosimulator.protocom.lang.txt.impl.JeeReadMe
import org.palladiosimulator.protocom.lang.xml.impl.JeeClasspath
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPBasicComponentClass
import org.palladiosimulator.protocom.tech.iiop.system.JavaEEIIOPClasspath
import org.palladiosimulator.protocom.tech.iiop.system.JavaEEIIOPClientClasspath
import org.palladiosimulator.protocom.tech.iiop.system.JavaEEIIOPReadMe
import org.palladiosimulator.protocom.traverse.framework.system.XSystem

class JeeSystem extends XSystem {
	override generate() {

		val assemblyConnectorSet = entity.connectors__ComposedStructure.filter(typeof(AssemblyConnector)).toSet
		val repositoryComponentList = entity.assemblyContexts__ComposedStructure

		repositoryComponentList.filter[BasicComponent.isInstance(it.encapsulatedComponent__AssemblyContext)].map[
			it.encapsulatedComponent__AssemblyContext as BasicComponent].forEach[
			generatedFiles.add(
				injector.getInstance(typeof(JeeClasspath)).createFor(
					new JavaEEIIOPClasspath((it), assemblyConnectorSet)))]

		repositoryComponentList.filter[BasicComponent.isInstance(it.encapsulatedComponent__AssemblyContext)].map[
			it.encapsulatedComponent__AssemblyContext as BasicComponent].forEach[
			generatedFiles.add(injector.getInstance(typeof(JeeClasspath)).createFor(new JavaEEIIOPClientClasspath(it)))]

		repositoryComponentList.filter[BasicComponent.isInstance(it.encapsulatedComponent__AssemblyContext)].map[
			it.encapsulatedComponent__AssemblyContext as BasicComponent].forEach[
			generatedFiles.add(
				injector.getInstance(typeof(JeeClass)).createFor(
					new JavaEEIIOPBasicComponentClass((it), assemblyConnectorSet)))]

		// ReadMe includes information about the generated Java EE prototype 
		generatedFiles.add(injector.getInstance(typeof(JeeReadMe)).createFor(new JavaEEIIOPReadMe(entity)))
	}
}
