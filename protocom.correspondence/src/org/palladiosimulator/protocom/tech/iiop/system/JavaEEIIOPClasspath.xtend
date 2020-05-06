package org.palladiosimulator.protocom.tech.iiop.system

import org.palladiosimulator.pcm.core.composition.AssemblyConnector
import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPClasspathFile
import java.util.Set

class JavaEEIIOPClasspath extends JavaEEIIOPClasspathFile<BasicComponent> {

	Set<AssemblyConnector> assemblyConnectors

	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}

	new(BasicComponent pcmEntity, Set<AssemblyConnector> assemblyConnectorSet) {
		super(pcmEntity)
		this.assemblyConnectors = assemblyConnectorSet
	}

	override classPathEntries() {
		'''
			<classpathentry kind="src" path="ejbModule"/>
			<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.7">
				<attributes>
					<attribute name="owner.project.facets" value="java"/>
				</attributes>
			</classpathentry>
			<classpathentry kind="con" path="oracle.eclipse.tools.glassfish.lib.system">
				<attributes>
					<attribute name="owner.project.facets" value="jst.ejb"/>
				</attributes>
			</classpathentry>
			<classpathentry kind="con" path="org.eclipse.jst.j2ee.internal.module.container"/>
			<classpathentry kind="var" path="ECLIPSE_HOME/plugins/«pluginJar("de.uka.ipd.sdq.simucomframework.variables")»"/>
			<classpathentry kind="var" path="ECLIPSE_HOME/plugins/«pluginJar("org.palladiosimulator.protocom.framework.java.ee")»"/>
			<classpathentry kind="var" path="ECLIPSE_HOME/plugins/«pluginJar("org.palladiosimulator.protocom.framework.java.se")»"/>
			<classpathentry kind="var" path="ECLIPSE_HOME/plugins/«pluginJar("org.palladiosimulator.protocom.resourcestrategies")»"/>
			'''
	}

	override filePath() {
		".classpath"
	}

	override projectName() {
		JavaNames::fqnJavaEEDescriptorProjectName(pcmEntity)
	}

	override clientClassPathEntry() {
		JavaNames::fqnJavaEEClientDeployName(pcmEntity)
	}

	override requiredClientProjects() {
		val basicComponentAssemblyConnectors = assemblyConnectors.filter[
			it.requiredRole_AssemblyConnector.requiringEntity_RequiredRole.equals(pcmEntity)]
		val results = newHashSet()

		for (assemblyConnector : basicComponentAssemblyConnectors) {
			var assemblyProvidedRole = assemblyConnector.providedRole_AssemblyConnector
			results += #[
				JavaNames::fqnJavaEEClientDeployName(assemblyProvidedRole.providingEntity_ProvidedRole)
			]
		}

		return results
	}
}
