package org.palladiosimulator.protocom.lang.java.impl;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Collections;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJCompilationUnit;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;

@SuppressWarnings("all")
public abstract class JCompilationUnit<E extends IJCompilationUnit> extends GeneratedFile<E> implements IJCompilationUnit {
  @Override
  public String packageName() {
    return this.provider.packageName();
  }
  
  @Override
  public String compilationUnitName() {
    return this.provider.compilationUnitName();
  }
  
  @Override
  public Collection<String> interfaces() {
    Collection<String> _xblockexpression = null;
    {
      final Collection<String> i = this.provider.interfaces();
      Collection<String> _xifexpression = null;
      boolean _notEquals = (!Objects.equal(i, null));
      if (_notEquals) {
        _xifexpression = i;
      } else {
        _xifexpression = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList());
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    Collection<? extends IJMethod> _xblockexpression = null;
    {
      final Collection<? extends IJMethod> m = this.provider.methods();
      Collection<? extends IJMethod> _xifexpression = null;
      boolean _notEquals = (!Objects.equal(m, null));
      if (_notEquals) {
        _xifexpression = m;
      } else {
        _xifexpression = Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList());
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    Collection<? extends IJField> _xblockexpression = null;
    {
      final Collection<? extends IJField> f = this.provider.fields();
      Collection<? extends IJField> _xifexpression = null;
      boolean _notEquals = (!Objects.equal(f, null));
      if (_notEquals) {
        _xifexpression = f;
      } else {
        _xifexpression = Collections.<IJField>unmodifiableList(CollectionLiterals.<IJField>newArrayList());
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
  public String generate() {
    StringConcatenation _builder = new StringConcatenation();
    String _header = this.header();
    _builder.append(_header, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _body = this.body();
    _builder.append(_body, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public CharSequence implementedClasses() {
    StringConcatenation _builder = new StringConcatenation();
    {
      Collection<String> _interfaces = this.interfaces();
      boolean _hasElements = false;
      for(final String implInterface : _interfaces) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append(" implements ", "");
        } else {
          _builder.appendImmediate(", ", "");
        }
        _builder.append(implInterface, "");
      }
    }
    return _builder;
  }
  
  /**
   * Template for the header part of this compilation unit: package, imports, type definition.
   */
  public abstract String header();
  
  /**
   * Template for the body part of this compilation unit: members, methods/signatures.
   */
  public abstract String body();
}
