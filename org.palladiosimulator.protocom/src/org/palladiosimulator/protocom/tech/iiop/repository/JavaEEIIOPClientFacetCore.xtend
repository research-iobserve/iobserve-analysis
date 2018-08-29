package org.palladiosimulator.protocom.tech.iiop.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.util.JavaNames

class JavaEEIIOPClientFacetCore extends JavaEEIIOPFacetCore {
	
	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}
	
	override fixedFacet() {
		#["java","jst.utility"]
	}
	
	override installedFacet() {
		newHashMap(
			"java"-> "1.7",
			"jst.utility" ->"1.0"
		)
	}
	override projectName() {
		JavaNames::fqnJavaEEOperationInterfaceProjectName(pcmEntity)	
	}
}