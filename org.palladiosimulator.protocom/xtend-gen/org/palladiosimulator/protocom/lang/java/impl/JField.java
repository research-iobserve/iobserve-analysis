package org.palladiosimulator.protocom.lang.java.impl;

import com.google.common.base.Objects;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.java.IJField;

/**
 * Class representing a field in Java.
 * 
 * This is a data class. Since methods are not a compilation unit, they do not
 * inherit GeneratedFile.
 * 
 * TODO: Change class to @Data
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public class JField implements IJField {
  private String name;
  
  private String type;
  
  private String visibility;
  
  private boolean staticModifier;
  
  private boolean finalModifier;
  
  private String initialization;
  
  @Override
  public String name() {
    return this.name;
  }
  
  @Override
  public String type() {
    return this.type;
  }
  
  @Override
  public String visibility() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(this.visibility, null));
      if (_notEquals) {
        _builder.append(this.visibility, "");
      } else {
        _builder.append("protected");
      }
    }
    return _builder.toString();
  }
  
  @Override
  public boolean staticModifier() {
    return this.staticModifier;
  }
  
  @Override
  public boolean finalModifier() {
    return this.finalModifier;
  }
  
  @Override
  public String initialization() {
    return this.initialization;
  }
  
  public JField withName(final String name) {
    JField _xblockexpression = null;
    {
      this.name = name;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JField withType(final String type) {
    JField _xblockexpression = null;
    {
      this.type = type;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JField withModifierVisibility(final String visibility) {
    JField _xblockexpression = null;
    {
      this.visibility = visibility;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JField withStaticModifier() {
    JField _xblockexpression = null;
    {
      this.staticModifier = true;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JField withFinalModifier() {
    JField _xblockexpression = null;
    {
      this.finalModifier = true;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JField withInitialization(final String initialization) {
    JField _xblockexpression = null;
    {
      this.initialization = initialization;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JField asDefaultSerialVersionUID() {
    JField _withModifierVisibility = this.withModifierVisibility("private");
    JField _withStaticModifier = _withModifierVisibility.withStaticModifier();
    JField _withFinalModifier = _withStaticModifier.withFinalModifier();
    JField _withType = _withFinalModifier.withType("long");
    JField _withName = _withType.withName("serialVersionUID");
    return _withName.withInitialization("1L");
  }
}
