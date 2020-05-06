package org.palladiosimulator.protocom.tech.rmi.system

import org.palladiosimulator.pcm.system.System
import org.palladiosimulator.protocom.tech.rmi.PojoBuildPropertiesFile

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