package org.palladiosimulator.protocom.traverse.jsestub.repository

import org.palladiosimulator.protocom.lang.java.impl.JClass
import org.palladiosimulator.protocom.traverse.framework.repository.XCollectionDataType
import org.palladiosimulator.protocom.tech.pojo.repository.PojoCollectionDataTypeClass

/**
 * A Collection Data Type translates into the following Java compilation units:
 * <ul>
 * 	<li> a dedicated data type class.
 * </ul>
 * 
 * @author Sebastian Lehrig
 */
class JseStubCollectionDataType extends XCollectionDataType {
	override generate() {
		// Class for this data type.
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoCollectionDataTypeClass(entity)))
	}
}
