package org.palladiosimulator.protocom.tech.servlet.resourceenvironment;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.model.resourceenvironment.ResourceContainerAdapter;
import org.palladiosimulator.protocom.model.resourceenvironment.ResourceEnvironmentAdapter;

@SuppressWarnings("all")
public class ServletResourceEnvironment implements IJClass {
  protected final String frameworkBase = "org.palladiosimulator.protocom.framework.java.ee";
  
  protected ResourceEnvironmentAdapter entity;
  
  public ServletResourceEnvironment(final ResourceEnvironmentAdapter entity) {
    this.entity = entity;
  }
  
  @Override
  public String superClass() {
    return null;
  }
  
  @Override
  public String packageName() {
    return "main";
  }
  
  @Override
  public String compilationUnitName() {
    return "ResourceEnvironment";
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    return CollectionLiterals.<IJMethod>newLinkedList();
  }
  
  @Override
  public Collection<? extends IJAnnotation> annotations() {
    return CollectionLiterals.<IJAnnotation>newLinkedList();
  }
  
  @Override
  public Collection<String> interfaces() {
    return CollectionLiterals.<String>newLinkedList();
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    List<IJMethod> _xblockexpression = null;
    {
      final List<ResourceContainerAdapter> containers = this.entity.getResourceContainers();
      int i = 0;
      JMethod _jMethod = new JMethod();
      JMethod _withVisibilityModifier = _jMethod.withVisibilityModifier("public");
      JMethod _withStaticModifier = _withVisibilityModifier.withStaticModifier();
      JMethod _withName = _withStaticModifier.withName("init");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(this.frameworkBase, "");
      _builder.append(".prototype.PrototypeBridge bridge");
      JMethod _withParameters = _withName.withParameters(_builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(this.frameworkBase, "");
      _builder_1.append(".prototype.PrototypeBridge.Container[] containers = new ");
      _builder_1.append(this.frameworkBase, "");
      _builder_1.append(".prototype.PrototypeBridge.Container[");
      int _length = ((Object[])Conversions.unwrapArray(containers, Object.class)).length;
      _builder_1.append(_length, "");
      _builder_1.append("];");
      _builder_1.newLineIfNotEmpty();
      _builder_1.newLine();
      {
        for(final ResourceContainerAdapter container : containers) {
          _builder_1.append("containers[");
          int _plusPlus = i++;
          _builder_1.append(_plusPlus, "");
          _builder_1.append("] = bridge.new Container(\"");
          String _id = container.getId();
          _builder_1.append(_id, "");
          _builder_1.append("\", \"");
          String _name = container.getName();
          _builder_1.append(_name, "");
          _builder_1.append("\", \"");
          String _cpuRate = container.getCpuRate();
          _builder_1.append(_cpuRate, "");
          _builder_1.append("\", \"");
          String _hddRate = container.getHddRate();
          _builder_1.append(_hddRate, "");
          _builder_1.append("\");");
          _builder_1.newLineIfNotEmpty();
        }
      }
      _builder_1.newLine();
      _builder_1.append("bridge.setContainers(containers);");
      _builder_1.newLine();
      JMethod _withImplementation = _withParameters.withImplementation(_builder_1.toString());
      _xblockexpression = Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    return CollectionLiterals.<IJField>newLinkedList();
  }
  
  @Override
  public String filePath() {
    return "src/main/ResourceEnvironment.java";
  }
  
  @Override
  public String projectName() {
    return null;
  }
}
