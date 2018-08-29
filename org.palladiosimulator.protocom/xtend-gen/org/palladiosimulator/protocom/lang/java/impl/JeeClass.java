package org.palladiosimulator.protocom.lang.java.impl;

import com.google.common.base.Objects;
import java.util.Collection;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.IJeeClass;
import org.palladiosimulator.protocom.lang.java.impl.JCompilationUnit;

@SuppressWarnings("all")
public class JeeClass extends JCompilationUnit<IJeeClass> implements IJeeClass {
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
  public Collection<String> interfaces() {
    return this.provider.interfaces();
  }
  
  @Override
  public String packageName() {
    return this.provider.packageName();
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
    _builder.append("@");
    String _jeeClassStatelessAnnotation = this.jeeClassStatelessAnnotation();
    _builder.append(_jeeClassStatelessAnnotation, "");
    _builder.newLineIfNotEmpty();
    _builder.append("public class ");
    String _compilationUnitName = this.compilationUnitName();
    _builder.append(_compilationUnitName, "");
    _builder.append(" ");
    {
      String _superClass = this.superClass();
      boolean _notEquals = (!Objects.equal(_superClass, null));
      if (_notEquals) {
        _builder.append("extends ");
        String _superClass_1 = this.superClass();
        _builder.append(_superClass_1, "");
      }
    }
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
      Collection<? extends IJField> _jeeClassDependencyInjection = this.jeeClassDependencyInjection();
      for(final IJField dependencyInjection : _jeeClassDependencyInjection) {
        _builder.append("@");
        String _jeeClassDependencyInjectionAnnotation = this.jeeClassDependencyInjectionAnnotation();
        _builder.append(_jeeClassDependencyInjectionAnnotation, "");
        _builder.append("(name=\"");
        CharSequence _jeeClassDependencyInjectionNameAttribute = this.jeeClassDependencyInjectionNameAttribute(dependencyInjection);
        _builder.append(_jeeClassDependencyInjectionNameAttribute, "");
        _builder.append("\")");
        _builder.newLineIfNotEmpty();
        CharSequence _field = this.field(dependencyInjection);
        _builder.append(_field, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      Collection<? extends IJField> _fields = this.fields();
      for(final IJField field : _fields) {
        CharSequence _field_1 = this.field(field);
        _builder.append(_field_1, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      Collection<? extends IJMethod> _constructors = this.constructors();
      for(final IJMethod constructor : _constructors) {
        CharSequence _constructor = this.constructor(constructor);
        _builder.append(_constructor, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      Collection<? extends IJMethod> _methods = this.methods();
      for(final IJMethod method : _methods) {
        CharSequence _method = this.method(method);
        _builder.append(_method, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    return _builder.toString();
  }
  
  public CharSequence field(final IJField field) {
    StringConcatenation _builder = new StringConcatenation();
    String _visibility = field.visibility();
    _builder.append(_visibility, "");
    _builder.append(" ");
    String _type = field.type();
    _builder.append(_type, "");
    _builder.append(" ");
    String _name = field.name();
    _builder.append(_name, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence constructor(final IJMethod method) {
    StringConcatenation _builder = new StringConcatenation();
    String _visibilityModifier = method.visibilityModifier();
    _builder.append(_visibilityModifier, "");
    _builder.append(" ");
    String _compilationUnitName = this.compilationUnitName();
    _builder.append(_compilationUnitName, "");
    _builder.append(" (");
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
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    String _body = method.body();
    _builder.append(_body, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence method(final IJMethod method) {
    StringConcatenation _builder = new StringConcatenation();
    String _methodAnnotation = method.methodAnnotation();
    _builder.append(_methodAnnotation, "");
    _builder.newLineIfNotEmpty();
    String _visibilityModifier = method.visibilityModifier();
    _builder.append(_visibilityModifier, "");
    _builder.append(" ");
    String _staticModifier = method.staticModifier();
    _builder.append(_staticModifier, "");
    _builder.append(" ");
    String _returnType = method.returnType();
    _builder.append(_returnType, "");
    _builder.append(" ");
    String _name = method.name();
    _builder.append(_name, "");
    _builder.append(" (");
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
      }
    }
    _builder.newLineIfNotEmpty();
    {
      String _body = method.body();
      boolean _notEquals_1 = (!Objects.equal(_body, null));
      if (_notEquals_1) {
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        String _body_1 = method.body();
        _builder.append(_body_1, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append(";");
        _builder.newLine();
      }
    }
    _builder.newLine();
    return _builder;
  }
  
  @Override
  public String jeeClassStatelessAnnotation() {
    return this.provider.jeeClassStatelessAnnotation();
  }
  
  @Override
  public String jeeClassDependencyInjectionAnnotation() {
    return this.provider.jeeClassDependencyInjectionAnnotation();
  }
  
  @Override
  public Collection<? extends IJField> jeeClassDependencyInjection() {
    return this.provider.jeeClassDependencyInjection();
  }
  
  public CharSequence jeeClassDependencyInjectionNameAttribute(final IJField field) {
    StringConcatenation _builder = new StringConcatenation();
    String _type = field.type();
    _builder.append(_type, "");
    return _builder;
  }
}
