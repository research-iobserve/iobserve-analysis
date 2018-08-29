package org.palladiosimulator.protocom.tech.servlet

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.tech.ConceptMapping
import org.palladiosimulator.protocom.lang.xml.IClasspath
import org.eclipse.core.runtime.Path
import org.eclipse.jdt.launching.JavaRuntime

class ServletClasspath<E extends Entity> extends ConceptMapping<E> implements IClasspath {
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override classPathEntries() {
		'''
		<classpathentry kind="src" path="src"/>
		<classpathentry kind="con" path="«new Path(JavaRuntime.JRE_CONTAINER)»">
			<attributes>
				<attribute name="owner.project.facets" value="java"/>
			</attributes>
		</classpathentry>
		<classpathentry kind="con" path="org.eclipse.jst.server.core.container/com.sap.core.javaweb.tomcat.classpath/Java Web Tomcat 7">
			<attributes>
				<attribute name="owner.project.facets" value="jst.web"/>
			</attributes>
		</classpathentry>
		<classpathentry kind="con" path="org.eclipse.jst.j2ee.internal.web.container"/>
		<classpathentry kind="con" path="org.eclipse.jst.j2ee.internal.module.container"/>
		<classpathentry kind="output" path="build/classes"/>
        '''
	}
	
	override filePath() {
		".classpath"
	}
	
	override projectName() {
	}
}
