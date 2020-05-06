package org.palladiosimulator.protocom.tech.iiop

import com.google.common.collect.ArrayListMultimap
import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.txt.IReadMe
import org.palladiosimulator.protocom.tech.ConceptMapping

class JavaEEIIOPReadMeFile <E extends Entity> extends ConceptMapping<E> implements IReadMe{
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override filePath() {
	}
	
	override projectName() {
	}
	
	override basicComponentClassName() {
		newHashMap
	}
	
	override basicComponentPortClassName() {
		ArrayListMultimap.create
	}
	
}