package org.palladiosimulator.protocom.lang.java.impl;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Collections;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;
import org.palladiosimulator.protocom.lang.java.IJMethod;

/**
 * Class representing a Java method.
 * 
 * This is a data class. Since methods are not a compilation unit, they do not
 * inherit GeneratedFile.
 * 
 * A JMethod without name should be handled as a constructor.
 * 
 * TODO: Change class to @Data ?
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public class JMethod implements IJMethod {
  private String returnType;
  
  private String name;
  
  private String parameters;
  
  private String implementation;
  
  private String throwsType;
  
  private String visibility;
  
  private String methodAnnotation;
  
  private Collection<? extends IJAnnotation> annotations;
  
  private boolean isStatic;
  
  public JMethod() {
  }
  
  @Override
  public String returnType() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(this.returnType, null));
      if (_notEquals) {
        _builder.append(this.returnType, "");
      } else {
        _builder.append("void");
      }
    }
    return _builder.toString();
  }
  
  @Override
  public String name() {
    return this.name;
  }
  
  @Override
  public String parameters() {
    return this.parameters;
  }
  
  @Override
  public String visibilityModifier() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(this.visibility, null));
      if (_notEquals) {
        _builder.append(this.visibility, "");
      } else {
        _builder.append("public");
      }
    }
    return _builder.toString();
  }
  
  @Override
  public String throwsType() {
    return this.throwsType;
  }
  
  @Override
  public String staticModifier() {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (this.isStatic) {
        _builder.append("static");
      }
    }
    return _builder.toString();
  }
  
  @Override
  public boolean isStatic() {
    return this.isStatic;
  }
  
  @Override
  public Collection<? extends IJAnnotation> annotations() {
    Collection<? extends IJAnnotation> _xifexpression = null;
    boolean _notEquals = (!Objects.equal(this.annotations, null));
    if (_notEquals) {
      _xifexpression = this.annotations;
    } else {
      _xifexpression = Collections.<IJAnnotation>unmodifiableList(CollectionLiterals.<IJAnnotation>newArrayList());
    }
    return _xifexpression;
  }
  
  @Override
  public String body() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(this.implementation, null));
      if (_notEquals) {
        _builder.append(this.implementation, "");
      } else {
      }
    }
    return _builder.toString();
  }
  
  @Override
  public String methodAnnotation() {
    return this.methodAnnotation;
  }
  
  public JMethod withReturnType(final String returnType) {
    JMethod _xblockexpression = null;
    {
      this.returnType = returnType;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JMethod withName(final String name) {
    JMethod _xblockexpression = null;
    {
      this.name = name;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JMethod withParameters(final String parameters) {
    JMethod _xblockexpression = null;
    {
      this.parameters = parameters;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JMethod withImplementation(final String implementation) {
    JMethod _xblockexpression = null;
    {
      this.implementation = implementation;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JMethod withVisibilityModifier(final String visibility) {
    JMethod _xblockexpression = null;
    {
      this.visibility = visibility;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JMethod withStaticModifier() {
    JMethod _xblockexpression = null;
    {
      this.isStatic = true;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JMethod withThrows(final String throwsType) {
    JMethod _xblockexpression = null;
    {
      this.throwsType = throwsType;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JMethod withMethodAnnotation(final String annotation) {
    JMethod _xblockexpression = null;
    {
      this.methodAnnotation = annotation;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JMethod withAnnotations(final Collection<? extends IJAnnotation> annotations) {
    JMethod _xblockexpression = null;
    {
      this.annotations = annotations;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public String throwExceptionSignature() {
    boolean _notEquals = (!Objects.equal(this.throwsType, null));
    if (_notEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(" ");
      _builder.append("throws ");
      _builder.append(this.throwsType, " ");
      return _builder.toString();
    }
    return "";
  }
}
