package org.palladiosimulator.protocom.tech.servlet

import org.palladiosimulator.pcm.system.System
import org.palladiosimulator.protocom.lang.xml.IJeeSettings
import org.palladiosimulator.protocom.tech.ConceptMapping

class ServletSettings extends ConceptMapping<System> implements IJeeSettings {
	val String contentId
	
	new(System pcmEntity, String contentId) {
		super(pcmEntity)
		this.contentId = contentId
	}
	
	override content() {
		switch (contentId){
		case ".jsdtscope":
			return '''
			<?xml version="1.0" encoding="UTF-8"?>
			<classpath>
				<classpathentry kind="src" path="WebContent"/>
				<classpathentry kind="con" path="org.eclipse.wst.jsdt.launching.JRE_CONTAINER"/>
				<classpathentry kind="con" path="org.eclipse.wst.jsdt.launching.WebProject">
					<attributes>
						<attribute name="hide" value="true"/>
					</attributes>
				</classpathentry>
				<classpathentry kind="con" path="org.eclipse.wst.jsdt.launching.baseBrowserLibrary"/>
				<classpathentry kind="output" path=""/>
			</classpath>
			'''
			
		case "org.eclipse.jdt.core.prefs":
			return '''
			eclipse.preferences.version=1
			org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled
			org.eclipse.jdt.core.compiler.codegen.targetPlatform=1.7
			org.eclipse.jdt.core.compiler.compliance=1.7
			org.eclipse.jdt.core.compiler.problem.assertIdentifier=error
			org.eclipse.jdt.core.compiler.problem.enumIdentifier=error
			org.eclipse.jdt.core.compiler.source=1.7
			'''
		
		case "org.eclipse.wst.common.component":
			return '''
			<?xml version="1.0" encoding="UTF-8"?>
			<project-modules id="moduleCoreId" project-version="1.5.0">
				<wb-module deploy-name="org.palladiosimulator.temporary">
					<wb-resource deploy-path="/" source-path="/WebContent" tag="defaultRootSource"/>
					<wb-resource deploy-path="/WEB-INF/classes" source-path="/src"/>
					<property name="context-root" value="org.palladiosimulator.temporary"/>
					<property name="java-output-path" value="/org.palladiosimulator.temporary/build/classes"/>
				</wb-module>
			</project-modules>
			'''
				
		case "org.eclipse.wst.common.project.facet.core.xml":
			return '''
			<?xml version="1.0" encoding="UTF-8"?>
			<faceted-project>
				<runtime name="Java Web Tomcat 7"/>
				<fixed facet="java"/>
				<fixed facet="wst.jsdt.web"/>
				<fixed facet="jst.web"/>
				<installed facet="java" version="1.7"/>
				<installed facet="jst.web" version="3.0"/>
				<installed facet="wst.jsdt.web" version="1.0"/>
			</faceted-project>
			'''
			
		case "org.eclipse.wst.jsdt.ui.superType.container":
			return '''org.eclipse.wst.jsdt.launching.baseBrowserLibrary'''
			
		case "org.eclipse.wst.jsdt.ui.superType.name":
			return '''Window'''
		}
	}
	
	override filePath() {
		'''.settings/«contentId»'''
	}
	
	override projectName() {
	}
}
