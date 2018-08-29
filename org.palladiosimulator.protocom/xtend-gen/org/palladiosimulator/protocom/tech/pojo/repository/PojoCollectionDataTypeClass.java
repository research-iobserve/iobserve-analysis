package org.palladiosimulator.protocom.tech.pojo.repository;

import java.util.Collection;
import java.util.Collections;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.repository.CollectionDataType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.protocom.lang.java.util.DataTypes;
import org.palladiosimulator.protocom.lang.java.util.JavaConstants;
import org.palladiosimulator.protocom.tech.rmi.PojoClass;

/**
 * Defining the content of collection data type implementations.
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoCollectionDataTypeClass extends PojoClass<CollectionDataType> {
  @Override
  public String superClass() {
    String _xblockexpression = null;
    {
      DataType innerType = this.pcmEntity.getInnerType_CollectionDataType();
      String _dataType2 = DataTypes.getDataType2(innerType);
      String _plus = ("java.util.ArrayList<" + _dataType2);
      _xblockexpression = (_plus + ">");
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<String> interfaces() {
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(JavaConstants.SERIALIZABLE_INTERFACE));
  }
  
  public PojoCollectionDataTypeClass(final CollectionDataType pcmEntity) {
    super(pcmEntity);
  }
}
