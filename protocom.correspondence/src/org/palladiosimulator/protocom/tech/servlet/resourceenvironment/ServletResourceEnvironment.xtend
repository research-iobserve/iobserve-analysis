package org.palladiosimulator.protocom.tech.servlet.resourceenvironment

import org.palladiosimulator.protocom.lang.java.IJClass
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.model.resourceenvironment.ResourceEnvironmentAdapter

// TODO: this class should inherit from a common class transformator
class ServletResourceEnvironment implements IJClass {
	protected val frameworkBase = "org.palladiosimulator.protocom.framework.java.ee"
	protected ResourceEnvironmentAdapter entity;
	
	new(ResourceEnvironmentAdapter entity) {
		this.entity = entity
	}
	
	override superClass() {
	}
	
	override packageName() {
		"main"
	}
	
	override compilationUnitName() {
		"ResourceEnvironment"
	}
	
	override constructors() {
		newLinkedList
	}
	
	override annotations() {
		newLinkedList
	}
	
	override interfaces() {
		newLinkedList
	}
	
	override methods() {
		val containers = entity.resourceContainers
		var i = 0
		
		#[
			new JMethod()
				.withVisibilityModifier("public")
				.withStaticModifier
				.withName("init")
				.withParameters('''«frameworkBase».prototype.PrototypeBridge bridge''')
				.withImplementation('''
					«frameworkBase».prototype.PrototypeBridge.Container[] containers = new «frameworkBase».prototype.PrototypeBridge.Container[«containers.length»];
					
					«FOR container : containers»
						containers[«i++»] = bridge.new Container("«container.id»", "«container.name»", "«container.cpuRate»", "«container.hddRate»");
					«ENDFOR»
					
					bridge.setContainers(containers);
				''')
		]
	}
	
	override fields() {
		newLinkedList
	}
	
	override filePath() {
		"src/main/ResourceEnvironment.java"
	}
	
	override projectName() {
	}	
}
