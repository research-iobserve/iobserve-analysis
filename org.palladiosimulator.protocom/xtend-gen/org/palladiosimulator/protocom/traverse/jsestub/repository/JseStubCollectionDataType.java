package org.palladiosimulator.protocom.traverse.jsestub.repository;

import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.impl.JClass;
import org.palladiosimulator.protocom.tech.pojo.repository.PojoCollectionDataTypeClass;
import org.palladiosimulator.protocom.traverse.framework.repository.XCollectionDataType;

/**
 * A Collection Data Type translates into the following Java compilation units:
 * <ul>
 * 	<li> a dedicated data type class.
 * </ul>
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class JseStubCollectionDataType extends XCollectionDataType {
  @Override
  public void generate() {
    JClass _instance = this.injector.<JClass>getInstance(JClass.class);
    PojoCollectionDataTypeClass _pojoCollectionDataTypeClass = new PojoCollectionDataTypeClass(this.entity);
    GeneratedFile<IJClass> _createFor = _instance.createFor(_pojoCollectionDataTypeClass);
    this.generatedFiles.add(_createFor);
  }
}
