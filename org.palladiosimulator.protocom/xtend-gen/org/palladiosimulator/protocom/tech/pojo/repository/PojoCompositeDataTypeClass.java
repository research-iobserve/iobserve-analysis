package org.palladiosimulator.protocom.tech.pojo.repository;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.repository.CompositeDataType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.InnerDeclaration;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.util.DataTypes;
import org.palladiosimulator.protocom.lang.java.util.JavaConstants;
import org.palladiosimulator.protocom.tech.rmi.PojoClass;

/**
 * Defining the content of composite data type implementations.
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoCompositeDataTypeClass extends PojoClass<CompositeDataType> {
  @Override
  public Collection<String> interfaces() {
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(JavaConstants.SERIALIZABLE_INTERFACE));
  }
  
  public PojoCompositeDataTypeClass(final CompositeDataType pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    LinkedList<JField> _xblockexpression = null;
    {
      final LinkedList<JField> results = CollectionLiterals.<JField>newLinkedList();
      EList<InnerDeclaration> _innerDeclaration_CompositeDataType = this.pcmEntity.getInnerDeclaration_CompositeDataType();
      final Function1<InnerDeclaration, JField> _function = (InnerDeclaration it) -> {
        JField _jField = new JField();
        JField _withModifierVisibility = _jField.withModifierVisibility("public");
        String _entityName = it.getEntityName();
        JField _withName = _withModifierVisibility.withName(_entityName);
        DataType _datatype_InnerDeclaration = it.getDatatype_InnerDeclaration();
        String _dataType = DataTypes.getDataType(_datatype_InnerDeclaration);
        return _withName.withType(_dataType);
      };
      List<JField> _map = ListExtensions.<InnerDeclaration, JField>map(_innerDeclaration_CompositeDataType, _function);
      Iterables.<JField>addAll(results, _map);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
}
