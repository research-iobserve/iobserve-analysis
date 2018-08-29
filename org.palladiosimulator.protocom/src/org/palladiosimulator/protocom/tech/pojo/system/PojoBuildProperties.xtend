package org.palladiosimulator.protocom.tech.pojo.system

import org.palladiosimulator.protocom.tech.rmi.PojoBuildPropertiesFile
import org.palladiosimulator.pcm.system.System

class PojoBuildProperties extends PojoBuildPropertiesFile<System> {
	
	new(System pcmEntity) {
		super(pcmEntity)
	}
	
	override output() {
		'''bin/'''
	}
	
	override source() {
		'''src/'''
	}
	
	override binIncludes() {
		''' plugin.xml,\
 META-INF/,\
 .'''
	}
	
	override filePath() {
		"build.properties"
	}
	
}