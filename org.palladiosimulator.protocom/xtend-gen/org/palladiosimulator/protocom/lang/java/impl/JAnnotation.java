package org.palladiosimulator.protocom.lang.java.impl;

import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;

@SuppressWarnings("all")
public class JAnnotation implements IJAnnotation {
  private String name;
  
  private List<String> values;
  
  @Override
  public String name() {
    return this.name;
  }
  
  @Override
  public List<String> values() {
    return this.values;
  }
  
  @Override
  public String generate() {
    String _xifexpression = null;
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(this.values, null));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _isEmpty = this.values.isEmpty();
      boolean _not = (!_isEmpty);
      _and = _not;
    }
    if (_and) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("@");
      _builder.append(this.name, "");
      _builder.append("(");
      {
        boolean _hasElements = false;
        for(final String v : this.values) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate(", ", "");
          }
          _builder.append(v, "");
        }
      }
      _builder.append(")");
      _xifexpression = _builder.toString();
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("@");
      _builder_1.append(this.name, "");
      _xifexpression = _builder_1.toString();
    }
    return _xifexpression;
  }
  
  public JAnnotation withName(final String name) {
    JAnnotation _xblockexpression = null;
    {
      this.name = name;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public JAnnotation withValues(final List<String> values) {
    JAnnotation _xblockexpression = null;
    {
      this.values = values;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
}
