package org.palladiosimulator.protocom.lang.java.impl;

import com.google.common.base.Objects;
import java.util.Collection;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.IJeeInterface;
import org.palladiosimulator.protocom.lang.java.impl.JCompilationUnit;

@SuppressWarnings("all")
public class JeeInterface extends JCompilationUnit<IJeeInterface> implements IJeeInterface {
  @Override
  public String header() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    String _packageName = this.packageName();
    _builder.append(_packageName, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("@");
    String _jeeInterfaceAnnotation = this.jeeInterfaceAnnotation();
    _builder.append(_jeeInterfaceAnnotation, "");
    _builder.newLineIfNotEmpty();
    _builder.append("public interface ");
    String _compilationUnitName = this.compilationUnitName();
    _builder.append(_compilationUnitName, "");
    _builder.append(" ");
    CharSequence _implementedClasses = this.implementedClasses();
    _builder.append(_implementedClasses, "");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  @Override
  public String body() {
    StringConcatenation _builder = new StringConcatenation();
    {
      Collection<? extends IJMethod> _methods = this.methods();
      for(final IJMethod method : _methods) {
        CharSequence _signature = this.signature(method);
        _builder.append(_signature, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  public CharSequence signature(final IJMethod method) {
    StringConcatenation _builder = new StringConcatenation();
    String _visibilityModifier = method.visibilityModifier();
    _builder.append(_visibilityModifier, "");
    _builder.append(" ");
    String _returnType = method.returnType();
    _builder.append(_returnType, "");
    _builder.append(" ");
    String _name = method.name();
    _builder.append(_name, "");
    _builder.append(" (");
    String _parameters = method.parameters();
    _builder.append(_parameters, "");
    _builder.append(")");
    {
      String _throwsType = method.throwsType();
      boolean _notEquals = (!Objects.equal(_throwsType, null));
      if (_notEquals) {
        _builder.append(" throws ");
        String _throwsType_1 = method.throwsType();
        _builder.append(_throwsType_1, "");
      }
    }
    _builder.append(";");
    return _builder;
  }
  
  @Override
  public CharSequence implementedClasses() {
    StringConcatenation _builder = new StringConcatenation();
    {
      Collection<String> _interfaces = this.interfaces();
      boolean _notEquals = (!Objects.equal(_interfaces, null));
      if (_notEquals) {
        {
          Collection<String> _interfaces_1 = this.interfaces();
          boolean _hasElements = false;
          for(final String implInterface : _interfaces_1) {
            if (!_hasElements) {
              _hasElements = true;
              _builder.append(" extends ", "");
            } else {
              _builder.appendImmediate(", ", "");
            }
            _builder.append(implInterface, "");
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  @Override
  public String jeeInterfaceAnnotation() {
    return this.provider.jeeInterfaceAnnotation();
  }
}
