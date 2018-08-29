package org.palladiosimulator.protocom.tech.pojo.repository

import org.palladiosimulator.pcm.repository.CompositeDataType
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.protocom.tech.rmi.PojoClass
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.util.DataTypes

/**
 * Defining the content of composite data type implementations.
 * 
 * @author Sebastian Lehrig
 */
class PojoCompositeDataTypeClass extends PojoClass<CompositeDataType> {
	override interfaces() {
		#[
			JavaConstants::SERIALIZABLE_INTERFACE
		]
	}
	
	new(CompositeDataType pcmEntity) {
		super(pcmEntity)
	}
	
	override fields() {
		val results = newLinkedList

		results += pcmEntity.innerDeclaration_CompositeDataType.map[
			new JField()
				.withModifierVisibility("public")
				.withName(it.entityName)
				.withType(DataTypes::getDataType(it.datatype_InnerDeclaration))				
		]
		
		results
	}
}