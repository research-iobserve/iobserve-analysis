package org.palladiosimulator.protocom.tech.servlet.allocation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.model.allocation.AllocationAdapter;
import org.palladiosimulator.protocom.model.allocation.AllocationContextAdapter;
import org.palladiosimulator.protocom.model.allocation.AssemblyContextAdapter;
import org.palladiosimulator.protocom.model.repository.BasicComponentAdapter;
import org.palladiosimulator.protocom.model.resourceenvironment.ResourceContainerAdapter;
import org.palladiosimulator.protocom.tech.servlet.ServletClass;

@SuppressWarnings("all")
public class ServletAllocationStorage extends ServletClass<Allocation> {
  protected final String frameworkBase = "org.palladiosimulator.protocom.framework.java.ee";
  
  private final AllocationAdapter entity;
  
  public ServletAllocationStorage(final AllocationAdapter entity, final Allocation pcmEntity) {
    super(pcmEntity);
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
    return "ComponentAllocation";
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    List<IJMethod> _xblockexpression = null;
    {
      Iterable<AllocationContextAdapter> contexts = this.entity.getAllocationContexts();
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
      _builder_1.append(".prototype.PrototypeBridge.Allocation[] allocations = new ");
      _builder_1.append(this.frameworkBase, "");
      _builder_1.append(".prototype.PrototypeBridge.Allocation[");
      final Iterable<AllocationContextAdapter> _converted_contexts = (Iterable<AllocationContextAdapter>)contexts;
      int _length = ((Object[])Conversions.unwrapArray(_converted_contexts, Object.class)).length;
      _builder_1.append(_length, "");
      _builder_1.append("];");
      _builder_1.newLineIfNotEmpty();
      _builder_1.newLine();
      {
        for(final AllocationContextAdapter context : contexts) {
          _builder_1.append("allocations[");
          int _plusPlus = i++;
          _builder_1.append(_plusPlus, "");
          _builder_1.append("] = bridge.new Allocation(\"");
          ResourceContainerAdapter _resourceContainer = context.getResourceContainer();
          String _id = _resourceContainer.getId();
          _builder_1.append(_id, "");
          _builder_1.append("\", ");
          AssemblyContextAdapter _assemblyContext = context.getAssemblyContext();
          BasicComponentAdapter _encapsulatedComponent = _assemblyContext.getEncapsulatedComponent();
          String _classFqn = _encapsulatedComponent.getClassFqn();
          _builder_1.append(_classFqn, "");
          _builder_1.append(".class, \"");
          AssemblyContextAdapter _assemblyContext_1 = context.getAssemblyContext();
          String _id_1 = _assemblyContext_1.getId();
          _builder_1.append(_id_1, "");
          _builder_1.append("\");");
          _builder_1.newLineIfNotEmpty();
        }
      }
      _builder_1.newLine();
      _builder_1.append("bridge.setAllocations(allocations);");
      _builder_1.newLine();
      JMethod _withImplementation = _withParameters.withImplementation(_builder_1.toString());
      _xblockexpression = Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
    }
    return _xblockexpression;
  }
  
  @Override
  public String filePath() {
    return "/src/main/ComponentAllocation.java";
  }
}
