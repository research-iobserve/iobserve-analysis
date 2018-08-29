package org.palladiosimulator.protocom.lang.java.impl;

import com.google.common.base.Objects;
import java.util.Collection;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JCompilationUnit;

@SuppressWarnings("all")
public class JClass extends JCompilationUnit<IJClass> implements IJClass {
  @Override
  public Collection<String> interfaces() {
    return this.provider.interfaces();
  }
  
  @Override
  public String superClass() {
    return this.provider.superClass();
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    return this.provider.constructors();
  }
  
  @Override
  public Collection<? extends IJAnnotation> annotations() {
    return this.provider.annotations();
  }
  
  @Override
  public String header() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    String _packageName = this.packageName();
    _builder.append(_packageName, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      boolean _and = false;
      Collection<? extends IJAnnotation> _annotations = this.annotations();
      boolean _notEquals = (!Objects.equal(_annotations, null));
      if (!_notEquals) {
        _and = false;
      } else {
        Collection<? extends IJAnnotation> _annotations_1 = this.annotations();
        boolean _isEmpty = _annotations_1.isEmpty();
        boolean _not = (!_isEmpty);
        _and = _not;
      }
      if (_and) {
        {
          Collection<? extends IJAnnotation> _annotations_2 = this.annotations();
          boolean _hasElements = false;
          for(final IJAnnotation a : _annotations_2) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate("\n", "");
            }
            String _generate = a.generate();
            _builder.append(_generate, "");
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("public class ");
    String _compilationUnitName = this.compilationUnitName();
    _builder.append(_compilationUnitName, "");
    {
      String _superClass = this.superClass();
      boolean _notEquals_1 = (!Objects.equal(_superClass, null));
      if (_notEquals_1) {
        _builder.append(" extends ");
        String _superClass_1 = this.superClass();
        _builder.append(_superClass_1, "");
      }
    }
    CharSequence _implementedClasses = this.implementedClasses();
    _builder.append(_implementedClasses, "");
    return _builder.toString();
  }
  
  @Override
  public String body() {
    StringConcatenation _builder = new StringConcatenation();
    {
      Collection<? extends IJField> _fields = this.fields();
      boolean _hasElements = false;
      for(final IJField field : _fields) {
        if (!_hasElements) {
          _hasElements = true;
        }
        CharSequence _field = this.field(field);
        _builder.append(_field, "");
        _builder.newLineIfNotEmpty();
      }
      if (_hasElements) {
        _builder.append("\n", "");
      }
    }
    {
      Collection<? extends IJMethod> _constructors = this.constructors();
      boolean _isEmpty = _constructors.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        {
          Collection<? extends IJMethod> _constructors_1 = this.constructors();
          boolean _hasElements_1 = false;
          for(final IJMethod constructor : _constructors_1) {
            if (!_hasElements_1) {
              _hasElements_1 = true;
            } else {
              _builder.appendImmediate("\n", "");
            }
            CharSequence _constructor = this.constructor(constructor);
            _builder.append(_constructor, "");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.newLine();
      }
    }
    {
      Collection<? extends IJMethod> _methods = this.methods();
      boolean _hasElements_2 = false;
      for(final IJMethod method : _methods) {
        if (!_hasElements_2) {
          _hasElements_2 = true;
        } else {
          _builder.appendImmediate("\n", "");
        }
        CharSequence _method = this.method(method);
        _builder.append(_method, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  public CharSequence field(final IJField field) {
    StringConcatenation _builder = new StringConcatenation();
    String _visibility = field.visibility();
    _builder.append(_visibility, "");
    {
      boolean _staticModifier = field.staticModifier();
      if (_staticModifier) {
        _builder.append(" static");
      }
    }
    {
      boolean _finalModifier = field.finalModifier();
      if (_finalModifier) {
        _builder.append(" final");
      }
    }
    _builder.append(" ");
    String _type = field.type();
    _builder.append(_type, "");
    _builder.append(" ");
    String _name = field.name();
    _builder.append(_name, "");
    {
      String _initialization = field.initialization();
      boolean _notEquals = (!Objects.equal(_initialization, null));
      if (_notEquals) {
        _builder.append(" = ");
        String _initialization_1 = field.initialization();
        _builder.append(_initialization_1, "");
      }
    }
    _builder.append(";");
    return _builder;
  }
  
  public CharSequence constructor(final IJMethod method) {
    StringConcatenation _builder = new StringConcatenation();
    String _visibilityModifier = method.visibilityModifier();
    _builder.append(_visibilityModifier, "");
    _builder.append(" ");
    String _compilationUnitName = this.compilationUnitName();
    _builder.append(_compilationUnitName, "");
    _builder.append("(");
    String _parameters = method.parameters();
    _builder.append(_parameters, "");
    _builder.append(") ");
    {
      String _throwsType = method.throwsType();
      boolean _notEquals = (!Objects.equal(_throwsType, null));
      if (_notEquals) {
        _builder.append("throws ");
        String _throwsType_1 = method.throwsType();
        _builder.append(_throwsType_1, "");
        _builder.append(" ");
      }
    }
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _body = method.body();
    _builder.append(_body, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence method(final IJMethod method) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Collection<? extends IJAnnotation> _annotations = method.annotations();
      boolean _hasElements = false;
      for(final IJAnnotation a : _annotations) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate("\n", "");
        }
        String _generate = a.generate();
        _builder.append(_generate, "");
      }
    }
    _builder.newLineIfNotEmpty();
    String _visibilityModifier = method.visibilityModifier();
    _builder.append(_visibilityModifier, "");
    {
      boolean _isStatic = method.isStatic();
      if (_isStatic) {
        _builder.append(" ");
        String _staticModifier = method.staticModifier();
        _builder.append(_staticModifier, "");
      }
    }
    _builder.append(" ");
    String _returnType = method.returnType();
    _builder.append(_returnType, "");
    _builder.append(" ");
    String _name = method.name();
    _builder.append(_name, "");
    _builder.append("(");
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
    {
      String _body = method.body();
      boolean _notEquals_1 = (!Objects.equal(_body, null));
      if (_notEquals_1) {
        _builder.append(" {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _body_1 = method.body();
        _builder.append(_body_1, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
      } else {
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
