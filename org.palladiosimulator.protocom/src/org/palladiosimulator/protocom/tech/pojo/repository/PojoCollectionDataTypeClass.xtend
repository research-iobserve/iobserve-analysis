package org.palladiosimulator.protocom.tech.pojo.repository

import org.palladiosimulator.pcm.repository.CollectionDataType
import org.palladiosimulator.protocom.lang.java.util.DataTypes
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.protocom.tech.rmi.PojoClass

/**
 * Defining the content of collection data type implementations.
 * 
 * @author Sebastian Lehrig
 */
class PojoCollectionDataTypeClass extends PojoClass<CollectionDataType> {
	
	override superClass() {
		var innerType = pcmEntity.innerType_CollectionDataType;
				
		"java.util.ArrayList<"+DataTypes::getDataType2(innerType)+">"
	}
	
	override interfaces() {
		#[
			JavaConstants::SERIALIZABLE_INTERFACE
		]
	}
	
	new(CollectionDataType pcmEntity) {
		super(pcmEntity)
	}
}