package org.palladiosimulator.protocom.tech.iiop.system

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.util.JavaNames

class JavaEEIIOPClientClasspath extends JavaEEIIOPClasspath {

	new(BasicComponent pcmEntity) {
		super(pcmEntity)
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
					<attribute name="owner.project.facets" value="jst.utility"/>
				</attributes>
			</classpathentry>
			<classpathentry kind="con" path="org.eclipse.jst.j2ee.internal.module.container"/>
			<classpathentry kind="var" path="ECLIPSE_HOME/plugins/«pluginJar("de.uka.ipd.sdq.simucomframework.variables")»"/>
			'''
	}

	override projectName() {
		JavaNames::fqnJavaEEOperationInterfaceProjectName(pcmEntity)
	}

	override clientClassPathEntry() {
	}

	override requiredClientProjects() {
		newHashSet()
	}
}
