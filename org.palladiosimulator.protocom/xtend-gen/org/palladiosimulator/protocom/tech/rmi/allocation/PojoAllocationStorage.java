package org.palladiosimulator.protocom.tech.rmi.allocation;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Collections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.rmi.PojoClass;

/**
 * @author Thomas Zolynski, Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoAllocationStorage extends PojoClass<Allocation> {
  public PojoAllocationStorage(final Allocation pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String superClass() {
    return "org.palladiosimulator.protocom.framework.java.se.AbstractAllocationStorage";
  }
  
  @Override
  public String packageName() {
    return "ProtoComBootstrap";
  }
  
  @Override
  public String compilationUnitName() {
    return "AllocationStorage";
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    JMethod _jMethod = new JMethod();
    JMethod _withName = _jMethod.withName("initContainerTemplate");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("String container;");
    _builder.newLine();
    _builder.append("String containerId;");
    _builder.newLine();
    _builder.append("Class<?> component;");
    _builder.newLine();
    _builder.append("String assemblyContext;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("// TODO This filters out Event Stuff. Maybe enable that in some future version.");
    _builder.newLine();
    {
      EList<AllocationContext> _allocationContexts_Allocation = this.pcmEntity.getAllocationContexts_Allocation();
      final Function1<AllocationContext, Boolean> _function = (AllocationContext i) -> {
        AssemblyContext _assemblyContext_AllocationContext = i.getAssemblyContext_AllocationContext();
        return Boolean.valueOf((!Objects.equal(_assemblyContext_AllocationContext, null)));
      };
      Iterable<AllocationContext> _filter = IterableExtensions.<AllocationContext>filter(_allocationContexts_Allocation, _function);
      for(final AllocationContext context : _filter) {
        _builder.append("containerId = \"");
        ResourceContainer _resourceContainer_AllocationContext = context.getResourceContainer_AllocationContext();
        String _id = _resourceContainer_AllocationContext.getId();
        _builder.append(_id, "");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("container = \"");
        ResourceContainer _resourceContainer_AllocationContext_1 = context.getResourceContainer_AllocationContext();
        String _entityName = _resourceContainer_AllocationContext_1.getEntityName();
        _builder.append(_entityName, "");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("component = ");
        AssemblyContext _assemblyContext_AllocationContext = context.getAssemblyContext_AllocationContext();
        RepositoryComponent _encapsulatedComponent__AssemblyContext = _assemblyContext_AllocationContext.getEncapsulatedComponent__AssemblyContext();
        String _fqn = JavaNames.fqn(_encapsulatedComponent__AssemblyContext);
        _builder.append(_fqn, "");
        _builder.append(".class;");
        _builder.newLineIfNotEmpty();
        _builder.append("assemblyContext = \"");
        AssemblyContext _assemblyContext_AllocationContext_1 = context.getAssemblyContext_AllocationContext();
        String _id_1 = _assemblyContext_AllocationContext_1.getId();
        _builder.append(_id_1, "");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("saveContainerComponent(containerId, container, component, assemblyContext);");
        _builder.newLine();
      }
    }
    JMethod _withImplementation = _withName.withImplementation(_builder.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
  }
  
  @Override
  public String filePath() {
    return "/src/ProtoComBootstrap/AllocationStorage.java";
  }
}
