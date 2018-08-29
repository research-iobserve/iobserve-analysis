package org.palladiosimulator.protocom.lang.java.util;

import java.util.Arrays;
import org.palladiosimulator.pcm.repository.CollectionDataType;
import org.palladiosimulator.pcm.repository.CompositeDataType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.EventType;
import org.palladiosimulator.pcm.repository.InfrastructureSignature;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.repository.PrimitiveTypeEnum;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;

/**
 * Utility class for creating datatype strings. Inspired by the old datatype xpand template.
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class DataTypes {
  /**
   * If this method is called, an error occured because every possible data type should be covered.
   */
  protected static String _getDataType(final DataType d) {
    return ((("Xtend2 GENERATION ERROR [org.palladiosimulator.protocom.lang.java.util.DataTypes]:" + 
      "Unknown data type found (") + d) + ").");
  }
  
  /**
   * Handles "void" as return type.
   */
  protected static String _getDataType(final Void d) {
    return "void";
  }
  
  /**
   * Primitive types can directly be resolved.
   */
  protected static String _getDataType(final PrimitiveDataType d) {
    String _switchResult = null;
    PrimitiveTypeEnum _type = d.getType();
    if (_type != null) {
      switch (_type) {
        case BOOL:
          _switchResult = "Boolean";
          break;
        case BYTE:
          _switchResult = "byte";
          break;
        case CHAR:
          _switchResult = "char";
          break;
        case DOUBLE:
          _switchResult = "double";
          break;
        case INT:
          _switchResult = "int";
          break;
        case LONG:
          _switchResult = "long";
          break;
        case STRING:
          _switchResult = "String";
          break;
        default:
          _switchResult = ((("Xtend2 GENERATION ERROR [org.palladiosimulator.protocom.lang.java.util.DataTypes]:" + 
            "Unknown primitive data type found (") + d) + ").");
          break;
      }
    } else {
      _switchResult = ((("Xtend2 GENERATION ERROR [org.palladiosimulator.protocom.lang.java.util.DataTypes]:" + 
        "Unknown primitive data type found (") + d) + ").");
    }
    return _switchResult;
  }
  
  protected static String _getDataType2(final DataType d) {
    return DataTypes.getDataType(d);
  }
  
  /**
   * Primitive types can directly be resolved.
   */
  protected static String _getDataType2(final PrimitiveDataType d) {
    String _switchResult = null;
    PrimitiveTypeEnum _type = d.getType();
    if (_type != null) {
      switch (_type) {
        case BOOL:
          _switchResult = "Boolean";
          break;
        case BYTE:
          _switchResult = "Byte";
          break;
        case CHAR:
          _switchResult = "Character";
          break;
        case DOUBLE:
          _switchResult = "Double";
          break;
        case INT:
          _switchResult = "Integer";
          break;
        case LONG:
          _switchResult = "Long";
          break;
        case STRING:
          _switchResult = "String";
          break;
        default:
          _switchResult = ((("Xtend2 GENERATION ERROR [org.palladiosimulator.protocom.lang.java.util.DataTypes]:" + 
            "Unknown primitive data type found (") + d) + ").");
          break;
      }
    } else {
      _switchResult = ((("Xtend2 GENERATION ERROR [org.palladiosimulator.protocom.lang.java.util.DataTypes]:" + 
        "Unknown primitive data type found (") + d) + ").");
    }
    return _switchResult;
  }
  
  /**
   * Collection data types can directly be resolved by their name.
   */
  protected static String _getDataType(final CollectionDataType d) {
    Repository _repository__DataType = d.getRepository__DataType();
    String _basePackageName = JavaNames.basePackageName(_repository__DataType);
    String _plus = (_basePackageName + ".datatypes.");
    String _entityName = d.getEntityName();
    return (_plus + _entityName);
  }
  
  /**
   * Composite data types can directly be resolved by their name.
   */
  protected static String _getDataType(final CompositeDataType d) {
    Repository _repository__DataType = d.getRepository__DataType();
    String _basePackageName = JavaNames.basePackageName(_repository__DataType);
    String _plus = (_basePackageName + ".datatypes.");
    String _entityName = d.getEntityName();
    return (_plus + _entityName);
  }
  
  protected static String _getReturnDataType(final Signature s) {
    return null;
  }
  
  protected static String _getReturnDataType(final OperationSignature s) {
    DataType _returnType__OperationSignature = s.getReturnType__OperationSignature();
    return DataTypes.getDataType(_returnType__OperationSignature);
  }
  
  protected static String _getReturnDataType(final InfrastructureSignature s) {
    return "void";
  }
  
  /**
   * TODO Implement EventTypes?
   */
  protected static String _getReturnDataType(final EventType s) {
    return "FIXME";
  }
  
  public static String getDataType(final DataType d) {
    if (d instanceof CollectionDataType) {
      return _getDataType((CollectionDataType)d);
    } else if (d instanceof CompositeDataType) {
      return _getDataType((CompositeDataType)d);
    } else if (d instanceof PrimitiveDataType) {
      return _getDataType((PrimitiveDataType)d);
    } else if (d != null) {
      return _getDataType(d);
    } else if (d == null) {
      return _getDataType((Void)null);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(d).toString());
    }
  }
  
  public static String getDataType2(final DataType d) {
    if (d instanceof PrimitiveDataType) {
      return _getDataType2((PrimitiveDataType)d);
    } else if (d != null) {
      return _getDataType2(d);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(d).toString());
    }
  }
  
  public static String getReturnDataType(final Signature s) {
    if (s instanceof EventType) {
      return _getReturnDataType((EventType)s);
    } else if (s instanceof InfrastructureSignature) {
      return _getReturnDataType((InfrastructureSignature)s);
    } else if (s instanceof OperationSignature) {
      return _getReturnDataType((OperationSignature)s);
    } else if (s != null) {
      return _getReturnDataType(s);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(s).toString());
    }
  }
}
