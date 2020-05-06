package org.palladiosimulator.protocom.lang.java.util

import org.palladiosimulator.pcm.repository.DataType
import org.palladiosimulator.pcm.repository.PrimitiveDataType
import org.palladiosimulator.pcm.repository.CollectionDataType
import org.palladiosimulator.pcm.repository.CompositeDataType
import org.palladiosimulator.pcm.repository.PrimitiveTypeEnum
import org.palladiosimulator.pcm.repository.Signature
import org.palladiosimulator.pcm.repository.OperationSignature
import org.palladiosimulator.pcm.repository.InfrastructureSignature
import org.palladiosimulator.pcm.repository.EventType

/**
 * Utility class for creating datatype strings. Inspired by the old datatype xpand template.
 * 
 * @author Sebastian Lehrig
 */
class DataTypes {

	/**
	 * If this method is called, an error occured because every possible data type should be covered.
	 */
	def static dispatch String getDataType(DataType d) {
		"Xtend2 GENERATION ERROR [org.palladiosimulator.protocom.lang.java.util.DataTypes]:" +
			"Unknown data type found (" + d + ")."
	}

	/**
	 * Handles "void" as return type.
	 */
	def static dispatch String getDataType(Void d) {
		"void"
	}

	/**
	 * Primitive types can directly be resolved.
	 */
	def static dispatch  String getDataType(PrimitiveDataType d) {
		switch d.type {
			case PrimitiveTypeEnum::BOOL:
				"Boolean"
			case PrimitiveTypeEnum::BYTE:
				"byte"
			case PrimitiveTypeEnum::CHAR:
				"char"
			case PrimitiveTypeEnum::DOUBLE:
				"double"
			case PrimitiveTypeEnum::INT:
				"int"
			case PrimitiveTypeEnum::LONG:
				"long"
			case PrimitiveTypeEnum::STRING:
				"String"			
			default:
				"Xtend2 GENERATION ERROR [org.palladiosimulator.protocom.lang.java.util.DataTypes]:" +
					"Unknown primitive data type found (" + d + ")."
		}
	}
	
	def static dispatch String getDataType2(DataType d) {
		getDataType(d)
	}
	
	/**
	 * Primitive types can directly be resolved.
	 */
	def static dispatch String getDataType2(PrimitiveDataType d) {
		switch d.type {
			case PrimitiveTypeEnum::BOOL:
				"Boolean"
			case PrimitiveTypeEnum::BYTE:
				"Byte"
			case PrimitiveTypeEnum::CHAR:
				"Character"
			case PrimitiveTypeEnum::DOUBLE:
				"Double"
			case PrimitiveTypeEnum::INT:
				"Integer"
			case PrimitiveTypeEnum::LONG:
				"Long"
			case PrimitiveTypeEnum::STRING:
				"String"			
			default:
				"Xtend2 GENERATION ERROR [org.palladiosimulator.protocom.lang.java.util.DataTypes]:" +
					"Unknown primitive data type found (" + d + ")."
		}
	}

	/**
	 * Collection data types can directly be resolved by their name.
	 */
	def static dispatch  String getDataType(CollectionDataType d) {
		JavaNames::basePackageName(d.repository__DataType) + ".datatypes."+d.entityName
	}

	/**
	 * Composite data types can directly be resolved by their name.
	 */
	def static dispatch String getDataType(CompositeDataType d) {
		JavaNames::basePackageName(d.repository__DataType) + ".datatypes."+d.entityName
	}
	
	def static dispatch String getReturnDataType(Signature s) {		
	}
	
	def static dispatch String getReturnDataType(OperationSignature s) {
		getDataType(s.returnType__OperationSignature)
	}
	
	def static dispatch String getReturnDataType(InfrastructureSignature s) {
		"void"
	}
	
	/**
	 * TODO Implement EventTypes?
	 */
	def static dispatch String getReturnDataType(EventType s) {
		"FIXME"
	}
}
