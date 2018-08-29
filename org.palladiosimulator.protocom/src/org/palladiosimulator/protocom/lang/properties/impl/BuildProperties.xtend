package org.palladiosimulator.protocom.lang.properties.impl

import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.properties.IBuildProperties

class BuildProperties extends GeneratedFile<IBuildProperties> implements IBuildProperties{
	
	override generate() {
		''' output.. = «output»
			source.. = «source»
			bin.includes = «binIncludes»
							
		'''
	}
	
	override output() {
		provider.output
	}
	
	override source() {
		provider.source
	}
	
	override binIncludes() {
		provider.binIncludes
	}
	
}