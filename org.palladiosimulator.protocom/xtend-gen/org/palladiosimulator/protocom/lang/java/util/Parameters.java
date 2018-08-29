package org.palladiosimulator.protocom.lang.java.util;

import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.protocom.lang.java.util.DataTypes;

/**
 * Utility class for creating parameter strings.
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class Parameters {
  protected static String _getParameterList(final Signature s) {
    return null;
  }
  
  protected static String _getParameterList(final OperationSignature s) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Parameter> _parameters__OperationSignature = s.getParameters__OperationSignature();
      boolean _hasElements = false;
      for(final Parameter parameter : _parameters__OperationSignature) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        DataType _dataType__Parameter = parameter.getDataType__Parameter();
        String _dataType = DataTypes.getDataType(_dataType__Parameter);
        _builder.append(_dataType, "");
        _builder.append(" ");
        String _parameterName = parameter.getParameterName();
        _builder.append(_parameterName, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  protected static String _getParameterUsageList(final Signature s) {
    return null;
  }
  
  protected static String _getParameterUsageList(final OperationSignature s) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<Parameter> _parameters__OperationSignature = s.getParameters__OperationSignature();
      boolean _hasElements = false;
      for(final Parameter parameter : _parameters__OperationSignature) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        _builder.append("param_");
        String _parameterName = parameter.getParameterName();
        _builder.append(_parameterName, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  public static String getParameterList(final Signature s) {
    if (s instanceof OperationSignature) {
      return _getParameterList((OperationSignature)s);
    } else if (s != null) {
      return _getParameterList(s);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(s).toString());
    }
  }
  
  public static String getParameterUsageList(final Signature s) {
    if (s instanceof OperationSignature) {
      return _getParameterUsageList((OperationSignature)s);
    } else if (s != null) {
      return _getParameterUsageList(s);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(s).toString());
    }
  }
}
