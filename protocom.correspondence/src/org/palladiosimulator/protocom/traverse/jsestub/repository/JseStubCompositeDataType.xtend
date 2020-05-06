package org.palladiosimulator.protocom.traverse.jsestub.repository

import org.palladiosimulator.protocom.lang.java.impl.JClass
import org.palladiosimulator.protocom.traverse.framework.repository.XCompositeDataType
import org.palladiosimulator.protocom.tech.pojo.repository.PojoCompositeDataTypeClass

/**
 * A Composite Data Type translates into the following Java compilation units:
 * <ul>
 * 	<li> a dedicated data type class.
 * </ul>
 * 
 * @author Sebastian Lehrig
 */
class JseStubCompositeDataType extends XCompositeDataType {
	override generate() {
		// Class for this data type.
		generatedFiles.add(injector.getInstance(typeof(JClass)).createFor(new PojoCompositeDataTypeClass(entity)))
	}
}
