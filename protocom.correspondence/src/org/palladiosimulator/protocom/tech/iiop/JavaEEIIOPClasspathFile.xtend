package org.palladiosimulator.protocom.tech.iiop

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.xml.IJeeClasspath
import org.palladiosimulator.protocom.tech.ConceptMapping
import org.palladiosimulator.commons.eclipseutils.FileHelper

class JavaEEIIOPClasspathFile <E extends Entity> extends ConceptMapping<E> implements IJeeClasspath {
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override classPathEntries() {
	}
	
	override filePath() {
	}
	
	override projectName() {
	}
	
	override clientClassPathEntry() {
	}
	
	override requiredClientProjects() {
	}
	
	def protected pluginJar(String source) {
		FileHelper.getPluginJarFile(source).getName()
	}
	
}